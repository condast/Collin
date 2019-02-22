package org.collin.core.def;

import org.collin.core.transaction.TetraTransaction;

public interface ITetraImplementation<N,D extends Object> {

	boolean fire(TetraTransaction<D> transaction);

	void register(TetraTransaction<D> transaction);

	void unregister( TetraTransaction<D> transaction );

	N getData();

}