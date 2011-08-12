package connect4.ui;

import connect4.engine.ConnectFourEngine;
import connect4.engine.InvalidColumnIndexException;
import connect4.engine.Move;
import connect4.engine.Player;

public class AbstractUserInterface implements UserInterface{

	private ConnectFourEngine e;

	@Override
	public void setEngine(ConnectFourEngine e) {
		this.e = e;
	}
	
	public ConnectFourEngine getEngine() {
		return e;
	}


	@Override
	public String put(int columnNumber) throws InvalidColumnIndexException {
		boolean putIsDone = e.put(new Move(columnNumber));
		if (putIsDone) {
			Player p = e.isGameOver();
			updateBoard();
			if (p != null) {
				gameOver(p);
				int player = p.getInt();
				if(player!=3)
					return "Player: " + player + " won.";
				else
					return "Draw.";
				
			}
		}
		return "movin'";
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateBoard() {
		// TODO Auto-generated method stub
	}

	@Override
	public Player whosTurn() {
		return e.getPlayerInTurn();
	}

	public void gameOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameOver(Player winner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newGame() {
		// TODO Auto-generated method stub
		e.getBoard().cleanUp();
	}
}
