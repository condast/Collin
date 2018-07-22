package org.collin.authentication.core;

import javax.servlet.http.HttpSession;

import org.condast.commons.authentication.ui.callback.AbstractCallbackHandler;
import org.condast.commons.messaging.http.AbstractHttpRequest.HttpStatus;
import org.condast.commons.number.NumberUtils;
import org.condast.commons.messaging.http.IHttpClientListener;
import org.condast.commons.messaging.http.ResponseEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.widgets.Display;

/**
 * Handles the callbacks
 */
public class CollinCallbackHandler extends AbstractCallbackHandler{

	private HttpSession session = RWT.getUISession().getHttpSession( );

	private IHttpClientListener listener = new IHttpClientListener() {

		@Override
		public void notifyResponse(ResponseEvent event) {
			if( event.getResponseCode() == HttpStatus.OK.getStatus()) {
				Display.getCurrent().asyncExec( new Runnable() {

					@Override
					public void run() {
						if( !NumberUtils.isStringNumeric( event.getResponse() ))
							return;							
						UISession session = RWT.getUISession();
						Object obj = session.getAttribute("handle");
						getDialog().close();
					}			
				});
			}	
		}	
	};
}