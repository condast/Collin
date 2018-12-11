package org.collin.core.operator;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetraListener;
import org.collin.core.transaction.TetraTransaction;
import org.xml.sax.Attributes;

public interface IOperator<D extends Object>{

	public void setParameters( Attributes attrs );
	
	/**
	 * Determines the response of the event for the given node. If a
	 * false is returned, then it means that the event cannot enter the
	 * next phase. It is possible to pass a result from the preprocessing in order
	 * to make the assessment
	 * @param 
	 * @param transaction
	 * @return
	 */
	boolean select( ITetraNode<D> source, ITetraListener.Results result, TetraTransaction<D> transaction);

	void dispose();
}