package org.collin.core.task;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;

public class TimedTask<D extends Object> extends AbstractTask<D>{

	public TimedTask() {
		super();
	}

	public TimedTask(IDataObject<D> sequence, ITetraNode<D> node ) {
		super( sequence, node );
	}
	
	@Override
	protected Results onProgress(ITetraNode<D> node, TetraEvent<D> event) {
		return calculate( event.getTransaction());			
	}

	@Override
	protected Results onComplete(ITetraNode<D> node, TetraEvent<D> event) {
		return Results.FAIL;//calculate( event.getTransaction());			
	}
}
