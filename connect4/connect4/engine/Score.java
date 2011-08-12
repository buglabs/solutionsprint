package connect4.engine;

public class Score {
	private int horizontal;
	private int vertical;
	private int backslash;
	private int forslash;

	public Score() {
		horizontal = 0;
		vertical = 0;
		backslash = 0;
		forslash = 0;
	}

	public synchronized int getHorizontal() {
		return horizontal;
	}

	public synchronized void setHorizontal(int horizontal) {
		this.horizontal = horizontal;
	}

	public synchronized int getVertical() {
		return vertical;
	}

	public synchronized void setVertical(int vertical) {
		this.vertical = vertical;
	}

	public synchronized int getBackslash() {
		return backslash;
	}

	public synchronized void setBackslash(int backslash) {
		this.backslash = backslash;
	}

	public synchronized int getForslash() {
		return forslash;
	}

	public synchronized void setForslash(int forslash) {
		this.forslash = forslash;
	}
}
