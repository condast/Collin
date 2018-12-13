package org.collin.moodle.model;

import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.transaction.TetraTransaction;

public class Coach extends AbstractTetraImplementation<SequenceNode>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public Coach(SequenceNode sequence, ITetra<SequenceNode> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory( sequence ));
	}

	@Override
	protected TetraEvent.Results onNodeChange(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event ) {
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		TetraTransaction<SequenceNode> transaction = event.getTransaction();
		switch( transaction.getState()) {
		case START:
			break;
		case PROGRESS:
			switch( node.getType()) {
			case GOAL:
				switch( event.getResult()) {
				case SUCCESS:
					break;//result = event.isFinished();
				case FAIL:
					break;//result = event.isFinished();
				default:
					break;
				}
				break;
			case SOLUTION:
				break;
			default:
				logger.info( "UPDATING TETRA: "+ node.getType().toString() + ":  " + transaction.getState().toString());
				break;
			}
			break;
		case COMPLETE:
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	protected TetraEvent.Results onTransactionUpdateRequest(TetraEvent<SequenceNode> event) {
		logger.info(event.getTransaction().getState().toString());
		return TetraEvent.Results.COMPLETE;
	}

	@Override
	protected void onTetraEventReceived(TetraEvent<SequenceNode> event) {
		logger.info(event.getTransaction().getState().toString());
	}	
}
