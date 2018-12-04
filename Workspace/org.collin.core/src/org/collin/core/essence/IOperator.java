package org.collin.core.essence;

import org.collin.core.def.ITetraNode;

interface IOperator<D extends Object>{

	/**
	 * Determines the response of the event for the given node. If a
	 * false is returned, then it means that the even cannot enter the
	 * next phase
	 * @param node
	 * @param event
	 * @return
	 */
	boolean select( ITetraNode<D> node, TetraEvent<D> event);

}