package org.collin.core.def;

import org.collin.core.essence.TetraEvent;

public interface ICollINDelegate<N,D extends Object> {


	/**
	 * Set the parameters
	 * @param attrs
	 */
	void setParameters( IDataObject<D> settings);

	/**
	 * Performs the required activities for the given node. This method returns a
	 * result, that is used to propagate the transaction through the tetra
	 * @param node
	 * @return
	 */
	public TetraEvent.Results perform( ITetraImplementation<N,D> node, TetraEvent<D> transaction );
}
