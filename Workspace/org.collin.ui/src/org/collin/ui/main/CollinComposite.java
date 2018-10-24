package org.collin.ui.main;

import java.io.IOException;
import java.util.EnumSet;
import java.util.logging.Logger;

import org.collin.core.xml.Sequence;
import org.collin.core.xml.SequenceNode;
import org.condast.commons.ui.player.PlayerImages;
import org.condast.commons.ui.widgets.AbstractButtonBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CollinComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_MOODLE_URL = "https://www.plusklas.nu";
	
	//private CollinWizard wizard;
	
	private Browser browser;
	private PlayerComposite<String> playerBar;
	private Sequence<?> builder;
	
	private int index;
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CollinComposite(Composite parent, int style) {
		super(parent, style);
		setLayout( new GridLayout(1, false));
		browser = new Browser( this, SWT.NONE );
		//browser.setUrl( "/ponte");
		GridData gd_browser = new GridData( SWT.FILL, SWT.FILL, true, true);
		browser.setLayoutData( gd_browser);
		playerBar = new PlayerComposite<>(this, SWT.BORDER);
		playerBar.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false));
		playerBar.addMouseListener( new MouseAdapter() {
			private static final long serialVersionUID = 1L;

			
			@Override
			public void mouseDown(MouseEvent e) {
				logger.info("Index: " + index++);			
			}			
		});
		//wizard = new CollinWizard();
		//wizard.createPageControls(this);
		//browser.setBackground(  Display.getDefault().getSystemColor( SWT.COLOR_BLUE));
		requestLayout();
		this.index = 0;
	}

	public void setInput( Class<?> clss) {
		try {
			builder = new Sequence<String>( clss);
			SequenceNode se = builder.start();
			String uri = "/ponte?path=" + se.getUri();
			browser.setUrl( uri );		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private class PlayerComposite<I extends Object> extends AbstractButtonBar<PlayerImages.Images, I> {
		private static final long serialVersionUID = 1L;

		public PlayerComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		protected EnumSet<PlayerImages.Images> setupButtonBar() {
			return EnumSet.of(PlayerImages.Images.START, 
					PlayerImages.Images.STOP, 
					PlayerImages.Images.NEXT,
					PlayerImages.Images.RESET);
		}

		@Override
		protected Control createButton(PlayerImages.Images type) {
			Button button = new Button( this, SWT.FLAT );
			switch( type ){
			case STOP:
				break;
			default:
				break;
			}
			button.setData(type);
			button.addSelectionListener( new SelectionAdapter() {
				private static final long serialVersionUID = 1L;

				@Override
				public void widgetSelected(SelectionEvent e) {
					try{
						Button button = (Button) e.getSource();
						PlayerImages.Images image = (PlayerImages.Images) button.getData();
						Button clear = (Button) getButton( PlayerImages.Images.RESET);
						switch( image ){
						case START:
							getButton( PlayerImages.Images.STOP).setEnabled(true);
							//button.setEnabled(false);
							clear.setEnabled( false );//!environment.isRunning() || environment.isPaused());
							getDisplay().asyncExec( new Runnable(){

								@Override
								public void run() {
									SequenceNode se = builder.next();
									String uri = "/ponte?path=" + se.getUri();
									browser.setUrl(uri);
									requestLayout();
								}		
							});
							break;
						case STOP:
							getButton( PlayerImages.Images.START).setEnabled(true);
							button.setEnabled(false);
							clear = (Button) getButton( PlayerImages.Images.RESET);
							clear.setEnabled( true );//!environment.isRunning() || environment.isPaused());
							break;
						case NEXT:
							clear = (Button) getButton( PlayerImages.Images.RESET);
							clear.setEnabled( true );//!environment.isRunning() || environment.isPaused());
							break;
						case RESET:
						default:
							break;
						}

					}
					catch( Exception ex ){
						ex.printStackTrace();
					}
				}		
			});
			button.setImage( PlayerImages.getInstance().getImage(type));
			return button;
		}
	}	
}
