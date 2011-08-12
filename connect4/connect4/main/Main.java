package connect4.main;

import java.util.Map;

import javax.swing.JOptionPane;

import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;



import connect4.engine.ConnectFourEngine;
import connect4.engine.InvalidColumnIndexException;
import connect4.engine.Player;
import connect4.ui.CUI;
import connect4.ui.GUI;

import connect4.ui.UserInterface;

public class Main implements ManagedRunnable {

	UserInterface ui;
	ConnectFourEngine e;
	public String start() {
		Player p1 = new Player(1);
		Player p2 = new Player(2);
		e = ConnectFourEngine.initInstance(p1, p2);

		ui = new GUI();

		ui.setEngine(e);
		ui.show();
		ui.start();
		return "Connect dem 4s";
	}

	@Override
	public void run(Map<Object, Object> services) {
		// TODO Auto-generated method stub
		start();
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		ui.gameOver();
	}

	public String move(int i) {
		try {
			return ui.put(i);

		} catch (InvalidColumnIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public UserInterface getUI(){
		return ui;
	}

	public ConnectFourEngine getEngine() {
		// TODO Auto-generated method stub
		return e;
	}

}