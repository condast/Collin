package org.collin.dashboard.core;

import org.collin.core.essence.ITetra;
import org.collin.ui.main.CollinComposite;
import org.collin.ui.main.CollinViewerComposite;
import org.collin.ui.main.TetraViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class Dispatcher implements ISelectionChangedListener{

	private static Dispatcher dispatcher = new Dispatcher();

	private CollinComposite ccomposite;
	private CollinViewerComposite vccomposite;
	private TetraViewer tetraViewer;

	private ISelectionChangedListener listener = new ISelectionChangedListener() {
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = event.getStructuredSelection();
			if( selection.isEmpty() )
				return;
			Object obj = selection.getFirstElement();
			if( obj instanceof ITetra)
				tetraViewer.setInput(0, (ITetra<?>) obj);
			
		}
	};

	public static Dispatcher getInstance() {
		return dispatcher;
	}

	
	public CollinComposite getCcomposite() {
		return ccomposite;
	}


	public CollinViewerComposite getVccomposite() {
		return vccomposite;
	}


	public void setCcomposite(CollinComposite ccomposite) {
		this.ccomposite = ccomposite;
	}


	public void setVccomposite(CollinViewerComposite vccomposite) {
		if( this.vccomposite != null )
			this.vccomposite.removeSelectionListener(listener);
		this.vccomposite = vccomposite;
		this.vccomposite.setSelectionListener(listener);
	}


	public void setTetraViewer(TetraViewer tetraViewer) {
		this.tetraViewer = tetraViewer;
	}


	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	
}
