package org.collin.core.connector;

public interface IConnectorListener<O,D extends Object> {

	public void notifyConnectorFired( ConnectorEvent<O,D> event );
}
