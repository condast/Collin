package org.collin.core.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDelegateFactory;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.ITetraListener;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.graph.AbstractCollINVertex;
import org.collin.core.graph.ICollINVertex;
import org.collin.core.transaction.ITransactionListener;
import org.collin.core.transaction.TetraTransaction;

public abstract class AbstractTetraImplementation<N,D extends Object> extends AbstractCollINVertex<D> implements ITetraImplementation<N,D> {

	//The data needed to build the tetra
	private N data;
	
	private ITetra<D> tetra;
	
	private Map<ICollINVertex<D>, Integer> progress;
	
	private IDelegateFactory<N,D> factory;

	private Map<ITetraNode<D>, ICollINDelegate<N,D>> delegates;

	private ITransactionListener<D> tlistener = new ITransactionListener<D>() {
	
		@Override
		public TetraEvent.Results transactionUpdateRequest(ICollINVertex<D> source, TetraEvent<D> event) {
			if( source instanceof ITetraNode ) {
				ITetraNode<D> tnode = (ITetraNode<D>) source;
				if( !tetra.equals(tnode.getParent()))
					return Results.CONTINUE;
			}
			if( TetraEvent.Results.COMPLETE.equals( event.getResult()))
				return event.getResult();
			
			TetraEvent.Results result = TetraEvent.Results.COMPLETE;
			if( source instanceof ITetraNode ) {
				ITetraNode<D> node = (ITetraNode<D>) source;
				switch( node.getType() ) {
				case FUNCTION:
					result = onCallFunction(node, event);
					break;
				case GOAL:
					result = onCallGoal(node, event);
					break;
				case TASK:
					result = onCallTask(node, event);
					break;
				case SOLUTION:
					result = onCallFunction(node, event);
					break;
				default:
					break;
				}
				return result;
			}
			return onTransactionUpdateRequest(event );
		}
	};

	private ITetraListener<D> listener = new ITetraListener<D>() {

		@Override
		public void notifyNodeSelected(Object source, TetraEvent<D> event) {
			onTetraEventReceived(event );
		}
	};
	
	public AbstractTetraImplementation( ITetra<D> tetra, N data, IDelegateFactory<N,D> factory) {
		this( tetra.getId(), tetra.getName(), tetra, data, factory );
	}
	
	public AbstractTetraImplementation( String id, String name, ITetra<D> tetra, N data, IDelegateFactory<N,D> factory) {
		super(id, name, null);
		this.tetra = tetra;
		this.data = data;
		this.tetra.addCollINListener(listener);
		progress = new LinkedHashMap<>();
		delegates = new HashMap<>(); 
		this.factory = factory;
	}

	protected ITetra<D> getTetra() {
		return tetra;
	}

	/**
	 * Data needed to build the tetra
	 * @return
	*/
	@Override
	public N getData() {
		return data;
	}

	protected IDelegateFactory<N,D> getFactory() {
		return factory;
	}

	protected abstract TetraEvent.Results onCallFunction( ITetraNode<D> node, TetraEvent<D> event );
	protected abstract TetraEvent.Results onCallGoal( ITetraNode<D> node, TetraEvent<D> event );
	protected abstract TetraEvent.Results onCallTask( ITetraNode<D> node, TetraEvent<D> event );
	protected abstract TetraEvent.Results onCallSolution( ITetraNode<D> node, TetraEvent<D> event );

	protected abstract TetraEvent.Results onTransactionUpdateRequest( TetraEvent<D> event );

	protected abstract void onTetraEventReceived( TetraEvent<D> event );
	
	public int getSelected( ICollINVertex<D> selector ) {
		return progress.get(selector);
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.util.ITetraImplementation#fire(org.collin.core.transaction.TetraTransaction)
	 */
	@Override
	public boolean fire(TetraTransaction<D> transaction) {
		this.tetra.fire(transaction);
		for( ICollINVertex<D> selector: transaction.getHistory()) {
			Integer retval = progress.get(selector );
			int selected = ( retval == null)?1: retval+1;
			progress.put(selector, selected);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.collin.core.util.ITetraImplementation#register(org.collin.core.transaction.TetraTransaction)
	 */
	@Override
	public void register( TetraTransaction<D> transaction ) {
		transaction.addTransactionListener(tlistener);		
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.util.ITetraImplementation#unregister()
	 */
	@Override
	public void unregister( TetraTransaction<D> transaction) {
		transaction.removeTransactionListener( tlistener);
		this.tetra.removeCollINListener(listener);
	}
	
	protected ICollINDelegate<N,D> getDelegate( ITetraNode<D> node ){
		ICollINDelegate<N,D> delegate = this.delegates.get(node);
		if( delegate == null ) {
			delegate = factory.createDelegate(this.getClass(), node);
			if( delegate != null )
				delegates.put(node, delegate);
		}
		return delegate;
	}

}
