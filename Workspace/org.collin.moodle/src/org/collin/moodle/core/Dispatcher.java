package org.collin.moodle.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.collin.core.def.ITetraImplementation;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.Compass;
import org.collin.core.essence.Compass.Tetras;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.transaction.TetraTransaction;
import org.collin.core.transaction.TetraTransaction.States;
import org.collin.core.util.AbstractTetraImplementation;
import org.collin.core.xml.CollinBuilder;
import org.collin.core.xml.SequenceNode;
import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.io.IOUtils;
import org.condast.commons.strings.StringUtils;


public class Dispatcher {

	private static Dispatcher dispatcher = new Dispatcher();
	
	private Map<Long, Integer> progress;	
	
	private Map<Long, URI> modules;
	
	private Map<Compass.Tetras, ITetraImplementation<SequenceNode>> implementations;
	
	private SequenceNode node;

	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	private Dispatcher() {
		super();
		progress = new HashMap<>();
		this.modules = new HashMap<>();
		CollinBuilder<SequenceNode> builder;
		try {
			builder = new CollinBuilder<SequenceNode>( getClass() );
			Compass<SequenceNode>[] compasses = (Compass<SequenceNode>[]) builder.build();	
			Compass<SequenceNode> compass = compasses[0];
			implementations = new HashMap<>();
			implementations.put( Compass.Tetras.CONSUMER, new Student( compass.getTetra(Tetras.CONSUMER) ));
			implementations.put( Compass.Tetras.COACH, new Coach( compass.getTetra( Tetras.COACH ) ));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Dispatcher getInstance() {
		return dispatcher;
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
	public String start( long moduleId ) throws Exception {
		InputStream stream = null;
		String result = null;
		try {
			node = readModules();
			TetraTransaction<SequenceNode> transaction = new TetraTransaction<SequenceNode>(this, node );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire( transaction );
			unregister();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
		return result;
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

	public SequenceNode getAdvice( long moduleId, long activityId, double progress ) throws Exception {
		try {
			SequenceNode find = findNode( node, String.valueOf( moduleId ), String.valueOf( activityId ));
			if( find == null )
				return null;
			TetraTransaction<SequenceNode> transaction = new TetraTransaction<SequenceNode>(this, States.PROGRESS, find, progress );
			register( transaction );
			Student student = (Student) this.implementations.get(Compass.Tetras.CONSUMER);
 			student.fire(  transaction);
 			unregister();
 			return find;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
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

	private static SequenceNode readModules() {
		SequenceNode node = null;
		try {
			ModuleBuilder builder = new ModuleBuilder( Dispatcher.class );
			node = builder.build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return node;
	}

	private class Student extends AbstractTetraImplementation<SequenceNode>{

		public Student(ITetra<SequenceNode> tetra) {
			super(tetra);
		}

		@Override
		protected boolean onNodeChange(ITetraNode<SequenceNode> node, TetraTransaction<SequenceNode> event) {
			boolean result = false;
			switch( event.getState()) {
			case START:
				result = true;
				break;
			case PROGRESS:
				switch( node.getType()) {
				case TASK:
					result = event.isFinished();
					break;
				case SOLUTION:
					result = true; 
					break;
				default:
					logger.info( "UPDATING TETRA: "+ node.getType().toString() + ":  " + event.getState().toString());
					break;
				}
				break;
			case COMPLETE:
				result = true;
				break;
			default:
				break;
			}
			return result;
		}

		@Override
		protected TetraEvent.Results onTransactionUpdateRequest(TetraTransaction<SequenceNode> event) {
			logger.info(event.getState().toString());
			return Results.CONTINUE;
		}

		@Override
		protected void onTetraEventReceived(TetraTransaction<SequenceNode> event) {
			logger.info(event.getState().toString());
		}		
	}

	private class Coach extends AbstractTetraImplementation<SequenceNode>{

		public Coach(ITetra<SequenceNode> tetra) {
			super(tetra);
		}

		@Override
		protected boolean onNodeChange(ITetraNode<SequenceNode> node, TetraTransaction<SequenceNode> event) {
			boolean result = false;
			switch( event.getState()) {
			case START:
				result = true;
				break;
			case PROGRESS:
				switch( node.getType()) {
				case TASK:
					result = event.isFinished();
					break;
				case SOLUTION:
					result = true; 
					break;
				default:
					logger.info( "UPDATING TETRA: "+ node.getType().toString() + ":  " + event.getState().toString());
					break;
				}
				break;
			case COMPLETE:
				result = true;
				break;
			default:
				break;
			}
			return result;
		}

		@Override
		protected TetraEvent.Results onTransactionUpdateRequest(TetraTransaction<SequenceNode> event) {
			logger.info(event.getState().toString());
			return TetraEvent.Results.CONTINUE;
		}

		@Override
		protected void onTetraEventReceived(TetraTransaction<SequenceNode> event) {
			logger.info(event.getState().toString());
		}	
	}
}
