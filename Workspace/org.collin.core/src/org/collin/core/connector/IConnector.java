package org.collin.core.connector;

import org.collin.core.def.ITetraNode;
import org.collin.core.operator.IOperator;

public interface IConnector<O, D extends Object> extends IOperator<D>{

	TetraConnector<O, D> getOwner();

	public boolean isEqual( ITetraNode<D> node1, ITetraNode<D> node2 );
}