package org.collin.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.collin.core.data.AbstractDataObject;

import org.condast.commons.Utils;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class SequenceNode extends AbstractDataObject<String>{

	public enum Nodes{
		COURSE,
		MODEL,
		VIEW,
		CONTROLLER,
		MODULES,
		MODULE,
		ACTIVITIES,
		ACTIVITY,
		FUNCTION,
		GOAL,
		TASK,
		SOLUTION,
		QUESTIONS,
		QUESTION,
		ANSWER,
		PARTS,
		PART,
		SEQUENCE,
		ADVICE,
		TEXT,
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

	public enum Types{
		NONE,
		DELAY;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	private Nodes node;
	private String id;
	private String name;
	private String collin;
	private int index;
	private String locale;
	private String delegate;

	//optional
	private String title;
	private String description;
	private String type;
	private String uri;
	private float progress;
	private long totalTime;

	private SequenceNode parent;

	private List<SequenceNode> children;

	public SequenceNode( Nodes node, Locale locale, String id, String name, int index, String title) {
		this( node, locale, id, name, null, index, -1 );
	}

	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, int index, String title) {
		this( node, locale, id, name, collin, index, -1 );
	}
	
	public SequenceNode( Nodes node, String id, String name, String collin, int index, String title, long totalTime) {
		this( node, Locale.ENGLISH, id, name, collin, index, totalTime );
	}
	
	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, int index, String title, long totalTime) {
		this( node, locale, id, name, collin, index, totalTime );
		this.title = title;
	}

	public SequenceNode( Nodes node, String id, String name, String collin, int index, long totalTime ) {
		this( node, Locale.ENGLISH, id, name, collin, index, totalTime );
	}

	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, int index ) {
		this( node, locale, id, name, collin, index, -1 );
	}
	
	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, int index, long totalTime ) {
		super();
		this.node = node;
		this.locale = locale.toString();
		this.id = id;
		this.name = name;
		this.collin = collin;
		this.index = index;
		this.locale = Locale.ENGLISH.toString();
		this.totalTime = totalTime;
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

	public String getCollin() {
		return collin;
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

	public Types getType() {
		return Types.valueOf( type );
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

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public String getLocale() {
		return locale;
	}

	public SequenceNode getParent() {
		return parent;
	}

	private void setParent(SequenceNode parent) {
		this.parent = parent;
	}

	public String getDelegate() {
		return delegate;
	}

	public void setDelegate(String delegate) {
		this.delegate = delegate;
	}

	public long getTotalTime() {
		return totalTime;
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
