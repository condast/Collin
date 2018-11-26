package org.collin.core.essence;

interface IOperator<D extends Object> extends ITetraListener<D>{

	void select(TetraEvent<D> event);

}