package org.collin.core.impl;

import java.lang.reflect.Constructor;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.IDelegateFactory;
import org.collin.core.def.ITetraNode;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractDelegateFactory<D,N extends Object> implements IDelegateFactory<D>{

	private N data;
	
	protected AbstractDelegateFactory( N sequence ) {
		this.data = sequence;
	}

	protected N getData() {
		return data;
	}
		
	@SuppressWarnings("unchecked")
	protected static <D extends Object> ICollINDelegate<D> constructDelegate( Class<?> clss, String className, 
			SequenceNode<D> sequence, ITetraNode<D> node){
		if( StringUtils.isEmpty( className ))
			return null;
		Class< ICollINDelegate<D>> builderClass;
		 ICollINDelegate<D> delegate = null;
		try {
			builderClass = (Class<ICollINDelegate<D>>) clss.getClassLoader().loadClass( className );
 			Constructor< ICollINDelegate<D>> constructor = builderClass.getConstructor( IDataObject.class, ITetraNode.class );;
			delegate = constructor.newInstance( sequence, node );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delegate;
	}
}