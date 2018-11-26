package org.collin.ui.test;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.collin.core.xml.ModuleBuilder;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.IHttpClientListener;
import org.condast.commons.messaging.http.ResponseEvent;
import org.condast.commons.strings.StringStyler;
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
	
	private Text textName;
	private Text textPassword;

	private Spinner spinner_progress;
	private Spinner spinner_module;
	
	private String modulePath;
	private int lessonId;
	
	public enum Requests{
		REGISTER,
		LOGIN,
		LOGOUT,
		UNREGISTER,
		LESSON,
		ADVICE;

		public String getPath(){
			return S_CONTEXT_PATH + this.name().toLowerCase();
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
		TOKEN,
		PATH,
		MODULE_ID,
		PROGRESS;
		
		@Override
		public String toString() {
			return StringStyler.xmlStyleString(this.name());
		}		
	}

	private IHttpClientListener listener = new IHttpClientListener() {

		@Override
		public void notifyResponse(ResponseEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private Text text;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestComposite(Composite parent, int style) {
		super(parent, style);
		this.modulePath = ModuleBuilder.getDefaultModuleLocation();
		this.lessonId = 1;
		this.createComposite(parent, style);
	}
	
	protected void createComposite( Composite parent, int style ) {
		setLayout(new GridLayout(4, false));
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		new Label(this, SWT.NONE);
		
		textName = new Text(this, SWT.BORDER);
		textName.setText("TestNaam");
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		new Label(this, SWT.NONE);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Password");
		new Label(this, SWT.NONE);
		
		textPassword = new Text(this, SWT.BORDER);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		textPassword.setText("TestPassword");
		new Label(this, SWT.NONE);
		
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
		new Label(this, SWT.NONE);
		
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
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		new Label(this, SWT.NONE);
		
		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Label lblLesson = new Label(this, SWT.NONE);
		lblLesson.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLesson.setText("Lesson:");
		
		Spinner spinner_lesson = new Spinner(this, SWT.BORDER);
		spinner_lesson.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				Spinner spinner = (Spinner) e.widget;
				lessonId = spinner.getSelection();
			}
		});
		spinner_lesson.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		text = new Text(this, SWT.BORDER);
		text.setText(this.modulePath);
		text.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;
		
			@Override
			public void modifyText(ModifyEvent event) {
				try{
					modulePath = text.getText();
				}
				catch(Exception ex ) {
					
				}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				WebClient client = new WebClient( S_CONTEXT_PATH);
				client.addListener(listener);
				Map<String, String> params = new HashMap<>();

				try {
					int moduleId = spinner_module.getSelection();
					params.put( Parameters.ID.toString(), "1");
					params.put( Parameters.TOKEN.toString(), "12");
					params.put(Parameters.PATH.toString(), modulePath );
					client.sendGet(Requests.LESSON, params);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setText("Set Module");
		
		Label lblModuleId = new Label(this, SWT.NONE);
		lblModuleId.setText("Module Id:");
		
		spinner_module = new Spinner(this, SWT.BORDER);
		spinner_module.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		Label lblProgress = new Label(this, SWT.NONE);
		lblProgress.setText("Progress:");
		
		spinner_progress = new Spinner(this, SWT.BORDER);
		spinner_progress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		
		Button btnGo = new Button(this, SWT.NONE);
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
					params.put( Parameters.ID.toString(), "1");
					params.put( Parameters.TOKEN.toString(), "12");
					params.put(Parameters.MODULE_ID.toString(), String.valueOf(moduleId));
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
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private class WebClient extends AbstractHttpRequest<Requests>{

		private String context;
		
		public WebClient( String context ) {
			super();
			this.context = context;
		}

		public void sendGet( Requests request, Map<String, String> parameters ) throws Exception {
			super.sendGet(request.getPath(), parameters);
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
