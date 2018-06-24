package org.collin.core.essence;

import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;

public class Compass<D extends Object> {

	public enum Tetras{
		PRODUCER,
		CONSUMER,
		PRODUCT,
		PROCESS,
		COACH_PRODUCT,
		COACH_PROCESS
	}

	private Map<Tetras, ITetra<D>> tetras;

	private TetraConnector<D> connectors;

	public Compass() {
		tetras = new HashMap<Tetras, ITetra<D>>();
		connectors = new TetraConnector<D>();
	}

	public void addTetra( Tetras type, ITetra<D> tetra ) {
		ITetra<D> current = this.tetras.get(type);
		if( current != null ){
			for( ITetraNode<D> nd: current.getNodes() ) {
				connectors.remove(nd);
			}
			tetras.remove(type);
		}
		ITetraNode<D> task = tetra.getNode( Nodes.TASK );
		ITetraNode<D> goal = tetra.getNode( Nodes.GOAL );
		ITetraNode<D> structure = tetra.getNode( Nodes.STRUCTURE );
		ITetra<D> other = null;
		switch( type ) {
		case PRODUCT:
			other = tetras.get(Tetras.PRODUCER);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			other = tetras.get(Tetras.PROCESS );
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			break;
		case PROCESS:
			other = tetras.get(Tetras.PRODUCT);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			other = tetras.get(Tetras.CONSUMER );
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			break;
		case CONSUMER:
			other = tetras.get(Tetras.COACH_PROCESS);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			other = tetras.get(Tetras.PROCESS);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			break;
		case COACH_PROCESS:
			other = tetras.get(Tetras.CONSUMER);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			other = tetras.get(Tetras.COACH_PRODUCT);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			break;		
		case COACH_PRODUCT:
			other = tetras.get(Tetras.PRODUCER);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			other = tetras.get(Tetras.COACH_PROCESS);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			break;
		case PRODUCER:
			other = tetras.get(Tetras.COACH_PRODUCT);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			other = tetras.get(Tetras.PRODUCT);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(structure, other.getNode( Nodes.STRUCTURE));
			break;
		default:
			break;
		}
	}
}
