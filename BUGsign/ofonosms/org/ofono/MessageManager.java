package org.ofono;
import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
public interface MessageManager extends DBusInterface
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
   public static class IncomingMessage extends DBusSignal
   {
      public final String a;
      public final Map<String,Variant> b;
      public IncomingMessage(String path, String a, Map<String,Variant> b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }
   public static class ImmediateMessage extends DBusSignal
   {
      public final String a;
      public final Map<String,Variant> b;
      public ImmediateMessage(String path, String a, Map<String,Variant> b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }
   public static class MessageAdded extends DBusSignal
   {
      public final DBusInterface a;
      public final Map<String,Variant> b;
      public MessageAdded(String path, DBusInterface a, Map<String,Variant> b) throws DBusException
      {
         super(path, a, b);
         this.a = a;
         this.b = b;
      }
   }
   public static class MessageRemoved extends DBusSignal
   {
      public final DBusInterface a;
      public MessageRemoved(String path, DBusInterface a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }

  public Map<String,Variant> GetProperties();
  public void SetProperty(String a, Variant b);
  public DBusInterface SendMessage(String a, String b);
  public List<Struct3> GetMessages();

}
