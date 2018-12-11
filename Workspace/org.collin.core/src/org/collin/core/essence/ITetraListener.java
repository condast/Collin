package org.collin.core.essence;

import org.collin.core.transaction.TetraTransaction;

public interface ITetraListener<D extends Object> {

	enum Results{
		SUCCESS,
		CONTINUE,
		FAIL;
	
		public static Results getResult( boolean choice ) {
			return choice?Results.SUCCESS: Results.FAIL;
		}
	}

	/**
	 * Notify listeners about an event that is propagating through the system.
	 * Add the direct source that propagates the event
	 * @param source
	 * @param event
	 */
	public void notifyNodeSelected( Object source, Results result, TetraTransaction<D> event );
}
