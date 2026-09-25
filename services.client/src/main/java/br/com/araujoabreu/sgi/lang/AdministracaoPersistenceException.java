package br.com.araujoabreu.sgi.lang;

public class AdministracaoPersistenceException extends AdministracaoException {

	private static final long serialVersionUID = -2307975084180497173L;

	public AdministracaoPersistenceException() {
	}

	public AdministracaoPersistenceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AdministracaoPersistenceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AdministracaoPersistenceException(String arg0) {
		super(arg0);
	}

	public AdministracaoPersistenceException(Throwable arg0) {
		super(arg0);
	}

}
