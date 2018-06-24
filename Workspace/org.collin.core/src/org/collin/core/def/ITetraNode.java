package org.collin.core.def;

import org.collin.core.essence.ITetraListener;

public interface ITetraNode<D extends Object> {

	public enum Nodes{
		GOAL,
		TASK,
		STRUCTURE,
		FUNCTION;
	}

	public String getId();
	
	void addTetraListener(ITetraListener<D> listener);

	void removeTetraListener(ITetraListener<D> listener);
	
	public D getData();

	void select();

	int getSelected();

	int balance(int offset);

}