package org.collin.core.def;

import org.collin.core.essence.ITetraListener;
import org.collin.core.essence.TetraEvent;
import org.collin.core.graph.ICollINShape;
import org.collin.core.graph.ICollINVertex;
import org.collin.core.operator.IOperator;

public interface ITetraNode<D extends Object> extends ICollINVertex<D>{

	public enum Nodes{
		UNDEFINED(0),
		GOAL(1),
		TASK(2),
		SOLUTION(3),
		FUNCTION(4);
		
		private int index;

		private Nodes( int index ) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
		
		public static Nodes getNode( int index ) {
			for( Nodes node: values()) {
				if( node.getIndex() == index )
					return node;
			}
			return Nodes.UNDEFINED;
		}
	}

	public Nodes getType();

	public ICollINShape<D> getParent();

	String getDescription();

	void setDescription(String description);

	ITetraListener<D>[] getListeners();

	boolean select(Nodes type, TetraEvent<D> incoming);

	void setOperator(IOperator<D> operator);

}