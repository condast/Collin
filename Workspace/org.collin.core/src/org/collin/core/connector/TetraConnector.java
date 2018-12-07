package org.collin.core.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.ITetraListener;
import org.collin.core.essence.TetraEvent;

public class TetraConnector<O,D extends Object> {

	private Collection<Connector> connectors;
	
	private Collection<IConnectorListener<O,D>> listeners;
	
	private TetraConnector<O,D> connector;
	
	private O owner;
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
		
	public TetraConnector( O owner ) {
		this.owner = owner;
		this.connector = this;
		connectors = new ArrayList<Connector>();
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

	public void connect( ITetraNode<D> node1, ITetraNode<D> node2) {
		connectors.add( new Connector( node1, node2 ));
	}

	public boolean disconnect( ITetraNode<D> node1, ITetraNode<D> node2) {
		for( Connector connector: this.connectors ) {
			if( !connector.isEqual(node1, node2))
				continue;
			return this.connectors.remove( connector );
		}
		return false;
	}

	public boolean remove( ITetraNode<D> node1 ) {
		Collection<Connector> temp = new ArrayList<Connector>();
		for( Connector connector: this.connectors ) {
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

	private class Connector implements IConnector<O,D>{
		
		private ITetraNode<D>[] nodes;
		
		private ITetraListener<D> listener = new ITetraListener<D>() {

			@Override
			public void notifyNodeSelected(TetraEvent<D> event) {
				ITetraNode<D> node = getOther(event.getPropagate());
				logger.info("Event from: " + event.getPropagate() + " to " + node);
				if( node != null ){
					ITetra<D> tetra = node.getParent();
					if(tetra.select( new TetraEvent<D>(event)))
						notifyConnectorListeners(new ConnectorEvent<O,D>( connector, event.getPropagate(), node ));
				}else {
					logger.severe("NULL event: " + event.getPropagate().getId());
				}
			}
		};

		@SuppressWarnings("unchecked")
		public Connector( ITetraNode<D> nd1, ITetraNode<D> nd2) {
			nodes = new ITetraNode[2];
			nodes[0] = nd1;
			nd1.addTetraListener(listener);
			nodes[1] = nd2;
			nd2.addTetraListener(listener);
		}
		
		@Override
		public TetraConnector<O,D> getOwner() {
			return connector;
		}
		
		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#contains(org.collin.core.def.ITetraNode)
		 */
		@Override
		public boolean contains( ITetraNode<D> node ) {
			return nodes[0].equals(node ) || nodes[1].equals( node );
		}
		
		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#isEqual(org.collin.core.def.ITetraNode, org.collin.core.def.ITetraNode)
		 */
		@Override
		public boolean isEqual( ITetraNode<D> node1, ITetraNode<D> node2 ) {
			boolean result = nodes[0].equals(node1) && nodes[1].equals( node2 );
			return result? result: nodes[0].equals(node2) && nodes[1].equals( node1 );
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#getOther(org.collin.core.def.ITetraNode)
		 */
		@Override
		public ITetraNode<D> getOther( ITetraNode<D> node ) {
			return nodes[0].equals(node)? nodes[1]: nodes[1].equals( node )?nodes[0]: null;
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#dispose()
		 */
		@Override
		public void dispose() {
			nodes[0].removeTetraListener(listener);
			nodes[1].removeTetraListener(listener);
		}

		@Override
		public String toString() {
			return nodes[0].getName() + "(" + nodes[0].getType()  + ")-" + 
					nodes[1].getName() + "(" +	nodes[1].getType() + ")";
		}
	}
}
