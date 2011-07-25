package org.ofono;
import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
public interface NetworkRegistration extends DBusInterface
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

  public Map<String,Variant> GetProperties();
  public void Register();
  public List<Struct1> GetOperators();
  public List<Struct2> Scan();

}
