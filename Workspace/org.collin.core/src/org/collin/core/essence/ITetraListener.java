package org.collin.core.essence;

import org.collin.core.transaction.TetraTransaction;

public interface ITetraListener<D extends Object> {

	/**
	 * Notify listeners about an event that is propagating through the system.
	 * Add the direct source that propagates the event
	 * @param source
	 * @param event
	 * @param blockLocal
	 */
	public void notifyNodeSelected( Object source, TetraTransaction<D> event, boolean blockLocal );
}
