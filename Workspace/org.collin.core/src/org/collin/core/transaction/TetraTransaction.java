package org.collin.core.transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashSet;

import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.graph.ICollINVertex;
import org.collin.core.graph.IEdge;
import org.condast.commons.strings.StringStyler;

public class TetraTransaction<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	public enum States{
		START,
		PROGRESS,
		COMPLETE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString( ));
		}
	}
	
	private States state;
	
	private long userId;
	
	private D data;
			
	private double progress;
	
	private Date create;
	
	private Collection<IEdge<D>> history;
	
	private Collection<ITransactionListener<D>> listeners;

	public TetraTransaction( Object source,long userId,  D data ) {
		this( source, userId, States.START, data, 0);
	}

	public TetraTransaction( Object source, long userId, States state, D data, double progress ) {
		super(source);
		this.state = state;
		this.data = data;
		this.progress = progress;
		this.history = new HashSet<>();
		this.create = Calendar.getInstance().getTime();
		this.listeners = new ArrayList<>();
	}
	
	public long getUserId() {
		return userId;
	}

	public States getState() {
		return state;
	}

	public D getData() {
		return data;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	public boolean isFinished() {
		return ( this.progress >= 100 );
	}

	public boolean addHistory( IEdge<D> node ) {
		if( this.history.contains(node))
			return false;
		return this.history.add(node);
	}
	
	@SuppressWarnings("unchecked")
	public ICollINVertex<D>[] getHistory(){
		return this.history.toArray( new ICollINVertex[ this.history.size()]);
	}
	
	public boolean hasBeenProcessed( IEdge<D> node ) {
		if( node == null )
			return true;
		return this.history.contains(node);
	}
	
	public Date getCreate() {
		return create;
	}

	public boolean addTransactionListener( ITransactionListener<D> listener ) {
		return this.listeners.add( listener);
	}

	public boolean removeTransactionListener( ITransactionListener<D> listener ) {
		return this.listeners.remove( listener);
	}
	
	/**
	 * Update the transaction. Returns true if the transaction was successfully updated,
	 * Every listener of the transaction is one of the tetra's in the compass, and only one
	 * should be able to give a result other than CONTINUE at any given time
	 * and the next node can be notified. 
	 * @param source
	 * @param event
	 * @return
	 */
	public Results updateTransaction( ICollINVertex<D> source, TetraEvent<D> event ) {
		Results result = Results.COMPLETE;
		for( ITransactionListener<D> listener: this.listeners ) {
			result = listener.transactionUpdateRequest( source, event);
			if( !Results.CONTINUE.equals(result))
				return result;
		}
		return result;
	}
}