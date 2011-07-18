package mb2controller;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;


import java.util.Map;
import java.io.*;
import java.net.*;


import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;

public class mb2ControlApp implements ManagedRunnable {
	private SerialPort port;
	private CommPortIdentifier comm;
	private InputStream is;
	private OutputStream os;
	private PrintWriter out;
	private Serial_reader reader;
	private boolean shutdown = false;
	Socket echoSocket = null;
	private PrintWriter out2 = null;
	private BufferedReader in = null;

	@Override
	public void run(Map<Object, Object> services) {
		// TODO Auto-generated method stub
		System.out.println("[Bugduino] service start.");
		System.out.println("[Bugduino] Attempting to open arduino serial port. ");
		//Connect bugduino
		while (!connectPort()){
			if (shutdown)
				return;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
		System.out.println("[Bugduino] Serial port initialized.");
		try {
			reader = new Serial_reader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		shutdown = true;
		reader.stop();
		out.write("Q\r\n");
		out.flush();
		try {
			is.close();
			os.close();
			port.close();
			out2.close();
			in.close();
			echoSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Bugduino] service stopped.");

	}
	private boolean connectPort(){
		try {
			System.out.println("try connect port");
			comm = CommPortIdentifier.getPortIdentifier("/dev/ttyBMI2");
		} catch (NoSuchPortException e1) {
			System.out.println("[Bugduino] Port cannot be found");
			e1.printStackTrace();
			return false;
		}
		try {
			System.out.println("try 2");
			port = (SerialPort)comm.open("bugduino", 1000);
			port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			is = port.getInputStream();
			os = port.getOutputStream();
			out = new PrintWriter(os, true);


		} catch (PortInUseException e2) {
			System.err.println("[Bugduino] port in use");
			return false;
		} catch (IOException e2) {
			System.err.println("[Bugduino] Can't get input stream");
			return false;
		} catch (Exception e){
			System.err.println("[Bugduino] Unknown error!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
	private class Serial_reader implements Runnable {
		boolean running = true;
		Thread mythread;
		InputStreamReader istr;
		BufferedReader br;


		public Serial_reader() throws IOException, InterruptedException{
			istr = new InputStreamReader(is);
			br = new BufferedReader(istr);
			mythread = new Thread(this, "serial reader");
			mythread.start();

			try {
				System.out.println("[Bugsocket] Trying to connect to motherbug2");
				echoSocket = new Socket("192.168.20.221", 23);
				out2 = new PrintWriter(echoSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
						echoSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: motherbug2.local.");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for "
						+ "the connection to: motherbug2.");
				System.exit(1);
			}
			System.out.println("[Bugsocket] Connected to motherbug2");

		}
		@Override
		public void run() {
			String line;

			while (running){

				try {
					line = br.readLine();
				} catch (Exception e) {
					System.err.println("Read thread exception - closing");
					running = false;
					return;
				}
				if(line.contains("quit")){
					stop();
					shutdown();
				}
				out2.println(line);
				try {
					System.out.println("echo: "+ in.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}
		public void stop() {
			//This will not work if the read is blocking
			//the input stream needs to be closed to force the read
			//to throw an IOException
			//For now, we shall assume that closing the app will do this.
			running = false;
		}


	}

}