package org.collin.core.essence;

import org.collin.core.def.ITetraNode;

public class DefaultOperator<D extends Object> implements IOperator<D>{

	protected DefaultOperator() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.IOperator#select(org.collin.core.essence.TetraEvent)
	 */
	@Override
	public boolean select( ITetraNode<D> node, TetraEvent<D> event ) {
		event.setState( TetraEvent.States.next( event.getState() ));
		node.select(event);
		return true;
	}
}
