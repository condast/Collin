package org.collin.core.operator;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.xml.sax.Attributes;

public interface IOperator<D extends Object>{

	public void setParameters( Attributes attrs );
	
	/**
	 * Determines the response of the event for the given node. If a
	 * false is returned, then it means that the event cannot enter the
	 * next phase. It is possible to pass a result from the preprocessing in order
	 * to make the assessment
	 * @param 
	 * @param event
	 * @return
	 */
	boolean select(ITetraNode<D> source, TetraEvent<D> event);

	void dispose();
}