package org.collin.dashboard;

import org.collin.dashboard.ds.Dispatcher;
import org.collin.ui.main.CollinComposite;
import org.collin.ui.main.CollinViewerComposite;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
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

	private enum Tabs{
		MAIN,
		TEST,
		CO_L_L_I_N,
		DEBUG;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	private CollinComposite ccomposite;
	private CollinViewerComposite vccomposite;
	
	private IBuildListener<Widget> listener = new IBuildListener<Widget>(){

		@Override
		public void notifyTestEvent(BuildEvent<Widget> event) {
			try {
				if( event.getData() instanceof CollinComposite ) {
					ccomposite=  (CollinComposite) event.getData();
					ccomposite.setInput( Activator.class);
					//ccomposite.setLoginProvider( dispatcher);
				}else if( event.getData() instanceof CollinViewerComposite ) {
					vccomposite=  (CollinViewerComposite) event.getData();
					vccomposite.setInput( Activator.class);
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
			if( StringUtils.isEmpty(item.getText()))
				return;
			switch( Tabs.valueOf( StringStyler.styleToEnum( item.getText()))){
			case MAIN:
				ccomposite.setInput( Activator.class);
				break;
			case CO_L_L_I_N:
				vccomposite.setInput( Activator.class);
				break;
			default:
				break;
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
