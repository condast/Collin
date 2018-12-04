package org.collin.core.essence;

import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;

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

	public TetraEvent( Object source, D data ) {
		this( source, data, 0);
	}

	public TetraEvent( Object source, D data, double progress ) {
		super(source);
		this.data = data;
		this.state = States.START;
		this.progress = progress;
		this.create = Calendar.getInstance().getTime();
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

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public Date getCreate() {
		return create;
	}
}