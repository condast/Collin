package org.collin.core.impl;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.ITetraNode;

public class SequenceDelegateFactory extends AbstractDelegateFactory<String, SequenceNode>{

	public SequenceDelegateFactory( SequenceNode sequence ) {
		super( sequence );
	}

	
	/* (non-Javadoc)
	 * @see org.collin.core.impl.IDelegateFactory#createDelegate(java.lang.Class, org.collin.core.def.ITetraNode)
	 */
	@Override
	public ICollINDelegate<String, SequenceNode> createDelegate( Class<?> clss, ITetraNode<SequenceNode> node ){
		SequenceQuery query = new SequenceQuery( super.getData() );
		SequenceNode sn = query.find(node.getType());
		if( sn == null )
			return null;
		return constructDelegate(clss, sn.getDelegate(), sn, node);
	}
}
