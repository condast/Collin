package org.collin.core.impl;

import org.collin.core.def.ITetraNode;
import org.collin.core.impl.SequenceNode.Nodes;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class SequenceQuery<D extends Object> {

	public enum Attributes{
		ID,
		NAME,
		COLLIN;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.toString());
		}		
	}
	
	private SequenceNode<D> root;

	public SequenceQuery(SequenceNode<D> node) {
		super();
		this.root = node;
	}

	public SequenceNode<D> getTetra( long moduleId, long activityId, Nodes node ) {
		SequenceNode<D> module = find( Nodes.MODULE, String.valueOf(moduleId), root );
		SequenceNode<D> activity = find( Nodes.ACTIVITY, String.valueOf(activityId), module );
		return searchParent(node, activity);
	}

	public SequenceNode<D> searchParent( Nodes node, SequenceNode<D> sn ) {
		if( sn == null )
			return null;
		if( node.equals(sn.getNode()))
			return sn;
		for( SequenceNode<D> child: sn.getChildren() ) {
			if( node.equals( child.getNode()))
				return child;
		}
		return searchParent(node, sn.getParent());
	}

	/**
	 * Returns the first upstream occurrence of a non-null attribute
	 * with the given name  
	 * @param attribute
	 * @param sn
	 * @return
	 */
	/**
	 * Returns the first upstream occurrence of a non-null attribute
	 * with the given name  
	 * @param attribute
	 * @param sn
	 * @return
	 */
	public String findUpStream( String attribute ) {
		return findUpStream(attribute, this.root);
	}
		
	protected String findUpStream( String attribute, SequenceNode<D> sn ) {
		if( sn == null )
			return null;
		String result = sn.getValue(attribute);
		if( !StringUtils.isEmpty(result))
			return result;
		return findUpStream( attribute, sn.getParent());
	}

	public SequenceNode<D> find( Nodes node ) {
		return find( node, root );
	}
	
	protected SequenceNode<D> find( Nodes node, SequenceNode<D> nd ) {
		if( node == null )
			return null;
		if( node.equals(nd.getNode()))
			return nd;
		for( SequenceNode<D> child: nd.getChildren() ) {
			SequenceNode<D> result = find( node, child );
			if( result != null )
				return result;
		}
		return null;
	}

	public SequenceNode<D> find( Nodes node, String id ) {
		if( StringUtils.isEmpty(id))
			return null;
		return find( node, id, root );
	}
	
	protected SequenceNode<D> find( Nodes node, String id, SequenceNode<D> nd ) {
		if(( node == null ) ||(  nd==null ))
			return null;
		if(( node.equals(nd.getNode())) && ( nd.getId().equals(id )))
			return nd;
		for( SequenceNode<D> child: nd.getChildren() ) {
			SequenceNode<D> result = find( node, id, child );
			if( result != null )
				return result;
		}
		return null;
	}

	public SequenceNode<D> find(ITetraNode.Nodes type ) {
		return find( type, root );
	}
	
	protected SequenceNode<D> find( ITetraNode.Nodes type, SequenceNode<D> nd ) {
		if(( type == null ) ||( ITetraNode.Nodes.UNDEFINED.equals(type)))
			return null;
		
		SequenceNode.Nodes sn = SequenceNode.Nodes.valueOf( type.name());	
		return find( sn );
	}

	public SequenceNode<D> find( Attributes attr, String description ) {
		return find( description, root );
	}
	
	protected SequenceNode<D> find( Attributes attr, String description, SequenceNode<D> nd ) {
		if( StringUtils.isEmpty(description))
			return null;
		switch( attr ) {
		case ID:
			if( description.equals(nd.getId()))
				return nd;
			break;
		case NAME:
			if( description.equals(nd.getName()))
				return nd;
			break;
		case COLLIN:
			if( description.equals(nd.getCollin()))
				return nd;
			break;
		}
		for( SequenceNode<D> child: nd.getChildren() ) {
			SequenceNode<D> result = find( description, child );
			if( result != null )
				return child;
		}
		return null;
	}

	public SequenceNode<D> findCollin( String name ) {
		return find( name, root );
	}
	
	protected SequenceNode<D> find( String name, SequenceNode<D> nd ) {
		if( StringUtils.isEmpty(name))
			return null;
		if( name.equals(nd.getCollin()))
			return nd;
		for( SequenceNode<D> child: nd.getChildren() ) {
			SequenceNode<D> result = find( name, child );
			if( result != null )
				return child;
		}
		return null;
	}
}
