package connect4.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import connect4.engine.ConnectFourBoard;
import connect4.engine.InvalidColumnIndexException;
import connect4.engine.Player;

public class CUI extends AbstractUserInterface{

	@Override
	public void gameOver(Player winner) {
		System.out.println(winner.getInt()+" wins");
	}
	
	@Override
	public void start() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (super.getEngine().isGameOver() == null) {
			System.out.println(super.getEngine().getPlayerInTurn().getInt()+"'s turn");
			
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			try {
				put(Integer.parseInt(line));
			} catch (NumberFormatException e1) {
				System.out.println("Please Enter a number.");
			} catch (InvalidColumnIndexException e1) {
				System.out.println("Please Enter a number from 0 to "+(super.getEngine().getColumnsNumber()-1));
			}
			System.out.println("-------------------------------");
		}
	}

	@Override
	public void updateBoard() {
		StringBuilder sb = new StringBuilder();
		int rowsNumber = super.getEngine().getRowsNumber();
		int columnsNumber = super.getEngine().getColumnsNumber();
		ConnectFourBoard b = super.getEngine().getBoard();
		for(int i =0;i < rowsNumber;i++){
			sb.append("|");
			for(int j = 0;j<columnsNumber;j++){
				try {
					sb.append(b.get(i, j)).append("|");
				} catch (InvalidColumnIndexException e) {}
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}
}
