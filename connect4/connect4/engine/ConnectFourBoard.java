package connect4.engine;



public class ConnectFourBoard implements Board {

	private static ConnectFourBoard connect4board;
	int[][] board;
	private int rowsNumber;
	private int columnsNumber;


	private ConnectFourBoard() {
		rowsNumber = 6;
		columnsNumber = 7;
		board = new int[rowsNumber][columnsNumber];
	}

	public static synchronized ConnectFourBoard getInstance() {
		if (connect4board == null)
			connect4board = new ConnectFourBoard();
		return connect4board;
	}

	public int getRowsNumber() {
		return rowsNumber;
	}

	public void cleanUp(){
		for(int i = 0; i< rowsNumber;i++){
			for(int j =0;j<columnsNumber;j++){
				board[i][j]=0;
			}
		}
	}
	
	public int getColumnsNumber() {
		return columnsNumber;
	}

	/**
	 * Check if the specific place(row,column) is full.
	 * 
	 * @param row
	 *            - index of the row (zero based).
	 * @param column
	 *            - index of the column (zero based).
	 * @return <code>true</code> if the <code>to</code> is between 0 and 6
	 *         (inclusive); <code>false</code> otherwise.
	 */
	public boolean isFullAt(int row, int column)
			throws InvalidColumnIndexException {
		if (isValidColumn(column) && isValidRow(row))
			return board[row][column] != 0;
		else
			throw new InvalidColumnIndexException();
	}

	/**
	 * Check the validity of the given column number(index).
	 * 
	 * @param columnNumber
	 *            - the column index to check its validity.
	 * @return <code>true</code> if the <code>columnNumber</code> is between 0
	 *         and <code>columnsNumber</code>-1 (inclusive); <code>false</code>
	 *         otherwise.
	 */
	public boolean isValidColumn(int columnNumber) {
		return columnNumber >= 0 && columnNumber <= columnsNumber - 1;
	}

	/**
	 * Check the validity of the given row number(index).
	 * 
	 * @param rowNumber
	 *            - the row index to check its validity.
	 * @return <code>true</code> if the <code>rowNumber</code> is between 0 and
	 *         <code>rowsNumber</code>-1 (inclusive); <code>false</code>
	 *         otherwise.
	 */
	public boolean isValidRow(int rowNumber) {
		return rowNumber >= 0 && rowNumber <= rowsNumber - 1;
	}

	/**
	 * Puts the given <code>playerNumber</code> in the bottom most empty place
	 * in the board.
	 * 
	 * @param playerNumber
	 *            - the Player's Number (non zero)
	 * @param columnNumber
	 *            - the column index to put in it.
	 * @return <code>true</code> if the put was successful; <code>false</code>
	 *         otherwise (the <code>columnNumber</code> is full).
	 * @throws InvalidColumnIndexException
	 *             if the <code>columnNumber</code> is invalid.
	 */
	public boolean put(int playerNumber, int columnNumber)
			throws InvalidColumnIndexException {
		if (!isValidColumn(columnNumber))
			throw new InvalidColumnIndexException();
		else {
			// TODO Auto-generated method stub
			for (int i = rowsNumber - 1; i >= 0; i--) {
				if (board[i][columnNumber] == 0) {
					board[i][columnNumber] = playerNumber;
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * is the board full or not.
	 * 
	 * @return <code>true</code> if the board is completely full;
	 *         <code>false</code> otherwise.
	 */
	public boolean isFull() {
		boolean b = true;
		for (int i = 0; i < columnsNumber; i++)
			try {
				b = b && isFullAt(0, i);
			} catch (InvalidColumnIndexException e) {
			}
		return b;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			b.append("|");
			for (int j = 0; j < board[0].length; j++)
				b.append(board[i][j]).append("|");
			b.append("\n");
		}

		return b.toString();
	}

	public int get(int rowIndex, int columnIndex)
			throws InvalidColumnIndexException {
		if (isValidColumn(columnIndex) && isValidRow(rowIndex))
			return board[rowIndex][columnIndex];
		else
			throw new InvalidColumnIndexException();
	}

	public int getColumnHeight(int columnIndex)
			throws InvalidColumnIndexException {
		if (!isValidColumn(columnIndex)) {
			throw new InvalidColumnIndexException();
		}
		int i;
		for (i = 0; i < getRowsNumber(); ++i) {
			if (board[i][columnIndex] != 0)
				break;
		}
		return getRowsNumber() - i;
	}

	public int[][] get2dArray() {
		return board;
	}
}