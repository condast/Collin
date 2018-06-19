package org.collin.authentication.core;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Handles the callbacks
 */
public class CollinCallbackHandler implements CallbackHandler {

	LoginMediator mediator = LoginMediator.getIntance();

	@Override
	public void handle(Callback[] arg0) throws IOException, UnsupportedCallbackException {
		for( Callback cb: arg0 ) {
			mediator.add(cb);
		}
	}
}