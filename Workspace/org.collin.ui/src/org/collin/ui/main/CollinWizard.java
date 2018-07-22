package org.collin.ui.main;

import java.io.InputStream;
import org.collin.core.xml.ModuleBuilder;
import org.condast.commons.ui.def.IWizardBuilder;
import org.condast.commons.ui.wizard.xml.AbstractXmlBrowserFlowWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;

public class CollinWizard extends AbstractXmlBrowserFlowWizard<String> {

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
		IWizardBuilder<String> builder = new ModuleBuilder<String>( in );
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
}
