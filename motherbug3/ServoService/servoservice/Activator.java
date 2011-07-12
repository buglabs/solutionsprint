package servoservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import com.buglabs.application.ServiceTrackerHelper;
import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;

public class Activator implements BundleActivator {
    private static LogService logger = null;
	private ServiceTracker serviceTracker;

	private static final String [] services = {					
		ICamera2Device.class.getName(),
		ICameraModuleControl.class.getName(),
		HttpService.class.getName()
	};	

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {   	
    	
    	//ServoApplication servoApp = new ServoApplication(context);
		//context.registerService(servo_controller.class.getName(), servoApp, null);

    	serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, new ServoApplication(context));
    	//ServiceTrackerHelper.openServiceTracker(context, services, new ServoServlet(context));
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
    	//Will cause the ManagedRunnable.shutdown() to be called.
		serviceTracker.close();
    }
    
	/**
	 * @return an instance of the LogService.
	 */
	public static LogService getLogger() {
		return logger;
	}
}