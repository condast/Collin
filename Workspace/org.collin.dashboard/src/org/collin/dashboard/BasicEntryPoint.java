package org.collin.dashboard;

import org.collin.dashboard.ds.Dispatcher;
import org.collin.dashboard.xml.XMLFactoryBuilder;
import org.collin.ui.authentication.AuthenticationComposite;
import org.condast.commons.xml.BuildEvent;
import org.condast.commons.xml.IBuildListener;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;


public class BasicEntryPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	private IBuildListener<Widget> listener = new IBuildListener<Widget>(){

		@Override
		public void notifyTestEvent(BuildEvent<Widget> event) {
			try {
				if( event.getData() instanceof AuthenticationComposite ) {
					AuthenticationComposite composite=  (AuthenticationComposite) event.getData();
					composite.setInput( Activator.createLoginContext());
					composite.setLoginProvider( dispatcher);
				}
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
		dispatcher.setMainComposite(parent);
	}
}
