package org.collin.ui.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.Compass;
import org.collin.core.essence.ITetra;
import org.collin.core.xml.CollinBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;

public class CollinViewerComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_MOODLE_URL = "https://www.plusklas.nu";
	
	private int index;
	
	private CollinBuilder<?> builder;
	private TreeViewer viewer;
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CollinViewerComposite(Composite parent, int style) {
		super(parent, style);
		setLayout( new GridLayout(1, false));
		
		viewer = new TreeViewer(this, SWT.BORDER);
		Tree tree = viewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.index = 0;
        viewer.setContentProvider(new TreeContentProvider());
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);

        TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
        viewerColumn.getColumn().setWidth(300);
        viewerColumn.getColumn().setText("Names");
        viewerColumn.setLabelProvider(new ColumnLabelProvider());

	}

	public void setInput( Class<?> clss) {
		try {
			builder = new CollinBuilder<Object>( clss);
			Compass<?>[] compass = (Compass<?>[]) builder.build();
			viewer.setInput(compass);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public class TreeContentProvider implements ITreeContentProvider {
		private static final long serialVersionUID = 1L;

		@Override
	    public boolean hasChildren(Object element) {
	    	if( element instanceof Compass ) {
	    		return true;
	    	}else if( element instanceof ITetra ) {
	    		return true;
	    	}
	    	return (!( element instanceof ITetraNode ));
	    }

	    @Override
	    public Object getParent(Object element) {
	    	if( element instanceof Compass ) {
	    		Compass<?> compass = (Compass<?>) element;
	    		return compass.getParent();
	    	}else if( element instanceof ITetraNode ) {
	    		ITetraNode<?> node = (ITetraNode<?>) element;
	    		return node.getParent();
	    	}
	    	return null;
	    }

	    @Override
	    public Object[] getElements(Object inputElement) {
	        return ArrayContentProvider.getInstance().getElements(inputElement);
	    }

	    @Override
	    public Object[] getChildren(Object parentElement) {
	    	if( parentElement instanceof Compass ) {
	    		Compass<?> compass = (Compass<?>) parentElement;
	    		Collection<Object> children = new ArrayList<>();
	    		children.addAll( Arrays.asList( compass.getChildren() ));
	    		children.addAll( Arrays.asList( compass.getTetras() ));
	    		return children.toArray( new Object[ children.size()]);
	    	}else if( parentElement instanceof ITetra ) {
	    		ITetra<?> node = (ITetra<?>) parentElement;
	    		return node.getNodes();
	    	}
	    	return null;
	    }

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}
	}
}
