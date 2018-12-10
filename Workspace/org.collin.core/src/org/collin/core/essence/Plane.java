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

	public Plane( Planes plane, ITetra<D> tetra ) {
		this( plane.name() );
		switch( plane ) {
		case AMBITION:
			this.nodes.put( ITetraNode.Nodes.GOAL, tetra.getNode(ITetraNode.Nodes.GOAL));
			this.nodes.put( ITetraNode.Nodes.SOLUTION, tetra.getNode(ITetraNode.Nodes.SOLUTION));
			this.nodes.put( ITetraNode.Nodes.FUNCTION, tetra.getNode(ITetraNode.Nodes.FUNCTION));
			break;
		case LEARNING:
			this.nodes.put( ITetraNode.Nodes.GOAL, tetra.getNode(ITetraNode.Nodes.GOAL));
			this.nodes.put( ITetraNode.Nodes.TASK, tetra.getNode(ITetraNode.Nodes.TASK));
			this.nodes.put( ITetraNode.Nodes.FUNCTION, tetra.getNode(ITetraNode.Nodes.FUNCTION));
			break;
		case OPERATION:
			this.nodes.put( ITetraNode.Nodes.TASK, tetra.getNode(ITetraNode.Nodes.TASK));
			this.nodes.put( ITetraNode.Nodes.SOLUTION, tetra.getNode(ITetraNode.Nodes.SOLUTION));
			this.nodes.put( ITetraNode.Nodes.FUNCTION, tetra.getNode(ITetraNode.Nodes.FUNCTION));
			break;
		case RECOVERY:
			this.nodes.put( ITetraNode.Nodes.GOAL, tetra.getNode(ITetraNode.Nodes.GOAL));
			this.nodes.put( ITetraNode.Nodes.SOLUTION, tetra.getNode(ITetraNode.Nodes.SOLUTION));
			this.nodes.put( ITetraNode.Nodes.TASK, tetra.getNode(ITetraNode.Nodes.TASK));
			break;
		default:
			break;
		}
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public ITetraNode<D> getNode(ITetraNode.Nodes node) {
		return nodes.get(node);
	}
}