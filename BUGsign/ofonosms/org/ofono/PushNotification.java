package org.ofono;
import org.freedesktop.dbus.DBusInterface;
public interface PushNotification extends DBusInterface
{

  public void RegisterAgent(DBusInterface a);
  public void UnregisterAgent(DBusInterface a);

}
