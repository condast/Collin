package org.collin.core.def;

import org.collin.core.def.ITetraNode.Nodes;

public interface IPlane<D extends Object> {

	public enum Planes{
		AMBITION,
		LEARNING,
		OPERATION,
		RECOVERY;
	}

	String getLabel();

	ITetraNode<D> getNode(Nodes node);
}