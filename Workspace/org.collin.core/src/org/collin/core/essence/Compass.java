package org.collin.core.essence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.collin.core.connector.IConnectorListener;
import org.collin.core.connector.TetraConnector;
import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;
import org.collin.core.graph.AbstractShape;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.Utils;
import org.condast.commons.strings.StringStyler;

public class Compass<D extends Object> extends AbstractShape<D>{

	public enum Tetras{
		UNDEFINED,
		PRODUCER,
		CONSUMER,
		PRODUCT,
		PROCESS,
		COACH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	private Map<Tetras, ITetra<D>> tetras;

	private TetraConnector<D> connectors;
	
	private List<Compass<D>> children;
	
	private Compass<D> parent;
	
	private int progress;
	
	private ITetraListener<D> listener = new ITetraListener<D>() {

		@Override
		public void notifyNodeSelected( Object source, TetraEvent<D> event) {
			if( progress >= children.size())
				notifyListeners( event);	
			else {
				Compass<D> child = children.get(++progress);
				child.fire(event.getTransaction());
			}
		}
		
	};
	
	public Compass( Class<?> clss, String id, String title) {
		this( clss, null, id, title );
	}
	
	public Compass( Class<?> clss, Compass<D> parent, String id, String title) {
		super( id, title );
		this.parent = parent;
		tetras = new LinkedHashMap<Tetras, ITetra<D>>();
		connectors = new TetraConnector<D>( clss, this );
		children = new ArrayList<>();
		super.addCollINListener(listener);
		this.progress = 0;
	}

	public void init() {
		for( Tetras tetra: Tetras.values()) {
			if( Tetras.UNDEFINED.equals(tetra) || 
					this.tetras.containsKey(tetra))
				continue;
			String tid = createId( this, tetra );
			ITetra<D> nt = new Tetra<>( this, tid, tetra.toString(),  ITetraNode.Nodes.UNDEFINED );
			nt.init();
			addTetra(tetra, nt);
		}
		for( Tetras tetra: Tetras.values()) {
			if( Tetras.UNDEFINED.equals(tetra))
				continue;
			ITetra<D> tn = this.tetras.get( tetra);
			addConnector(tetra, tn);
		}
	}
	
	public Compass<D> getParent() {
		return parent;
	}

	public void addConnectorListener( IConnectorListener<D> listener ) {
		this.connectors.addConnectorListener(listener);
	}

	public void removeConnectorListener( IConnectorListener<D> listener ) {
		this.connectors.removeConnectorListener(listener);
	}

	public void addChild( Compass<D> child ) {
		this.children.add(child);
		child.addCollINListener(listener);
	}

	public void removeChild( Compass<D> child ) {
		this.children.remove(child);
		child.removeCollINListener(listener);
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
		if( Tetras.UNDEFINED.equals( type ))
			return;
		this.tetras.put(type, tetra);
	}

	protected boolean addConnector( Tetras type, ITetra<D> tetra ) {
		if( this.tetras.size() <=1)
			return false;
		ITetraNode<D> task = tetra.getNode( Nodes.TASK );
		ITetraNode<D> goal = tetra.getNode( Nodes.GOAL );
		ITetra<D> other = null;
		switch( type ) {
		case CONSUMER:
			other = tetras.get(Tetras.PROCESS);
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
			}
			other = tetras.get(Tetras.COACH);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
			}
			break;
		case COACH:
			other = tetras.get(Tetras.PRODUCER);
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
			}
			other = tetras.get(Tetras.CONSUMER);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
			}
			break;
		case PRODUCER:
			other = tetras.get(Tetras.PRODUCT);
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
			}
			other = tetras.get(Tetras.COACH);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
			}
			break;
		case PRODUCT:
			other = tetras.get(Tetras.PROCESS );
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
			}
			other = tetras.get(Tetras.PRODUCER);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
			}
			break;			
		case PROCESS:
			other = tetras.get(Tetras.CONSUMER );
			if( other != null ) {
				connectors.connect(goal, other.getNode( Nodes.TASK));
			}
			other = tetras.get(Tetras.PRODUCT);
			if( other != null ) {
				connectors.connect(task, other.getNode( Nodes.GOAL));
			}
			break;
		default:
			break;
		}
		return true;
	}

	public void removeTetra( Tetras type ) {
		ITetra<D> current = this.tetras.get(type);
		if( current != null ){
			for( ITetraNode<D> nd: current.getNodes() ) {
				removeVertex(nd);
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

	public boolean fire( Tetras type, TetraTransaction<D> event ) {
		ITetra<D> tetra = null;
		if( this.children.isEmpty() ) {
			tetra = tetras.get(type);
		}else {
			Compass<D> child = this.children.iterator().next(); 
			tetra = child.getTetra(type);
		}		
		return tetra.fire( event );
	}

	@Override
	public boolean fire( TetraTransaction<D> event ) {
		return fire( Tetras.CONSUMER, event );
	}

	@Override
	public String toString() {
		return super.getId();
	}
	
	public static String createId( Compass<?> compass, Tetras tetra ) {
		return compass.getId() + "." + StringStyler.toMethodString(tetra.name());
	}
}