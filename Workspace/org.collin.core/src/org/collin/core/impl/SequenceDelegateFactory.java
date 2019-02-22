package org.collin.core.impl;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.ITetraNode;

public class SequenceDelegateFactory<D extends Object> extends AbstractDelegateFactory<SequenceNode<D>,D>{

	public SequenceDelegateFactory( SequenceNode<D> sequence ) {
		super( sequence );
	}
	
	@Override
	public ICollINDelegate<SequenceNode<D>,D> createDelegate(Class<?> clss, ITetraNode<D> node) {
		SequenceQuery<D> query = new SequenceQuery<D>( super.getData() );
		SequenceNode<D> sn = query.find(node.getType());
		if( sn == null )
			return null;
		return constructDelegate(clss, sn.getDelegate(), sn, node);
	}
}
