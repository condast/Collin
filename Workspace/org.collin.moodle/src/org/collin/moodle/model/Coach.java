package org.collin.moodle.model;

import java.util.Random;
import java.util.logging.Logger;

import org.collin.core.advice.AdviceFactory;
import org.collin.core.advice.IAdvice;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.transaction.TetraTransaction;

public class Coach extends AbstractTetraImplementation<IAdvice, SequenceNode>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private boolean completed; 

	public Coach(SequenceNode sequence, ITetra<SequenceNode> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory( sequence ));
		this.completed = false;
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
		return event.getResult();
	}

	@Override
	protected TetraEvent.Results onCallTask(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event ) {
		this.completed = false;
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		TetraTransaction<SequenceNode> transaction = event.getTransaction();
		switch( transaction.getState()) {
		case START:
			break;
		case PROGRESS:
			switch( event.getResult()) {
			case SUCCESS:
			case FAIL:
				SequenceQuery query = new SequenceQuery( super.getData());
				SequenceNode sn = query.find(node.getType());
				//String url = sn.getUri();
				AdviceFactory factory = new AdviceFactory( sn);
				factory.load( this.getClass(), AdviceFactory.S_DEFAULT_LOCATION);
				IAdvice[] results = factory.getAdvice( Results.SUCCESS.equals(event.getResult())?IAdvice.AdviceTypes.SUCCESS: IAdvice.AdviceTypes.FAIL);
				Random random = new Random();
				int choice = (int)random.nextInt(results.length);
				event.getTransaction().getData().addDatum( results[choice]);
				this.completed = true;
				result = Results.COMPLETE;//the coach has successfully given an advice
				break;
			case COMPLETE:
				break;
			default:
				break;
			}
		default:
			break;
		}
 		return result;
	}

	@Override
	protected TetraEvent.Results onCallSolution(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = completed? TetraEvent.Results.COMPLETE:TetraEvent.Results.FAIL ;
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
