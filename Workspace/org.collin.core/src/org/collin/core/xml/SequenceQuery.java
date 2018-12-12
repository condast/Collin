package org.collin.core.xml;

import org.collin.core.xml.SequenceNode.Nodes;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class SequenceQuery {

	public enum Attributes{
		ID,
		NAME,
		COLLIN;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.toString());
		}		
	}
	
	private SequenceNode root;

	public SequenceQuery(SequenceNode node) {
		super();
		this.root = node;
	}

	public SequenceNode find( Nodes node ) {
		return find( node, root );
	}
	
	protected SequenceNode find( Nodes node, SequenceNode nd ) {
		if( node == null )
			return null;
		if( node.equals(nd.getNode()))
			return nd;
		for( SequenceNode child: nd.getChildren() ) {
			SequenceNode result = find( node, child );
			if( result != null )
				return child;
		}
		return null;
	}

	public SequenceNode find( Attributes attr, String description ) {
		return find( description, root );
	}
	
	protected SequenceNode find( Attributes attr, String description, SequenceNode nd ) {
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
		for( SequenceNode child: nd.getChildren() ) {
			SequenceNode result = find( description, child );
			if( result != null )
				return child;
		}
		return null;
	}

	public SequenceNode findCollin( String name ) {
		return find( name, root );
	}
	
	protected SequenceNode find( String name, SequenceNode nd ) {
		if( StringUtils.isEmpty(name))
			return null;
		if( name.equals(nd.getCollin()))
			return nd;
		for( SequenceNode child: nd.getChildren() ) {
			SequenceNode result = find( name, child );
			if( result != null )
				return child;
		}
		return null;
	}

}
