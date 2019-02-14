package org.collin.moodle.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.collin.core.impl.SequenceEvent;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.xml.ModuleBuilder.SequenceEvents;
import org.condast.commons.Utils;

public class Sequence {

	private ModuleBuilder<IAdviceMap> builder;
	private SequenceNode<IAdviceMap> root;
	private SequenceNode<IAdviceMap> current;
	
	public Sequence( Class<?> clss) throws IOException {
		super();
		builder = new ModuleBuilder<>( clss);
		root = builder.build();
	}

	public Sequence( InputStream in) throws IOException {
		super();
		builder = new ModuleBuilder<>( in );
		root = builder.build();
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.xml.IWizardBuilder#build(org.condast.commons.ui.wizard.xml.AbstractXmlFlowWizard)
	 */
	public SequenceEvent process( SequenceEvents event, SequenceEvent current ) {
		SequenceEvent result = null;
		try {
			SequenceNode<IAdviceMap> find = null;
			SequenceNode<IAdviceMap> parent = null;
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
		SequenceNode<IAdviceMap> is = ModuleBuilder.find( root, Nodes.SEQUENCE );
		return new SequenceEvent( this, is.getId(), is.getUri(), is.getIndex());
	}
		
	public SequenceNode<IAdviceMap> start() {
		SequenceNode<IAdviceMap> is = ModuleBuilder.find( root, Nodes.SEQUENCE );
		Collection<SequenceNode<IAdviceMap>> steps = is.getChildren();
		if( Utils.assertNull(steps))
			return is;
		SequenceNode<IAdviceMap> step = steps.iterator().next();
		Collection<SequenceNode<IAdviceMap>> parts = step.getChildren();
		current = Utils.assertNull(parts)?step: parts.iterator().next().getChildren().iterator().next();
		return current;
	}
	
	public SequenceNode<IAdviceMap> next() {
		SequenceNode<IAdviceMap> is = ModuleBuilder.find( root, Nodes.SEQUENCE );
		Collection<SequenceNode<IAdviceMap>> steps = is.getChildren();
		if( Utils.assertNull(steps))
			return is;
		SequenceNode<IAdviceMap> step = steps.iterator().next();
		List<SequenceNode<IAdviceMap>> parts = step.getChildren();
		SequenceNode<IAdviceMap> curparts = current.getParent();
		int index = curparts.getChildren().indexOf(current);
		int next = (index >= curparts.getChildren().size())?-1:++index;
		if( next >= 0 )
			return curparts.getChildren().get(next);
		else {
			int partsindex = parts.indexOf(curparts);
			SequenceNode<IAdviceMap> nextpart = parts.get(++partsindex);
			return nextpart.getChildren().iterator().next();
			//TODO implement 
		}
	}

	public static SequenceNode<IAdviceMap> find( SequenceNode<IAdviceMap> current, String id ) {
		if( id.equals(current.getId()))
			return current;
		for( SequenceNode<IAdviceMap> child: current.getChildren()) {
			SequenceNode<IAdviceMap> find = find( child, id);
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
