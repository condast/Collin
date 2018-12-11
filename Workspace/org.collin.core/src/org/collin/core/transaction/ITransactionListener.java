package org.collin.core.transaction;

import org.collin.core.graph.ICollINVertex;
import org.collin.core.transaction.TetraTransaction;

public interface ITransactionListener<D extends Object> {

	/**
	 * Notify listeners about an event that is propagating through the system.
	 * Add the direct source that propagates the event
	 * @param source
	 * @param event
	 * @return true if one of the listeners has successfully updated the transaction
	 */
	public boolean transactionUpdateRequest( ICollINVertex<D> source, TetraTransaction<D> event );
}
