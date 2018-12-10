package org.collin.core.transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashSet;

import org.collin.core.def.ICollINSelector;
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
	
	private D data;
			
	private double progress;
	
	private Date create;
	
	private Collection<ICollINSelector<D>> history;
	
	private Collection<ITransactionListener<D>> listeners;

	public TetraTransaction( Object source, D data ) {
		this( source, States.START, data, 0);
	}

	public TetraTransaction( Object source, States state, D data, double progress ) {
		super(source);
		this.state = state;
		this.data = data;
		this.progress = progress;
		this.history = new HashSet<>();
		this.create = Calendar.getInstance().getTime();
		this.listeners = new ArrayList<>();
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

	public boolean addHistory( ICollINSelector<D> node ) {
		if( hasBeenProcessed(node))
			return false;
		return this.history.add(node);
	}
	
	@SuppressWarnings("unchecked")
	public ICollINSelector<D>[] getHistory(){
		return this.history.toArray( new ICollINSelector[ this.history.size()]);
	}
	
	public boolean hasBeenProcessed( ICollINSelector<D> node ) {
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
	 * and the next nod can be notified. 
	 * @param source
	 * @param event
	 * @return
	 */
	public boolean updateTransaction( ICollINSelector<D> source, TetraTransaction<D> event ) {
		boolean result = false;
		for( ITransactionListener<D> listener: this.listeners )
			result |= listener.transactionUpdateRequest( source, event);
		return result;
	}
}