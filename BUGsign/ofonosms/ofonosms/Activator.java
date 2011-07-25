// java -Djava.library.path=/usr/lib/jni/ -cp /usr/share/java/bundle/com.buglabs.bug.jni.libmatthew.jar:/usr/share/java/bundle/org.freedesktop.dbus.jar org.freedesktop.dbus.bin.CreateInterface --create-files --system org.ofono /E0334BDD21662DE0

package ofonosms;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

import org.ofono.MessageManager;



public class Activator implements BundleActivator {
	private DBusConnection bus;
	private DBusSigHandler incomingMessageHandler;
	private DBusSigHandler immediateMessageHandler;
	public void start(BundleContext context) throws Exception {
		bus = DBusConnection.getConnection(DBusConnection.SYSTEM);
		incomingMessageHandler = new IncomingMessageHandler();
		immediateMessageHandler = new ImmediateMessageHandler();
		bus.addSigHandler(MessageManager.IncomingMessage.class, new IncomingMessageHandler());
		bus.addSigHandler(MessageManager.ImmediateMessage.class, new ImmediateMessageHandler());
	}

	public void stop(BundleContext context) throws Exception {
		bus.removeSigHandler(MessageManager.IncomingMessage.class, incomingMessageHandler);
		bus.removeSigHandler(MessageManager.ImmediateMessage.class, immediateMessageHandler);
	}
}