package mb2image;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.buglabs.application.ServiceTrackerHelper;
import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.lcd.pub.IModuleDisplay;
import com.buglabs.device.ButtonEvent;
import com.buglabs.device.IButtonEventProvider;


public class Activator implements BundleActivator {
	private static final String [] services = {		

		IModuleDisplay.class.getName()

	};	
	private ServiceTracker serviceTracker;

	public void start(BundleContext context) throws Exception {
		System.out.println("mb2image: start");
		mb2image app = new mb2image();
		serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, app);
	}

	public void stop(BundleContext context) throws Exception {
		serviceTracker.close();
		System.out.println("bugCamera stop");
	}
}