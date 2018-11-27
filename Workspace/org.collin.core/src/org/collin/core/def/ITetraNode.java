package org.collin.core.def;

import org.collin.core.essence.ITetra;
import org.collin.core.essence.ITetraListener;
import org.collin.core.essence.TetraEvent;

public interface ITetraNode<D extends Object> {

	public enum Nodes{
		UNDEFINED,
		START,
		GOAL,
		TASK,
		SOLUTION,
		FUNCTION,
		COMPLETE;
	}

	public String getId();
	
	public Nodes getType();

	public ITetra<D> getParent();

	boolean addTetraListener(ITetraListener<D> listener);

	boolean removeTetraListener(ITetraListener<D> listener);
		
	public D getData();

	void select( TetraEvent<D> event );

	int getSelected();

	int balance(int offset);

	String getDescription();

	void setDescription(String description);

}