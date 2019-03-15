package org.collin.moodle.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.Compass;
import org.collin.core.essence.Compass.Tetras;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.transaction.TetraTransaction;
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

public class ActorService {

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
	
	private Map<Compass.Tetras, ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>> implementations;
	
	private Collection<Long> users;
	
	private static ActorService service = new ActorService();
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	private ActorService() {
		super();
		implementations = new HashMap<>();
		users = new TreeSet<>();
	}

	public static ActorService getInstance() {
		return service;
	}
	
	public boolean isregistered( long userId ) {
		return this.users.contains(userId );
	}
	
	protected void register( TetraTransaction<IAdviceMap> transaction ) {
		for( ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>impl: this.implementations.values()) {
			impl.register(transaction);
		}
	}

	protected void unregister( TetraTransaction<IAdviceMap> transaction ) {
		for( ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>impl: this.implementations.values()) {
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
		this.users.add(userId);
		try {
			readModules();
			TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId, new AdviceMap( userId, 0) );
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
	public IAdviceMap createAdvice( long userId, long moduleId, long activityId, double progress ) throws Exception {
		try {
			if( !isregistered(userId)) {
				start( userId, moduleId );
			}
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
			TetraTransaction<IAdviceMap> transaction = student.createTransaction( userId, moduleId, activityId, progress );
			register( transaction );
 			student.fire( transaction );
 			unregister( transaction);
 			return transaction.getData();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
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
	public IAdviceMap updateAdvice( long userId, int adviceId, IAdvice.Notifications notification, double progress ) throws Exception {
		try {
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
			if( student == null ) {
				logger.warning("This advice was not prepared: " + userId );
				return null;
			}
			TetraTransaction<IAdviceMap> transaction = student.updateTransaction(adviceId, notification);
			register( transaction );
			student.fire( transaction );
 			unregister( transaction);
 			return transaction.getData();
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
	 * Read the modules
	 * @return
	 */
	private SequenceNode<IAdviceMap> readModules() {
		SequenceNode<IAdviceMap> node = null;
		try {
			ModuleBuilder<IAdviceMap> builder = new ModuleBuilder<IAdviceMap>( ActorService.class );
			node = builder.build();
			SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>(node);
			
			CollinBuilder<IAdviceMap> cbuilder = new CollinBuilder<IAdviceMap>( getClass() );
			Compass<IAdviceMap>[] compasses = (Compass<IAdviceMap>[]) cbuilder.build();	
			Compass<IAdviceMap> compass = compasses[0];
			SequenceNode<IAdviceMap> actor = query.findCollin(Actors.STUDENT.toString());
			implementations.put( Compass.Tetras.CONSUMER, new Student( actor, compass.getTetra(Tetras.CONSUMER) ));
			actor = query.findCollin(Actors.COACH.toString());
			if( actor == null)
				actor = new SequenceNode<IAdviceMap>( Nodes.MODEL, Actors.COACH.name(), Actors.COACH.toString(), Actors.COACH.toString(), null, 0 );
			implementations.put( Compass.Tetras.COACH, new Coach( actor, compass.getTetra( Tetras.COACH ) ));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return node;
	}
}
