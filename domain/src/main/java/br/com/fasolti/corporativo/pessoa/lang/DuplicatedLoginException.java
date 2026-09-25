package br.com.fasolti.corporativo.pessoa.lang;

public class DuplicatedLoginException extends Exception {

	private static final long serialVersionUID = -9065821055921880094L;

	public DuplicatedLoginException() {
		super();
	}

	public DuplicatedLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedLoginException(String message) {
		super(message);
	}

	public DuplicatedLoginException(Throwable cause) {
		super(cause);
	}

}
