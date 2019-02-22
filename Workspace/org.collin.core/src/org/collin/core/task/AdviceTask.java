package org.collin.core.task;

import java.util.Date;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;

public class AdviceTask<N,D extends Object> extends AbstractTask<N, D>{

	public AdviceTask() {
		super();
	}

	public AdviceTask(IDataObject<D> sequence, ITetraNode<D> node ) {
		super( sequence, node );
	}

	
	@Override
	protected Results onProgress(ITetraImplementation<N, D> node, TetraEvent<D> event) {
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
	protected Results onComplete(ITetraImplementation<N, D> node, TetraEvent<D> event) {
		// TODO Auto-generated method stub
		return null;
	}
}
