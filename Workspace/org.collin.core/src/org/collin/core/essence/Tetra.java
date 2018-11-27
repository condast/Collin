package org.collin.core.essence;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;
import org.condast.commons.strings.StringUtils;

public class Tetra<D extends Object> extends TetraNode<D> implements ITetra<D> {

	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;
	
	private OperatorFactory<D> factory;
	private IOperator<D> operator;
	
	private String name;

	public Tetra( String name, String label ) {
		this( name, label, ITetraNode.Nodes.UNDEFINED);
	}

	public Tetra( String name, String label, ITetraNode.Nodes type ) {
		this( null, name, label, type, null );
	}

	public Tetra( ITetra<D> parent, String name, String label, ITetraNode.Nodes type, D data, OperatorFactory<D> factory ) {
		super( parent, label, type, data );
		nodes = new HashMap<>();
		this.name = !StringUtils.isEmpty(name)? name: type.toString();
		this.factory = factory;
		if( this.factory != null )
			operator = this.factory.createOperator(this);
	}
	
	public Tetra( ITetra<D> parent, String name, String label, ITetraNode.Nodes type, D data ) {				
		this( parent, name, label, type, data, null);
		this.factory = new DefaultOperatorFactory(this);
		operator = this.factory.createOperator(this);
	}

	public Tetra( String name, ITetraNode<D> node ) {				
		this( node.getParent(), name, node.getId(), node.getType(), node.getData());
	}
	
	@Override
	public String getName() {
		return name;
	}

	protected OperatorFactory<D> getOperatorFactory() {
		return factory;
	}

	public void setOperator(OperatorFactory<D> factory) {
		this.factory = factory;
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
		factory.createOperator( node );
	}

	@Override
	public void removeNode( ITetraNode<D> node ) {
		this.nodes.remove(node.getType() );
		factory.removeOperator(node);
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
			operator.select(event);
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

	private class DefaultOperatorFactory extends OperatorFactory<D>{

		
		protected DefaultOperatorFactory(Tetra<D> tetra) {
			super(tetra);
		}

		@Override
		protected void onNodeSelected(Nodes type, TetraEvent<D> event) {
		}
	}
}