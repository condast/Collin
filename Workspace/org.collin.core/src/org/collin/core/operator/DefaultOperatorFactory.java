package org.collin.core.operator;

import java.lang.reflect.Constructor;

import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;
import org.collin.core.essence.ITetraListener;
import org.collin.core.essence.TetraEvent;
import org.collin.core.graph.IEdge;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class DefaultOperatorFactory<D extends Object> implements IOperatorFactory<D> {

	private Class<?> clss;

	public DefaultOperatorFactory( ) {
	}

	public DefaultOperatorFactory(Class<?> clss ) {
		super();
		this.clss = clss;
	}

	@Override
	public IOperator<D> createOperator( String className, IEdge<D> edge ) {
		IOperator<D> operator = null;
		if( edge.getOrigin().equals(edge.getDestination()))
			return null;
		ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin(); 
		ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination(); 
		switch( origin.getType() ) {
		case FUNCTION:
			if( ITetraNode.Nodes.FUNCTION.equals( destination.getType() ))
				break;
			operator = create( className, edge );
			break;
		case GOAL:
			if( ITetraNode.Nodes.TASK.equals( destination.getType() ))
				operator = create( className, edge );
			break;
		case TASK:
			if( ITetraNode.Nodes.SOLUTION.equals( destination.getType() ))
				operator = create( className, edge );
			break;
		default:
			break;
		}
		return operator;
	}

	protected IOperator<D> create( String className, IEdge<D> edge ){
		if( StringUtils.isEmpty(className))
			return new DefaultOperator(edge);
		else
			return constructOperator(clss, className, (ITetraNode<D> ) edge.getOrigin(), (ITetraNode<D> )edge.getDestination());
	}


	@SuppressWarnings("unchecked")
	public static <D> IOperator<D> constructOperator( Class<?> clss, String className, ITetraNode<D> origin, ITetraNode<D> destination){
		if( StringUtils.isEmpty( className ))
			return null;
		Class<IOperator<D>> builderClass;
		IOperator<D> operator = null;
		try {
			builderClass = (Class<IOperator<D>>) clss.getClassLoader().loadClass( className );
			Constructor<IOperator<D>> constructor = builderClass.getConstructor();
			operator = constructor.newInstance( origin, destination );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return operator;
	}

	protected class DefaultOperator implements IOperator<D>{

		private ITetraListener<D> listener = new ITetraListener<D>() {

			@SuppressWarnings("unchecked")
			@Override
			public void notifyNodeSelected(Object source, TetraEvent<D> event) {
				if( event.getTransaction().hasBeenProcessed(edge))
					return;
				select((ITetraNode<D>) source, event);
			}

			@Override
			public String toString() {
				ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin();
				ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination();
				return origin.getName() + "(" + origin.getType()  + ")-" + 
						destination.getName() + "(" +	destination.getType() + ")";
			}
		};

		protected IEdge<D> edge;

		public DefaultOperator(IEdge<D> edge) {
			super();
			this.edge = edge;
			edge.getOrigin().addCollINListener(listener);
			edge.getDestination().addCollINListener(listener);
		}


		@Override
		public void setParameters(Attributes attrs) {
			// TODO Auto-generated method stub
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#contains(org.collin.core.def.ITetraNode)
		 */
		public boolean contains( ITetraNode<D> node ) {
			ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin();
			ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination();
			return origin.equals(node ) || destination.equals( node );
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#isEqual(org.collin.core.def.ITetraNode, org.collin.core.def.ITetraNode)
		 */
		protected boolean isEqual( ITetraNode<D> node1, ITetraNode<D> node2 ) {
			ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin();
			ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination();
			boolean result = origin.equals(node1) && destination.equals( node2 );
			return result? result: origin.equals(node2) && destination.equals( node1 );
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#getOther(org.collin.core.def.ITetraNode)
		 */
		protected ITetraNode<D> getOther( ITetraNode<D> node ) {
			ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin();
			ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination();
			return origin.equals(node)? destination: destination.equals( node )?origin: null;
		}

		/* (non-Javadoc)
		 * @see org.collin.core.essence.IOperator#select(org.collin.core.essence.TetraEvent)
		 */
		@Override
		public boolean select( ITetraNode<D> source, TetraEvent<D> event ) {
			ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin();
			ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination();
			boolean result = false;
			if( destination == null )
				return result;
						
			//First propagate through the tetra:
			//1: Function -> Goal -> Task -Solution
			//2: Fail: All -> Function
			switch( event.getResult()) {
			case FAIL:
				switch( destination.getType() ) {
				case TASK:
					if( Nodes.GOAL.equals(source.getType()))
						result = destination.select( source.getType(), event );
					break;
				case FUNCTION:
					result = destination.select( source.getType(), event );
					break;
				default:
					break;
				}
				break;
			default:
				switch(source.getType() ) {
				case FUNCTION:
					if( Nodes.GOAL.equals(destination.getType()))
						result = destination.select( source.getType(), event );
					break;
				case GOAL:
					if( Nodes.TASK.equals(destination.getType()))
						result = destination.select( source.getType(), event );
					break;
				case TASK:
					if( ITetraNode.Nodes.SOLUTION.equals(destination.getType()))
						result = destination.select( source.getType(), event );
				case SOLUTION:
					result = true;
					break;
				default:
					break;
				}
			}
			//3: Always: jump to other tetras
			if(!origin.getParent().equals(destination.getParent()))
				result = destination.select( source.getType(), event );
			
			return result;
		}

			/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#dispose()
		 */
		@Override
		public void dispose() {
			edge.getOrigin().removeCollINListener(listener);
			edge.getDestination().removeCollINListener(listener);
		}

		@Override
		public String toString() {
			ITetraNode<D> origin = (ITetraNode<D>) edge.getOrigin();
			ITetraNode<D> destination = (ITetraNode<D>) edge.getDestination();
			return origin.getName() + "(" + origin.getType()  + ")-" + 
					destination.getName() + "(" +	destination.getType() + ")";
		}
	}
}
