package org.collin.core.def;

public interface IDelegateFactory<T extends Object, D extends IDataObject<T>> {

	ICollINDelegate<T, D> createDelegate(Class<?> clss, ITetraNode<D> node);

}