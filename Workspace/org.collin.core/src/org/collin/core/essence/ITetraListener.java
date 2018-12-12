package org.collin.core.essence;

public interface ITetraListener<D extends Object> {

	/**
	 * Notify listeners about an event that is propagating through the system.
	 * Add the direct source that propagates the event
	 * @param source
	 * @param event
	 */
	public void notifyNodeSelected( Object source, TetraEvent<D> event );
}
