package org.collin.core.def;

import org.collin.core.essence.ITetraListener;
import org.collin.core.transaction.TetraTransaction;

public interface ICollINSelector<D extends Object> {

	public String getId();
	
	boolean addTetraListener(ITetraListener<D> listener);

	boolean removeTetraListener(ITetraListener<D> listener);
		
	String getDescription();

	void setDescription(String description);

	String getName();

	boolean fire( TetraTransaction<D> event);
}