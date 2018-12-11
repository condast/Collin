package org.collin.core.essence;

import java.util.Collection;

import org.collin.core.def.ITetraNode;
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
public class TetraNode<D extends Object> extends AbstractCollINSelector<D> implements ITetraNode<D>{

	private ITetraNode.Nodes type;
	
	private ITetra<D> parent;

	private IOperator<D> operator;

	public TetraNode( ITetra<D> parent, String id, String name, ITetraNode.Nodes type ) {
		this( parent, id, name, type, new DefaultOperator<D>() ) ;
		DefaultOperator<D> dop = (DefaultOperator<D>) this.operator;
		dop.setOwner(this);
	}
	
	public TetraNode( ITetra<D> parent, String id, String name, ITetraNode.Nodes type, IOperator<D> operator ) {
		super( id, name);
		this.parent = parent;
		this.type = type;
		this.operator = operator;
	}

	protected TetraNode(String id, String name, ITetraNode.Nodes type) {
		this( null, id, name, type );
	}

	@Override
	public Nodes getType() {
		return type;
	}

	@Override
	public ITetra<D> getParent() {
		return parent;
	}

	@Override
	public void setOperator(IOperator<D> operator) {
		this.operator = operator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITetraListener<D>[] getListeners(){
		Collection<ITetraListener<D>> listeners = super.getTetraListeners();
		return listeners.toArray( new ITetraListener[listeners.size()]);
	}
	
	@Override
	public boolean fire(TetraTransaction<D> event) {
		return select( getType(), event );
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetraNode#select()
	 */
	@Override
	public boolean select( ITetraNode.Nodes type , TetraTransaction<D> event ) {
    		boolean result = this.operator.select(this, event);
		if( !result ) {
			notifyTetraListeners(event, true);
			return result;
		}
		result = event.updateTransaction(this, event);
		notifyTetraListeners(event, false);
		return result;
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
			// TODO Auto-generated method stub
			
		}
				
		@Override
		public boolean select( ITetraNode<D> source, TetraTransaction<D> event) {
			if( event.hasBeenProcessed( source ))
				return false;
			owner.notifyTetraListeners( event, false );
			return true;
		}

		@Override
		public boolean contains(ITetraNode<D> node) {
			return owner.equals(node);
		}

		@Override
		public void dispose() { /* NOTHING */}
	}
}
