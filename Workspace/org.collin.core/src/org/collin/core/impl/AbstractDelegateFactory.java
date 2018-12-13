package org.collin.core.impl;

import java.lang.reflect.Constructor;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.IDelegateFactory;
import org.collin.core.def.ITetraNode;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractDelegateFactory<T extends Object, D extends IDataObject<T>> implements IDelegateFactory<T,D>{

	private D data;
	
	protected AbstractDelegateFactory( D sequence ) {
		this.data = sequence;
	}

	protected D getData() {
		return data;
	}
		
	@SuppressWarnings("unchecked")
	protected static <T extends Object, D extends IDataObject<T>> ICollINDelegate<T, D> constructDelegate( Class<?> clss, String className, 
			SequenceNode sequence, ITetraNode<D> node){
		if( StringUtils.isEmpty( className ))
			return null;
		Class< ICollINDelegate<T,D>> builderClass;
		 ICollINDelegate<T,D> delegate = null;
		try {
			builderClass = (Class<ICollINDelegate<T,D>>) clss.getClassLoader().loadClass( className );
 			Constructor< ICollINDelegate<T,D>> constructor = builderClass.getConstructor( IDataObject.class, ITetraNode.class );;
			delegate = constructor.newInstance( sequence, node );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delegate;
	}

}
