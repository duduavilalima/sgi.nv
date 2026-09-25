package br.com.fasolti.security.crypto;

public class DigestException extends RuntimeException {

	private static final long serialVersionUID = -7759268413966098506L;

	public DigestException(String message, Throwable cause) {
		super(message, cause);
	}

	public DigestException(String message) {
		super(message);
	}
}
