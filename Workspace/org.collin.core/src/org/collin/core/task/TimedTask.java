package org.collin.core.task;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;

public class TimedTask<T extends Object, D extends IDataObject<T>> extends AbstractTask<T,D>{

	public TimedTask() {
		super();
	}

	public TimedTask(IDataObject<T> sequence, ITetraNode<D> node ) {
		super( sequence, node );
	}
	
	@Override
	protected Results onProgress(ITetraNode<D> node, TetraEvent<D> event) {
		return calculate( event.getTransaction());			
	}

	@Override
	protected Results onComplete(ITetraNode<D> node, TetraEvent<D> event) {
		return calculate( event.getTransaction());			
	}
}
