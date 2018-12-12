package org.collin.core.graph;

import org.collin.core.essence.ITetraListener;
import org.collin.core.operator.IOperator;
import org.collin.core.transaction.TetraTransaction;

public interface ICollINVertex<D extends Object> {

	public String getId();
	
	public IOperator<D> getOperator();
	
	boolean addCollINListener(ITetraListener<D> listener);

	boolean removeCollINListener(ITetraListener<D> listener);
		
	String getDescription();

	void setDescription(String description);

	String getName();

	boolean fire( TetraTransaction<D> transaction );
}