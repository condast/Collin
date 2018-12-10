package org.collin.core.essence;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.def.ICollINSelector;
import org.collin.core.def.ITetraNode;
import org.collin.core.transaction.TetraTransaction;
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
public abstract class AbstractCollINSelector<D extends Object> implements ICollINSelector<D>{

	private String id;
	private String name;
	
	private String description;
	
	private Collection<ITetraListener<D>> listeners;

	protected AbstractCollINSelector( String id, String name ) {
		super();
		this.id = id;
		this.name = StringUtils.isEmpty( name )? id: name;
		this.listeners = new ArrayList<>();
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
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean addTetraListener( ITetraListener<D> listener ) {
		return this.listeners.add( listener);
	}

	@Override
	public boolean removeTetraListener( ITetraListener<D> listener ) {
		return this.listeners.remove( listener);
	}
	
	protected void notifyTetraListeners( TetraTransaction<D> event ) {
		event.addHistory(this);
		for( ITetraListener<D> listener: this.listeners )
			listener.notifyNodeSelected( this, event);
	}

	protected Collection<ITetraListener<D>> getTetraListeners() {
		return listeners;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
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
