package motherbug2;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.framework.BundleContext;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;
import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.gps.pub.IPositionProvider;


public class MotherBugApp implements ManagedRunnable, servo_controller {

	private HttpService http_service;
	private ICameraModuleControl camera_control;
	private ICamera2Device camera;
	private ServoServlet servlet; 
	private BundleContext context;
	private IPositionProvider gps;
	private SerialPort port;
	private CommPortIdentifier comm;
	private InputStream is;
	private OutputStream os;
	private PrintWriter out;
	public boolean shutdown = false;

	public MotherBugApp(BundleContext c){
		context=c;
	}
	
	public void run(Map<Object, Object> services) {
//		Activator.getLogger().log(LogService.LOG_INFO, this.getClass().getName() + " has started!");
		http_service = (HttpService) services.get(HttpService.class.getName());
		camera = (ICamera2Device) services.get(ICamera2Device.class.getName());
		camera_control = (ICameraModuleControl) services.get(ICameraModuleControl.class.getName());
		servlet = new ServoServlet(context);
		gps = (IPositionProvider) services.get(IPositionProvider.class.getName());
		CaptureHttpContext capcon =  new CaptureHttpContext(context);

		servlet.setCamera(camera);
		servlet.setCameraModuleControl(camera_control);
		servlet.setBundleContext(context);
		servlet.setHttpService(http_service);
		servlet.setGPS(gps);
		
		
		
		while (!connectPort()){
			if (shutdown)
				return;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
		servlet.setBugduinoOutput(out);
		try {
			http_service.registerResources(CaptureHttpContext.CAPTURE_ALIAS, CaptureHttpContext.NAME,capcon);

		} catch (NamespaceException e) {
			e.printStackTrace();

		}

		context.registerService(ServoServlet.class.getName(), servlet, null);


	}

	public void shutdown() {
		if (http_service != null) {
			http_service.unregister(CaptureHttpContext.CAPTURE_ALIAS);
		}
		shutdown = true;
		out.write("Q\r\n");
		out.flush();
		try {
			is.close();
			os.close();
			port.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private boolean connectPort(){
		System.out.println("connecting");
		try {
			//TODO: checking to see which slot bugduino is in
			comm = CommPortIdentifier.getPortIdentifier("/dev/ttyBMI2");
		} catch (NoSuchPortException e1) {
			System.out.println("[Bugduino] Port cannot be found");
			e1.printStackTrace();
			return false;
		}
		try {
			port = (SerialPort)comm.open("bugduino", 1000);
			port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			is = port.getInputStream();
			os = port.getOutputStream();
			out = new PrintWriter(os, true);

		} catch (PortInUseException e2) {
			System.err.println("[Bugduino] port in use");
			return false;
		} catch (IOException e2) {
			System.err.println("[Bugduino] Can't get input stream");
			return false;
		} catch (Exception e){
			System.err.println("[Bugduino] Unknown error!");
			e.printStackTrace();
			return false;
		}
		return true;
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

	@Override
	public void set(String degree) {
		// TODO Auto-generated method stub
		
	}
}