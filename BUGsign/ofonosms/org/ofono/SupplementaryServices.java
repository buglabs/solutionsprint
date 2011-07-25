package org.ofono;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
public interface SupplementaryServices extends DBusInterface
{
   public static class NotificationReceived extends DBusSignal
   {
      public final String a;
      public NotificationReceived(String path, String a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }
   public static class RequestReceived extends DBusSignal
   {
      public final String a;
      public RequestReceived(String path, String a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }
   public static class PropertyChanged extends DBusSignal
   {
      public final String a;
      public final Variant b;
      public PropertyChanged(String path, String a, Variant b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }

  public Pair<String, Variant> Initiate(String a);
  public String Respond(String a);
  public void Cancel();
  public Map<String,Variant> GetProperties();

}
