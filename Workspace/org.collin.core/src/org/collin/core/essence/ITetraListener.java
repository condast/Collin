package org.collin.core.essence;

public interface ITetraListener<D extends Object> {

	public void notifyNodeSelected( TetraEvent<D> event );
}
