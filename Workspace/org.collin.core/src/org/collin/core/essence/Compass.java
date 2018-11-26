package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
	
	private List<Compass<D>> children;
	
	private int progress;
	
	private ITetraListener<D> listener = new ITetraListener<D>() {

		@Override
		public void notifyNodeSelected(TetraEvent<D> event) {
			if( !ITetraNode.Nodes.COMPLETE.equals( event.getType()))
				return;
			if( progress >= children.size())
				notifyListeners(event);	
			else {
				Compass<D> child = children.get(++progress);
				child.start(event);
			}
		}
		
	};
	
	private Collection<ITetraListener<D>> listeners;

	public Compass() {
		tetras = new HashMap<Tetras, ITetra<D>>();
		connectors = new TetraConnector<D>();
		children = new ArrayList<>();
		listeners = new ArrayList<>();
		listeners.add(listener);
		this.progress = 0;
	}

	public void addListener( ITetraListener<D> listener ) {
		this.listeners.add(listener);
	}

	public void removeListener( ITetraListener<D> listener ) {
		this.listeners.remove(listener);
	}
	
	protected void notifyListeners( TetraEvent<D> event ) {
		for( ITetraListener<D> listener: this.listeners)
			listener.notifyNodeSelected(event);
	}

	public void addChild( Compass<D> child ) {
		this.children.add(child);
		child.addListener(listener);
	}

	public void removeChild( Compass<D> child ) {
		this.children.remove(child);
		child.removeListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	protected Compass<D>[] getChildren() {
		return this.children.toArray( new Compass[this.children.size()] );
	}

	public void addTetra( Tetras type, ITetra<D> tetra ) {
		removeTetra(type);
		ITetraNode<D> task = tetra.getNode( Nodes.TASK );
		ITetraNode<D> goal = tetra.getNode( Nodes.GOAL );
		ITetraNode<D> solution = tetra.getNode( Nodes.SOLUTION );
		ITetra<D> other = null;
		switch( type ) {
		case PRODUCT:
			other = tetras.get(Tetras.PRODUCER);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			other = tetras.get(Tetras.PROCESS );
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			break;
		case PROCESS:
			other = tetras.get(Tetras.PRODUCT);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			other = tetras.get(Tetras.CONSUMER );
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			break;
		case CONSUMER:
			other = tetras.get(Tetras.COACH_PROCESS);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			other = tetras.get(Tetras.PROCESS);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			break;
		case COACH_PROCESS:
			other = tetras.get(Tetras.CONSUMER);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			other = tetras.get(Tetras.COACH_PRODUCT);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			break;		
		case COACH_PRODUCT:
			other = tetras.get(Tetras.PRODUCER);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			other = tetras.get(Tetras.COACH_PROCESS);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			break;
		case PRODUCER:
			other = tetras.get(Tetras.COACH_PRODUCT);
			connectors.connect(task, other.getNode( Nodes.GOAL));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			other = tetras.get(Tetras.PRODUCT);
			connectors.connect(goal, other.getNode( Nodes.TASK));
			connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			break;
		default:
			break;
		}
	}

	public void removeTetra( Tetras type ) {
		ITetra<D> current = this.tetras.get(type);
		if( current != null ){
			for( ITetraNode<D> nd: current.getNodes() ) {
				connectors.remove(nd);
			}
			tetras.remove(type);
		}
	}
	
	public ITetra<D> getTetra( Tetras type ){
		return this.tetras.get(type);
	}
	
	public void start( TetraEvent<D> event ) {
		ITetra<D> tetra = null;
		if( this.children.isEmpty() ) {
			tetra = tetras.get(Tetras.CONSUMER);
		}else {
			Compass<D> child = this.children.iterator().next(); 
			tetra = child.getTetra(Tetras.CONSUMER);
		}
		tetra.select( new TetraEvent<D>( this, null, null, 0));
	}
}
