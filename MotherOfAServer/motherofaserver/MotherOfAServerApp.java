package motherofaserver;

import java.net.ServerSocket;
import java.util.Map;
import motherbug2.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;



import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;

public class MotherOfAServerApp implements ManagedRunnable {
	final int PORT_NUM = 23;
	ServerSocket sock;
	ServoServlet servlet;
	boolean running = true;
	private List<do_socket> clients;
	@Override
	public void run(Map<Object, Object> services) {			
		System.out.println("[mothersocketserver] Startup");
		clients = new ArrayList<do_socket>();
		servlet = (ServoServlet) services.get(ServoServlet.class.getName());
		try {
			sock = new ServerSocket(PORT_NUM);
		} catch (IOException e) {
			System.err.println("[mothersocketserver] Unable to open server socket, quitting");
			shutdown();
			return;
		}
		while (running){
			Socket client = null;
			try {
				client = sock.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				clients.add(new do_socket(client));
			}
		}
	}

	@Override
	public void shutdown() {
		System.out.println("[mothersocketserver] Shutdown");
		kill_all_clients();
		running = false;
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO Add shutdown code here if necessary.
	}

	String doCommand(String degree){

		servlet.set(degree);
		return "Set " + degree;
	}

	public String parsePkt(String command){
		if (command.contains("#")){
			//If we see a pound, assume the content up to that character is garbage
			//I'm not sure why, but i'm seeing garbage ending in a # when using 'telnet'
			//under ubuntu 10.11
			command = command.substring(command.lastIndexOf('#')+1);
		}
		System.out.println("[MothabugSocketServer] Incoming Message: "+command);
		if(isInt(command))
			return doCommand(Integer.parseInt(command)+"\n");
		else if (command.contains("random")){
			Random generator = new Random();
			return doCommand(generator.nextInt(179)+"\n");
		} else if (command.contains("q")){
			return "Goodbye!";
		} else {
			return "number please...\r\n";
		}
	}


	private class do_socket implements Runnable {
		Thread mythread;
		Socket client;
		BufferedReader in;
		PrintWriter out;

		public do_socket(Socket new_client){
			client = new_client;
			mythread = new Thread(this, "serial reader");
			mythread.start();		
		}
		@Override
		public void run() {
			String command = null;
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader( new InputStreamReader( client.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			out.println("\nEnter a degree between 0 and 180.");
			out.println("Or enter 'random' for a random degree.");
			out.println("Type q or 'quit' to end connection.");
			do {
				try {
					command = in.readLine();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				String response = parsePkt(command);
				out.print(response);
				out.flush();
			} while (!command.contains("q") && running);
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			remove_client(this);
		}

		public void stop() {
			mythread.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			remove_client(this);
		}

	}

	public void kill_all_clients(){
		for (int i=0;i<clients.size();i++){
			clients.get(i).stop();
		}
	}

	public void remove_client(do_socket client){
		clients.remove(client);
	}
	public boolean isInt(String i)
	{
		try
		{
			Integer.parseInt(i);
			return true;
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
	}
}