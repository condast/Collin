package org.collin.core.essence;

import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import org.collin.core.def.ITetraNode;

public class TetraEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	public enum States{
		UNDEFINED(0),
		START(1),
		GOAL(2),
		TASK(3),
		SOLUTION(4),   
		COMPLETE(9);
		
		private int index;

		private States( int index ) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
		
		public static States getState( int index ) {
			for( States node: values()) {
				if( node.getIndex() == index )
					return node;
			}
			return States.UNDEFINED;
		}

		public static States next( States current ) {
			int next = (current.getIndex() + 1);
			return getState( next );
		}


	}
	
	private D data;
		
	private States state;
	
	private double progress;
	
	private Date create;
	
	private ITetraNode<D> propagate;
	
	private Map<ITetraNode<D>, States> history;

	public TetraEvent( Object source, D data ) {
		this( source, data, 0);
	}

	/**
	 * Clones the given event, but resets te state
	 * @param event
	 */
	public TetraEvent( TetraEvent<D> event ) {
		this( event.getSource(), event.getData(), event.getProgress());
		this.history = new LinkedHashMap<>( event.history );
		this.create = event.create;
	}
	
	public TetraEvent( Object source, D data, double progress ) {
		super(source);
		this.data = data;
		this.state = States.START;
		this.progress = progress;
		this.history = new LinkedHashMap<>();//preserves insertion order
		this.create = Calendar.getInstance().getTime();
	}
	
	public D getData() {
		return data;
	}

	public ITetraNode<D> getPropagate() {
		return propagate;
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public void addHistory( ITetraNode<D> node ) {
		this.propagate = node;
		this.history.put(node, this.state);
	}
	
	@SuppressWarnings("unchecked")
	public ITetraNode<D>[] getHistory(){
		return this.history.keySet().toArray( new ITetraNode[ this.history.size()]);
	}
	
	public States getState( ITetraNode<D> node ) {
		return this.history.get(node);
	}
	
	public boolean hasBeenProcessed( ITetraNode<D> node ) {
		return node.equals(propagate) || this.history.containsKey(node);
	}
	
	public Date getCreate() {
		return create;
	}
}