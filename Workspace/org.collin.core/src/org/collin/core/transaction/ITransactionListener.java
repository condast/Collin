package org.collin.core.transaction;

import org.collin.core.essence.TetraEvent;
import org.collin.core.graph.ICollINVertex;

public interface ITransactionListener<D extends Object> {

	/**
	 * Notify listeners about an event that is propagating through the system.
	 * Add the direct source that propagates the event
	 * @param source
	 * @param event
	 * @return true if one of the listeners has successfully updated the transaction
	 */
	public TetraEvent.Results transactionUpdateRequest( ICollINVertex<D> source, TetraEvent<D> event );
}
