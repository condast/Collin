package org.collin.core.connector;

import org.collin.core.def.ITetraNode;

public interface IConnector<O, D extends Object> {

	boolean contains(ITetraNode<D> node);

	boolean isEqual(ITetraNode<D> node1, ITetraNode<D> node2);

	ITetraNode<D> getOther(ITetraNode<D> node);

	void dispose();

	TetraConnector<O, D> getOwner();

}