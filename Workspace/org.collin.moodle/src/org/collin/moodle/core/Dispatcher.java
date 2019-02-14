package org.collin.moodle.core;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.Compass;
import org.collin.core.essence.Compass.Tetras;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.transaction.TetraTransaction;
import org.collin.core.transaction.TetraTransaction.States;
import org.collin.core.xml.CollinBuilder;
import org.collin.moodle.advice.AdviceMap;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.model.Coach;
import org.collin.moodle.model.Student;
import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.io.IOUtils;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

import nl.martijndwars.webpush.core.PushManager;


public class Dispatcher {

	public enum Actors{
		UNDEFINED,
		STUDENT,
		COACH,
		SUPPORT,
		MODULE,
		STUDY;
		
		public static Actors getActor( Tetras tetra ) {
			switch( tetra ) {
			case CONSUMER:
				return STUDENT;
			case COACH:
				return COACH;
			case PROCESS:
				return STUDY;
			case PRODUCT:
				return MODULE;
			case PRODUCER:
				return SUPPORT;
			default:
				return UNDEFINED;
			}
		}

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.toString());
		}	
	}
	
	private static Dispatcher dispatcher = new Dispatcher();
	
	private Map<Long, Integer> progress;	
	
	private Map<Long, URI> modules;
	
	private Map<Compass.Tetras, ITetraImplementation<IAdviceMap>> implementations;
	
	private PushManager pushMananger;
	
	private Map<Long,AdviceManager> managers;
	
	private Dispatcher() {
		super();
		progress = new HashMap<>();
		this.modules = new HashMap<>();
		implementations = new HashMap<>();
		pushMananger = new PushManager();
		this.managers = new HashMap<>();
	}

	public static Dispatcher getInstance() {
		return dispatcher;
	}

	public PushManager getPushMananger() {
		return pushMananger;
	}

	public AdviceManager getAdviceManager( long userId) {
		return managers.get(userId);
	}

	public long addModule( String path ) {
		long lessonId = this.modules.size();
		URI uri = URI.create(path);
		if( uri == null )
			return -1;
		modules.put(lessonId, uri);
		return lessonId;
	}

	protected void register( TetraTransaction<IAdviceMap> transaction ) {
		for( ITetraImplementation<IAdviceMap>impl: this.implementations.values()) {
			impl.register(transaction);
		}
	}

	protected void unregister( TetraTransaction<IAdviceMap> transaction ) {
		for( ITetraImplementation<IAdviceMap>impl: this.implementations.values()) {
			impl.unregister( transaction);
		}
	}

	public void start( long userId, long moduleId ){
		InputStream stream = null;
		try {
			readModules();
			TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire( transaction );
			unregister( transaction);
			AdviceManager manager = new AdviceManager();
			this.managers.put(userId, manager);
			manager.start(userId, moduleId );
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public SequenceNode<IAdviceMap> findLesson( long moduleId, long activityId ) throws Exception {
		try {
			SequenceNode<IAdviceMap> find = null;//findNode( node, String.valueOf( moduleId ), String.valueOf( activityId ));
			if( find == null )
				return find;
 			return find;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
	}
	
	protected SequenceNode<IAdviceMap> findNode( SequenceNode<IAdviceMap> node, String moduleId, String activityId ) {
		if(( !StringUtils.isEmpty( node.getId())) && node.getId().equals(moduleId)) {
			if(SequenceNode.Nodes.MODULE.equals( node.getNode() ) && ( activityId == null ) || ( activityId.equals("0" )))
				return node;
			else {
				if( SequenceNode.Nodes.ACTIVITY.equals( node.getNode()))
					return node;
			}
		}
		for( SequenceNode<IAdviceMap> child: node.getChildren()) {
			SequenceNode<IAdviceMap> result = findNode( child, moduleId, activityId );
			if( result != null )
				return result;
		}
		return null;
	}

	public IAdviceMap getAdvice( long userId, long moduleId, long activityId, double progress ) throws Exception {
		try {
			IAdviceMap advice = new AdviceMap( userId, moduleId, activityId, progress );
			TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId, States.PROGRESS, advice, progress );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire( transaction );
 			AdviceManager manager = this.managers.get(userId);
 			manager.addAdvice(userId, advice);
 			unregister( transaction);
 			return advice;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
	}

	public void updateAdvice( long userId, long adviceId, IAdvice.Notifications notification ) throws Exception {
			AdviceManager manager = this.managers.get(userId);
			manager.updateAdvice(userId, adviceId, notification);
	}

	public int getProgress( long moduleId ) {
		Integer value = progress.get( moduleId);
		if( value != null ) {
			int next = ( value >= 100 )? 1000: value + 10;
			progress.put(moduleId, next );
		}else {
			progress.put(moduleId, 0);			
		}
		return ( value == null)?0: value;
	}

	private SequenceNode<IAdviceMap> readModules() {
		SequenceNode<IAdviceMap> node = null;
		try {
			ModuleBuilder<IAdviceMap> builder = new ModuleBuilder<IAdviceMap>( Dispatcher.class );
			node = builder.build();
			SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>(node);
			
			CollinBuilder<IAdviceMap> cbuilder = new CollinBuilder<IAdviceMap>( getClass() );
			Compass<IAdviceMap>[] compasses = (Compass<IAdviceMap>[]) cbuilder.build();	
			Compass<IAdviceMap> compass = compasses[0];
			SequenceNode<IAdviceMap> actor = query.findCollin(Actors.STUDENT.toString());
			implementations.put( Compass.Tetras.CONSUMER, new Student( actor, compass.getTetra(Tetras.CONSUMER) ));
			actor = query.findCollin(Actors.COACH.toString());
			if( actor == null)
				actor = new SequenceNode<IAdviceMap>( Nodes.MODEL, Actors.COACH.name(), Actors.COACH.toString(), Actors.COACH.toString(), null, 0, 900 );
			implementations.put( Compass.Tetras.COACH, new Coach( actor, compass.getTetra( Tetras.COACH ) ));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return node;
	}
}
