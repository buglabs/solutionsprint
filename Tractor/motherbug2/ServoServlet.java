package motherbug2;


import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;


import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;

import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.gps.pub.IPositionProvider;
import com.buglabs.bug.module.gps.pub.LatLon;
import com.buglabs.bug.module.vonhippel.pub.IVonHippelSerialPort;
import com.buglabs.services.ws.IWSResponse;
import com.buglabs.services.ws.PublicWSDefinition;
import com.buglabs.services.ws.PublicWSProvider;
import com.buglabs.services.ws.PublicWSProvider2;
import com.buglabs.services.ws.PublicWSProviderWithParams;
import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;


/**
 * Servlet for dealing with Maps. Find at /666
 * 
 * @author brian
 * 
 * Update: AK 2009-12-16 quick update-- change to PublicWSProviderWithParams
 */
public class ServoServlet implements PublicWSProviderWithParams, ManagedRunnable{

	public static final String ALIAS = "Servo";
	private static final long serialVersionUID = 1L;
	private IVonHippelSerialPort vhsp;
	private ICameraModuleControl cmc;
	private ICamera2Device cam;
	private BundleContext bundleContext;
	private HttpService http_service;
	public static final String CAPTURE_ALIAS = "/servoresource";

	private SerialPort port;
	private CommPortIdentifier comm;
	private InputStream is;
	private OutputStream os;
	private PrintWriter out;
	private boolean shutdown = false;
	private double lat =40.725184,lon = -73.996942;
	private String map = "<iframe width=\"425\" height=\"350\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" " +
	"src=\"http://maps.google.com/maps?client=ubuntu&amp;channel=cs&amp;q=40.725184,-73.996942&amp;ie=UTF8&amp;" +
	"hq=&amp;hnear=0x89c2598fa621eaef:0x29339a172316e930,%2B40%C2%B0+43'+30.66%22,+-73%C2%B0+59'+48.99%22&amp;gl=us&" +
	"amp;z=14&amp;ll=" + lat+","+lon+"&amp;output=embed\"></iframe><br /><small><a href=\"http://maps.google.com/maps?client=" +
	"ubuntu&amp;channel=cs&amp;q=" + lat+","+lon+"&amp;ie=UTF8&amp;hq=&amp;hnear=0x89c2598fa621eaef:0x29339a172316e930,%" +
	"2B40%C2%B0+43'+30.66%22,+-73%C2%B0+59'+48.99%22&amp;gl=us&amp;z=14&amp;ll=" + lat+","+lon+"&amp;source=embed\" style=\"" +
	"color:#0000FF;text-align:left\">View Larger Map</a></small>\"";
	private IPositionProvider gps;
	public ServoServlet (BundleContext c){
		c.registerService(PublicWSProvider.class.getName(), this, null);
	}

	/**
	 * handle GET request 
	 * 
	 */
	
	
	private long map(long x, long in_min, long in_max, long out_min,
			long out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	public void setVonHippelSerialPort(IVonHippelSerialPort vhsp) {
		this.vhsp = vhsp;
	}

	/**
	 * @param cam
	 *            the cam to set
	 */
	public void setCamera(ICamera2Device cam) {
		this.cam = cam;
	}

	/**
	 * @return the cam
	 */
	public ICamera2Device getCamera() {
		return cam;
	}

	/**
	 * @param cmc
	 *            the cmc to set
	 */
	public void getCameraModuleControl(ICameraModuleControl cmc) {
		this.cmc = cmc;
	}

	/**
	 * @return the cmc
	 */
	public ICameraModuleControl getCameraModuleControl() {
		return cmc;
	}

	/**
	 * @return the vhsp
	 */
	public IVonHippelSerialPort getVonHippelSerialPort() {
		return vhsp;
	}

	/**
	 * @param cmc
	 *            the cmc to set
	 */
	public void setCameraModuleControl(ICameraModuleControl cmc) {
		this.cmc = cmc;
	}

	/**
	 * @param bundleContext the bundleContext to set
	 */
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setHttpService(HttpService http_service) {
		this.http_service = http_service;
	}

	/**
	 * @return the http_service
	 */
	public HttpService getHttpService() {
		return http_service;
	}	

	public void setPublicName(String name) {
	}
	public void set(String degree) {

		out.write(degree);
		out.flush();
	}
	public void setGPS(IPositionProvider gps) {
		// TODO Auto-generated method stub
		this.gps = gps;
	}

	public PublicWSDefinition discover(int operation) {
		if (operation == PublicWSProvider2.GET) {
			return new PublicWSDefinition() {

				public List getParameters() {
					return null;
				}

				public String getReturnType() {
					return "text/html";
				}
			};
		}
		return null;
	}

	private void writeError2(StringWriter writer) {
		System.out.println("writing error 2");
		ClassLoader cl = getClass().getClassLoader();
		URL url = cl.getResource("/resources/error2.html");	    
		BufferedReader in;
		try {
			in = new BufferedReader(
					new InputStreamReader(
							url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				writer.write(inputLine);
			writer.write(map);
			writer.write("</body>");
			writer.write("</html>");
			System.out.println(writer.toString());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*
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
*/


	public IWSResponse execute(int operation, String input, final Map get, Map post)  {
		if (operation == PublicWSProvider2.GET) {
			return new IWSResponse() {
				public Object getContent() {
					System.out.println("execute");
					StringWriter writer = new StringWriter(); 

					// if no param, make a list of configs
					if (get == null || get.get("degree") == null) {
						writeError2(writer);
						return writer.toString();
					}

					String param = get.get("degree") +"";

					System.out.println(param);
					int degrees;
					if(isInt(param))
						degrees = Integer.parseInt(param);
					else
						degrees = -1;

					if (degrees < 0 || degrees > 180) {
						writeError2(writer);
						return writer.toString();
					}

					int integerOut = (int) map(degrees, 0, 180, 0, 9);
					char c = Integer.toString(integerOut).charAt(0);

					set(degrees + "\n");

					LatLon locate = gps.getLatitudeLongitude();
					if(locate!=null){
						System.out.println(lat + "@"+lon);
						lat = locate.latitude;
						lon = locate.longitude;
					}

					System.out.println("writing index.html");
					ClassLoader cl = getClass().getClassLoader();
					URL url = cl.getResource("/resources/index.html");	    
					BufferedReader in;
					try {
						in = new BufferedReader(
								new InputStreamReader(
										url.openStream()));
						String inputLine;
						while ((inputLine = in.readLine()) != null){
							writer.write(inputLine);
						}
						writer.write(map);
						writer.write("</body>");
						writer.write("</html>");
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println(writer.toString());
					return writer.toString(); 
				}
				public int getErrorCode() {
					return 0;
				}

				public String getErrorMessage() {
					return null;
				}

				public String getMimeType() {
					return "text/html";
				}

				public boolean isError() {
					return false;
				}

			}; 
		}
		return null; 
	}

	public String getDescription() {
		return "Displays a servo control";
	}

	public String getPublicName() {
		return ALIAS;
	}

	public IWSResponse execute(int operation, String input) {
		return null;
	}

	public void run(Map<Object, Object> services) {

	}

	public void shutdown() {

	}
	public boolean isInt(String i)
	{
		try
		{
			Integer.parseInt(i);
			return true;
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
	}

	public void setBugduinoOutput(PrintWriter out) {
		// TODO Auto-generated method stub
		this.out = out;
	}



}