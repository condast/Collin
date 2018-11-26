package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.def.ITetraNode;

public class TetraConnector<D extends Object> {

	private Collection<Connector> connectors;
		
	public TetraConnector() {
		connectors = new ArrayList<Connector>();
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

	public void dispose() {
		for( Connector connector: this.connectors )
			connector.dispose();
	}

	private class Connector{
		
		private ITetraNode<D>[] nodes;
		
		private boolean block;

		private ITetraListener<D> listener = new ITetraListener<D>() {

			@SuppressWarnings("unchecked")
			@Override
			public void notifyNodeSelected(TetraEvent<D> event) {
				if( block )
					return;
				
				//Prevent a double selection event
				block = true;
				ITetraNode<D> other = getOther( (ITetraNode<D>) event.getSource() );
				other.select( event );
				block = false;
			}
		};

		@SuppressWarnings("unchecked")
		public Connector( ITetraNode<D> nd1, ITetraNode<D> nd2) {
			nodes = new ITetraNode[2];
			this.block = false;
			nodes[0] = nd1;
			nd1.addTetraListener(listener);
			nodes[1] = nd2;
			nd2.addTetraListener(listener);
		}
		
		public boolean contains( ITetraNode<D> node ) {
			return nodes[0].equals(node ) || nodes[1].equals( node );
		}
		
		public boolean isEqual( ITetraNode<D> node1, ITetraNode<D> node2 ) {
			boolean result = nodes[0].equals(node1) && nodes[1].equals( node2 );
			return result? result: nodes[0].equals(node2) && nodes[1].equals( node1 );
		}

		public ITetraNode<D> getOther( ITetraNode<D> node ) {
			return nodes[0].equals(node)? nodes[1]: nodes[1].equals( node )?nodes[0]: null;
		}

		public void dispose() {
			nodes[0].removeTetraListener(listener);
			nodes[1].removeTetraListener(listener);
		}
	}
}
