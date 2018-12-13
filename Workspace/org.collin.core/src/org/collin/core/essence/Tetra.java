package org.collin.core.essence;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.collin.core.graph.AbstractEdge;
import org.collin.core.graph.AbstractShape;
import org.collin.core.graph.ICollINShape;
import org.collin.core.graph.ICollINVertex;
import org.collin.core.operator.DefaultOperatorFactory;
import org.collin.core.operator.IOperator;
import org.collin.core.operator.IOperatorFactory;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringUtils;

public class Tetra<D extends Object> extends AbstractShape<D> implements ITetra<D> {

	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;
	
	private IOperatorFactory<D> factory;
	private ICollINShape<D> parent;
	private Nodes type;
	
	public Tetra( String id, String name ) {
		this( id, name, ITetraNode.Nodes.UNDEFINED);
	}

	public Tetra( String id, String name, ITetraNode.Nodes type ) {
		this( null, id, name, type );
	}

	public Tetra( String name, ITetraNode<D> node ) {				
		this( node.getParent(), node.getId(), name, node.getType());
	}

	public Tetra( ICollINShape<D> parent, String id, String name) {
		this( parent, new DefaultOperatorFactory<D>(), id, name, ITetraNode.Nodes.UNDEFINED );
	}

	public Tetra( ICollINShape<D> parent, String id, String name, ITetraNode.Nodes type ) {
		this( parent, new DefaultOperatorFactory<D>(), id, name, type );
	}
		
	public Tetra( ICollINShape<D> parent, IOperatorFactory<D> factory, String id, String name, ITetraNode.Nodes type ) {
		super( id, !StringUtils.isEmpty(name)? name: type.toString() );
		nodes = new HashMap<>();
		this.factory = factory;
		this.parent = parent;
		this.type = type;
	}

	@Override
	public void init() {
		//First complete the nodes
		Set<Nodes> set = EnumSet.of(Nodes.FUNCTION, Nodes.GOAL, Nodes.TASK, Nodes.SOLUTION);
		for( Nodes node: set) {
			if(!nodes.containsKey(node )) {
				String name = createName(this, node );
				ITetraNode<D> tn = new TetraNode<D>(this, createId(this, node), name, node );
				addNode( tn );
			}
		}
		//Then create the edges
		for( Nodes node: set) {
			ITetraNode<D> origin = nodes.get(node);
			for( ITetraNode<D> nd: this.nodes.values()) {
				if( node.equals(nd.getType()))
					continue;
				Edge edge = new Edge( this, origin, nd );
				IOperator<D> operator = this.factory.createOperator( null, edge );
				edge.setOperator(operator);
				addEdge( edge );
			}
		}	
	}

	@Override
	public Nodes getType() {
		return type;
	}

	@Override
	public  ICollINShape<D> getParent() {
		return parent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITetraListener<D>[] getListeners() {
		Collection<ITetraListener<D>> listeners = super.getTetraListeners();
		return listeners.toArray( new ITetraListener[ listeners.size()]);
	}

	@Override
	public void setOperator(IOperator<D> operator) {
		super.setOperator(operator);
	}

	public void setOperatorFactory( IOperatorFactory<D> factory) {
		this.factory = factory;
	}

	protected void removeOperatorFactory( IOperatorFactory<D> factory) {
		this.factory = null;
	}

	@Override
	public void addNode( ITetraNode<D> node ) {
		this.nodes.put(node.getType(), node );
	}

	@Override
	public void removeNode( ITetraNode<D> node ) {
		this.nodes.remove(node.getType() );
		super.removeVertex(node);
	}

	@Override
	public boolean isChild(ICollINVertex<D> node) {
		return this.nodes.containsValue(node);
	}

	@Override
	public ITetraNode<D> getNode( ITetraNode.Nodes type ) {
		return this.nodes.get(type);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public ITetraNode<D>[] getNodes() {
		return this.nodes.values().toArray( new ITetraNode[ this.nodes.size()]);
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
	@Override
	public boolean select( ITetraNode.Nodes type, TetraEvent<D> event ) {
		ITetraNode<D> node = null;
		boolean result = false;
		switch( type ) {
		case UNDEFINED:
			node = getNode( Nodes.GOAL);
			result = node.select(type, event);
			break;
		case FUNCTION:
			node = getNode( Nodes.FUNCTION);
			result = node.select(type, event);
			break;
		case GOAL:
			node = getNode( Nodes.TASK);
			result = node.select( type, event);
			break;
		case TASK:
			node = getNode( Nodes.GOAL);
			result = node.select(type, event);
			break;
		case SOLUTION:
			node = getNode( Nodes.SOLUTION);
	 		super.notifyListeners( event);
			result = true;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public Map<IPlane.Planes, IPlane<D>> getPlanes() {
		Map<IPlane.Planes, IPlane<D>> planes = new HashMap<>();
		planes.put(IPlane.Planes.AMBITION, new Plane<>( this, IPlane.Planes.AMBITION ));
		planes.put(IPlane.Planes.LEARNING, new Plane<>( this, IPlane.Planes.LEARNING ));
		planes.put(IPlane.Planes.OPERATION, new Plane<>( this, IPlane.Planes.OPERATION ));
		planes.put(IPlane.Planes.RECOVERY, new Plane<>( this, IPlane.Planes.RECOVERY ));		
		return null;
	}

	@Override
	public String toString() {
		return StringUtils.isEmpty(getName())? super.getId(): super.getName();
	}
	
	private class Edge extends AbstractEdge<D>{

		protected Edge(ICollINShape<D> owner, ICollINVertex<D> origin, ICollINVertex<D> destination) {
			super(owner, combine( origin.getId(), destination.getId()), combine( origin.getName(), destination.getName()), origin, destination, null);
		}

		@Override
		public void setOperator(IOperator<D> operator) {
			super.setOperator(operator);
		}

		@Override
		public boolean fire(TetraTransaction<D> event) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}