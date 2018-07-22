package org.collin.authentication.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.servlet.http.HttpSession;

import org.collin.ui.menu.CollinComposite.Requests;
import org.condast.commons.authentication.ui.dialog.AbstractLoginDialog;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.AbstractHttpRequest.HttpStatus;
import org.condast.commons.number.NumberUtils;
import org.condast.commons.messaging.http.IHttpClientListener;
import org.condast.commons.messaging.http.ResponseEvent;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.verification.IVerification;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Handles the callbacks
 */
public class CollinCallbackHandler implements CallbackHandler{

	private enum Parameters{
		ID,
		NAME,
		PASSWORD,
		EMAIL,
		TOKEN;
		
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}		
	}

	private Display display;
	private LoginDialog dialog;
	
	private WebClient client;	
	
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
						dialog.close();
					}			
				});
			}	
		}	
	};

	public CollinCallbackHandler() {
 		this.client = new WebClient();
		this.client.addListener(listener);
    }

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.callback.CallbackHandler#handle(javax.security.auth
	 * .callback.Callback[])
	 */
	@Override
	public void handle( final Callback[] callbacks ) throws IOException {
		this.display = Display.getDefault();
		display.asyncExec( new Runnable() {

			@Override
			public void run() {
				dialog = new LoginDialog( display.getActiveShell(), "Login");
		   		dialog.setBlockOnOpen(true);
				dialog.setCallback(callbacks);
				if( dialog.open() == Window.OK )
					return;
			}
			
		});
	}
	    
    private class LoginDialog extends AbstractLoginDialog{
		private static final long serialVersionUID = 1L;

		private boolean registering;
		private Text confirmText;
		private Link registerLink;
		private Text emailText;
		private String email;

    	protected LoginDialog(Shell parentShell, String title) {
    		super(parentShell, title);
    		this.registering = false;
    	}
 	
		protected Point getInitialSize() {
    		return new Point(500, 300);
    	}

    	@Override
		protected void onHandleLogin(SelectionEvent event) {
			try {
				Requests request = registering?Requests.REGISTER: Requests.LOGIN;
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put( Parameters.NAME.toString(), getName());
				parameters.put( Parameters.PASSWORD.toString(), getPassword());
				if( registering )
					parameters.put( Parameters.EMAIL.toString(), email);
				client.sendGet( request.getPath(), parameters);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite comp = (Composite) super.createDialogArea(parent);
			
			Text nameText = getNameText();
			nameText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					getButton( IDialogConstants.OK_ID).setEnabled(isValidEntry());
				}
			});

			Text passwordText = getPasswordText();
			passwordText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					getButton( IDialogConstants.OK_ID).setEnabled(isValidEntry());
				}
			});
			
			Label confirmLabel = new Label( comp, SWT.NONE );
			confirmLabel.setText("Confirm: ");
			confirmLabel.setLayoutData( new GridData( SWT.FILL, SWT.RIGHT, false, false ));
			confirmLabel.setVisible(registering);
			confirmText = new Text( comp, SWT.BORDER );
			confirmText.setVisible(registering);
			confirmText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
			confirmText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					getButton( IDialogConstants.OK_ID).setEnabled(isValidEntry());
				}
			});
			
			registerLink = new Link(comp, SWT.NONE);
			registerLink.addSelectionListener(new SelectionAdapter() {
				private static final long serialVersionUID = 1L;

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						registering = true;
						registerLink.setText("Email:");
						emailText.setVisible(registering);
						confirmLabel.setVisible(registering);
						confirmText.setVisible(registering);
						getButton( IDialogConstants.OK_ID).setEnabled(false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			registerLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			registerLink.setText("<a>Register</a>");

			emailText= new Text( comp, SWT.BORDER );
			emailText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
			emailText.setVisible(false);
			emailText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					getButton( IDialogConstants.OK_ID).setEnabled(isValidEntry());
					email = emailText.getText();
				}
			});
			return comp;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);
			getButton( IDialogConstants.OK_ID).setEnabled(false);
		}

		protected boolean isValidEntry() {
			if( StringUtils.isEmpty( getName() ) || StringUtils.isEmpty( getPassword() ))
				return false;
			if(!registering )
				return true;
			if( StringUtils.isEmpty( confirmText.getText() ) || !getPassword().equals( confirmText.getText()))
				return false;
			boolean valid = IVerification.VerificationTypes.verify( VerificationTypes.EMAIL, emailText.getText() );
			return registering?valid: true;
		}  
		
		
    }
 
	private class WebClient extends AbstractHttpRequest{

		@Override
		protected String onHandleResponse(URL url, int responsecode, BufferedReader reader) throws IOException {
			try{
				Requests request = Requests.getRequest(url);
				ResponseEvent response= new ResponseEvent( this, request.name(), reader );
				notifyListeners( response );
				return Responses.OK.name();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			return Responses.BAD.name();
		}
	}
}