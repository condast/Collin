package org.collin.core.xml;

import java.util.ArrayList;
import java.util.List;

import org.condast.commons.Utils;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class SequenceNode {

	public enum Nodes{
		COURSE,
		MODEL,
		VIEW,
		CONTROLLER,
		MODULES,
		MODULE,
		PARTS,
		PART,
		SEQUENCE,
		STEP;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isNode( String value ){
			if( StringUtils.isEmpty( value ))
				return false;
			for( Nodes node: values() ){
				if( node.name().equals( value ))
					return true;
			}
			return false;
		}
	}

	private Nodes node;
	private String id;
	private String name;
	private int index;

	//optional
	private String title;
	private String description;
	private String type;
	private String uri;

	private SequenceNode parent;

	private List<SequenceNode> children;

	public SequenceNode( Nodes node, String id, String name, int index, String title) {
		this( node, id, name, index );
		this.title = title;
	}

	public SequenceNode( Nodes node, String id, String name, int index ) {
		super();
		this.node = node;
		this.id = id;
		this.name = name;
		this.index = index;
		this.children = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public Nodes getNode() {
		return node;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public SequenceNode getParent() {
		return parent;
	}

	private void setParent(SequenceNode parent) {
		this.parent = parent;
	}

	public void addChild( SequenceNode child ) {
		this.children.add(child);
		child.setParent(this);
	}

	public List<SequenceNode> getChildren(){
		return children;
	}
	
	public SequenceNode find( String id ) {
		for( SequenceNode child: this.children ) {
			if( child.getId().equals(id))
				return child;
		}
		return null;
	}
	
	public SequenceNode firstChild() {
		return ( Utils.assertNull(children)?null: children.get(0)); 
	}

	public SequenceNode lastChild() {
		return ( Utils.assertNull(children)?null: children.get(children.size()-1)); 
	}

	public SequenceNode next( SequenceNode arg ) {
		if( !children.contains(arg))
			return null;
		if( arg.getIndex() >= children.size())
			return null;
		return children.get( arg.getIndex()+1);
	}

	public SequenceNode previous( SequenceNode arg ) {
		if( !children.contains(arg))
			return null;
		if( arg.getIndex() == 0)
			return null;
		return children.get( arg.getIndex()-1);
	}

	public SequenceNode offset( SequenceNode arg, int offset ) {
		if( !children.contains(arg))
			return null;
		int location = arg.getIndex() + offset;
		if(( location < 0 ) || ( location >= children.size() ))
			return null;
		return children.get( location);
	}

	@Override
	public String toString() {
		return this.node.name() + ": " + this.id;
	}
}
