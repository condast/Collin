package org.collin.core.essence;

import java.util.EventObject;

import org.collin.core.def.ITetraNode;

public class TetraEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	public enum States{
		START,
		COMPLETE;
	}
	
	private D data;
	
	private int selected;
	
	private ITetraNode<D> node;
	
	private States state;
	
	public TetraEvent( Object source, ITetraNode<D> node, D data, int selected ) {
		super(source);
		this.node = node;
		this.data = data;
		this.selected = selected;
		this.state = States.START;
	}

	public TetraEvent( ITetraNode<D> source, D data, int selected ) {
		this( source, source, data, selected);
	}

	public TetraEvent( ITetraNode<D> source, D data ) {
		this( source, source, data, 0);
	}

	/**
	 * Events can be propagated. This ensures that the original source is
	 * known all the time
	 * @param source
	 * @param event
	 */
	public TetraEvent( ITetraNode<D> source, TetraEvent<D> event ) {
		this( event, source, event.getData(), event.getSelected());
	}
	
	public ITetraNode<D> getNode(){
		return node;
	}
	
	public ITetraNode.Nodes getType(){
		return node.getType();
	}
	
	public D getData() {
		return data;
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public int getSelected() {
		return selected;
	}
}
