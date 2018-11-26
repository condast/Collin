package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.def.ITetraNode;

/**
 * A Tetra node is the core atom of tetralogic. It serves as a placeholder for the tetra. 
 * Note that the data object can itself be a tetra!
 * 
 * @author Condast
 *
 * @param <D>
 */
public class TetraNode<D extends Object> implements ITetraNode<D>{

	private String id;
	
	private D data;
	
	private ITetraNode.Nodes type;
	
	private ITetra<D> parent;

	private int selected;

	private Collection<ITetraListener<D>> listeners;
		
	public TetraNode( ITetra<D> parent, String id, ITetraNode.Nodes type, D data ) {
		super();
		this.parent = parent;
		this.id = id;
		this.type = type;
		this.selected = 0;
		this.listeners = new ArrayList<ITetraListener<D>>();
	}

	public TetraNode(ITetra<D> parent, String id, ITetraNode.Nodes type) {
		this( parent, id, type, null );
	}

	protected TetraNode(String id, ITetraNode.Nodes type) {
		this( null, id, type, null );
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Nodes getType() {
		return type;
	}

	@Override
	public D getData() {
		return this.data;
	}

	@Override
	public ITetra<D> getParent() {
		return parent;
	}

	@Override
	public boolean addTetraListener( ITetraListener<D> listener ) {
		return this.listeners.add( listener);
	}

	@Override
	public boolean removeTetraListener( ITetraListener<D> listener ) {
		return this.listeners.remove( listener);
	}
	
	void notifyTetraListeners( TetraEvent<D> event ) {
		for( ITetraListener<D> listener: this.listeners )
			listener.notifyNodeSelected(event);
	}

	@Override
	public int getSelected() {
		return selected;
	}
	
	@Override
	public int balance( int offset ) {
		this.selected -= offset;
		return this.selected;
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetraNode#select()
	 */
	@Override
	public void select( TetraEvent<D> event ) {
		this.selected++;
		notifyTetraListeners( event );
	}
}
