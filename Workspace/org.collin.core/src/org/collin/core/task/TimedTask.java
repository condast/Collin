package org.collin.core.task;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;

public class TimedTask<N,D extends Object> extends AbstractDelegate<N,D>{

	public TimedTask() {
		super();
	}

	public TimedTask(IDataObject<D> sequence ) {
		super( sequence );
	}

	@Override
	protected Results onProgress(ITetraImplementation<N, D> node, TetraEvent<D> event) {
		return Results.CONTINUE;
	}

	@Override
	protected Results onComplete(ITetraImplementation<N, D> node, TetraEvent<D> event) {
		// TODO Auto-generated method stub
		return null;
	}	
}
