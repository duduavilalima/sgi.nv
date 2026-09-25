package br.com.araujoabreu.sgi.tesouraria.lang;

public class TesourariaValidateException extends Exception {

	private static final long serialVersionUID = -5773263044910386062L;

	public TesourariaValidateException() {
		super();
	}

	public TesourariaValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TesourariaValidateException(String message) {
		super(message);
	}

	public TesourariaValidateException(Throwable cause) {
		super(cause);
	}

}
