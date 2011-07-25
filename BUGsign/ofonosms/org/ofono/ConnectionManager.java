package org.ofono;
import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
public interface ConnectionManager extends DBusInterface
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
   public static class ContextAdded extends DBusSignal
   {
      public final DBusInterface a;
      public final Map<String,Variant> b;
      public ContextAdded(String path, DBusInterface a, Map<String,Variant> b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }
   public static class ContextRemoved extends DBusSignal
   {
      public final DBusInterface a;
      public ContextRemoved(String path, DBusInterface a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }

  public Map<String,Variant> GetProperties();
  public void SetProperty(String a, Variant b);
  public DBusInterface AddContext(String a);
  public void RemoveContext(DBusInterface a);
  public void DeactivateAll();
  public List<Struct4> GetContexts();

}
