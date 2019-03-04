package org.collin.core.def;

public interface IDelegateFactory<N,D extends Object> {

	ICollINDelegate<N,D> createDelegate(Class<?> clss);

}