package org.collin.core.operator;

import org.collin.core.def.ITetraNode;
import org.collin.core.transaction.TetraTransaction;

public class TimedTask<D extends Object> extends AbstractOperator<D> {

	@Override
	protected boolean onStart(ITetraNode<D> source, TetraTransaction<D> event) {
		return event.updateTransaction(source, event);
	}

	@Override
	protected boolean onProgress(ITetraNode<D> source, double progress, TetraTransaction<D> event) {
		return event.updateTransaction(source, event);
	}

	@Override
	protected boolean onComplete(ITetraNode<D> source, TetraTransaction<D> event) {
		return event.updateTransaction(source, event);
	}
}
