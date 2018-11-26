package org.collin.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.collin.core.xml.ModuleBuilder.SequenceEvents;
import org.collin.core.xml.SequenceNode.Nodes;
import org.condast.commons.Utils;

public class Sequence {

	private ModuleBuilder builder;
	private SequenceNode root;
	private SequenceNode current;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public Sequence( Class<?> clss) throws IOException {
		super();
		builder = new ModuleBuilder( clss);
		root = builder.build();
	}

	public Sequence( InputStream in) throws IOException {
		super();
		builder = new ModuleBuilder( in );
		root = builder.build();
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.xml.IWizardBuilder#build(org.condast.commons.ui.wizard.xml.AbstractXmlFlowWizard)
	 */
	public SequenceEvent process( SequenceEvents event, SequenceEvent current ) {
		SequenceEvent result = null;
		try {
			SequenceNode find = null;
			SequenceNode parent = null;
			switch( event) {
			case RESET:
				find = root;
				break;
			case CURRENT:
				find = find( root, current.getId());
				break;
			case NEXT:
				find = find( root, current.getId());
				parent = find.getParent();
				find = ( parent == null )? null: parent.next( find );
				break;
			case PREVIOUS:
				find = find( root, current.getId());
				parent = find.getParent();
				find = ( parent == null )? null: parent.previous( find );
				break;
			case OFFSET:
				find = find( root, current.getId());
				parent = find.getParent();
				find = ( parent == null )? null: parent.offset( find, current.getOffset() );
				break;
			case NEXT_TRACK:
				find = find( root, current.getId());
				find = find.getParent();
				if( find == null )
					return null;
				parent = find.getParent();
				find = ( find == null )? null: find .next( find );
				break;
			case PREVIOUS_TRACK:
				find = find( root, current.getId());
				find = find.getParent();
				if( find == null )
					return null;
				parent = find.getParent();
				find = ( find == null )? null: find .previous( find );
				break;
			default:
				break;
			}
			if( find == null )
				return null;
			result = new SequenceEvent( this, find.getId(), find.getUri(), find.getIndex());
		}
		catch( Exception ex ){
			ex.printStackTrace();;
		}
		return result;
	}

	public SequenceEvent getSequence() {
		SequenceNode is = ModuleBuilder.find( root, Nodes.SEQUENCE );
		return new SequenceEvent( this, is.getId(), is.getUri(), is.getIndex());
	}
		
	public SequenceNode start() {
		SequenceNode is = ModuleBuilder.find( root, Nodes.SEQUENCE );
		Collection<SequenceNode> steps = is.getChildren();
		if( Utils.assertNull(steps))
			return is;
		SequenceNode step = steps.iterator().next();
		Collection<SequenceNode> parts = step.getChildren();
		current = Utils.assertNull(parts)?step: parts.iterator().next().getChildren().iterator().next();
		return current;
	}
	
	public SequenceNode next() {
		SequenceNode is = ModuleBuilder.find( root, Nodes.SEQUENCE );
		Collection<SequenceNode> steps = is.getChildren();
		if( Utils.assertNull(steps))
			return is;
		SequenceNode step = steps.iterator().next();
		List<SequenceNode> parts = step.getChildren();
		SequenceNode curparts = current.getParent();
		int index = curparts.getChildren().indexOf(current);
		int next = (index >= curparts.getChildren().size())?-1:++index;
		if( next >= 0 )
			return curparts.getChildren().get(next);
		else {
			int partsindex = parts.indexOf(curparts);
			SequenceNode nextpart = parts.get(++partsindex);
			return nextpart.getChildren().iterator().next();
			//TODO implement 
		}
	}

	public static SequenceNode find( SequenceNode current, String id ) {
		if( id.equals(current.getId()))
			return current;
		for( SequenceNode child: current.getChildren()) {
			SequenceNode find = find( child, id);
			if( find != null )
				return find;
		}
		return null;
	}

	public String getAdvice(long moduleId, double progress) {
		// TODO Auto-generated method stub
		return null;
	}

}
