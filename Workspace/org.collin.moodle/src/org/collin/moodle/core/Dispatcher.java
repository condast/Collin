package org.collin.moodle.core;

import java.io.InputStream;
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
import org.condast.commons.messaging.push.ISubscription;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

import nl.martijndwars.webpush.core.PushManager;


public class Dispatcher {

	public static final String S_PUBLIC_KEY = "BDvq04Lz9f7WBugyNHW2kdgFI7cjd65fzfFRpNdRpa9zWvi4yAD8nAvgb8c8PpRXdtgUqqZDG7KbamEgxotOcaA";
	public static final String S_PRIVATE_KEY = "CxbJjjbVMABqzv72ZL4GH_0gNStbZV0TSBaNOIzLwbE";
	
	public static final String S_CODED = "BMfyyFPnyR8MRrzPJ6jloLC26FyXMcrL8v46d7QEUccbQVArghc9YHC6USyp4TggrFleNzAUq8df0RiSS13xwtM";

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
	
	private Map<Compass.Tetras, ITetraImplementation<IAdviceMap>> implementations;
	
	private PushManager pushMananger;
	
	private Dispatcher() {
		super();
		implementations = new HashMap<>();
		pushMananger = new PushManager();
	}

	public static Dispatcher getInstance() {
		return dispatcher;
	}

	public PushManager getPushMananger() {
		return pushMananger;
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

	/**
	 * Start a module and register the user
	 * @param userId
	 * @param moduleId
	 */
	public void start( long userId, long moduleId ){
		InputStream stream = null;
		try {
			readModules();
			TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId, new AdviceMap( userId, moduleId) );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire( transaction );
			unregister( transaction);
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
	}

	/**
	 * Request advice
	 * @param userId
	 * @param moduleId
	 * @param activityId
	 * @param progress
	 * @return
	 * @throws Exception
	 */
	public IAdviceMap getAdvice( long userId, long moduleId, long activityId, double progress ) throws Exception {
		try {
			IAdviceMap advice = new AdviceMap( userId, moduleId, activityId, progress );
			TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId, States.PROGRESS, advice, progress );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire( transaction );
 			unregister( transaction);
 			return advice;
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

	/**
	 * Push the given advice to the user
	 * @param userId
	 * @param advice
	 * @return
	 */
	public boolean sendPushMessage( long userId, IAdvice advice ) {
		if(( advice ==  null ) ||( userId < 0 ))
			return false;
		PushManager pm = dispatcher.getPushMananger();
		ISubscription subscription = pm.getSubscription( userId );
		PushOptionsAdviceBuilder builder = new PushOptionsAdviceBuilder();
		builder.createPayLoad( advice, true );
		try {
			PushManager.sendPushMessage( Dispatcher.S_PUBLIC_KEY, Dispatcher.S_PRIVATE_KEY, subscription, builder.createPayLoad());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}				
		return true;
	}

	/**
	 * Read the modules
	 * @return
	 */
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
