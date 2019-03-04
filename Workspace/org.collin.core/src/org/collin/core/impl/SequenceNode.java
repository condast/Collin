package org.collin.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.collin.core.data.AbstractDataObject;
import org.condast.commons.Utils;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class SequenceNode<D extends Object> extends AbstractDataObject<D>{

	public static final int DEFAULT_DURATION = 5000;//5 seconds

	public enum Nodes{
		COURSE,
		MODEL,
		VIEW,
		CONTROLLER,
		SECTIONS,
		SECTION,
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
		STEP,
		SUCCESS,
		PROGRESS,
		FAIL;

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

	private SequenceNode<D> parent;
	
	private Map<String, String> attributes;

	private List<SequenceNode<D>> children;

	public SequenceNode( Nodes node, Locale locale, String id, String name, Attributes attributes, int index, String title) {
		this( node, locale, id, name, null, attributes, index );
	}

	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, Attributes attributes, int index, String title) {
		this( node, locale, id, name, collin, attributes, index );
	}
	
	public SequenceNode( Nodes node, String id, String name, String collin, Attributes attributes, int index, String title) {
		this( node, Locale.ENGLISH, id, name, collin, attributes, index );
	}
	
	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, Attributes attributes, int index, String title, long totalTime) {
		this( node, locale, id, name, collin, attributes, index);
		this.title = title;
	}

	public SequenceNode( Nodes node, String id, String name, String collin, Attributes attributes, int index ) {
		this( node, Locale.ENGLISH, id, name, collin, attributes, index );
	}
	
	public SequenceNode( Nodes node, Locale locale, String id, String name, String collin, Attributes attributes, int index ) {
		super();
		this.node = node;
		this.locale = locale.toString();
		this.id = id;
		this.name = name;
		this.collin = collin;
		this.index = index;
		this.locale = Locale.ENGLISH.toString();
		this.attributes = new HashMap<>();
		for( int i=0; i<attributes.getLength(); i++ ) {
			this.attributes.put(attributes.getQName(i), attributes.getValue(i));
		}
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
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

	public SequenceNode<D> getParent() {
		return parent;
	}

	private void setParent(SequenceNode<D> parent) {
		this.parent = parent;
	}

	public String getDelegate() {
		return delegate;
	}

	public void setDelegate(String delegate) {
		this.delegate = delegate;
	}

	public int getDuration() {
		String duration = this.getValue( StringStyler.xmlStyleString( AttributeNames.DURATION.name()));
		return StringUtils.isEmpty(duration)?DEFAULT_DURATION: Integer.parseInt(duration);
	}

	public String getValue( String key ) {
		return attributes.get(key);
	}
	
	public void addChild( SequenceNode<D> child ) {
		this.children.add(child);
		child.setParent(this);
	}

	public List<SequenceNode<D>> getChildren(){
		return children;
	}
	
	public SequenceNode<D> find( String id ) {
		for( SequenceNode<D> child: this.children ) {
			if( child.getId().equals(id))
				return child;
		}
		return null;
	}

	public SequenceNode<D> firstChild() {
		return ( Utils.assertNull(children)?null: children.get(0)); 
	}

	public SequenceNode<D> lastChild() {
		return ( Utils.assertNull(children)?null: children.get(children.size()-1)); 
	}

	public SequenceNode<D> next( SequenceNode<D> arg ) {
		if( !children.contains(arg))
			return null;
		if( arg.getIndex() >= children.size())
			return null;
		return children.get( arg.getIndex()+1);
	}

	public SequenceNode<D> previous( SequenceNode<D> arg ) {
		if( !children.contains(arg))
			return null;
		if( arg.getIndex() == 0)
			return null;
		return children.get( arg.getIndex()-1);
	}

	public SequenceNode<D> offset( SequenceNode<D> arg, int offset ) {
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
