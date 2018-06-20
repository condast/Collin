package org.collin.authentication.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.collin.ui.authentication.AuthenticationComposite.Requests;
import org.condast.commons.authentication.dialog.AbstractLoginDialog;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.ResponseEvent;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.verification.IWidgetVerificationDelegate;
import org.condast.commons.ui.verification.IWidgetVerificationDelegate.VerificationTypes;
import org.eclipse.jface.dialogs.IDialogConstants;
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
	private boolean registering;
	private Link registerLink;
	private Text emailText;
	private String email;
	
	public CollinCallbackHandler() {
		this.registering = false;
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
				LoginDialog dialog = new LoginDialog( display.getActiveShell(), "Login");
				dialog.setCallback(callbacks);
				try {
					dialog.openDialog();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	    
    private class LoginDialog extends AbstractLoginDialog{
		private static final long serialVersionUID = 1L;

    	protected LoginDialog(Shell parentShell, String title) {
    		super(parentShell, title);
    	}
 	
    	@Override
		protected void onHandleLogin(SelectionEvent event) {
			try {
				WebClient client = new WebClient();
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

			registerLink = new Link(comp, SWT.NONE);
			registerLink.addSelectionListener(new SelectionAdapter() {
				private static final long serialVersionUID = 1L;

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						registering = true;
						registerLink.setText("Email:");
						emailText.setVisible(true);
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
			boolean valid = IWidgetVerificationDelegate.VerificationTypes.verify( VerificationTypes.EMAIL, emailText.getText() );
			return registering?valid: true;
		}
		protected Point getInitialSize() {
    		return new Point(500, 300);
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