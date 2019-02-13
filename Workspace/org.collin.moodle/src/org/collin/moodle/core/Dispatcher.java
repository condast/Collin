package org.collin.moodle.core;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.collin.core.advice.IAdvice;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.Compass;
import org.collin.core.essence.Compass.Tetras;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.transaction.TetraTransaction;
import org.collin.core.transaction.TetraTransaction.States;
import org.collin.core.xml.CollinBuilder;
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
	
	private Map<Compass.Tetras, ITetraImplementation<SequenceNode>> implementations;
	
	private SequenceNode node;

	private PushManager pushMananger;
	
	private AdviceManager adviceManager;
	
	private Dispatcher() {
		super();
		progress = new HashMap<>();
		this.modules = new HashMap<>();
		implementations = new HashMap<>();
		pushMananger = new PushManager();
		this.adviceManager = new AdviceManager();
		node = readModules();
	}

	public static Dispatcher getInstance() {
		return dispatcher;
	}

	public PushManager getPushMananger() {
		return pushMananger;
	}

	public AdviceManager getAdviceManager() {
		return adviceManager;
	}

	public long addModule( String path ) {
		long lessonId = this.modules.size();
		URI uri = URI.create(path);
		if( uri == null )
			return -1;
		modules.put(lessonId, uri);
		return lessonId;
	}

	protected void register( TetraTransaction<SequenceNode> transaction ) {
		for( ITetraImplementation<SequenceNode>impl: this.implementations.values()) {
			impl.register(transaction);
		}
	}

	protected void unregister( ) {
		for( ITetraImplementation<SequenceNode>impl: this.implementations.values()) {
			impl.unregister();
		}
	}

	public void start( long userId, long moduleId ){
		node = readModules();
		InputStream stream = null;
		try {
			TetraTransaction<SequenceNode> transaction = new TetraTransaction<SequenceNode>(this, userId, node );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire( transaction );
			unregister();
			this.adviceManager.start(userId, moduleId );
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public SequenceNode findLesson( long moduleId, long activityId ) throws Exception {
		try {
			SequenceNode find = findNode( node, String.valueOf( moduleId ), String.valueOf( activityId ));
			if( find == null )
				return find;
 			return find;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
	}
	
	protected SequenceNode findNode( SequenceNode node, String moduleId, String activityId ) {
		if(( !StringUtils.isEmpty( node.getId())) && node.getId().equals(moduleId)) {
			if(SequenceNode.Nodes.MODULE.equals( node.getNode() ) && ( activityId == null ) || ( activityId.equals("0" )))
				return node;
			else {
				if( SequenceNode.Nodes.ACTIVITY.equals( node.getNode()))
					return node;
			}
		}
		for( SequenceNode child: node.getChildren()) {
			SequenceNode result = findNode( child, moduleId, activityId );
			if( result != null )
				return result;
		}
		return null;
	}

	public SequenceNode getAdvice( long userId, long moduleId, long activityId, double progress ) throws Exception {
		try {
			SequenceNode find = findNode( node, String.valueOf( moduleId ), String.valueOf( activityId ));
			if( find == null )
				return null;
			TetraTransaction<SequenceNode> transaction = new TetraTransaction<SequenceNode>(this, userId, States.PROGRESS, find, progress );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire(  transaction );
			SequenceNode node = transaction.getData();
 			this.adviceManager.addAdvice(userId, node);
 			unregister();
 			return find;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
	}

	public void updateAdvice( long userId, long adviceId, IAdvice.Notifications notification ) throws Exception {
		this.adviceManager.updateAdvice(userId, adviceId, notification);
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

	private SequenceNode readModules() {
		SequenceNode node = null;
		try {
			ModuleBuilder builder = new ModuleBuilder( Dispatcher.class );
			node = builder.build();
			SequenceQuery query = new SequenceQuery(node);
			
			CollinBuilder<SequenceNode> cbuilder = new CollinBuilder<SequenceNode>( getClass() );
			Compass<SequenceNode>[] compasses = (Compass<SequenceNode>[]) cbuilder.build();	
			Compass<SequenceNode> compass = compasses[0];
			SequenceNode actor = query.findCollin(Actors.STUDENT.toString());
			implementations.put( Compass.Tetras.CONSUMER, new Student( actor, compass.getTetra(Tetras.CONSUMER) ));
			actor = query.findCollin(Actors.COACH.toString());
			if( actor == null)
				actor = new SequenceNode( Nodes.MODEL, Actors.COACH.name(), Actors.COACH.toString(), Actors.COACH.toString(), null, 0, 900 );
			implementations.put( Compass.Tetras.COACH, new Coach( actor, compass.getTetra( Tetras.COACH ) ));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return node;
	}
}
