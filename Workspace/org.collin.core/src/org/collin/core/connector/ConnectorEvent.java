package org.collin.core.connector;

import java.util.EventObject;

import org.collin.core.def.ITetraNode;
import org.collin.core.graph.ICollINShape;

public class ConnectorEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	private ITetraNode<D> origin, destination;
	
	/**
	 * Clones the given event, but resets te state
	 * @param event
	 */
	public ConnectorEvent( ICollINShape<D> connector, ITetraNode<D> origin, ITetraNode<D> destination ) {
		super( connector );
		this.origin = origin;
		this.destination = destination;
	}
	
	@SuppressWarnings("unchecked")
	public ICollINShape<D> getConnector() {
		return (ICollINShape<D>) super.getSource();
	}

	public ITetraNode<D> getOrigin() {
		return origin;
	}

	public ITetraNode<D> getDestination() {
		return destination;
	}
}