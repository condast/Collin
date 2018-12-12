package org.collin.core.graph;

public interface ICollINShape<D extends Object> {

	boolean addEdge(IEdge<D> edge);

	boolean removeEdge(IEdge<D> edge);

	boolean isChild(ICollINVertex<D> vertex);

	ICollINVertex<D> getVertex(String id);

	boolean removeVertex(ICollINVertex<D> vertex);

	IEdge<D>[] getEdges();

}