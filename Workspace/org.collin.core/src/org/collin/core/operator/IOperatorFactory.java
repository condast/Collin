package org.collin.core.operator;

import org.collin.core.def.ITetraNode;

public interface IOperatorFactory<D extends Object> {

	/**
	 * Create a default operator, or a custom one with the given className if this is not empty
	 * @param className
	 * @param origin
	 * @param destination
	 * @return
	 */
	public IOperator<D> createOperator(String className, ITetraNode<D> origin, ITetraNode<D> destination);
}
