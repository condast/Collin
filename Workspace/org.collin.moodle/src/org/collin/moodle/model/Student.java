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
import org.collin.moodle.advice.IAdviceMap;

public class Student extends AbstractTetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public Student(SequenceNode<IAdviceMap> sequence, ITetra<IAdviceMap> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory<IAdviceMap>( sequence ));
	}

	@Override
	protected TetraEvent.Results onCallFunction(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		return result;
	}

	@Override
	protected TetraEvent.Results onCallGoal(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.CONTINUE;
		return result;
	}

	@Override
	protected TetraEvent.Results onCallTask(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.CONTINUE;
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();		
		ICollINDelegate<IAdviceMap> delegate = getDelegate( node );
		result = (delegate == null)? result: delegate.perform(node, event );
		switch( transaction.getState()) {
		case START:
			break;
		case PROGRESS:
			logger.info( "UPDATING TETRA ("+ result + "): " + node.getType().toString() + ":  " + transaction.getState().toString());
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
	protected TetraEvent.Results onCallSolution(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		return result;
	}	

	@Override
	protected TetraEvent.Results onTransactionUpdateRequest(TetraEvent<IAdviceMap> event) {
		logger.info(event.getTransaction().getState().toString());
		return Results.COMPLETE;
	}

	@Override
	protected void onTetraEventReceived(TetraEvent<IAdviceMap> event) {
		logger.info(event.getTransaction().getState().toString());
	}		
}