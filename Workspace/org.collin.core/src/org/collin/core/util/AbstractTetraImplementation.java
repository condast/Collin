package org.collin.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

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

public abstract class AbstractTetraImplementation<D extends Object> extends AbstractCollINVertex<D> implements ITetraImplementation<D> {

	private ITetra<D> tetra;
	
	private Map<ICollINVertex<D>, Integer> progress;

	private ITransactionListener<D> tlistener = new ITransactionListener<D>() {
	
		@Override
		public TetraEvent.Results transactionUpdateRequest(ICollINVertex<D> source, TetraEvent<D> event) {
			if( !tetra.isChild(source))
				return Results.CONTINUE;
			
			if( source instanceof ITetraNode ) {
				ITetraNode<D> node = (ITetraNode<D>) source;
				if( node.getParent().equals(tetra))
					onNodeChange(node, event.getTransaction());
			}
			return onTransactionUpdateRequest(event.getTransaction());
		}
	};

	private ITetraListener<D> listener = new ITetraListener<D>() {

		@Override
		public void notifyNodeSelected(Object source, TetraEvent<D> event) {
			onTetraEventReceived(event.getTransaction());
		}
	};
	
	public AbstractTetraImplementation( ITetra<D> tetra) {
		this( tetra.getId(), tetra.getName(), tetra );
	}
	
	public AbstractTetraImplementation( String id, String name, ITetra<D> tetra) {
		super(id, name, null);
		this.tetra = tetra;
		this.tetra.addCollINListener(listener);
		progress = new LinkedHashMap<>();
	}

	protected abstract boolean onNodeChange( ITetraNode<D> solution, TetraTransaction<D> event );

	protected abstract TetraEvent.Results onTransactionUpdateRequest( TetraTransaction<D> event );

	protected abstract void onTetraEventReceived( TetraTransaction<D> event );
	
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
	public void register( TetraTransaction<D> event ) {
		event.addTransactionListener(tlistener);		
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.util.ITetraImplementation#unregister()
	 */
	@Override
	public void unregister() {
		this.tetra.removeCollINListener(listener);
	}
}
