package org.collin.authentication.core;

import javax.security.auth.callback.Callback;

import org.condast.commons.mediator.AbstractMediator;

public class LoginMediator extends AbstractMediator<Callback>{

	private static LoginMediator mediator =  new LoginMediator();
	
	public static LoginMediator getIntance(){
		return mediator;
	}
}
