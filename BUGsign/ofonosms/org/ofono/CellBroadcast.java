package org.ofono;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.UInt16;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
public interface CellBroadcast extends DBusInterface
{
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
   public static class IncomingBroadcast extends DBusSignal
   {
      public final String a;
      public final UInt16 b;
      public IncomingBroadcast(String path, String a, UInt16 b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }
   public static class EmergencyBroadcast extends DBusSignal
   {
      public final String a;
      public final Map<String,Variant> b;
      public EmergencyBroadcast(String path, String a, Map<String,Variant> b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }

  public Map<String,Variant> GetProperties();
  public void SetProperty(String a, Variant b);

}
