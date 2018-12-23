package org.collin.dashboard.entry;

import org.collin.ui.test.TestComposite;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class MoodleTestPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;
			
	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new FillLayout());       
		TestComposite comp = new TestComposite( parent, SWT.BORDER );	
		comp.setInput(this.getClass());
	}
}