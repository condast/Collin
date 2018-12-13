package org.collin.core.def;

public interface IDelegateFactory<D extends Object> {

	ICollINDelegate<D> createDelegate(Class<?> clss, ITetraNode<D> node);

}