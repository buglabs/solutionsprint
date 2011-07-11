package servoservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import com.buglabs.application.ServiceTrackerHelper;
import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.vonhippel.pub.IVonHippelSerialPort;
import com.buglabs.util.LogServiceUtil;

public class Activator implements BundleActivator {
    //private BundleContext context;
    private static LogService logger = null;
	private ServiceTracker serviceTracker;
	private ServiceRegistration ServoApplicationRegistration;
	private static final String [] services = {					
		ICamera2Device.class.getName(),
		ICameraModuleControl.class.getName(),
		IVonHippelSerialPort.class.getName(),
		HttpService.class.getName()
	};	


	
    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        //this.context = context; // TODO: is this even useful?
    	ServoApplication ServoApp = new ServoApplication();
		ServoServlet web_service = new ServoServlet();

		ServoApplicationRegistration = context.registerService(ServoApplication.class.getName(), ServoApp, null);
		context.registerService( ServoServlet.class.getName(), web_service, null );
		serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, ServoApp);
		ServiceTrackerHelper.openServiceTracker(context, services, web_service);
        System.out.println("how about here");
        logger = LogServiceUtil.getLogService(context);
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
    	//Will cause the ManagedRunnable.shutdown() to be called.
    	ServoApplicationRegistration.unregister();
		serviceTracker.close();
    }
    
	/**
	 * @return an instance of the LogService.
	 */
	public static LogService getLogger() {
		return logger;
	}
}