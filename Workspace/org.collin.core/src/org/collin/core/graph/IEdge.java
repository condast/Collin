package org.collin.core.graph;

public interface IEdge<D extends Object>{

	public enum Direction{
		NONE,
		FORWARD,
		BACKWARD;
	}
	
	public ICollINShape<D> getOwner();
	
	public ICollINVertex<D> getOrigin();
	
	public ICollINVertex<D> getDestination();	
	
	public Direction getDirection();

	public boolean isEqual( ICollINVertex<D> node1, ICollINVertex<D> node2 );

	boolean contains(ICollINVertex<D> vertex);
}