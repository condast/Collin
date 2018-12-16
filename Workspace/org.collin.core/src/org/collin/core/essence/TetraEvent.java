package org.collin.core.essence;

import java.util.EventObject;

import org.collin.core.graph.IEdge;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringStyler;

public class TetraEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	public enum Results{
		CONTINUE(0),// Default; propagate the event without constraints
		COMPLETE(1),// Assume that the event is finished and does not need to be propagated  
		FAIL(2),    // Handling of the event failed; inform the function and other connected members
		SUCCESS(3); // Handling succeeded; inform the solution node and other connected members 
		
		private int index;
		
		private Results(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static Results getResult( boolean choice ) {
			return choice? Results.SUCCESS: Results.FAIL;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString( ));
		}
	}
	
	private Results result;
	
	private TetraTransaction<D> transaction;
			
	public TetraEvent( Object source, TetraTransaction<D> transaction ) {
		this( source, Results.SUCCESS, transaction);
	}

	public TetraEvent( Object source, TetraEvent<D> event ) {
		this( source, event.getResult(), event.getTransaction());
	}

	public TetraEvent( Object source, Results result, TetraTransaction<D> transaction ) {
		super(source);
		this.result = result;
		this.transaction = transaction;
	}
	
	public Results getResult() {
		return result;
	}

	public TetraTransaction<D> getTransaction() {
		return transaction;
	}
	
	/**
	 * Returns true if the given edge can be processed 
	 * @param edge
	 * @return
	 */
	public boolean canProcess( IEdge<D> edge ){
		if( transaction.hasBeenProcessed(edge))
			return false;
		transaction.addHistory(edge);
		return true;

	}
}