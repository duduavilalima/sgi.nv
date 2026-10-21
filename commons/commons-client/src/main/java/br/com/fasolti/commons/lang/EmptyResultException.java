package br.com.fasolti.commons.lang;

public class EmptyResultException extends ServiceException {
	public EmptyResultException(String message) {
		super(message);
	}

	public EmptyResultException(String message, Throwable cause) {
		super(message, cause);
	}
}
