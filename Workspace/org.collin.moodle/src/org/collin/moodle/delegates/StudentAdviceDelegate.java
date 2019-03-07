package org.collin.moodle.delegates;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.task.AbstractDelegate;
import org.collin.core.transaction.TetraTransaction;
import org.collin.core.transaction.TetraTransaction.States;
import org.collin.moodle.advice.AdviceMap;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;

public class StudentAdviceDelegate extends AbstractDelegate<SequenceNode<IAdviceMap>, IAdviceMap> {

	private long userId;

	public StudentAdviceDelegate(IDataObject<IAdviceMap> sequence) {
		super(sequence);
	}

	
	@Override
	public void setParameters(IDataObject<IAdviceMap> settings) {
		// NOTHING	
	}


	protected long getUserId() {
		return userId;
	}

	public TetraTransaction<IAdviceMap> createTransaction( long userId, long moduleId, long activityId, double progress ) {
		IAdviceMap map = new AdviceMap( userId, moduleId, activityId, progress );
		TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId, States.PROGRESS, map, progress );
		return transaction;
	}

	public TetraTransaction<IAdviceMap> updateTransaction( int adviceId, IAdvice.Notifications notification ) {
		IAdviceMap map = new AdviceMap( userId, adviceId, notification );
		return new TetraTransaction<IAdviceMap>(this, map.getUserId(), States.PROGRESS, map, map.getProgress()  );
	}

	@Override
	protected Results onStart(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		Results result = super.onStart(node, event);
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();
		IAdviceMap map = transaction.getData();
		this.userId = map.getUserId();
		result = Results.CONTINUE;
		return result;
	}

	@Override
	protected Results onProgress(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		TetraEvent.Results result = TetraEvent.Results.CONTINUE;
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();
		IAdviceMap adviceMap = transaction.getData(); 
		if( adviceMap == null ) {
			return result;
		}
		switch( adviceMap.getInteraction()) {
		default:
			break;
		}
		return result;
	}


	@Override
	protected Results onComplete(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		Results result = Results.COMPLETE;
		return result;
	}
}