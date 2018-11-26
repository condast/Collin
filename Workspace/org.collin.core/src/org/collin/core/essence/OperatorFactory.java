package org.collin.core.essence;

import java.util.HashMap;
import java.util.Map;

import org.collin.core.def.ITetraNode;

public class OperatorFactory<D extends Object>  {

	private Tetra<D> tetra;
	
	private Map<ITetraNode<D>, ITetraListener<D>> operators;

	protected OperatorFactory(Tetra<D> tetra) {
		super();
		this.tetra = tetra;
		this.operators = new HashMap<>();
	}
	
	public IOperator<D> createOperator( ITetraNode<D> node){
		IOperator<D> operator =  new DefaultOperator( tetra );
		node.addTetraListener(operator);
		operators.put(node, operator);
		return operator;
	}

	public void removeOperator( ITetraNode<D> node){
		ITetraListener<D> operator =  operators.remove(node);
		if( operator != null )
			node.removeTetraListener(operator);
	}

	protected void onNodeSelected( ITetraNode.Nodes type, TetraEvent<D> event ) {
		/* DEFAULT NOTHING */
	}

	private class DefaultOperator implements IOperator<D>{

		private ITetraListener<D> listener = new ITetraListener<D>() {

			@SuppressWarnings("unchecked")
			@Override
			public void notifyNodeSelected(TetraEvent<D> event) {
				if( ITetraNode.Nodes.COMPLETE.equals( event.getType() )) {
					notifyNodeSelected((TetraEvent<D>) event.getSource());
				}
			}	
		};


		private Tetra<D> tetra;
		
		protected DefaultOperator(Tetra<D> tetra) {
			super();
			this.tetra = tetra;
			for( ITetraNode<D> node: tetra.getNodes())
				node.addTetraListener(listener);
		}
		
		protected void notifyTetraListeners( TetraEvent<D> event ) {
			tetra.notifyTetraListeners(event);
		}

		/* (non-Javadoc)
		 * @see org.collin.core.essence.IOperator#select(org.collin.core.essence.TetraEvent)
		 */
		@Override
		public void select( TetraEvent<D> event ) {
			for( ITetraNode<D> node: this.tetra.getNodes()) {
				if( node instanceof ITetra )
					node.select(event);
				else
					notifyNodeSelected(event);
			}

		}

		@SuppressWarnings("unchecked")
		@Override
		public void notifyNodeSelected(TetraEvent<D> event) {
			TetraEvent<D> propagate = event;
			switch( tetra.getType()) {
			case GOAL: 
				switch( event.getType()) {
				case START:
					notifyTetraListeners(event);
					break;
				case TASK:
					propagate = new TetraEvent<D>(tetra.getNode(ITetraNode.Nodes.TASK), event);
					tetra.getNode( ITetraNode.Nodes.SOLUTION).select(propagate);
					break;
				case SOLUTION:
					propagate = new TetraEvent<D>(tetra.getNode(ITetraNode.Nodes.SOLUTION), event);
					tetra.getNode( ITetraNode.Nodes.COMPLETE).select(propagate);
					notifyTetraListeners((TetraEvent<D>) event.getSource());
					break;
				default:
					break;
				}
			case TASK:
				switch( event.getType()) {
				case GOAL:
					propagate = new TetraEvent<D>(tetra.getNode(ITetraNode.Nodes.TASK), event);
					tetra.getNode( ITetraNode.Nodes.SOLUTION).select(propagate);
					break;
				default:
					break;
				}
			case SOLUTION:
				switch( event.getType()) {
				case TASK:
					propagate = new TetraEvent<D>(tetra.getNode(ITetraNode.Nodes.COMPLETE), event);
					tetra.getNode( ITetraNode.Nodes.GOAL).select(propagate);
					break;
				default:
					break;
				}
			default:
				break;
			}
			onNodeSelected(tetra.getType(), propagate);
		}
	}
}
