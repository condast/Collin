package org.collin.core.essence;

import java.util.Collection;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.graph.AbstractCollINVertex;
import org.collin.core.graph.ICollINShape;
import org.collin.core.operator.IOperator;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringStyler;
import org.xml.sax.Attributes;

/**
 * A Tetra node is the core atom of tetralogic. It serves as a placeholder for the tetra. 
 * Note that the data object can itself be a tetra!
 * 
 * @author Condast
 *
 * @param <D>
 */
public class TetraNode<D extends Object> extends AbstractCollINVertex<D> implements ITetraNode<D>{

	private ITetraNode.Nodes type;
	
	private ICollINShape<D> parent;

	public TetraNode( ICollINShape<D> parent, ITetraNode.Nodes type ) {
		this( parent, type.name(), type.toString(), type );
	}
	
	public TetraNode( ICollINShape<D> parent, String id, String name, ITetraNode.Nodes type ) {
		this( parent, id, name, type, new DefaultOperator<D>() ) ;
		DefaultOperator<D> dop = (DefaultOperator<D>) super.getOperator();
		dop.setOwner(this);
	}
	
	public TetraNode( ICollINShape<D> parent, String id, String name, ITetraNode.Nodes type, IOperator<D> operator ) {
		super( id, name, operator);
		this.parent = parent;
		this.type = type;
	}

	protected TetraNode(String id, String name, ITetraNode.Nodes type) {
		this( null, id, name, type );
	}

	@Override
	public Nodes getType() {
		return type;
	}

	@Override
	public ICollINShape<D> getParent() {
		return parent;
	}

	@Override
	public void setOperator(IOperator<D> operator) {
		super.setOperator(operator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITetraListener<D>[] getListeners(){
		Collection<ITetraListener<D>> listeners = super.getTetraListeners();
		return listeners.toArray( new ITetraListener[listeners.size()]);
	}
	
	@Override
	public boolean fire(TetraTransaction<D> transaction) {
		return select( getType(), new TetraEvent<D>( this, transaction ));
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetraNode#select()
	 */
	@Override
	public boolean select( ITetraNode.Nodes type, TetraEvent<D> incoming ) {
		TetraTransaction<D> transaction = incoming.getTransaction();
		Results result = transaction.updateTransaction(this, incoming);		
		if( Results.COMPLETE.equals(result))
			return true;			
		return super.getOperator().select(this, new TetraEvent<D>( this, result, transaction ));
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( this.type.toString() + ": ");
		buffer.append( super.toString());
		return buffer.toString();
	}

	/**
	 * Create a default id
	 * @param parent
	 * @param node
	 * @return
	 */
	public static String createId( ITetra<?> parent, ITetraNode.Nodes node) {
		return parent.getId() + "." + StringStyler.toMethodString(node.name());
	}

	/**
	 * Create a default name
	 * @param parent
	 * @param node
	 * @return
	 */
	public static String createName( ITetraNode<?> parent, ITetraNode.Nodes node) {
		return parent.getName() + "." + StringStyler.toMethodString(node.name());
	}

	private static class DefaultOperator<D extends Object> implements IOperator<D>{

		private TetraNode<D> owner;
		
		public DefaultOperator() {
			super();
		}

		public void setOwner(TetraNode<D> owner) {
			this.owner = owner;
		}

		@Override
		public void setParameters(Attributes attrs) {
			// NOTHING		
		}
				
		@Override
		public boolean select( ITetraNode<D> source, TetraEvent<D> event) {
			owner.notifyListeners( event );
			return true;
		}

		@Override
		public void dispose() { /* NOTHING */}
	}
}
