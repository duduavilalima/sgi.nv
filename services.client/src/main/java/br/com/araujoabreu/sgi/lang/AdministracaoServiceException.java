package br.com.araujoabreu.sgi.lang;

public class AdministracaoServiceException extends AdministracaoException {

	private static final long serialVersionUID = 2996234681954964269L;

	public AdministracaoServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AdministracaoServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AdministracaoServiceException(String arg0) {
		super(arg0);
	}

	public AdministracaoServiceException(Throwable arg0) {
		super(arg0);
	}

}
