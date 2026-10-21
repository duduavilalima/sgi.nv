package br.com.fasolti.commons.lang;

public class InvalidParametersException extends ServiceException {
	public InvalidParametersException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParametersException(String message) {
		super(message);
	}
}
