package org.collin.moodle.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.advice.AdviceManager;
import org.collin.moodle.advice.IAdviceMap;

public class Coach extends AbstractTetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private boolean completed; 
	
	private Map<Long,AdviceManager> managers;
	
	public Coach(SequenceNode<IAdviceMap> sequence, ITetra<IAdviceMap> tetra) {
		super(tetra, sequence );
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
		
		//If the student doesn't add an advice request, then skip
		IAdviceMap adviceMap = transaction.getData(); 
		if( adviceMap == null ) {
			this.completed = true;
			return result;
		}
		long userId = adviceMap.getUserId();
		AdviceManager manager = null;
		switch( transaction.getState()) {
		case START:
			SequenceDelegateFactory<IAdviceMap> factory = new SequenceDelegateFactory<>(super.getSource());
			manager = (AdviceManager)factory.createDelegate( this.getClass(), node );
			this.managers.put( userId, manager );			
			break;
		default:
			manager = managers.get(userId );
			break;
		}
		manager.perform(this, event);
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
