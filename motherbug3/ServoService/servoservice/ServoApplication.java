package servoservice;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.service.log.LogService;

import com.buglabs.application.ServiceTrackerHelper;
import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;
import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.vonhippel.pub.IVonHippelSerialPort;


public class ServoApplication implements ManagedRunnable, Servlet{
	
	private HttpService http_service;
	private ConfigurationAdmin config_admin;
	private IVonHippelSerialPort vh_serial;
	private ICameraModuleControl camera_control;
	private ICamera2Device camera;
	private BundleContext context;
	private ServoServlet servlet; 
	
	public ServoApplication(BundleContext c){
		context=c;
	}
	
	public void run(Map<Object, Object> services) {
		
		http_service = (HttpService) services.get(HttpService.class.getName());
		camera = (ICamera2Device) services.get(ICamera2Device.class.getName());
		camera_control = (ICameraModuleControl) services.get(ICameraModuleControl.class.getName());
		vh_serial = (IVonHippelSerialPort) services.get(IVonHippelSerialPort.class.getName());
		
		Activator.getLogger().log(LogService.LOG_INFO, this.getClass().getName() + " has started!");
		
		servlet = new ServoServlet(context);
		CaptureHttpContext capcon =  new CaptureHttpContext(context);
		
		servlet.setVonHippelSerialPort(vh_serial);
		servlet.setCamera(camera);
		servlet.setCameraModuleControl(camera_control);
		servlet.setBundleContext(context);
		servlet.setHttpService(http_service);
		try {
			//http_service.registerServlet(ServoServlet.ALIAS, servlet, null, null);
			http_service.registerResources(CaptureHttpContext.CAPTURE_ALIAS, CaptureHttpContext.NAME,capcon);
			
		} catch (NamespaceException e) {
			e.printStackTrace();

		}
		
		context.registerService(ServoServlet.class.getName(), servlet, null);
    	
		
	}

	public void shutdown() {
		//if (http_service != null) {
		//	http_service.unregister(ServoServlet.ALIAS);
		//}
		if (http_service != null) {
			http_service.unregister(CaptureHttpContext.CAPTURE_ALIAS);
		}
		
	}

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
