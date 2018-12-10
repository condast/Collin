package org.collin.core.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.operator.DefaultOperatorFactory;
import org.collin.core.operator.IOperator;
import org.collin.core.transaction.TetraTransaction;

public class TetraConnector<O,D extends Object> {

	private Collection<IConnector<O,D>> connectors;

	private Collection<IConnectorListener<O,D>> listeners;

	private TetraConnector<O,D> connector;
	private ConnectorFactory factory;

	private O owner;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public TetraConnector( Class<?> clss, O owner ) {
		this.owner = owner;
		this.connector = this;
		factory = new ConnectorFactory(clss);
		connectors = new ArrayList<>();
		this.listeners = new ArrayList<>();
	}

	public O getOwner() {
		return owner;
	}

	public boolean addConnectorListener( IConnectorListener<O,D> listener ) {
		return this.listeners.add( listener);
	}

	public boolean removeConnectorListener( IConnectorListener<O,D> listener ) {
		return this.listeners.remove( listener);
	}

	protected void notifyConnectorListeners( ConnectorEvent<O,D> event ) {
		for( IConnectorListener<O,D> listener: this.listeners )
			listener.notifyConnectorFired(event);
	}

	@SuppressWarnings("unchecked")
	public void connect( ITetraNode<D> node1, ITetraNode<D> node2) {
		if(( node1 == null ) ||( node2 == null ))
			return;
		connectors.add( (IConnector<O, D>) factory.createOperator(null, node1, node2 ));
	}

	public boolean disconnect( ITetraNode<D> node1, ITetraNode<D> node2) {
		for( IConnector<O,D> connector: this.connectors ) {
			if( !connector.isEqual(node1, node2))
				continue;
			return this.connectors.remove( connector );
		}
		return false;
	}

	public boolean remove( ITetraNode<D> node1 ) {
		Collection<IConnector<O,D>> temp = new ArrayList<>();
		for( IConnector<O,D> connector: this.connectors ) {
			if( !connector.contains( node1 ))
				temp.add( connector );
		}
		return this.connectors.removeAll( temp );
	}
	@SuppressWarnings("unchecked")
	public IConnector<O,D>[] getConnectors() {
		return this.connectors.toArray( new IConnector[ this.connectors.size()]);
	}

	public void dispose() {
		for( IConnector<O,D> connector: this.connectors )
			connector.dispose();
	}

	private class ConnectorFactory extends DefaultOperatorFactory<D>{

		public ConnectorFactory( Class<?> clss) {
			super( clss );
		}
		
		@Override
		public IOperator<D> createOperator(String className, ITetraNode<D> origin, ITetraNode<D> destination) {
			return new Connector( origin, destination);
		}

		private class Connector extends DefaultOperator implements IConnector<O,D>{

			public Connector(ITetraNode<D> origin, ITetraNode<D> destination) {
				super(origin, destination);
			}

			@Override
			public TetraConnector<O,D> getOwner() {
				return connector;
			}

			/* (non-Javadoc)
			 * @see org.collin.core.connector.IConnector#isEqual(org.collin.core.def.ITetraNode, org.collin.core.def.ITetraNode)
			 */
			@Override
			public boolean isEqual( ITetraNode<D> node1, ITetraNode<D> node2 ) {
				boolean result = origin.equals(node1) && destination.equals( node2 );
				return result? result: origin.equals(node2) && destination.equals( node1 );
			}

			@Override
			public boolean select( ITetraNode<D> source, TetraTransaction<D> event) {
				ITetraNode<D> node = getOther(source);
				logger.info("Event from: " + source + " to " + node);
				if( node != null ){
					ITetra<D> tetra = node.getParent();
					if(tetra.select( source.getType(), event))
						notifyConnectorListeners(new ConnectorEvent<O,D>( connector, source, node ));
				}else {
					logger.severe("NULL event: " + source.getId());
				}
				return true;///return super.select(event);
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean contains(ITetraNode<D> node) {
				// TODO Auto-generated method stub
				return false;
			}
		}
	}
}
