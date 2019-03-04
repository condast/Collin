package org.collin.core.impl;

import java.lang.reflect.Constructor;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.IDelegateFactory;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractDelegateFactory<N,D extends Object> implements IDelegateFactory<N,D>{

	private N data;
	
	protected AbstractDelegateFactory( N data ) {
		this.data = data;
	}

	protected N getData() {
		return data;
	}
		
	@SuppressWarnings("unchecked")
	protected static <N,D extends Object> ICollINDelegate<N,D> constructDelegate( Class<?> clss, String className, 
			N sequence){
		if( StringUtils.isEmpty( className ))
			return null;
		Class< ICollINDelegate<N,D>> builderClass;
		 ICollINDelegate<N,D> delegate = null;
		try {
			builderClass = (Class<ICollINDelegate<N,D>>) clss.getClassLoader().loadClass( className );
 			Constructor< ICollINDelegate<N,D>> constructor = builderClass.getConstructor( IDataObject.class );
			delegate = constructor.newInstance( sequence );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delegate;
	}
}