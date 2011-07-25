package mb2controller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.buglabs.application.ServiceTrackerHelper;
import com.buglabs.device.IButtonEventProvider;


public class Activator implements BundleActivator {
	private ServiceTracker serviceTracker;
	private static final String [] services = {		
		IButtonEventProvider.class.getName()
	};
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		mb2ControlApp app = new mb2ControlApp();
		serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, app);

	}

    /*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	
		//Will cause the ManagedRunnable.shutdown() to be called.
		serviceTracker.close();
	}
}