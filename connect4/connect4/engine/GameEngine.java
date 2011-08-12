package connect4.engine;

public interface GameEngine {
	public Player isGameOver();
	public boolean isValidMove(Move m) throws InvalidColumnIndexException;
	public Player getPlayerInTurn();
	boolean put(Move m) throws InvalidColumnIndexException;
}