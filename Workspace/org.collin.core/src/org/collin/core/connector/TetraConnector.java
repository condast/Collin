package org.collin.core.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.graph.AbstractEdge;
import org.collin.core.graph.ICollINShape;
import org.collin.core.graph.IEdge;
import org.collin.core.operator.DefaultOperatorFactory;
import org.collin.core.operator.IOperator;
import org.collin.core.transaction.TetraTransaction;

public class TetraConnector<D extends Object> {

	private Collection<IConnectorListener<D>> listeners;

	private ConnectorFactory factory;

	private ICollINShape<D> owner;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public TetraConnector( Class<?> clss, ICollINShape<D> owner ) {
		this.owner = owner;this.listeners = new ArrayList<>();
		factory = new ConnectorFactory(clss);
	}

	public ICollINShape<D> getOwner() {
		return owner;
	}

	public boolean addConnectorListener( IConnectorListener<D> listener ) {
		return this.listeners.add( listener);
	}

	public boolean removeConnectorListener( IConnectorListener<D> listener ) {
		return this.listeners.remove( listener);
	}

	protected void notifyConnectorListeners( ConnectorEvent<D> event ) {
		for( IConnectorListener<D> listener: this.listeners )
			listener.notifyConnectorFired(event);
	}

	protected boolean isConnected( ITetraNode<D> node1, ITetraNode<D> node2) {
		for( IEdge<D> edge: owner.getEdges() ) {
			if( edge.isEqual(node1, node2))
				return true;
		}
		return false;
	}
	
	public void connect( ITetraNode<D> node1, ITetraNode<D> node2) {
		if(( node1 == null ) ||( node2 == null ))
			return;
		if(!isConnected(node1, node2)) {
			Edge edge = new Edge( getOwner() , null, null, node1, node2);
			IOperator<D> operator = factory.createOperator(null, edge);
			edge.setOperator(operator);
			owner.addEdge((IEdge<D>) edge );
		}
	}

	public boolean disconnect( ITetraNode<D> node1, ITetraNode<D> node2) {
		for( IEdge<D> connector: owner.getEdges() ) {
			if( !connector.isEqual(node1, node2))
				continue;
			return owner.removeEdge( connector );
		}
		return false;
	}

	private class Edge extends AbstractEdge<D>{

		public Edge(ICollINShape<D> owner, String id, String name, ITetraNode<D> origin, ITetraNode<D> destination ) {
			super(owner, id, name, origin, destination, null);
		}
	
		@Override
		public void setOperator(IOperator<D> operator) {
			super.setOperator(operator);
		}

		@Override
		public boolean fire(TetraTransaction<D> event) {
			return false;
		}
		
	}
	private class ConnectorFactory extends DefaultOperatorFactory<D>{

		public ConnectorFactory( Class<?> clss) {
			super( clss );
		}
		
		@Override
		public IOperator<D> createOperator(String className, IEdge<D> edge) {
			return new Connector( edge);
		}

		private class Connector extends DefaultOperator implements IOperator<D>{

			public Connector(IEdge<D> edge) {
				super((ITetraNode<D>) edge.getOrigin(), (ITetraNode<D>)edge.getDestination());
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
			public boolean select( ITetraNode<D> source, TetraEvent<D> event) {
				ITetraNode<D> node = getOther(source);
				logger.info("Event from: " + source + " to " + node);
				if( node != null ){
					ITetra<D> tetra = (ITetra<D>) node.getParent();
					if(tetra.select( source.getType(), event))
						notifyConnectorListeners(new ConnectorEvent<D>( owner, source, node ));
				}else {
					logger.severe("NULL event: " + source.getId());
				}
				return true;
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
