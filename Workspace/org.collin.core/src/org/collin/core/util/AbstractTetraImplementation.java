package org.collin.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.collin.core.def.ICollINSelector;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.AbstractCollINSelector;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.ITetraListener;
import org.collin.core.transaction.ITransactionListener;
import org.collin.core.transaction.TetraTransaction;

public abstract class AbstractTetraImplementation<D extends Object> extends AbstractCollINSelector<D> implements ITetraImplementation<D> {

	private ITetra<D> tetra;
	
	private Map<ICollINSelector<D>, Integer> progress;

	private ITransactionListener<D> tlistener = new ITransactionListener<D>() {
	
		@Override
		public boolean transactionUpdateRequest(ICollINSelector<D> source, TetraTransaction<D> event) {
			if( !tetra.isChild(source))
				return false;
			
			if( source instanceof ITetraNode ) {
				ITetraNode<D> node = (ITetraNode<D>) source;
				if( node.getParent().equals(tetra))
					onNodeChange(node, event);
			}
			return onTransactionUpdateRequest(event);
		}
	};

	private ITetraListener<D> listener = new ITetraListener<D>() {

		@Override
		public void notifyNodeSelected(Object source, TetraTransaction<D> event, boolean blockLocal) {
			onTetraEventReceived(event);
		}
	};
	
	public AbstractTetraImplementation( ITetra<D> tetra) {
		this( tetra.getId(), tetra.getName(), tetra );
	}
	
	public AbstractTetraImplementation( String id, String name, ITetra<D> tetra) {
		super(id, name);
		this.tetra = tetra;
		this.tetra.addTetraListener(listener);
		progress = new LinkedHashMap<>();
	}

	protected abstract boolean onNodeChange( ITetraNode<D> solution, TetraTransaction<D> event );

	protected abstract boolean onTransactionUpdateRequest( TetraTransaction<D> event );

	protected abstract void onTetraEventReceived( TetraTransaction<D> event );
	
	public int getSelected( ICollINSelector<D> selector ) {
		return progress.get(selector);
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.util.ITetraImplementation#fire(org.collin.core.transaction.TetraTransaction)
	 */
	@Override
	public boolean fire(TetraTransaction<D> event) {
		this.tetra.fire(event);
		for( ICollINSelector<D> selector: event.getHistory()) {
			Integer retval = progress.get(selector);
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
		this.tetra.removeTetraListener(listener);
	}
}
