package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent.States;
import org.condast.commons.strings.StringUtils;

public class Tetra<D extends Object> extends TetraNode<D> implements ITetra<D> {

	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;
	
	private Collection<ITetraListener<D>> deepListeners;
	
	private IOperator<D> operator;
	
	public Tetra( String id, String name ) {
		this( id, name, ITetraNode.Nodes.UNDEFINED);
	}

	public Tetra( String id, String name, ITetraNode.Nodes type ) {
		this( null, id, name, type, null );
	}
	public Tetra( ITetra<D> parent, String id, String name, ITetraNode.Nodes type, D data ) {				
		this( parent, id, name, type, data, null);
		this.operator = new DefaultOperator<D>();
	}

	public Tetra( String name, ITetraNode<D> node ) {				
		this( node.getParent(), node.getId(), name, node.getType(), node.getData());
	}

	protected Tetra( ITetra<D> parent, String id, String name, ITetraNode.Nodes type, D data, DefaultOperator<D> operator ) {
		super( parent, id, !StringUtils.isEmpty(name)? name: type.toString(), type, data );
		nodes = new HashMap<>();
		this.operator = operator; 
		this.deepListeners = new ArrayList<>();
	}

	@Override
	public void init() {
		Set<Nodes> set = EnumSet.of(Nodes.FUNCTION, Nodes.GOAL, Nodes.TASK, Nodes.SOLUTION);
		for( Nodes node: set) {
			if(!nodes.containsKey(node )) {
				String name = createName(this, node );
				ITetraNode<D> tn = new TetraNode<D>(this, createId(this, node), name, node );
				addNode( tn );
			}
		}
		this.deepListeners.clear();
		for( ITetraNode<D> node: this.nodes.values()) {
			if( node instanceof ITetra<?>) {
				ITetra<D> tetra = (ITetra<D>) node;
				for( ITetraListener<D> listener: this.deepListeners )
					tetra.addDeepListener(listener);
				tetra.init();
			}
		}
	}

	protected IOperator<D> getOperator() {
		return operator;
	}

	public void setOperator( IOperator<D> operator) {
		this.operator = operator;
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
	public void addDeepListener( ITetraListener<D> listener ) {
		this.deepListeners.add( listener );
	}

	@Override
	public void removeDeepListener( ITetraListener<D> listener ) {
		this.deepListeners.remove( listener );
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
	public boolean select( TetraEvent<D> event ) {
		ITetraNode<D> node = null;
		boolean result = false;
		switch( event.getState()) {
		case START:
			node = getNode( Nodes.GOAL);
			if( operator.select(node, event))
				result = node.select(event);
		case GOAL:
			node = getNode( Nodes.GOAL);
			if( operator.select(node, event)) {
				node = getNode( Nodes.TASK);
				event.setState(States.TASK);
				result = node.select(event);
			}
		case TASK:
			node = getNode( Nodes.TASK);
			if( operator.select(node, event)) {
				node = getNode( Nodes.SOLUTION);
				event.setState(States.SOLUTION);
				result = node.select(event);
			}
		case SOLUTION:
			node = getNode( Nodes.SOLUTION);
			if( operator.select(node, event)) {
				event.setState(States.COMPLETE);
			}
			result = true;
			break;
		default:
			break;
		}
 		super.notifyTetraListeners(event);
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