package org.collin.moodle.delegates;

import java.util.LinkedHashMap;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.task.AbstractDelegate;
import org.collin.core.transaction.TetraTransaction;
import org.collin.core.transaction.TetraTransaction.States;
import org.collin.moodle.advice.AdviceMap;
import org.collin.moodle.advice.IAdviceMap;
import org.condast.commons.strings.StringStyler;

public class StudentAdviceDelegate extends AbstractDelegate<SequenceNode<IAdviceMap>, IAdviceMap> {

	public static final int DEFAULT_COUNT = 120;//two minutes
	public static final int DEFAULT_POLL_TIME = 1000;//1 seconds

	public enum AttributeNames{
		DURATION,
		POLL_TIME;

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}
	private LinkedHashMap<Integer, IAdviceMap> advice;

	private long userId;

	public StudentAdviceDelegate(IDataObject<IAdviceMap> sequence, ITetraNode<IAdviceMap> node) {
		super(sequence, node);
		this.advice = new LinkedHashMap<>();
	}

	protected long getUserId() {
		return userId;
	}

	public TetraTransaction<IAdviceMap> createTransaction( long userId, long moduleId, long activityId, double progress ) {
		IAdviceMap map = new AdviceMap( userId, moduleId, activityId, progress );
		TetraTransaction<IAdviceMap> transaction = new TetraTransaction<IAdviceMap>(this, userId, States.PROGRESS, map, progress );
		this.advice.put( map.getAdviceId(), map );
		return transaction;
	}

	public TetraTransaction<IAdviceMap> updateTransaction( int adviceId ) {
		IAdviceMap map = this.advice.remove(adviceId);
		if( map == null )
			return null;
		return new TetraTransaction<IAdviceMap>(this, map.getUserId(), States.PROGRESS, map, map.getProgress()  );
	}

	@Override
	protected Results onStart(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		Results result = super.onStart(node, event);
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();
		IAdviceMap map = transaction.getData();
		this.userId = map.getUserId();
		this.advice.put( map.getAdviceId(), map );
		result = Results.CONTINUE;
		return result;
	}

	public IAdviceMap getAdvice( int adviceId ) {
		return this.advice.get(adviceId);
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