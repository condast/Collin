package org.collin.ui.authentication;

import javax.security.auth.callback.CallbackHandler;

import org.eclipse.swt.widgets.Display;

public interface IDialogCallbackHandler extends CallbackHandler{

	public void init( Display display );
}