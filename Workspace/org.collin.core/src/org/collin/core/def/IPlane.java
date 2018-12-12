package org.collin.core.def;

import org.collin.core.graph.ICollINShape;

public interface IPlane<D extends Object> extends ICollINShape<D>{

	public enum Planes{
		AMBITION,
		LEARNING,
		OPERATION,
		RECOVERY;
	}
	
	public Planes getType();
}