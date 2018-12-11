package org.collin.core.essence;

import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.collin.core.graph.ICollINShape;
import org.collin.core.graph.ICollINVertex;
import org.collin.core.transaction.TetraTransaction;

public interface ITetra<D extends Object> extends ITetraNode<D>, ICollINShape<D> {

	void init();

	Map<IPlane.Planes, IPlane<D>> getPlanes();

	public ITetraNode<D>[] getNodes();

	ITetraNode<D> getNode(Nodes type);

	/**
	 * Returns true if the given nod is a child of this tetra
	 * @param node
	 * @return
	 */
	boolean isChild(ICollINVertex<D> node);

	void addNode(ITetraNode<D> node);

	void removeNode(ITetraNode<D> node);

	String getName();

	boolean fire( TetraTransaction<D> event);
}