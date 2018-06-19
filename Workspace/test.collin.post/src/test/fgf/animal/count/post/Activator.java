package test.fgf.animal.count.post;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import test.fgf.animal.count.post.test.TestSuite;

public class Activator implements BundleActivator {

	public static final String S_BUNDLE_ID = "test.fgf.animal.count.post";
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		TestSuite suite = new TestSuite();
		suite.performTests();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
