package org.collin.core.operator;

import org.collin.core.def.ITetraNode;
import org.collin.core.transaction.TetraTransaction;
import org.xml.sax.Attributes;

public interface IOperator<D extends Object>{

	public void setParameters( Attributes attrs );
	
	/**
	 * Determines the response of the event for the given node. If a
	 * false is returned, then it means that the even cannot enter the
	 * next phase
	 * @param 
	 * @param event
	 * @return
	 */
	boolean select( ITetraNode<D> source, TetraTransaction<D> event);

	void dispose();

	boolean contains(ITetraNode<D> node);
}