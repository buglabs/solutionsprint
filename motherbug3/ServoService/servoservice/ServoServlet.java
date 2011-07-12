package servoservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
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
	
	ServoServlet (BundleContext c){
		c.registerService(PublicWSProvider.class.getName(), this, null);
	}
	
	/**
	 * handle GET request 
	 * 
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//see who's doing the request.  sucks with NAT
		
		System.out.println(req.getRemoteHost());
		System.out.println(req.getRemoteAddr());

		String param = req.getQueryString();
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();

		// if no param, make a list of configs
		if (param == null || param.indexOf("degree")<0) {
			writeError(writer);
			return;
		}
		param = param.substring("degree".length()+1);
		/*if (param.equals("\"\""))
			param = "0"*/
		System.out.println(param);
		int degrees = Integer.parseInt(param);
		if (degrees < 0 && degrees > 180) {
			writeError(writer);
			return;
		}

		int integerOut = (int) map(degrees, 0, 180, 0, 9);
		char c = Integer.toString(integerOut).charAt(0);
		
		//figure out which vm we're running on
		//and figure out whether to try using VH serial APIS or just basic FileIO to control the servo
		//the codeblock below just writes a value out through the VonHippel to control a servo
		//This can be removed for other contexts
		System.out.println(System.getProperty("java.vm.name"));
		System.out.println(System.getProperty("java.version"));
		ServiceReference sr = bundleContext.getServiceReference(IVonHippelSerialPort.class.getName());
		String slotNum = (String) sr.getProperty("Slot");
		System.out.println("Slot: "+slotNum);
		if (System.getProperty("java.vm.name").indexOf("CVM")>-1){
			OutputStream os = vhsp.getSerialOutputStream();
			os.write(c);
		}
		else{
			sr = bundleContext.getServiceReference(IVonHippelSerialPort.class.getName());
			slotNum = (String) sr.getProperty("Slot");
			System.out.println(slotNum);
			OutputStream os = new FileOutputStream(new File("/dev/ttymxc"+Integer.parseInt(slotNum)));
			os.write(c);
		}
	

		//read image from cam and write it to a file for imagemagick to convert
		byte[] jpeg = cam.grabFull();
		FileOutputStream fos = new FileOutputStream(new
		File("/tmp/image.jpg"));
		fos.write(jpeg, 0, jpeg.length);
		System.out.println("Wrote image");
		
		//Get a file that's findable from our context
		File resizedFile = bundleContext.getDataFile("image-new.jpg");
		//System.out.println("Resizing to "+ resizedFile.getAbsolutePath());
		
		//shell out to imagemagick to do conversion, result is stored in file 
		//received from getDateFile()
		Process p = Runtime.getRuntime().exec(
				new String[] { "convert", "-resize", "400x300", "-rotate", "270",
						"/tmp/image.jpg", resizedFile.getAbsolutePath() });
		
		int t;
		try {
			t = p.waitFor();
			System.out.println(t);
		
		//copy the file to /home/root in case I want them later... like when we have an Open House at BUG
		Date d = new Date();
		p = Runtime.getRuntime().exec(new String[]{"cp", resizedFile.getAbsolutePath(),
				"/home/root/"+Long.toString(d.getTime())+".jpeg"});
		p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ClassLoader cl = getClass().getClassLoader();
	    URL url = cl.getResource("/resources/index.html");	    
	    BufferedReader in;
		try {
			in = new BufferedReader(
			        new InputStreamReader(
			        url.openStream()));
	      String inputLine;
	      while ((inputLine = in.readLine()) != null)
	          writer.write(inputLine);
	      in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeError(PrintWriter writer) {
		System.out.println("writing error 1");
		ClassLoader cl = getClass().getClassLoader();
	    URL url = cl.getResource("/resources/error1.html");	    
	    BufferedReader in;
		try {
			in = new BufferedReader(
			        new InputStreamReader(
			        url.openStream()));
	      String inputLine;
	      while ((inputLine = in.readLine()) != null)
	          writer.write(inputLine);
	      in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	      in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IWSResponse execute(int operation, String input, final Map get, Map post)  {
		if (operation == PublicWSProvider2.GET) {
			return new IWSResponse() {
				public Object getContent() {
  					StringWriter writer = new StringWriter(); 

  					// if no param, make a list of configs
  					if (get == null || get.get("degree") == null) {
  						writeError2(writer);
  						return writer.toString();
  					}
  					
  					String param = get.get("degree") +"";
  					/*if (param.equals("\"\""))
  						param = "0"*/
  					System.out.println(param);
  					int degrees = Integer.parseInt(param);
  					if (degrees < 0 && degrees > 180) {
  						writeError2(writer);
  						return writer.toString();
  					}

  					int integerOut = (int) map(degrees, 0, 180, 0, 9);
  					char c = Integer.toString(integerOut).charAt(0);
  					
  					//figure out which vm we're running on
  					//and figure out whether to try using VH serial APIS or just basic FileIO to control the servo
  					//the codeblock below just writes a value out through the VonHippel to control a servo
  					//This can be removed for other contexts
  					System.out.println(System.getProperty("java.vm.name"));
  					System.out.println(System.getProperty("java.version"));
  					ServiceReference sr = bundleContext.getServiceReference(IVonHippelSerialPort.class.getName());
  					String slotNum = (String) sr.getProperty("Slot");
  					System.out.println("Slot: "+slotNum);
  					if (System.getProperty("java.vm.name").indexOf("CVM")>-1){
  						OutputStream os;
						try {
							os = vhsp.getSerialOutputStream();
							os.write(c);
						} catch (IOException e) {
							e.printStackTrace();
						}
  						
  					}
  					else{
  						sr = bundleContext.getServiceReference(IVonHippelSerialPort.class.getName());
  						slotNum = (String) sr.getProperty("Slot");
  						System.out.println(slotNum);
  						OutputStream os;
						try {
							os = new FileOutputStream(new File("/dev/ttymxc"+Integer.parseInt(slotNum)));
							os.write(c);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
  						
  					}


  					//read image from cam and write it to a file for imagemagick to convert
  					byte[] jpeg = cam.grabFull();
  					FileOutputStream fos;
					try {
						fos = new FileOutputStream(new
						File("/tmp/image.jpg"));
	  					fos.write(jpeg, 0, jpeg.length);						
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
  					//System.out.println("Wrote image");
  					
  					//Get a file that's findable from our context
  					File resizedFile = bundleContext.getDataFile("image-new.jpg");
  					//System.out.println("Resizing to "+ resizedFile.getAbsolutePath());
  					
  					//shell out to imagemagick to do conversion, result is stored in file 
  					//received from getDateFile()
  					Process p;
					try {
						p = Runtime.getRuntime().exec(
								new String[] { "convert", "-resize", "400x300", "-rotate", "270",
										"/tmp/image.jpg", resizedFile.getAbsolutePath() });
						int t;
						t = p.waitFor();
						System.out.println(t);
	 					Date d = new Date();
	  					p = Runtime.getRuntime().exec(new String[]{"cp", resizedFile.getAbsolutePath(), "/home/root/"+Long.toString(d.getTime())+".jpeg"});
	  					p.waitFor();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
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
				      while ((inputLine = in.readLine()) != null)
				          writer.write(inputLine);
				      in.close();
					} catch (IOException e) {
						e.printStackTrace();
					} 					
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
		System.out.println("panda");
		// TODO Auto-generated method stub
		
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
