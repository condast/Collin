package org.collin.moodle.model;

import java.util.logging.Logger;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.transaction.TetraTransaction;

public class Student extends AbstractTetraImplementation<String, SequenceNode>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public Student(SequenceNode sequence, ITetra<SequenceNode> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory( sequence ));
	}

	@Override
	protected TetraEvent.Results onCallFunction(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		return result;
	}

	@Override
	protected TetraEvent.Results onCallGoal(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.CONTINUE;
		return result;
	}

	@Override
	protected TetraEvent.Results onCallTask(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.CONTINUE;
		TetraTransaction<SequenceNode> transaction = event.getTransaction();		
		ICollINDelegate<String, SequenceNode> delegate = getDelegate( node );
		result = (delegate == null)? result: delegate.perform(node, event );
		switch( transaction.getState()) {
		case START:
			getDelegate(node);
			break;
		case PROGRESS:
			logger.info( "UPDATING TETRA ("+ result + "): " + node.getType().toString() + ":  " + transaction.getState().toString());
			result = ( delegate == null )? Results.COMPLETE: delegate.perform(node, event);
			break;
		case COMPLETE:
			result = Results.COMPLETE;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	protected TetraEvent.Results onCallSolution(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		return result;
	}	

	@Override
	protected TetraEvent.Results onTransactionUpdateRequest(TetraEvent<SequenceNode> event) {
		logger.info(event.getTransaction().getState().toString());
		return Results.COMPLETE;
	}

	@Override
	protected void onTetraEventReceived(TetraEvent<SequenceNode> event) {
		logger.info(event.getTransaction().getState().toString());
	}		
}