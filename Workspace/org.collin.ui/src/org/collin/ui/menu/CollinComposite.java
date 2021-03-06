package org.collin.ui.menu;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.condast.commons.authentication.core.AuthenticationEvent;
import org.condast.commons.authentication.core.IAuthenticationListener;
import org.condast.commons.authentication.core.ILoginProvider;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.ResponseEvent;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

public class CollinComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_COLLIN = "collin";

	public static final String S_AUTHENTICATION_URL = "http://localhost:10080/collin/auth/";
	private Text textName;
	private Text textPassword;
	//public static final String S_COLLIN_URL = "http://www.condast.com:8080/collin/auth/";
	
	private ILoginContext module;

	public enum Requests{
		ACTIVATE,
		REGISTER,
		LOGIN,
		LOGOUT,
		UNREGISTER;

		public String getPath(){
			return S_AUTHENTICATION_URL + this.name().toLowerCase();
		}

		@Override
		public String toString() {
			String str = this.name().toLowerCase();
			return str;
		}
		
		public static Requests getRequest( URL url ){
			for( Requests request: values()){
				String path = url.toExternalForm();
				if( path.contains(request.toString()))
					return request;
			}
			return null;
		}
	}

	private enum Parameters{
		ID,
		NAME,
		PASSWORD,
		TOKEN;
		
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}		
	}

	private IAuthenticationListener alistener=  new IAuthenticationListener(){

		@Override
		public void notifyLoginChanged(final AuthenticationEvent event) {
			getDisplay().asyncExec( new Runnable() {

				@Override
				public void run() {
					try {
						switch( event.getEvent()) {
						case REGISTER:
						case LOGIN:
							menuButton.loggedIn( event.getUser());
							break;
						default:
							//ILoginUser user = menuButton.getData();
							//provider.logout( user.getId(), user.getToken() );
							menuButton.logOut();
							break;
						}
					}
					catch( Exception ex ) {
						ex.printStackTrace();
					}
				}			
			});
		}	
	};

	private ILoginProvider provider;

	private MenuButton menuButton;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CollinComposite(Composite parent, int style) {
		super(parent, style);
		createComposite( parent, style );
	}
	
	protected void createComposite( Composite parent, int style ) {
		setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblName.setText("Name:");
		
		textName = new Text(this, SWT.BORDER);
		textName.setText("TestNaam");
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblNewLabel.setText("Password");
		
		textPassword = new Text(this, SWT.BORDER);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		textPassword.setText("TestPassword");

		menuButton = new MenuButton(this, SWT.NONE);
		menuButton.setData( RWT.CUSTOM_VARIANT, S_COLLIN);
		menuButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		Link loginLink = new Link(this, SWT.NONE);
		loginLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		loginLink.setText("<a>Login</a>");
		loginLink.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				WebClient client = new WebClient();
				try {
					Map<String, String> parameters = new HashMap<String, String>();
					parameters.put( Parameters.NAME.toString(), textName.getText());
					client.sendGet( Requests.LOGIN.getPath(), parameters);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Link registerLink = new Link(this, SWT.NONE);
		registerLink.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					WebClient client = new WebClient();
					Map<String, String> parameters = new HashMap<String, String>();
					parameters.put( Parameters.NAME.toString(), textName.getText());
					parameters.put( Parameters.PASSWORD.toString(), textPassword.getText());
					client.sendGet( Requests.REGISTER.getPath(), parameters);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		registerLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		registerLink.setText("<a>Register</a>");
	}

	public void setLoginProvider( ILoginProvider provider ) {
		this.provider = provider;
		this.provider.addAuthenticationListener(alistener);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void dispose() {
		this.provider.removeAuthenticationListener(alistener);
		super.dispose();
	}

	private class WebClient extends AbstractHttpRequest{

		public WebClient() {
			super();
		}

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
