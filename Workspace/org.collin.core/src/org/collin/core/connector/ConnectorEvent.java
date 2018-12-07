package org.collin.core.connector;

import java.util.EventObject;

import org.collin.core.def.ITetraNode;

public class ConnectorEvent<O, D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	private ITetraNode<D> origin, destination;
	
	/**
	 * Clones the given event, but resets te state
	 * @param event
	 */
	public ConnectorEvent( TetraConnector<O,D>connector, ITetraNode<D> origin, ITetraNode<D> destination ) {
		super( connector );
		this.origin = origin;
		this.destination = destination;
	}
	
	@SuppressWarnings("unchecked")
	public TetraConnector<O,D> getConnector() {
		return (TetraConnector<O,D>) super.getSource();
	}

	public ITetraNode<D> getOrigin() {
		return origin;
	}

	public ITetraNode<D> getDestination() {
		return destination;
	}
}