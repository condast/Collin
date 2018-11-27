package org.collin.core.essence;

import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;

public interface ITetra<D extends Object> extends ITetraNode<D> {

	void init();

	Map<IPlane.Planes, IPlane<D>> getPlanes();

	public   ITetraNode<D>[] getNodes();

	ITetraNode<D> getNode(Nodes type);

	void addNode(ITetraNode<D> node);

	void removeNode(ITetraNode<D> node);

	String getName();

}