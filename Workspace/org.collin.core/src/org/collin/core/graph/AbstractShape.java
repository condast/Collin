package org.collin.core.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.collin.core.graph.ICollINVertex;
import org.collin.core.graph.IEdge.Direction;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractShape<D extends Object> extends AbstractCollINVertex<D> implements ICollINShape<D> {

	private Collection<IEdge<D>> edges;

	protected AbstractShape( String type ) {
		this( type, type);
	}
	
	protected AbstractShape( String id, String name ) {
		super( id, name, null );
		this.edges = new HashSet<>();
	}

	/* (non-Javadoc)
	 * @see org.collin.core.graph.ICollINShape#addEdge(org.collin.core.graph.IEdge)
	 */
	public boolean addEdge(IEdge<D> edge ) {
		if( this.edges.contains(edge))
			return false;
		if( containsEdge(edge.getOrigin(), edge.getDestination()))
			return false;
		return this.edges.add( edge );
	}

	/* (non-Javadoc)
	 * @see org.collin.core.graph.ICollINShape#removeEdge(org.collin.core.graph.IEdge)
	 */
	public boolean removeEdge( IEdge<D> edge ) {
		return this.edges.remove( edge );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IEdge<D>[] getEdges(){
		return edges.toArray( new IEdge[ this.edges.size() ]);
	}

	/* (non-Javadoc)
	 * @see org.collin.core.graph.ICollINShape#isChild(org.collin.core.graph.ICollINVertex)
	 */
	@Override
	public boolean isChild(ICollINVertex<D> vertex) {
		Collection<ICollINVertex<D>> vertices = getVertices();
		return vertices.contains(vertex);
	}

	/* (non-Javadoc)
	 * @see org.collin.core.graph.ICollINShape#getVertex(java.lang.String)
	 */
	@Override
	public ICollINVertex<D> getVertex( String id ) {
		if( StringUtils.isEmpty(id))
			return null;
		Collection<ICollINVertex<D>> vertices = getVertices();
		for( ICollINVertex<D> vertex: vertices ) {
			if( id.equals(vertex.getId() ))
				return vertex;
		}
		return null;
	}

	protected Collection<ICollINVertex<D>> getVertices() {
		Collection<ICollINVertex<D>> vertices = new HashSet<>();
		for( IEdge<D> edge: this.edges ) {
			vertices.add(edge.getOrigin());
			vertices.add(edge.getDestination());
		}
		return vertices;
	}

	/* (non-Javadoc)
	 * @see org.collin.core.graph.ICollINShape#removeVertex(org.collin.core.graph.ICollINVertex)
	 */
	@Override
	public boolean removeVertex( ICollINVertex<D> vertex) {
		Collection<IEdge<D>> temp = new ArrayList<>();
		for( IEdge<D> edge: this.edges ) {
			if( edge.contains(vertex))
				temp.add( edge );
		}
		return this.edges.removeAll(temp);
	}

	public boolean containsEdge( ICollINVertex<D> v1, ICollINVertex<D> v2 ) {
		for( IEdge<D> edge: this.edges ) {
			if( edge.getOrigin().equals(v1) && edge.getDestination().equals(v2 ))
				return !Direction.BACKWARD.equals( edge.getDirection());
			if( edge.getOrigin().equals(v2) && edge.getDestination().equals(v1 ))
				return !Direction.FORWARD.equals( edge.getDirection());;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.isEmpty(getName())? super.getId(): super.getName();
	}
}