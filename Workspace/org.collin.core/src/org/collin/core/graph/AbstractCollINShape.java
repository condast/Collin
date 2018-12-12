package org.collin.core.graph;

import java.util.Collection;

import org.collin.core.essence.ITetraListener;
import org.collin.core.operator.IOperator;
import org.collin.core.operator.IOperatorFactory;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractCollINShape<D extends Object> extends AbstractShape<D>{

	private IOperatorFactory<D> factory;
		
	public AbstractCollINShape( IOperatorFactory<D> factory, String id, String name ) {
		super( id, name );
		this.factory = factory;
	}

	public abstract void init();

	@Override
	public boolean addEdge(IEdge<D> edge) {
		IOperator<D> operator = this.factory.createOperator( null, edge );
		Edge edgeImpl = (AbstractCollINShape<D>.Edge) edge;
		edgeImpl.setOperator(operator);
		return super.addEdge(edge);
	}

	@SuppressWarnings("unchecked")
	public ITetraListener<D>[] getListeners() {
		Collection<ITetraListener<D>> listeners = super.getTetraListeners();
		return listeners.toArray( new ITetraListener[ listeners.size()]);
	}

	public void setOperatorFactory( IOperatorFactory<D> factory) {
		this.factory = factory;
	}

	protected void removeOperatorFactory( IOperatorFactory<D> factory) {
		this.factory = null;
	}
		
	@Override
	public String toString() {
		return StringUtils.isEmpty(getName())? super.getId(): super.getName();
	}
	
	protected class Edge extends AbstractEdge<D>{

		public Edge(ICollINShape<D> owner, ICollINVertex<D> origin, ICollINVertex<D> destination) {
			super(owner, combine( origin.getId(), destination.getId()), combine( origin.getName(), destination.getName()), origin, destination, null);
		}

		@Override
		public void setOperator(IOperator<D> operator) {
			super.setOperator(operator);
		}

		@Override
		public boolean fire(TetraTransaction<D> transaction) {
			return false;
		}
	}
}