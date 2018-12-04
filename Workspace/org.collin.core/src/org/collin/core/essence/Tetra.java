package org.collin.core.essence;

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
	
	private IOperator<D> operator;
	
	private String name;

	public Tetra( String name, String label ) {
		this( name, label, ITetraNode.Nodes.UNDEFINED);
	}

	public Tetra( String name, String label, ITetraNode.Nodes type ) {
		this( null, name, label, type, null );
	}
	public Tetra( ITetra<D> parent, String name, String label, ITetraNode.Nodes type, D data ) {				
		this( parent, name, label, type, data, null);
		this.operator = new DefaultOperator<D>();
	}

	public Tetra( String name, ITetraNode<D> node ) {				
		this( node.getParent(), name, node.getId(), node.getType(), node.getData());
	}

	protected Tetra( ITetra<D> parent, String name, String label, ITetraNode.Nodes type, D data, DefaultOperator<D> operator ) {
		super( parent, label, type, data );
		nodes = new HashMap<>();
		this.name = !StringUtils.isEmpty(name)? name: type.toString();
		this.operator = operator; 
	}
		
	@Override                                                
	public String getName() {
		return name;
	}

	protected IOperator<D> getOperator() {
		return operator;
	}

	public void setOperator( IOperator<D> operator) {
		this.operator = operator;
	}

	@Override
	public void init() {
		Set<Nodes> set = EnumSet.of(Nodes.FUNCTION, Nodes.GOAL, Nodes.TASK, Nodes.SOLUTION);
		for( Nodes node: set) {
			if(!nodes.containsKey(node )) {
				ITetraNode<D> tn = new TetraNode<D>(this, node.toString(), node );
				addNode( tn );
			}
		}
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
	public void select( TetraEvent<D> event ) {
		ITetraNode<D> node = null;
		switch( event.getState()) {
		case START:
			node = getNode( Nodes.GOAL);
			if( operator.select(node, event))
				node.select(event);
		case GOAL:
			node = getNode( Nodes.GOAL);
			if( operator.select(node, event)) {
				node = getNode( Nodes.TASK);
				event.setState(States.TASK);
				node.select(event);
			}
		case TASK:
			node = getNode( Nodes.TASK);
			if( operator.select(node, event)) {
				node = getNode( Nodes.SOLUTION);
				event.setState(States.SOLUTION);
				node.select(event);
			}
		case SOLUTION:
			node = getNode( Nodes.SOLUTION);
			if( operator.select(node, event)) {
				event.setState(States.COMPLETE);
				notifyTetraListeners(event);
			}
			break;
		default:
			break;
		}
		super.notifyTetraListeners(event);
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
		return this.name + ":" + super.getId();
	}
}