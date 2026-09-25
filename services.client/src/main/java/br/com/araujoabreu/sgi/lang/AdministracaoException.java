package br.com.araujoabreu.sgi.lang;

public class AdministracaoException extends SgiException {

	private static final long serialVersionUID = -7198468813429487991L;

	public AdministracaoException() {
		super();
	}

	public AdministracaoException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AdministracaoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AdministracaoException(String arg0) {
		super(arg0);
	}

	public AdministracaoException(Throwable arg0) {
		super(arg0);
	}
}
