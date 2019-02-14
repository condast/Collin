package org.collin.core.impl;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.ITetraNode;

public class SequenceDelegateFactory<D extends Object> extends AbstractDelegateFactory<D,SequenceNode<D>>{

	public SequenceDelegateFactory( SequenceNode<D> sequence ) {
		super( sequence );
	}

	
	/* (non-Javadoc)
	 * @see org.collin.core.impl.IDelegateFactory#createDelegate(java.lang.Class, org.collin.core.def.ITetraNode)
	 */
	@Override
	public ICollINDelegate<D> createDelegate( Class<?> clss, ITetraNode<D> node ){
		SequenceQuery<D> query = new SequenceQuery<D>( super.getData() );
		SequenceNode<D> sn = query.find(node.getType());
		if( sn == null )
			return null;
		return constructDelegate(clss, sn.getDelegate(), sn, node);
	}
}
