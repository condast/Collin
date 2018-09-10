package org.collin.ui.menu;

import org.condast.commons.authentication.ui.menu.AbstractMenuButton;
import org.condast.commons.authentication.user.ILoginUser;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.ui.help.AbstractHelpDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuButton extends AbstractMenuButton<ILoginUser> {
	private static final long serialVersionUID = 1L;
	
	public static final String S_HELP_URL = "/help/help.html";
	public static final String S_ABOUT_URL = "/help/about.html";

	private enum Requests{
		USERDATA;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(name());
		}
	}

	private enum Attributes{
		USERID,
		TOKEN;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(name());
		}
	}

	private Button button;
	
	private int selection;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public MenuButton(Composite parent, int style) {
		super(parent, style);
		this.button = this;
		this.selection = 0;
	}

	public int getSelected() {
		return selection;
	}

	@Override
	protected void onSetupMenu(Menu menu) {
		MenuItem mntmRegisterVessel = new MenuItem(menu, SWT.NONE);
		mntmRegisterVessel.setText("Register Vessel");
		mntmRegisterVessel.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
			}
		});

		MenuItem mntmAccountInfo = new MenuItem(menu, SWT.NONE);
		mntmAccountInfo.setText("Account Info");
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmHelpItem = new MenuItem(menu, SWT.NONE);
		mntmHelpItem.setText("Help");
		mntmHelpItem.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				HelpDialog dialog = new HelpDialog(button.getShell(), "Help");
				if( dialog.open() == IDialogConstants.OK_ID ) {
					return;
				}
				super.widgetSelected(e);
			}
		});
		
		MenuItem mntmAbout = new MenuItem(menu, SWT.NONE);
		mntmAbout.setText("About");
		mntmAbout.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				AboutDialog dialog = new AboutDialog(button.getShell(), "About Aquabots");
				if( dialog.open() == IDialogConstants.OK_ID ) {
					return;
				}
				super.widgetSelected(e);
			}
		});		
	}

	@Override
	protected void onPrepareLogout(ILoginUser user) {
		// TODO Auto-generated method stub
		
	}

	private class HelpDialog extends AbstractHelpDialog{
		private static final long serialVersionUID = 1L;

		protected HelpDialog(Shell parentShell, String title) {
			super(parentShell, title, false);
		}

		@Override
		protected String createForm(boolean fromURL) {
			return super.scanForm(  this.getClass().getResourceAsStream( S_HELP_URL ));
		}		
	}
	
	private class AboutDialog extends AbstractHelpDialog{
		private static final long serialVersionUID = 1L;

		protected AboutDialog(Shell parentShell, String title) {
			super(parentShell, title, false);
		}

		@Override
		protected String createForm(boolean fromURL) {
			return super.scanForm(  this.getClass().getResourceAsStream( S_ABOUT_URL ));
		}
		
	}
}
