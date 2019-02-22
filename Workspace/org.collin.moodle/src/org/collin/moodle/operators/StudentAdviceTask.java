package org.collin.moodle.operators;

import java.util.LinkedHashMap;

import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.task.AbstractTask;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.advice.IAdviceMap;
import org.condast.commons.strings.StringStyler;

public class StudentAdviceTask extends AbstractTask<SequenceNode<IAdviceMap>, IAdviceMap> {

	public static final int DEFAULT_COUNT = 120;//two minutes
	public static final int DEFAULT_POLL_TIME = 1000;//1 seconds

	public enum AttributeNames{
		DURATION,
		POLL_TIME;

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}
	private LinkedHashMap<Long, IAdviceMap> advice;

	private long userId;

	public StudentAdviceTask() {
		this.advice = new LinkedHashMap<>();
	}

	
	@Override
	protected Results onStart(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();
		IAdviceMap map = transaction.getData();
		this.userId = map.getUserId();
		this.advice.put( map.getAdviceId(), map );
		return super.onStart(node, event);
	}


	public IAdviceMap getAdvice( int adviceId ) {
		return this.advice.get(adviceId);
	}


	@Override
	protected Results onProgress(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
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