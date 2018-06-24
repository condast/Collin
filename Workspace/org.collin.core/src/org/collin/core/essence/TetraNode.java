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
	
	private int selected;

	private Collection<ITetraListener<D>> listeners;
	
	public TetraNode( String id, D data ) {
		super();
		this.id = id;
		this.selected = 0;
		this.listeners = new ArrayList<ITetraListener<D>>();
	}

	@Override
	public String getId() {
		return id;
	}


	@Override
	public D getData() {
		return this.data;
	}

	@Override
	public void addTetraListener( ITetraListener<D> listener ) {
		this.listeners.add( listener);
	}

	@Override
	public void removeTetraListener( ITetraListener<D> listener ) {
		this.listeners.remove( listener);
	}
	
	protected void notifyTetraListeners( TetraEvent<D> event ) {
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
	public void select() {
		this.selected++;
		notifyTetraListeners( new TetraEvent<D>( this, data, this.selected ));
	}
}
