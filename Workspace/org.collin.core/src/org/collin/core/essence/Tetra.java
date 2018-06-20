package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;

public class Tetra<N extends Object> {

	private enum Planes{
		AMBITION,
		LEARNING,
		OPERATION,
		RECOVERY;
	}

	private Map<Planes, Plane> planes;
	
	private Map<ITetraNode.Nodes, Node> nodes;
	
	private ITetraNode<N> selected;

	protected Tetra() {
		planes = new HashMap<Planes, Plane>();
		nodes = new HashMap<ITetraNode.Nodes,Node>();
	}

	public Tetra(N task, N goal, N structure, N function) {
		this();
		this.planes.put( Planes.AMBITION,  new Plane( ITetraNode.Nodes.GOAL, goal, ITetraNode.Nodes.STRUCTURE, structure, ITetraNode.Nodes.FUNCTION, function  ));
		this.planes.put( Planes.LEARNING,  new Plane( ITetraNode.Nodes.GOAL, goal, ITetraNode.Nodes.TASK, task, ITetraNode.Nodes.FUNCTION, function ));
		this.planes.put( Planes.OPERATION, new Plane( ITetraNode.Nodes.STRUCTURE, structure, ITetraNode.Nodes.TASK, task, ITetraNode.Nodes.FUNCTION, function ));
		this.planes.put( Planes.RECOVERY,  new Plane( ITetraNode.Nodes.GOAL, goal, ITetraNode.Nodes.TASK, task, ITetraNode.Nodes.STRUCTURE, structure ));
	}

	public Map<Planes, Plane> getPlanes() {
		return planes;
	}
	
	public int getBalance( Planes plane ) {
		return this.planes.get(plane).getBalance();
	}
		
	public void select( ITetraNode.Nodes node ) {
		this.selected = this.nodes.get(node);
		this.selected.select();
		int low = Integer.MAX_VALUE;
		for( Node nd: this.nodes.values()) {
			if( nd.selected < low )
				low = nd.selected;
		}
		for( Node nd: this.nodes.values()) {
			nd.selected -= low;
		}
	}

	private class Plane implements IPlane{
		
		private Map<ITetraNode.Nodes, Node> nodes;

		public Plane() {
			super();
			nodes = new HashMap<ITetraNode.Nodes,Node>();
		}

		public Plane( ITetraNode.Nodes ns1, N node1, ITetraNode.Nodes ns2, N node2, ITetraNode.Nodes ns3, N node3) {
			this();
			this.nodes.put( ns1, new Node( ns1.toString(), node1 ));
			this.nodes.put( ns2, new Node( ns2.toString(), node2 ));
			this.nodes.put( ns3, new Node( ns3.toString(), node3 ));
		}
		
		@Override
		public ITetraNode<N> getNode(ITetraNode.Nodes node) {
			return nodes.get(node);
		}

		/* (non-Javadoc)
		 * @see org.collin.core.essence.IPlane#getBalance()
		 */
		@Override
		public int getBalance() {
			Collection<Node> select = this.nodes.values();
			int min = Integer.MAX_VALUE;
			int max = 0;
			for( Node nd: this.nodes.values()) {
				if( nd.selected < min )
					min = nd.selected;
				if( nd.selected > max )
					max = nd.selected;
			}	
			return (max - min );
		}
	}
	
	private class Node implements ITetraNode<N>{
		
		private String id;
		
		private N data;
		
		private int selected;

		private Collection<ITetraListener<N>> listeners;
		
		public Node( String id, N data ) {
			super();
			this.id = id;
			this.selected = 0;
			this.listeners = new ArrayList<ITetraListener<N>>();
		}
 
		@Override
		public String getId() {
			return id;
		}


		@Override
		public N getData() {
			return this.data;
		}

		@Override
		public void addTetraListener( ITetraListener<N> listener ) {
			this.listeners.add( listener);
		}

		@Override
		public void removeTetraListener( ITetraListener<N> listener ) {
			this.listeners.remove( listener);
		}
		
		protected void notifyTetraListeners( TetraEvent<N> event ) {
			for( ITetraListener<N> listener: this.listeners )
				listener.notifyNodeSelected(event);
		}

		/* (non-Javadoc)
		 * @see org.collin.core.essence.ITetraNode#select()
		 */
		@Override
		public void select() {
			this.selected++;
			notifyTetraListeners( new TetraEvent<N>( this, data, this.selected ));
		}
	}
}