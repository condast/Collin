package org.collin.core.task;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.operator.AbstractOperator;

public class TimedTask<D extends Object> extends AbstractOperator<D> {

	@Override
	protected TetraEvent.Results onStart(ITetraNode<D> source, TetraEvent<D> event) {
		return event.getTransaction().updateTransaction(source, event);
	}

	@Override
	protected TetraEvent.Results onProgress(ITetraNode<D> source, double progress, TetraEvent<D> event) {
		return event.getTransaction().updateTransaction(source, event);
	}

	@Override
	protected TetraEvent.Results onComplete(ITetraNode<D> source, TetraEvent<D> event) {
		return event.getTransaction().updateTransaction(source, event);
	}
}
