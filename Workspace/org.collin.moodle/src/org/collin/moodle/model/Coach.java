package org.collin.moodle.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.operators.AdviceManager;

public class Coach extends AbstractTetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private boolean completed; 
	
	private Map<Long,AdviceManager> managers;
	
	public Coach(SequenceNode<IAdviceMap> sequence, ITetra<IAdviceMap> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory<IAdviceMap>( sequence ));
		this.completed = false;
		this.managers = new HashMap<>();
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
		return event.getResult();
	}

	@Override
	protected TetraEvent.Results onCallTask(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event ) {
		this.completed = false;
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();
		
		//If the student doens't add an advice request, then skip
		IAdviceMap adviceMap = transaction.getData(); 
		if( adviceMap == null ) {
			this.completed = true;
			return result;
		}
		long userId;
		switch( transaction.getState()) {
		case START:
			userId = adviceMap.getUserId();
			AdviceManager manager = (AdviceManager) node.getOperator();
			this.managers.put( userId, manager );			
			break;
		case PROGRESS:
			userId = adviceMap.getUserId();
			manager = managers.get(userId );
			
			SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap> ( super.getData());
			SequenceNode<IAdviceMap> task = query.getTetra( adviceMap.getModuleId(), adviceMap.getActivityId(), Nodes.TASK );
			manager.start( task, transaction );

			IAdvice.AdviceTypes type = IAdvice.AdviceTypes.SUCCESS;
			switch( event.getResult()) {
			case FAIL:
				type = IAdvice.AdviceTypes.FAIL;
				break;
			case COMPLETE:
				break;
			default:
				break;
			}
			IAdvice advice = manager.createAdvice( super.getData(), adviceMap, type );
 			adviceMap.addAdvice(advice);
			this.completed = ( advice != null );
			result = Results.COMPLETE;//the coach has successfully given an advice
		default:
			break;
		}
 		return result;
	}

	@Override
	protected TetraEvent.Results onCallSolution(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = completed? TetraEvent.Results.COMPLETE:TetraEvent.Results.FAIL ;
		return result;
	}	

	@Override
	protected TetraEvent.Results onTransactionUpdateRequest(TetraEvent<IAdviceMap> event) {
		logger.info(event.getTransaction().getState().toString());
		return TetraEvent.Results.COMPLETE;
	}

	@Override
	protected void onTetraEventReceived(TetraEvent<IAdviceMap> event) {
		logger.info(event.getTransaction().getState().toString());
	}	
}
