package org.collin.moodle.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.Compass;
import org.collin.core.essence.Compass.Tetras;
import org.collin.core.transaction.TetraTransaction;
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
	
	private AbstractTetraImplementation<SequenceNode> monitor;
	
	private SequenceNode node;

	private Dispatcher() {
		super();
		progress = new HashMap<>();
		this.modules = new HashMap<>();
		CollinBuilder<SequenceNode> builder;
		try {
			builder = new CollinBuilder<SequenceNode>( getClass() );
			Compass<SequenceNode>[] compasses = (Compass<SequenceNode>[]) builder.build();	
			Compass<SequenceNode> compass = compasses[0];
			monitor = new TetraImplementation( compass);
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

	public String start( long moduleId ) throws Exception {
		InputStream stream = null;
		String result = null;
		try {
			node = readModules();
 			monitor.fire( new TetraTransaction<SequenceNode>(this, node ));
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
 			monitor.fire( new TetraTransaction<SequenceNode>(this, find ));
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

	private class TetraImplementation extends AbstractTetraImplementation<SequenceNode>{

		private Date start;
		
		private Logger logger = Logger.getLogger( this.getClass().getName());
		
		public TetraImplementation(Compass<SequenceNode> compass) {
			super(compass.getTetra(Tetras.CONSUMER));
		}

		@Override
		protected boolean onNodeChange(ITetraNode<SequenceNode> node, TetraTransaction<SequenceNode> event) {
			boolean result = false;
			SequenceNode sn = event.getData();
			switch( event.getState()) {
			case START:
				start = Calendar.getInstance().getTime();
				result = true;
				break;
			case PROGRESS:
				switch( node.getType()) {
				case TASK:
					result = event.isFinished();
					break;
				case SOLUTION:
					sn = event.getData();
					break;
				default:
					logger.info( "UPDATING TETRA: "+ node.getType().toString() + ":  " + event.getState().toString());
					break;
				}
				result = true; 
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
		protected boolean onTransactionUpdateRequest(TetraTransaction<SequenceNode> event) {
			logger.info(event.getState().toString());
			return true;
		}


		@Override
		protected void onTetraEventReceived(TetraTransaction<SequenceNode> event) {
			logger.info(event.getState().toString());
		}
		
	}
}
