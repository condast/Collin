package org.collin.core.def;

import org.collin.core.def.ITetraNode.Nodes;

public interface IPlane<D extends Object> {

	int getBalance();

	ITetraNode<D> getNode(Nodes node);

}