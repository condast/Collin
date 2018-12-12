package org.collin.core.essence;

import java.util.EventObject;

import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringStyler;

public class TetraEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	public enum Results{
		COMPLETE(0),
		CONTINUE(1),
		FAIL(2),
		SUCCESS(3);
		
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
	
	private Results state;
	
	private TetraTransaction<D> transaction;
			
	public TetraEvent( Object source, TetraTransaction<D> transaction ) {
		this( source, Results.SUCCESS, transaction);
	}

	public TetraEvent( Object source, TetraEvent<D> event ) {
		this( source, event.getResult(), event.getTransaction());
	}

	public TetraEvent( Object source, Results state, TetraTransaction<D> transaction ) {
		super(source);
		this.state = state;
		this.transaction = transaction;
	}
	
	public Results getResult() {
		return state;
	}

	public TetraTransaction<D> getTransaction() {
		return transaction;
	}
}