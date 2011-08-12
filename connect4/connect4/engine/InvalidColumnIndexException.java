package connect4.engine;

public class InvalidColumnIndexException extends Exception {
	
	private final String message = "The index provided is not in range.";
	
	public InvalidColumnIndexException() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
}
