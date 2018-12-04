package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;
import org.condast.commons.Utils;

public class Compass<D extends Object> {

	public enum Tetras{
		UNDEFINED,
		PRODUCER,
		CONSUMER,
		PRODUCT,
		PROCESS,
		COACH;
	}

	private Map<Tetras, ITetra<D>> tetras;

	private TetraConnector<D> connectors;
	
	private List<Compass<D>> children;
	
	private Compass<D> parent;
	
	private int progress;
	
	private String description;
	
	private ITetraListener<D> listener = new ITetraListener<D>() {

		@Override
		public void notifyNodeSelected(TetraEvent<D> event) {
			if( !TetraEvent.States.COMPLETE.equals( event.getState()))
				return;
			if( progress >= children.size())
				notifyListeners(event);	
			else {
				Compass<D> child = children.get(++progress);
				child.fire(event);
			}
		}
		
	};
	
	private Collection<ITetraListener<D>> listeners;
	
	private String title;
	private String id;

	public Compass( String id, String title) {
		this( null, id, title );
	}
	
	public Compass( Compass<D> parent, String id, String title) {
		this.parent = parent;
		this.id = id;
		this.title = title;
		tetras = new HashMap<Tetras, ITetra<D>>();
		connectors = new TetraConnector<D>();
		children = new ArrayList<>();
		listeners = new ArrayList<>();
		listeners.add(listener);
		this.progress = 0;
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Compass<D> getParent() {
		return parent;
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
	
	public boolean hasChildren() {
		return !Utils.assertNull(children);
	}
	@SuppressWarnings("unchecked")
	public Compass<D>[] getChildren() {
		return this.children.toArray( new Compass[this.children.size()] );
	}

	public void addTetra( Tetras type, ITetra<D> tetra ) {
		removeTetra(type);
		this.tetras.put(type, tetra);
		if( this.tetras.size() <=1)
			return;
		ITetraNode<D> task = tetra.getNode( Nodes.TASK );
		ITetraNode<D> goal = tetra.getNode( Nodes.GOAL );
		ITetraNode<D> solution = tetra.getNode( Nodes.SOLUTION );
		ITetra<D> other = null;
		switch( type ) {
		case PRODUCT:
			other = tetras.get(Tetras.PRODUCER);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			other = tetras.get(Tetras.PROCESS );
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			break;
		case PROCESS:
			other = tetras.get(Tetras.PRODUCT);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			other = tetras.get(Tetras.CONSUMER );
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			break;
		case CONSUMER:
			other = tetras.get(Tetras.COACH);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			other = tetras.get(Tetras.PROCESS);
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			break;
		case COACH:
			other = tetras.get(Tetras.PRODUCER);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			other = tetras.get(Tetras.CONSUMER);
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			break;
		case PRODUCER:
			other = tetras.get(Tetras.COACH);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
			other = tetras.get(Tetras.PRODUCT);
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
				connectors.connect(solution, other.getNode( Nodes.SOLUTION));
			}
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

	@SuppressWarnings("unchecked")
	public ITetra<D>[] getTetras(){
		return this.tetras.values().toArray( new ITetra[ this.tetras.size()]);
	}

	public void fire( Tetras type, TetraEvent<D> event ) {
		ITetra<D> tetra = null;
		if( this.children.isEmpty() ) {
			tetra = tetras.get(type);
		}else {
			Compass<D> child = this.children.iterator().next(); 
			tetra = child.getTetra(type);
		}
		tetra.select( new TetraEvent<D>( this, event.getData() ));
	}

	public void fire( TetraEvent<D> event ) {
		fire( Tetras.CONSUMER, event );
	}

	@Override
	public String toString() {
		return this.id;
	}
}
