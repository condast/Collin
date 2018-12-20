package org.collin.ui.test;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.IHttpClientListener;
import org.condast.commons.messaging.http.ResponseEvent;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.condast.js.commons.push.PushRegistrationComposite;
import org.eclipse.nebula.widgets.richtext.RichTextEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Spinner;

public class TestComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_CONTEXT_PATH = "http://localhost:10080/moodle/module/";
	public static final String S_PUSH_CONTEXT_PATH = "http://localhost:10080/moodle/push/";

	public static final String S_SERVICE_WORKER = "http://localhost:10080/moodleresources/js/collin-service.js/";
	public static final String S_SUBSCRIPTION_SERVER =  S_PUSH_CONTEXT_PATH;

	private Text textName;
	private Text textPassword;

	private Spinner spinner_progress;
	private Spinner spinner_module;
	private Spinner spinner_activity;
	
	private String modulePath;
	
	public enum Requests{
		REGISTER,
		LOGIN,
		LOGOUT,
		UNREGISTER,
		SUBSCRIBE,
		START,
		LESSON,
		ADVICE;

		public String getPath(){
			switch( this ) {
			case SUBSCRIBE:
				return S_PUSH_CONTEXT_PATH + this.name().toLowerCase();
			default:
				return S_CONTEXT_PATH + this.name().toLowerCase();
			}
		}

		@Override
		public String toString() {
			String str = this.name().toLowerCase();
			return str;
		}
		
		public static Requests getRequest( URL url ){
			return getRequest( url.toExternalForm());
		}

		public static Requests getRequest( String path ){
			for( Requests request: values()){
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
		TOKEN,
		PATH,
		MODULE_ID,
		ACTIVITY_ID,
		PROGRESS;
		
		@Override
		public String toString() {
			return StringStyler.xmlStyleString(this.name());
		}		
	}

	private IHttpClientListener listener = new IHttpClientListener() {

		@Override
		public void notifyResponse(ResponseEvent event) {
			Requests request = Requests.getRequest(event.getRequest());
			switch( request ){
			case START:
				break;
			case LESSON:
				String uri = event.getResponse();
				if( StringUtils.isEmpty(uri))
					break;
				viewer.setText(uri);
				btnGo.setEnabled(!StringUtils.isEmpty(modulePath));
				break;
			default:
					break;
			}
		}
	};
	
	private PushRegistrationComposite pushComposite;
	private Text text;
	private Button btnSelectButton;
	private Button btnGo;
	
	private RichTextEditor viewer;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestComposite(Composite parent, int style) {
		super(parent, style);
		this.modulePath = ModuleBuilder.getDefaultModuleLocation();
		this.createComposite(parent, style);
	}
	
	protected void createComposite( Composite parent, int style ) {
		setLayout(new GridLayout(3, false));
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		textName = new Text(this, SWT.BORDER);
		textName.setText("TestNaam");
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		pushComposite = new PushRegistrationComposite(this, SWT.BORDER);
		pushComposite.setLayoutData(new GridData( SWT.FILL, SWT.FILL, true, false, 1, 2));
		pushComposite.setServiceWorker(S_SERVICE_WORKER);
		pushComposite.setServerRegistration(S_SUBSCRIPTION_SERVER);
		//pushComposite.setInput(PushRegistrationComposite.S_RESOURCE);
		pushComposite.setInput("/moodleresources/push.html");
				
		Label lblPasswordLabel = new Label(this, SWT.NONE);
		lblPasswordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPasswordLabel.setText("Password");
		
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
				WebClient client = new WebClient( S_CONTEXT_PATH);
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
					WebClient client = new WebClient( S_CONTEXT_PATH);
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
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		new Label(this, SWT.NONE);
		text = new Text(this, SWT.BORDER);
		text.setText(this.modulePath);
		text.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;
		
			@Override
			public void modifyText(ModifyEvent event) {
				try{
					modulePath = text.getText();
					btnSelectButton.setEnabled( !StringUtils.isEmpty(modulePath));
					btnGo.setEnabled( !StringUtils.isEmpty(modulePath));
				}
				catch(Exception ex ) {
					
				}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		btnSelectButton = new Button(this, SWT.NONE);
		btnSelectButton.setEnabled(!StringUtils.isEmpty(modulePath));
		btnSelectButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				WebClient client = new WebClient( S_CONTEXT_PATH);
				client.addListener(listener);
				Map<String, String> params = new HashMap<>();

				try {
					params.put( Parameters.ID.toString(), "1");
					params.put( Parameters.TOKEN.toString(), "12");
					params.put(Parameters.PATH.toString(), modulePath );
					client.sendGet(Requests.START, params);
					client.sendPost(Requests.SUBSCRIBE, params, "hello");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSelectButton.setText("Set Module");
				
		Label lblModuleId = new Label(this, SWT.NONE);
		lblModuleId.setText("Module Id:");	
		spinner_module = new Spinner(this, SWT.BORDER);
		spinner_module.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		spinner_module.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				WebClient client = new WebClient( S_CONTEXT_PATH);
				client.addListener(listener);
				Map<String, String> params = new HashMap<>();

				try {
					int moduleId = spinner_module.getSelection();
					params.put( Parameters.ID.toString(), String.valueOf( moduleId ));
					params.put( Parameters.TOKEN.toString(), "12");
					params.put( Parameters.MODULE_ID.toString(), String.valueOf( moduleId ));
					params.put( Parameters.ACTIVITY_ID.toString(), String.valueOf(0 ));
					client.sendGet(Requests.LESSON, params);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				super.widgetSelected(e);
			}		
		});
		new Label(this, SWT.NONE);

		Label lblActivityId = new Label(this, SWT.NONE);
		lblActivityId.setText("Activity Id:");	
		spinner_activity = new Spinner(this, SWT.BORDER);
		spinner_activity.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		spinner_activity.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				WebClient client = new WebClient( S_CONTEXT_PATH);
				client.addListener(listener);
				Map<String, String> params = new HashMap<>();

				try {
					int moduleId = spinner_module.getSelection();
					int activityId = spinner_activity.getSelection();
					params.put( Parameters.ID.toString(), String.valueOf( moduleId ));
					params.put( Parameters.TOKEN.toString(), "12");
					params.put( Parameters.MODULE_ID.toString(), String.valueOf( moduleId ));
					params.put( Parameters.ACTIVITY_ID.toString(), String.valueOf( activityId ));
					client.sendGet(Requests.LESSON, params);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				super.widgetSelected(e);
			}		
		});
		new Label(this, SWT.NONE);
		
				Label lblProgress = new Label(this, SWT.NONE);
				lblProgress.setText("Progress:");
		spinner_progress = new Spinner(this, SWT.BORDER);
		spinner_progress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		spinner_progress.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				WebClient client = new WebClient( S_CONTEXT_PATH);
				client.addListener(listener);
				Map<String, String> params = new HashMap<>();

				try {
					int moduleId = spinner_module.getSelection();
					int activityId = spinner_activity.getSelection();
					int progress = spinner_progress.getSelection();
					params.put( Parameters.ID.toString(), String.valueOf( moduleId ));
					params.put( Parameters.TOKEN.toString(), "12");
					params.put( Parameters.MODULE_ID.toString(), String.valueOf( moduleId ));
					params.put( Parameters.ACTIVITY_ID.toString(), String.valueOf( activityId ));
					params.put( Parameters.PROGRESS.toString(), String.valueOf( progress ));
					client.sendGet(Requests.ADVICE, params);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				super.widgetSelected(e);
			}		
		});

		new Label(this, SWT.NONE);
		btnGo = new Button(this, SWT.NONE);
		btnGo.setEnabled(false);
		btnGo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGo.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				WebClient client = new WebClient( S_CONTEXT_PATH);
				client.addListener(listener);
				Map<String, String> params = new HashMap<>();

				try {
					int moduleId = spinner_module.getSelection();
					int activityId = spinner_activity.getSelection();
					params.put( Parameters.ID.toString(), "1");
					params.put( Parameters.TOKEN.toString(), "12");
					params.put(Parameters.MODULE_ID.toString(), String.valueOf(moduleId));
					params.put(Parameters.ACTIVITY_ID.toString(), String.valueOf(activityId));
					int progress=  spinner_progress.getSelection();
					params.put(Parameters.PROGRESS.toString(), String.valueOf( progress));
					client.sendGet(Requests.ADVICE, params);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnGo.setText("Go");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		viewer = new RichTextEditor(this, SWT.BORDER);
		viewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private class WebClient extends AbstractHttpRequest<Requests>{

		public WebClient( String context ) {
			super(context );
		}

		public void sendGet( Requests request, Map<String, String> parameters ) throws Exception {
			super.sendGet(request.getPath(), parameters);
		}
		
		@Override
		protected void sendPost(Requests request, Map<String, String> parameters, String data) throws Exception {
			super.sendPost(request.getPath(), parameters, data);
		}

		@Override
		protected String onHandleResponse(URL url, int responsecode, BufferedReader reader) throws IOException {
			try{
				return super.transform(reader);
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			return Responses.BAD.name();
		}
	}
}
