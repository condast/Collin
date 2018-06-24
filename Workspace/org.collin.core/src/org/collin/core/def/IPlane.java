package org.collin.core.def;

import org.collin.core.def.ITetraNode.Nodes;

public interface IPlane<D extends Object> {

	String getLabel();

	int getBalance();

	ITetraNode<D> getNode(Nodes node);
}