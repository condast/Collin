package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.collin.core.def.ICollINSelector;
import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.collin.core.operator.DefaultOperatorFactory;
import org.collin.core.operator.IOperator;
import org.collin.core.operator.IOperatorFactory;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringUtils;

public class Tetra<D extends Object> extends TetraNode<D> implements ITetra<D> {

	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;
	
	private IOperatorFactory<D> factory;
	private Collection<IOperator<D>> operators;
	
	public Tetra( String id, String name ) {
		this( id, name, ITetraNode.Nodes.UNDEFINED);
	}

	public Tetra( String id, String name, ITetraNode.Nodes type ) {
		this( null, id, name, type );
	}

	public Tetra( String name, ITetraNode<D> node ) {				
		this( node.getParent(), node.getId(), name, node.getType());
	}

	public Tetra( ITetra<D> parent, String id, String name, ITetraNode.Nodes type ) {
		this( parent, new DefaultOperatorFactory<D>(), id, name, type );
	}
		
	public Tetra( ITetra<D> parent, IOperatorFactory<D> factory, String id, String name, ITetraNode.Nodes type ) {
		super( parent, id, !StringUtils.isEmpty(name)? name: type.toString(), type );
		nodes = new HashMap<>();
		this.factory = factory;
		this.operators = new ArrayList<>();
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
		//Then create the operators
		for( Nodes node: set) {
			ITetraNode<D> origin = nodes.get(node);
			for( ITetraNode<D> nd: this.nodes.values()) {
				IOperator<D> operator = this.factory.createOperator( null, origin, nd);
				this.operators.add( operator);
			}
		}	
	}

	protected void setOperator( IOperatorFactory<D> factory) {
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
	}

	@Override
	public boolean isChild(ICollINSelector<D> node) {
		return this.nodes.containsValue(node);
	}

	@Override
	public ITetraNode<D> getNode( ITetraNode.Nodes type ) {
		return this.nodes.get(type);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean addTetraListener(ITetraListener<D> listener) {
		boolean result = false;
		ITetraNode<D> tn = null; 
		if(!(listener instanceof ITetraNode))
			return super.addTetraListener(listener);

		ITetraNode<D> tetra = (ITetraNode<D>) listener;
		switch( tetra.getType()) {
		case FUNCTION:
			tn = nodes.get(ITetraNode.Nodes.FUNCTION);
			result = tn.addTetraListener(listener);
			break;
		case GOAL:
			tn = nodes.get(ITetraNode.Nodes.TASK);
			result = tn.addTetraListener(listener);
			break;
		case TASK:
			tn = nodes.get(ITetraNode.Nodes.GOAL);
			result = tn.addTetraListener(listener);
			break;
		case SOLUTION:
			tn = nodes.get(ITetraNode.Nodes.SOLUTION);
			result = tn.addTetraListener(listener);
			break;
		default:
			result = super.addTetraListener(listener);
			break;
		}
		return result;
	}

	@Override
	public boolean removeTetraListener(ITetraListener<D> listener) {
		boolean result = false;
		for( ITetraNode<D> node: nodes.values())
			result |= node.removeTetraListener(listener);
		result |= super.removeTetraListener(listener);
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
	public boolean fire( TetraTransaction<D> event ) {
		return select( ITetraNode.Nodes.UNDEFINED, event );
	}
	
	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetra#select(org.collin.core.def.ITetraNode.Nodes)
	 */
	@Override
	public boolean select( ITetraNode.Nodes type, TetraTransaction<D> event ) {
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
	 		super.notifyTetraListeners(event, false);
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
		planes.put(IPlane.Planes.AMBITION, new Plane<>( IPlane.Planes.AMBITION, this ));
		planes.put(IPlane.Planes.LEARNING, new Plane<>( IPlane.Planes.LEARNING, this ));
		planes.put(IPlane.Planes.OPERATION, new Plane<>( IPlane.Planes.OPERATION, this ));
		planes.put(IPlane.Planes.RECOVERY, new Plane<>( IPlane.Planes.RECOVERY, this ));		
		return null;
	}

	@Override
	public String toString() {
		return StringUtils.isEmpty(getName())? super.getId(): super.getName();
	}
}