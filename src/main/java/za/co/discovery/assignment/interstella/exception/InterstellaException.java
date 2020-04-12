package za.co.discovery.assignment.interstella.exception;

public class InterstellaException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Source and Destination cannot be the same";
	}
}