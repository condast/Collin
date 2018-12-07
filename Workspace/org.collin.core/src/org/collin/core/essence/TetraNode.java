package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.def.ITetraNode;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

/**
 * A Tetra node is the core atom of tetralogic. It serves as a placeholder for the tetra. 
 * Note that the data object can itself be a tetra!
 * 
 * @author Condast
 *
 * @param <D>
 */
public class TetraNode<D extends Object> implements ITetraNode<D>{

	private String id;
	private String name;
	
	private D data;
	
	private ITetraNode.Nodes type;
	
	private ITetra<D> parent;

	private int selected;
	
	private String description;

	private Collection<ITetraListener<D>> listeners;
		
	public TetraNode( ITetra<D> parent, String id, String name, ITetraNode.Nodes type, D data ) {
		super();
		this.parent = parent;
		this.id = id;
		this.name = StringUtils.isEmpty( name )? id: name;
		this.type = type;
		this.selected = 0;
		this.listeners = new ArrayList<>();
	}

	public TetraNode(ITetra<D> parent, String id, String name, ITetraNode.Nodes type) {
		this( parent, id, name, type, null );
	}

	protected TetraNode(String id, String name, ITetraNode.Nodes type) {
		this( null, id, name, type, null );
	}

	@Override
	public String getId() {
		return id;
	}

	@Override                                                
	public String getName() {
		return name;
	}

	@Override
	public Nodes getType() {
		return type;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public D getData() {
		return this.data;
	}

	@Override
	public ITetra<D> getParent() {
		return parent;
	}

	@Override
	public boolean addTetraListener( ITetraListener<D> listener ) {
		return this.listeners.add( listener);
	}

	@Override
	public boolean removeTetraListener( ITetraListener<D> listener ) {
		return this.listeners.remove( listener);
	}
	
	protected void notifyTetraListeners( TetraEvent<D> event ) {
		event.addHistory(this);
		for( ITetraListener<D> listener: this.listeners )
			listener.notifyNodeSelected(event);
	}

	@Override
	public int getSelected() {
		return selected;
	}
	
	@Override
	public int balance( int offset ) {
		this.selected -= offset;
		return this.selected;
	}

	/* (non-Javadoc)
	 * @see org.collin.core.essence.ITetraNode#select()
	 */
	@Override
	public boolean select( TetraEvent<D> event ) {
		if( this.equals( event.getPropagate()) || event.hasBeenProcessed(this ))
			return false;
		this.selected++;
		notifyTetraListeners( event );
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( this.type.toString() + ": ");
		if( !StringUtils.isEmpty( this.name ))
			buffer.append(this.name);
		return buffer.toString();
	}

	/**
	 * Create a default id
	 * @param parent
	 * @param node
	 * @return
	 */
	public static String createId( ITetra<?> parent, ITetraNode.Nodes node) {
		return parent.getId() + "." + StringStyler.toMethodString(node.name());
	}

	/**
	 * Create a default name
	 * @param parent
	 * @param node
	 * @return
	 */
	public static String createName( ITetraNode<?> parent, ITetraNode.Nodes node) {
		return parent.getName() + "." + StringStyler.toMethodString(node.name());
	}

}
