package org.collin.dashboard.operators;

import java.util.Date;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.task.AbstractDelegate;

public class AdviceManager<D extends Object> extends AbstractDelegate<SequenceNode<D>,D>{

	public AdviceManager(IDataObject<D> sequence ) {
		super( sequence );
	}

	@Override
	protected Results onProgress(ITetraImplementation<SequenceNode<D>, D> node, TetraEvent<D> event) {
		Date start = super.getStart();
		Date end = super.getEndTime();
		Date current = super.getCurrentTime();
		IDataObject<D> sequence = super.getData();
		switch( event.getResult()) {
		case SUCCESS:
			break;
		case FAIL:
			double diff = getDifference();
			if( end.getTime()%120 == 0 ) {

			}
			break;
		default:
			break;
		}
		return Results.COMPLETE;
	}

	@Override
	protected Results onComplete(ITetraImplementation<SequenceNode<D>, D> node, TetraEvent<D> event) {
		// TODO Auto-generated method stub
		return null;
	}
}

