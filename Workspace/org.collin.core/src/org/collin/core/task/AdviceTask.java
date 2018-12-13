package org.collin.core.task;

import java.io.File;
import java.util.Date;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;

public class AdviceTask<T extends Object, D extends IDataObject<T>> extends AbstractTask<T,D>{

	public AdviceTask() {
		super();
	}

	public AdviceTask(D sequence, ITetraNode<D> node ) {
		super( sequence, node );
	}

	@Override
	protected Results onProgress(ITetraNode<D> node, TetraEvent<D> event) {
		Date start = super.getStart();
		Date end = super.getEndTime();
		Date current = super.getCurrentTime();
		IDataObject<T> sequence = super.getData();
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
	protected Results onComplete(ITetraNode<D> node, TetraEvent<D> event) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
