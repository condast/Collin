package org.collin.ui.test;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.ResponseEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class TestComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_AUTHENTICATION_URL = "http://localhost:10080/waterdiertjes/auth/";
	private Text textName;
	private Text textPassword;
	//public static final String S_ARNAC_URL = "http://www.condast.com:8080/arnac/rest/";

	public enum Requests{
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

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		textName = new Text(this, SWT.BORDER);
		textName.setText("TestNaam");
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Password");
		
		textPassword = new Text(this, SWT.BORDER);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		textPassword.setText("TestPassword");
		
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
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Button btnAddMeasurement = new Button(this, SWT.NONE);
		btnAddMeasurement.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		btnAddMeasurement.setText("Add Measurement");
		new Label(this, SWT.NONE);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
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
