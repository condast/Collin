package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;

public class TetraConnector<D extends Object> {

	private IPlane<D>[] planes;
	
	private Collection<Connector> connectors;
		
	@SuppressWarnings("unchecked")
	public TetraConnector( IPlane<D> first, IPlane<D> second ) {
		connectors = new ArrayList<Connector>();
		planes = new IPlane[2];
		planes[0] = first;
		planes[1] = second;
	}
	
	public void connect( ITetraNode.Nodes node1, ITetraNode.Nodes node2) {
		ITetraNode<D> nd1 = planes[0].getNode(node1);
		ITetraNode<D> nd2 = planes[1].getNode(node2);
		connectors.add( new Connector( nd1, nd2 ));
	}

	public boolean disconnect( ITetraNode<D> node1, ITetraNode<D> node2) {
		for( Connector connector: this.connectors ) {
			if( !connector.isEqual(node1, node2))
				continue;
			return this.connectors.remove( connector );
		}
		return false;
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
				other.select();
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
