package org.collin.moodle.model;

import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.delegates.StudentAdviceDelegate;

public class Student extends AbstractTetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private StudentAdviceDelegate delegate;
	
	public Student(SequenceNode<IAdviceMap> sequence, ITetra<IAdviceMap> tetra) {
		super(tetra, sequence );
		SequenceDelegateFactory<IAdviceMap> factory = new SequenceDelegateFactory<>(super.getSource());
		delegate = (StudentAdviceDelegate) factory.createDelegate( this.getClass() );
	}

	public TetraTransaction<IAdviceMap> updateTransaction( int adviceId ) {
		return delegate.updateTransaction(adviceId);
	}

	public TetraTransaction<IAdviceMap> createTransaction( long userId, long moduleId, long activityId, double progress ) {
		return delegate.createTransaction(userId, moduleId, activityId, progress);
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
		result = (delegate == null)? result: delegate.perform(this, event );
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