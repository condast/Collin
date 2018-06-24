package org.collin.core.essence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;

public class Tetra<D extends Object> implements ITetra<D> {

	private String label;
	
	private Map<Planes, IPlane<D>> planes;
	
	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;
	
	private ITetraNode<D> selected;

	protected Tetra( String label ) {
		this.label = label;
		planes = new HashMap<Planes, IPlane<D>>();
		nodes = new HashMap<ITetraNode.Nodes, ITetraNode<D>>();
	}

	@Override
	public String getLabel() {
		return label;
	}


	public void addNode( Planes type, Plane<D> plane ) {
		this.planes.put(type, plane );
	}
	
	@Override
	public ITetraNode<D> getNode( ITetraNode.Nodes type ) {
		return this.nodes.get(type);
	}
	
	@Override
	public Collection<ITetraNode<D>> getNodes() {
		return this.nodes.values();
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetra#getPlanes()
	 */
	@Override
	public Map<Planes, IPlane<D>> getPlanes() {
		return planes;
	}
	
	public int getBalance( Planes plane ) {
		return this.planes.get(plane).getBalance();
	}
		
	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetra#select(org.collin.core.def.ITetraNode.Nodes)
	 */
	@Override
	public void select( ITetraNode.Nodes node ) {
		this.selected = this.nodes.get(node);
		this.selected.select();
		int low = Integer.MAX_VALUE;
		for( ITetraNode<D> nd: this.nodes.values()) {
			if( nd.getSelected() < low )
				low = nd.getSelected();
		}
		for( ITetraNode<D> nd: this.nodes.values()) {
			nd.balance( low );
		}
	}		
}