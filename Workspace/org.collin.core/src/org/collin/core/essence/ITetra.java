package org.collin.core.essence;

import java.util.Collection;
import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;

public interface ITetra<D extends Object> {

	public enum Planes{
		AMBITION,
		LEARNING,
		OPERATION,
		RECOVERY;
	}

	String getLabel();

	Map<Planes, IPlane<D>> getPlanes();

	void select(ITetraNode.Nodes node);
	
	public Collection<ITetraNode<D>> getNodes();

	ITetraNode<D> getNode(Nodes type);

}