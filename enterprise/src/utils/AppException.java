package utils;

public class AppException extends RuntimeException {
	/**
	 * 		When an exception is called
	 * @param e the exception
	 */
	public AppException(Exception e) {
		super(e);
	}
	
	/**
	 * 		Sends string to RuntimeException
	 * @param msg the string
	 */
	public AppException(String msg) {
		super(msg);
	}
	
}