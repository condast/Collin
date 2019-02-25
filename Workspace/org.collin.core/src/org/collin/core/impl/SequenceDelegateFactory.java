package org.collin.core.impl;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.ITetraNode;
import org.collin.core.def.ITetraNode.Nodes;
import org.condast.commons.strings.StringUtils;
public class SequenceDelegateFactory<D extends Object> extends AbstractDelegateFactory<SequenceNode<D>,D>{

	public static final String S_DELEGATE = "delegate";
	
	public SequenceDelegateFactory( SequenceNode<D> sequence ) {
		super( sequence );
	}
	
	@Override
	public ICollINDelegate<SequenceNode<D>,D> createDelegate(Class<?> clss, ITetraNode<D> node) {
		SequenceQuery<D> query = new SequenceQuery<D>( super.getData() );
		SequenceNode<D> task = query.find(Nodes.TASK);
		String delegate_str = task.getDelegate();
		if( StringUtils.isEmpty(delegate_str))
			return null;
		return constructDelegate(clss, delegate_str, task, node);
	}
}
