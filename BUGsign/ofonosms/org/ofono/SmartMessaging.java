package org.ofono;
import java.util.List;
import org.freedesktop.dbus.DBusInterface;
public interface SmartMessaging extends DBusInterface
{

  public void RegisterAgent(DBusInterface a);
  public void UnregisterAgent(DBusInterface a);
  public DBusInterface SendBusinessCard(String a, List<Byte> b);
  public DBusInterface SendAppointment(String a, List<Byte> b);

}
