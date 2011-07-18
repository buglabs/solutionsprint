package motherofaserver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;



import com.buglabs.application.ServiceTrackerHelper;

import motherbug2.ServoServlet;

public class Activator implements BundleActivator {
	private ServiceTracker serviceTracker;
	private static final String [] services = {		
		ServoServlet.class.getName(),
	};
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, new MotherOfAServerApp());
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		serviceTracker.close();
	}
}