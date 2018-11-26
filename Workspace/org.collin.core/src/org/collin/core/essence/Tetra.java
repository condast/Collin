package org.collin.core.essence;

import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.IPlane;
import org.collin.core.def.ITetraNode;

public class Tetra<D extends Object> extends TetraNode<D> implements ITetra<D> {

	private Map<ITetraNode.Nodes, ITetraNode<D>> nodes;
	
	private OperatorFactory<D> factory;
	private IOperator<D> operator;

	public Tetra( String label ) {
		this( label, ITetraNode.Nodes.COMPLETE);
	}

	public Tetra( String label, ITetraNode.Nodes type ) {
		this( null, label, type, null );
	}

	public Tetra( ITetra<D> parent, String label, ITetraNode.Nodes type, D data, OperatorFactory<D> factory ) {
		super( parent, label, type, data );
		nodes = new HashMap<>();
		this.factory = factory;
		operator = this.factory.createOperator(this);
	}
	
	public Tetra( ITetra<D> parent, String label, ITetraNode.Nodes type, D data ) {				
		this( parent, label, type, data, null);
		this.factory = new DefaultOperatorFactory(this);
	}
	
	protected OperatorFactory<D> getOperatorFactory() {
		return factory;
	}

	public void setOperator(OperatorFactory<D> factory) {
		this.factory = factory;
	}

	@Override
	public void init() {
		for( Nodes node: Nodes.values()) {
			if(!nodes.containsKey(node )) {
				ITetraNode<D> tn = new TetraNode<D>(this, node.toString(), node );
				addNode( tn );
			}
		}
	}

	public void addNode( ITetraNode<D> node ) {
		this.nodes.put(node.getType(), node );
		factory.createOperator( node );
	}

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

	private class DefaultOperatorFactory extends OperatorFactory<D>{

		
		protected DefaultOperatorFactory(Tetra<D> tetra) {
			super(tetra);
		}

		@Override
		protected void onNodeSelected(Nodes type, TetraEvent<D> event) {
		}
	}
}