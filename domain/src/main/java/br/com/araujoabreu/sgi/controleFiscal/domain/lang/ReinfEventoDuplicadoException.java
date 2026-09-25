package br.com.araujoabreu.sgi.controleFiscal.domain.lang;

public class ReinfEventoDuplicadoException extends Exception {

	private static final long serialVersionUID = -2586556925565521944L;

	public ReinfEventoDuplicadoException(String message) {
		super(message);
	}

	public ReinfEventoDuplicadoException(String message, Throwable cause) {
		super(message, cause);
	}
}
