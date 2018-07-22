package org.collin.ui.main;

import java.awt.Color;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class CollinComposite extends Composite {
	private static final long serialVersionUID = 1L;

	private CollinWizard wizard;
	
	private Browser browser;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CollinComposite(Composite parent, int style) {
		super(parent, style);
		setLayout( new FillLayout());
		wizard = new CollinWizard();
		wizard.createPageControls(this);
		setBackground(  Display.getDefault().getSystemColor( SWT.COLOR_BLUE));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
