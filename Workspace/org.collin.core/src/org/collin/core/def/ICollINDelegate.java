package org.collin.core.def;

import org.collin.core.essence.TetraEvent;

public interface ICollINDelegate<T extends Object, D extends IDataObject<T>> {

	/**
	 * Performs the required activities for the given node. This method returns a
	 * result, that is used to propagate the transaction through the tetra
	 * @param node
	 * @return
	 */
	public TetraEvent.Results perform( ITetraNode<D> node, TetraEvent<D> transaction );
}
