package org.collin.core.essence;

import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;

public class Plane<D extends Object> implements IPlane<D> {

	private String label;
	
	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;

	public Plane( String label ) {
		super();
		this.label = label;
		nodes = new HashMap<ITetraNode.Nodes, ITetraNode<D>>();
	}

	public Plane( String label, ITetraNode.Nodes ns1, D node1, ITetraNode.Nodes ns2, D node2, ITetraNode.Nodes ns3, D node3) {
		this( label );
		this.nodes.put( ns1, new TetraNode<D>( ns1.toString(), node1 ));
		this.nodes.put( ns2, new TetraNode<D>( ns2.toString(), node2 ));
		this.nodes.put( ns3, new TetraNode<D>( ns3.toString(), node3 ));
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public ITetraNode<D> getNode(ITetraNode.Nodes node) {
		return nodes.get(node);
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.IPlane#getBalance()
	 */
	@Override
	public int getBalance() {
		int min = Integer.MAX_VALUE;
		int max = 0;
		for( ITetraNode<D> nd: this.nodes.values()) {
			if( nd.getSelected() < min )
				min = nd.getSelected();
			if( nd.getSelected() > max )
				max = nd.getSelected();
		}	
		return (max - min );
	}
}