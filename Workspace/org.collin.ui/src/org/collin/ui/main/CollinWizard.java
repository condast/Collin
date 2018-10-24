package org.collin.ui.main;

import java.io.InputStream;
import org.condast.commons.ui.def.IWizardBuilder;
import org.condast.commons.ui.wizard.IHeadlessWizardContainer.ContainerTypes;
import org.condast.commons.ui.wizard.xml.AbstractXmlBrowserFlowWizard;
import org.condast.commons.ui.wizard.xml.IXmlFlowWizard;
import org.condast.commons.ui.wizard.xml.IndexStore;
import org.condast.commons.ui.wizard.xml.XMLWizardBuilder;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;

public class CollinWizard extends AbstractXmlBrowserFlowWizard<String> implements IXmlFlowWizard<String> {

	public static String S_DEFAULT_FOLDER = "/design/";
	public static String S_DEFAULT_DESIGN_FILE = S_DEFAULT_FOLDER + "wizard.xml";

	public enum Pages{
		BAR,
		INTRO,
		SEARCH,
		SHOW,
		EDIT;

		@Override
		public String toString() {
			String str = name().toLowerCase(); 
			str = str.replaceAll("_", "-");
			str += ".html";
			return str;
		}

		public static String[] getItems(){
			String[] items = new String[ values().length ];
			for( int i=0; i< items.length; i++ ){
				items[i] = Pages.values()[i].name();
			}
			return items;
		}
	}

	public CollinWizard() {
		super( CollinWizard.class.getResourceAsStream(S_DEFAULT_DESIGN_FILE ), CollinWizard.class, null );
	}

	@Override
	protected IWizardBuilder<String> createBuilder(InputStream in) {
		IWizardBuilder<String> builder = new XMLWizardBuilder<String>( in );
		return builder;
	}

	@Override
	protected IWizardPage onAddPage(String pageName, String description, String message) {
		IWizardPage wizard = 
		new FlowWizardPage( pageName, description, message ){
			private static final long serialVersionUID = 1L;

			
			@Override
			protected void onControlCreated(int index, Composite composite) {
				Pages page = Pages.values()[ index ];
				//try {
				//} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				//}
				switch( page ){
				case SEARCH:
					break;
				case SHOW:
						//ecomp.addDataEventListener(dlistener);
					//ecomp.setInput( (IModelLeaf<IDescriptor>) model );
					break;
				case EDIT:
						//acomp.setInput( (IModelLeaf<IDescriptor>) model );
					break;

				default:
					break;
				}
			}

			@Override
			public void onCreateIcon(Composite iconbar) {
				// TODO Auto-generated method stub
				
			}	
		};
		return wizard;
	}

	@Override
	public void setPreviousNext(boolean previousNext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IndexStore addPage(String pageName, String description, String message, ContainerTypes type,
			boolean onCancel, boolean onFinish) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexStore getCurrent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTitleStyle(String titleStyle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setButtonbarStyle(String buttonbarStyle) {
		// TODO Auto-generated method stub
		
	}
}
