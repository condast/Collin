package org.collin.core.essence;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.collin.core.graph.ICollINShape;
import org.collin.core.operator.DefaultOperatorFactory;
import org.collin.core.operator.IOperatorFactory;

public class Plane<D extends Object> extends TetraShape<IPlane.Planes, D> implements IPlane<D> {

	public Plane( String id, String name, IPlane.Planes type ) {
		this( null, id, name, type );
	}

	public Plane( ICollINShape<D> parent, IPlane.Planes type ) {
		this( parent, new DefaultOperatorFactory<D>(), type.name(), type.toString(), type );
	}

	public Plane( ICollINShape<D> parent, String id, String name, IPlane.Planes type ) {
		this( parent, new DefaultOperatorFactory<D>(), id, name, type );
	}
		
	public Plane( ICollINShape<D> parent, IOperatorFactory<D> factory, String id, String name, IPlane.Planes type ) {
		super( parent, factory, id, name, type );
	}

	@Override
	public void init() {
		//First complete the nodes
		super.init();
					
		switch( super.getType() ) {
		case AMBITION:
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.FUNCTION ), getNode(ITetraNode.Nodes.GOAL )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.FUNCTION ), getNode(ITetraNode.Nodes.SOLUTION )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.GOAL ), getNode(ITetraNode.Nodes.SOLUTION)));
			break;
		case LEARNING:
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.FUNCTION ), getNode(ITetraNode.Nodes.GOAL )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.FUNCTION ), getNode(ITetraNode.Nodes.TASK )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.GOAL ), getNode(ITetraNode.Nodes.TASK)));
			break;
		case OPERATION:
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.FUNCTION ), getNode(ITetraNode.Nodes.SOLUTION )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.FUNCTION ), getNode(ITetraNode.Nodes.TASK )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.SOLUTION ), getNode(ITetraNode.Nodes.TASK)));
			break;
		case RECOVERY:
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.SOLUTION ), getNode(ITetraNode.Nodes.GOAL )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.SOLUTION ), getNode(ITetraNode.Nodes.TASK )));
			super.addEdge( new Edge( this, getNode( ITetraNode.Nodes.GOAL ), getNode(ITetraNode.Nodes.TASK)));
			break;
		default:
			break;
		}
	}
}