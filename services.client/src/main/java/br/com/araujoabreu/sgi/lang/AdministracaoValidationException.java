package br.com.araujoabreu.sgi.lang;

public class AdministracaoValidationException extends AdministracaoException {


	private static final long serialVersionUID = -1043775160260881834L;

	public AdministracaoValidationException() {
	}

	public AdministracaoValidationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AdministracaoValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AdministracaoValidationException(String arg0) {
		super(arg0);
	}

	public AdministracaoValidationException(Throwable arg0) {
		super(arg0);
	}

}
