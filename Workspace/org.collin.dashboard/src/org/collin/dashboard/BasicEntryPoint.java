package org.collin.dashboard;

import org.collin.dashboard.ds.Dispatcher;
import org.collin.ui.main.CollinComposite;
import org.condast.commons.ui.xml.XMLFactoryBuilder;
import org.condast.commons.xml.BuildEvent;
import org.condast.commons.xml.IBuildListener;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;


public class BasicEntryPoint extends AbstractEntryPoint {
	private static final long serialVersionUID = 1L;

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	private CollinComposite ccomposite;
	private IBuildListener<Widget> listener = new IBuildListener<Widget>(){

		@Override
		public void notifyTestEvent(BuildEvent<Widget> event) {
			try {
				if( event.getData() instanceof CollinComposite ) {
					ccomposite=  (CollinComposite) event.getData();
					ccomposite.setInput( Activator.class);
					//ccomposite.setLoginProvider( dispatcher);
				}else if( event.getData() instanceof TabFolder ) {
					TabFolder folder =  ( TabFolder) event.getData();
					folder.addSelectionListener(slistener);
				}
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		}
	};
	
	private SelectionListener slistener = new SelectionAdapter() {
		private static final long serialVersionUID = 1L;

		@Override
		public void widgetSelected(SelectionEvent e) {
			TabItem item = (TabItem) e.item;
			if(item.getText().equals("Main")) {
				ccomposite.setInput( Activator.class);
			}
			super.widgetSelected(e);
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
