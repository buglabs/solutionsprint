package bugmom;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.imageio.stream.*;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;



import com.buglabs.bug.module.camera.pub.ICamera2Device;
import com.buglabs.bug.module.camera.pub.ICamera2ModuleControl;
import com.buglabs.bug.module.camera.pub.ICameraModuleControl;
import com.buglabs.bug.module.lcd.pub.IModuleDisplay;
import com.buglabs.device.IButtonEventProvider;
import com.buglabs.services.ws.IWSResponse;
import com.buglabs.services.ws.PublicWSDefinition;
import com.buglabs.services.ws.PublicWSProvider;
import com.buglabs.services.ws.PublicWSProvider2;
import com.buglabs.services.ws.WSResponse;


/**
*
* @author kgilmer
*
*/
public class cam implements PublicWSProvider2 {
private static final String JPEG_MIME_TYPE = "image/jpg";
private IModuleDisplay display;
private ICamera2Device camera;
private ICameraModuleControl cameraLED;
private IButtonEventProvider buttonEventProvider;
private final static int imageWidth  = 320;
private final static int imageHeight = 280;
private final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
private final int [] buf = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

private ServiceRegistration moduleControlReg;

private String pictureServiceName = "mom_ping";


private int previewsGrabbed = 0;
private int fullsGrabbed = 0;

int getPreviewsGrabbed() {
return previewsGrabbed;
}

int getFullsGrabbed() {
return fullsGrabbed;
}
cam(BundleContext c){
	c.registerService(PublicWSProvider.class.getName(), this, null);
}

public final void run(final Map<Object, Object> services) {
	camera 				= (ICamera2Device) 			services.get(ICamera2Device.class.getCanonicalName());
	buttonEventProvider = (IButtonEventProvider) 	services.get(IButtonEventProvider.class.getCanonicalName());
	display 			= (IModuleDisplay) 			services.get(IModuleDisplay.class.getCanonicalName());
	cameraLED 			= (ICameraModuleControl) 	services.get(ICameraModuleControl.class.getCanonicalName());
	
	// decided to both open and start the camera here to speed up shutter times
	System.out.println(camera.cameraOpenDefault());
	// as long as cameraStart is open other applications cannot use it
	System.out.println(camera.cameraStart());
	
}


public final void shutdown() {
	// shut down the camera gracefully
	try { cameraLED.setLEDFlash(false); } catch (IOException e) { e.printStackTrace(); }
	if (camera.isCameraStarted()) 	{ camera.cameraStop(); }
	if (camera.isCameraOpen()) 		{ camera.cameraClose(); }
}


public IWSResponse execute(int operation, String input) {
if (operation == PublicWSProvider2.GET) {

// we'll leave the camera running
return new WSResponse(new ByteArrayInputStream(camera.grabFull()), JPEG_MIME_TYPE);
}
return null;
}

@Override
public PublicWSDefinition discover(int operation) {
	// TODO Auto-generated method stub
	if (operation == PublicWSProvider2.GET) {
		return new PublicWSDefinition() {

		public List getParameters() {
		return null;
		}

		public String getReturnType() {
		return JPEG_MIME_TYPE;
		}
		};
		}
	return null;
}

@Override
public String getPublicName() {
	return pictureServiceName;
}

@Override
public String getDescription() {
	return "This service is used for motherbug functionality";
}

@Override
public void setPublicName(String name) {
	pictureServiceName = name;	
}





}

