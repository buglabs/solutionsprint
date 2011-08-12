package connect4;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import com.buglabs.application.ServiceTrackerHelper;
import com.buglabs.bug.ircbot.pub.IChannelMessageConsumer;
import com.buglabs.bug.ircbot.pub.IChannelMessageEvent;
import com.buglabs.bug.module.gps.pub.IPositionProvider;
import com.buglabs.bug.module.lcd.pub.IModuleDisplay;
import com.buglabs.util.LogServiceUtil;

import connect4.main.Main;


public class Activator implements BundleActivator {
	private static final String [] services = {		
		IModuleDisplay.class.getName()
	};	
	private ServiceTracker serviceTracker;

	public void start(BundleContext context) throws Exception {

		// TODO Auto-generated method stub
		final Main app = new Main();
		serviceTracker = ServiceTrackerHelper.openServiceTracker(context, services, app);

		context.registerService(IChannelMessageConsumer.class.getName(), new IChannelMessageConsumer() {
			public String onChannelMessage(IChannelMessageEvent e) {
				System.out.println(e.getMessage());

				if (e.getMessage().startsWith(e.getBotName() + ": ")) {


					String command = e.getMessage().substring((e.getBotName() + ": ").length());
					System.out.println(command);

					if (command.compareTo("new") == 0) {
						app.getUI().newGame();
						app.getUI().updateBoard();
						return "Connect dem 4s";
					}
					else if (Integer.parseInt(command) >= 0 && Integer.parseInt(command) <= 6) {
						return app.move(Integer.parseInt(command));
					}
					else
						return "hrm";
				}


				return null;
			}
		}, null);
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		serviceTracker.close();
	}
}