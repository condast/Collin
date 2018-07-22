package org.collin.dashboard.authentication;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.collin.dashboard.ds.Dispatcher;

/*
*
* Copyright (c) 2000, 2002, Oracle and/or its affiliates. All rights reserved.
*
* Redistribution and use in source and binary forms, with or
* without modification, are permitted provided that the following
* conditions are met:
*
* -Redistributions of source code must retain the above copyright
* notice, this  list of conditions and the following disclaimer.
*
* -Redistribution in binary form must reproduct the above copyright
* notice, this list of conditions and the following disclaimer in
* the documentation and/or other materials provided with the
* distribution.
*
* Neither the name of Oracle nor the names of
* contributors may be used to endorse or promote products derived
* from this software without specific prior written permission.
*
* This software is provided "AS IS," without a warranty of any
* kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
* WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
* EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
* DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR
* RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
* ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
* FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
* SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
* CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
* THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that Software is not designed, licensed or
* intended for use in the design, construction, operation or
* maintenance of any nuclear facility.
*/

import org.eclipse.swt.SWT;

/**
* <p> This sample LoginModule authenticates users with a password.
*
* <p> This LoginModule only recognizes one user:       testUser
* <p> testUser's password is:  testPassword
*
* <p> If testUser successfully authenticates itself,
* a <code>SamplePrincipal</code> with the testUser's user name
* is added to the Subject.
*
* <p> This LoginModule recognizes the debug option.
* If set to true in the login Configuration,
* debug messages will be output to the output stream, System.out.
*
*/
public class CollinLoginModule implements LoginModule {

    private CallbackHandler callbackHandler;
    private Subject subject;
    
	public CollinLoginModule() {
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callback, Map<String, ?> arg2, Map<String, ?> arg3) {
        this.subject = subject;
        this.callbackHandler = callback;
    }

    public boolean login() throws LoginException {
        Callback label = new TextOutputCallback(
                TextOutputCallback.INFORMATION,
                "Please login! Hint: user1/rap");
        NameCallback nameCallback = new NameCallback("Username:");
        PasswordCallback passwordCallback = new PasswordCallback(
                "Password:", false);
        try {
        	callbackHandler.handle(new Callback[] { label, nameCallback,
                    passwordCallback });
        } catch (ThreadDeath death) {
            LoginException loginException = new LoginException();
            loginException.initCause(death);
            throw loginException;
        } catch (Exception exception) {
            LoginException loginException = new LoginException();
            loginException.initCause(exception);
            throw loginException;
        }
        return true;
    }

    public boolean commit() throws LoginException {
        //subject.getPublicCredentials().add(USERS);
        //subject.getPrivateCredentials().add(Display.getCurrent());
        subject.getPrivateCredentials().add(SWT.getPlatform());
        return true;
     }

    public boolean abort() throws LoginException {
         return true;
    }

    public boolean logout() throws LoginException {
    	Dispatcher.getInstance().logoutRequest();
    	return true;
    }
}