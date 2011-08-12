package connect4.engine;

import java.util.Random;

public class AIPlayer extends Player {
	public AIPlayer(int playerNum) {
		super(playerNum);
	}

	public Move getMove(){
		Random r = new Random();
		int maxColumnNumber = ConnectFourBoard.getInstance().getColumnsNumber();
		return new Move(r.nextInt(maxColumnNumber));
	}
}