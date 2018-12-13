package org.collin.core.operator;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
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
			operator = create( className, origin, destination );
			break;
		case GOAL:
			if( ITetraNode.Nodes.TASK.equals( destination.getType() ))
				operator = create( className, origin, destination );
			break;
		case TASK:
			if( ITetraNode.Nodes.SOLUTION.equals( destination.getType() ))
				operator = create( className, origin, destination );
			break;
		default:
			break;
		}
		return operator;
	}

	protected IOperator<D> create( String className, ITetraNode<D> origin, ITetraNode<D> destination ){
		if( StringUtils.isEmpty(className))
			return new DefaultOperator(origin, destination);
		else
			return constructOperator(clss, className, origin, destination);
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
				select((ITetraNode<D>) source, event);
			}

			@Override
			public String toString() {
				return origin.getName() + "(" + origin.getType()  + ")-" + 
						destination.getName() + "(" +	destination.getType() + ")";
			}
		};

		protected ITetraNode<D> origin, destination;

		private Logger logger = Logger.getLogger(this.getClass().getName());

		public DefaultOperator(ITetraNode<D> origin, ITetraNode<D> destination) {
			super();
			this.origin = origin;
			origin.addCollINListener(listener);
			this.destination =  destination;
			destination.addCollINListener(listener);
		}


		@Override
		public void setParameters(Attributes attrs) {
			// TODO Auto-generated method stub
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#contains(org.collin.core.def.ITetraNode)
		 */
		public boolean contains( ITetraNode<D> node ) {
			return origin.equals(node ) || destination.equals( node );
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#isEqual(org.collin.core.def.ITetraNode, org.collin.core.def.ITetraNode)
		 */
		protected boolean isEqual( ITetraNode<D> node1, ITetraNode<D> node2 ) {
			boolean result = origin.equals(node1) && destination.equals( node2 );
			return result? result: origin.equals(node2) && destination.equals( node1 );
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#getOther(org.collin.core.def.ITetraNode)
		 */
		protected ITetraNode<D> getOther( ITetraNode<D> node ) {
			return origin.equals(node)? destination: destination.equals( node )?origin: null;
		}

		/* (non-Javadoc)
		 * @see org.collin.core.essence.IOperator#select(org.collin.core.essence.TetraEvent)
		 */
		@Override
		public boolean select( ITetraNode<D> source, TetraEvent<D> event ) {
			ITetraNode<D> node = getOther( source );
			if( node == null )
				return false;
			switch( event.getResult()) {
			case SUCCESS:
				switch(source.getType() ) {
				case TASK:
					if( !ITetraNode.Nodes.SOLUTION.equals(node.getType()))
						return false;
					break;
				default:
					break;
				}
				break;
			case FAIL:
				switch(source.getType() ) {
				case FUNCTION:
					break;
				default:
					if( !ITetraNode.Nodes.FUNCTION.equals(node.getType()))
						return false;
					break;
				}
				break;
			default:
				break;
			}

			logger.info("Event to node: " + node.getName());
			return node.select( source.getType(), event );
		}

		/* (non-Javadoc)
		 * @see org.collin.core.connector.IConnector#dispose()
		 */
		@Override
		public void dispose() {
			origin.removeCollINListener(listener);
			destination.removeCollINListener(listener);
		}

		@Override
		public String toString() {
			return origin.getName() + "(" + origin.getType()  + ")-" + 
					destination.getName() + "(" +	destination.getType() + ")";
		}
	}
}
