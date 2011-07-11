package bugmom;

import java.util.Map;

import org.osgi.framework.BundleContext;

import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICamera2ModuleControl;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;

import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;
/**
 * This class represents the running application when all service dependencies are fulfilled.
 * 
 * The run() method will be called with a map containing all the services specified in ServiceTrackerHelper.openServiceTracker().
 * The application will run in a separate thread than the caller of start() in the Activator.  See 
 * ManagedInlineRunnable for a thread-less application.
 * 
 * By default, the application will only be started when all service dependencies are fulfilled.  For 
 * finer grained service binding logic, see ServiceTrackerHelper.openServiceTracker(BundleContext context, String[] services, Filter filter, ServiceTrackerCustomizer customizer)
 */
public class BugMOMApplication implements ManagedRunnable {
	BundleContext context;
	
	BugMOMApplication(BundleContext c){
		context=c;
	}
	
	@Override
	public void run(Map<Object, Object> services) {			
		ICamera2Device icamera2device = (ICamera2Device) services.get(ICamera2Device.class.getName());			
		ICamera2ModuleControl icamera2modulecontrol = (ICamera2ModuleControl) services.get(ICamera2ModuleControl.class.getName());			
		ICameraModuleControl icameramodulecontrol = (ICameraModuleControl) services.get(ICameraModuleControl.class.getName());
		// TODO Use services here.
		cam pic=new cam(context);
		pic.run(services);
	}

	@Override
	public void shutdown() {
		// TODO Add shutdown code here if necessary.
	}
}