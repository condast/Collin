package org.collin.dashboard;

import org.collin.dashboard.xml.XMLFactoryBuilder;
import org.condast.commons.xml.BuildEvent;
import org.condast.commons.xml.IBuildListener;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;


public class BasicEntryPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;

	private IBuildListener<Widget> listener = new IBuildListener<Widget>(){

		@Override
		public void notifyTestEvent(BuildEvent<Widget> event) {
			try {
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		}
	};

	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new FillLayout());       
		XMLFactoryBuilder builder = new XMLFactoryBuilder( parent, this.getClass());
		builder.addListener(listener);
		builder.build();
		builder.removeListener(listener);
	}
}
