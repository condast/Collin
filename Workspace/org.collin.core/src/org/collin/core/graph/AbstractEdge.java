package org.collin.core.graph;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.operator.IOperator;

public abstract class AbstractEdge<D extends Object> extends AbstractCollINVertex<D> implements IEdge<D> {

	private Direction direction;
	
	private ICollINShape<D> owner;
	
	private ICollINVertex<D> origin, destination;
	
	protected AbstractEdge( ICollINShape<D> owner, String id, String name, ICollINVertex<D> origin, ICollINVertex<D> destination, IOperator<D> operator ) {
		this( owner, id, name, origin, destination, Direction.NONE, operator );
	}
	
	protected AbstractEdge( ICollINShape<D> owner, String id, String name, ICollINVertex<D> origin, ICollINVertex<D> destination, Direction direction, IOperator<D> operator ) {
		super( id, name, operator );
		this.origin = origin;
		this.destination = destination;
		this.direction = direction;
		this.owner = owner;
	}

	@Override
	public ICollINShape<D> getOwner() {
		return owner;
	}

	@Override
	public ICollINVertex<D> getOrigin() {
		return origin;
	}

	@Override
	public ICollINVertex<D> getDestination() {
		return destination;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public boolean isEqual(ICollINVertex<D> vertex1, ICollINVertex<D> vertex2) {
		boolean result = origin.equals(vertex1) && destination.equals( vertex2 );
		return result? result: origin.equals(vertex2) && destination.equals( vertex1 );
	}

	@Override
	public boolean contains(ICollINVertex<D> vertex) {
		if( vertex == null )
			return false;
		return ( this.origin.equals(vertex) || this.destination.equals(vertex));
	}

	public boolean fire(TetraEvent<D> event) {
		if( origin.equals( event.getSource())) {
			return super.getOperator().select( (ITetraNode<D>) origin, event);
		}
		if( destination.equals( event.getSource())) {
			return super.getOperator().select( (ITetraNode<D>) destination, event);
		}
		return false;
	}

	public static String combine( String str1, String str2 ) {
		return str1 + "-" + str2;
	}
	
	@Override
	public int hashCode() {	
		Long total = new Long( this.getOrigin().hashCode()<<32 + this.destination.hashCode());
		return total.hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if( super.equals(obj))
			return true;
		if(!( obj instanceof IEdge ))
			return false;
		IEdge<D> edge = (IEdge<D>) obj;
		return this.isEqual(edge.getOrigin(), edge.getDestination());
	}

	@Override
	public String toString() {
		return "[" + this.origin.toString() + ", " + this.destination.toString() + "]";
	}
}
