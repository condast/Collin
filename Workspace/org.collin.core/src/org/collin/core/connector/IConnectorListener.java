package org.collin.core.connector;

public interface IConnectorListener<D extends Object> {

	public void notifyConnectorFired( ConnectorEvent<D> event );
}
