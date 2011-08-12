package connect4.engine;


public class ConnectFourEngine implements GameEngine {


	private static ConnectFourEngine e = null;
	private ConnectFourBoard board;
	private Player p1, p2, inTurn;
	
	private Move lastLegalMove;
	public boolean moved = false;

	private ConnectFourEngine(Player p1, Player p2) {
		board = ConnectFourBoard.getInstance();
		this.p1 = p1;
		this.p2 = p2;
		inTurn = this.p1;
	}

	public static synchronized ConnectFourEngine getInstance() {
		return e;
	}

	public static synchronized ConnectFourEngine initInstance(Player p1, Player p2) {
		if (e == null)
			e = new ConnectFourEngine(p1, p2);
		return getInstance();
	}

	public static synchronized ConnectFourEngine reInitInstance(Player p1, Player p2) {
		e = new ConnectFourEngine(p1, p2);
		return e;
	}

	@Override
	public boolean put(Move m) throws InvalidColumnIndexException {
		if (board.put(inTurn.getInt(), m.getTo())) {
			lastLegalMove = m;
			nextTurn();
			return true;
		} else
			return false;
	}

	private void nextTurn() {
		if (inTurn == p1)
			inTurn = p2;
		else
			inTurn = p1;
	}
	
	public boolean wins() {
		try {
			int hCount = crawl(1, 0) + crawl(-1, 0) - 1;
			if (hCount >= 4)
				return true;
			
			int vCount = crawl(0, 1) + crawl(0, -1) - 1;
			if (vCount >= 4)
				return true;
			
			int backslashCount = crawl(1, 1) + crawl(-1, -1) - 1;
			if (backslashCount >= 4)
				return true;
			
			int foreslashCount = crawl(1, -1) + crawl(-1, 1) - 1;
			if (foreslashCount >= 4)
				return true;
		} catch (InvalidColumnIndexException e) {

		}
		
		return false;
	}
	
	private int crawl(int h, int v) throws InvalidColumnIndexException {
		int hStep = (h > 0)? 1 : (h < 0)? -1 : 0;
		int vStep = (v > 0)? 1 : (v < 0)? -1 : 0;
		
		int lastMoveColumn = lastLegalMove.getTo();
		int row = board.getRowsNumber() - board.getColumnHeight(lastMoveColumn);
		int col = lastMoveColumn;
		
		int curRow = row;
		int curCol = col;
		int count;
		for (count = 0; count < 4; ++count) {
			curRow += vStep;
			curCol += hStep;
			if (curRow < 0 || curRow == board.getRowsNumber() ||
					curCol < 0 || curCol == board.getColumnsNumber() || 
					board.get(curRow, curCol) != board.get(row, col))
				break;
		}
		return 1 + count;
	}
	
	@Override
	public Player isGameOver() {
		if (board.isFull()){
		//.cleanUp();
			return new Player(3);
		}
		else {
			if (wins()) {
			//	board.cleanUp();
				if (inTurn == p1)
					return p2;
				else
					return p1;
			}
		}
		return null;
	}

	@Override
	public boolean isValidMove(Move m) throws InvalidColumnIndexException {
		return !board.isFullAt(0, m.getTo()) && board.isValidColumn(m.getTo());
	}

	@Override
	public Player getPlayerInTurn() {
		return inTurn;
	}

	public int getRowsNumber() {
		return board.getRowsNumber();
	}

	public int getColumnsNumber() {
		return board.getColumnsNumber();
	}

	public ConnectFourBoard getBoard() {
		return board;
	}

	public boolean notNull() {
		// TODO Auto-generated method stub
		return false;
	}
}
