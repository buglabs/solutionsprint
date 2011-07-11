package servoservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
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
		//IVonHippelSerialPort.class.getName(),
		HttpService.class.getName()
	};	

	private HttpService http_service;
	private ConfigurationAdmin config_admin;
	private IVonHippelSerialPort vh_serial;
	private ICameraModuleControl camera_control;
	private ICamera2Device camera;
	private BundleContext context;
	private ServoServlet servlet;

	
    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        //this.context = context; // TODO: is this even useful?
/*    	ServoApplication ServoApp = new ServoApplication();
		ServoServlet web_service = new ServoServlet();

		ServoApplicationRegistration = context.registerService(ServoApplication.class.getName(), ServoApp, null);
		context.registerService( ServoServlet.class.getName(), web_service, null );
		
		serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, ServoApp);
		ServiceTrackerHelper.openServiceTracker(context, services, web_service);*/
        //logger = LogServiceUtil.getLogService(context);
    	//serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, new ServoApplication());
    	/*ServoServlet web_service = new ServoServlet();
    	context.registerService( ServoServlet.class.getName(), web_service, null );
    	ServiceTrackerHelper.openServiceTracker(context, services, web_service);*/
    	//ServiceTrackerHelper.openServiceTracker(context, services, web_service);
    	
    	serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, new ServoApplication(context));
    	ServiceTrackerHelper.openServiceTracker(context, services, new ServoServlet(context));
/*		http_service = (HttpService) services.get(HttpService.class.getName());
		camera = (ICamera2Device) services.get(ICamera2Device.class.getName());
		camera_control = (ICameraModuleControl) services.get(ICameraModuleControl.class.getName());
		vh_serial = (IVonHippelSerialPort) services.get(IVonHippelSerialPort.class.getName());
		
		//Activator.getLogger().log(LogService.LOG_INFO, this.getClass().getName() + " has started!");
		
		servlet = new ServoServlet();
		CaptureHttpContext capcon =  new CaptureHttpContext(context);
		
		servlet.setVonHippelSerialPort(vh_serial);
		//servlet.setCamera(camera);
		servlet.setCameraModuleControl(camera_control);
		servlet.setBundleContext(context);
		servlet.setHttpService(http_service);
		try {
			//http_service.registerServlet(ServoServlet.ALIAS, servlet, null, null);
			http_service.registerResources(CaptureHttpContext.CAPTURE_ALIAS, CaptureHttpContext.NAME,capcon);
			
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
		
		ServiceTrackerHelper.openServiceTracker(context, services, web_service);*/	
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
    	//Will cause the ManagedRunnable.shutdown() to be called.
    	//ServoApplicationRegistration.unregister();
		serviceTracker.close();
    }
    
	/**
	 * @return an instance of the LogService.
	 */
	public static LogService getLogger() {
		return logger;
	}
}