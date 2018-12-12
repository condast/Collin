package org.collin.core.essence;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;
import org.collin.core.graph.AbstractCollINShape;
import org.collin.core.graph.ICollINShape;
import org.collin.core.graph.ICollINVertex;
import org.collin.core.graph.IEdge;
import org.collin.core.operator.DefaultOperatorFactory;
import org.collin.core.operator.IOperatorFactory;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringUtils;

public class TetraShape<E extends Enum<E>, D extends Object> extends AbstractCollINShape<D>{

	private ICollINShape<D> parent;
	private E type;
	private EnumMap<ITetraNode.Nodes, ITetraNode<D>> nodes;

	public TetraShape( String id, String name, E type ) {
		this( null, id, name, type );
	}

	public TetraShape( ICollINShape<D> parent, String id, String name, E type ) {
		this( parent, new DefaultOperatorFactory<D>(), id, name, type );
	}
		
	public TetraShape( ICollINShape<D> parent, IOperatorFactory<D> factory, String id, String name, E type ) {
		super( factory, id, name );
		this.parent = parent;
		this.type = type;
		nodes = new EnumMap<ITetraNode.Nodes, ITetraNode<D>>(ITetraNode.Nodes.class );
	}

	@Override
	public void init() {
		//First complete the nodes
		Collection<ICollINVertex<D>> vertices = super.getVertices();
		Set<ITetraNode.Nodes> nodeset = EnumSet.allOf( ITetraNode.Nodes.class); 
		nodeset.remove(ITetraNode.Nodes.UNDEFINED);
		ITetraNode<D> tn = null;
		for( ICollINVertex<D> vertex: vertices ) {
			tn = (ITetraNode<D>) vertex;
			nodes.put(tn.getType(), tn);
			nodeset.remove(tn.getType());
		}
		for( ITetraNode.Nodes node: nodeset ) {
			tn = new TetraNode<D>( this, node );
			nodes.put(tn.getType(), tn);
		}
	}

	public E getType() {
		return type;
	}

	public  ICollINShape<D> getParent() {
		return parent;
	}

	protected ITetraNode<D> getNode( ITetraNode.Nodes node) {
		return nodes.get(node);
	}

	@Override
	public boolean addEdge(IEdge<D> edge) {
		ITetraNode<D> node = (ITetraNode<D>) edge.getOrigin();
		if( !nodes.containsKey(node.getType()))
			nodes.put(node.getType(), node);
		node = (ITetraNode<D>) edge.getDestination();
		if( !nodes.containsKey(node.getType()))
			nodes.put(node.getType(), node);
		return super.addEdge(edge);
	}

	@Override
	public boolean removeEdge(IEdge<D> edge) {
		EnumSet<ITetraNode.Nodes> set = EnumSet.allOf(ITetraNode.Nodes.class);
		set.remove( ITetraNode.Nodes.UNDEFINED);
		boolean result = super.removeEdge(edge);
		if( !result )
			return result;
		
		for( IEdge<D> edg: super.getEdges() ) {
			ITetraNode<D> node = (ITetraNode<D>) edg.getOrigin();
			set.remove(node.getType() );
			node = (ITetraNode<D>) edg.getDestination();
			set.remove(node.getType() );
		}
		for( ITetraNode.Nodes node: set ) {
			super.removeVertex( nodes.get(node));
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addCollINListener(ITetraListener<D> listener) {
		boolean result = false;
		ITetraNode<D> tn = null; 
		if(!(listener instanceof ITetraNode))
			return super.addCollINListener(listener);

		ITetraNode<D> tetra = (ITetraNode<D>) listener;
		switch( tetra.getType()) {
		case FUNCTION:
			tn = nodes.get(ITetraNode.Nodes.FUNCTION);
			result = tn.addCollINListener(listener);
			break;
		case GOAL:
			tn = nodes.get(ITetraNode.Nodes.TASK);
			result = tn.addCollINListener(listener);
			break;
		case TASK:
			tn = nodes.get(ITetraNode.Nodes.GOAL);
			result = tn.addCollINListener(listener);
			break;
		case SOLUTION:
			tn = nodes.get(ITetraNode.Nodes.SOLUTION);
			result = tn.addCollINListener(listener);
			break;
		default:
			result = super.addCollINListener(listener);
			break;
		}
		return result;
	}

	@Override
	public boolean removeCollINListener(ITetraListener<D> listener) {
		boolean result = false;
		for( ITetraNode<D> node: nodes.values())
			result |= node.removeCollINListener(listener);
		result |= super.removeCollINListener(listener);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetra#select(org.collin.core.def.ITetraNode.Nodes)
	 */
	@Override
	public boolean fire( TetraTransaction<D> transaction ) {
		return select( ITetraNode.Nodes.UNDEFINED, new TetraEvent<D>( this, transaction ));
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetra#select(org.collin.core.def.ITetraNode.Nodes)
	 */
	public boolean select( ITetraNode.Nodes type, TetraEvent<D> event ) {
		ITetraNode<D> node = null;
		boolean result = false;
		switch( type ) {
		case UNDEFINED:
			node = nodes.get( Nodes.GOAL);
			result = node.select(type, event);
			break;
		case FUNCTION:
			node = nodes.get( Nodes.FUNCTION);
			result = node.select(type, event);
			break;
		case GOAL:
			node = nodes.get( Nodes.TASK);
			result = node.select( type, event);
			break;
		case TASK:
			node = nodes.get( Nodes.GOAL);
			result = node.select(type, event);
			break;
		case SOLUTION:
			node = nodes.get( Nodes.SOLUTION);
	 		super.notifyListeners( event);
			result = true;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		return StringUtils.isEmpty(getName())? super.getId(): super.getName();
	}	
}