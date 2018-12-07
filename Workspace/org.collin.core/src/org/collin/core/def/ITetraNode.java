package org.collin.core.def;

import org.collin.core.essence.ITetra;
import org.collin.core.essence.ITetraListener;
import org.collin.core.essence.TetraEvent;

public interface ITetraNode<D extends Object> {

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

	public String getId();
	
	public Nodes getType();

	public ITetra<D> getParent();

	boolean addTetraListener(ITetraListener<D> listener);

	boolean removeTetraListener(ITetraListener<D> listener);
		
	public D getData();

	boolean select( TetraEvent<D> event );

	int getSelected();

	int balance(int offset);

	String getDescription();

	void setDescription(String description);

	String getName();

}