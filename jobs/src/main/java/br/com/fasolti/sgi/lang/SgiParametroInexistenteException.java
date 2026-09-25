package br.com.fasolti.sgi.lang;

import br.com.araujoabreu.sgi.lang.SgiException;

public class SgiParametroInexistenteException extends SgiException {

	private static final long serialVersionUID = -6222884211345454713L;

	public SgiParametroInexistenteException() {
		super();
	}

	public SgiParametroInexistenteException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SgiParametroInexistenteException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SgiParametroInexistenteException(String arg0) {
		super(arg0);
	}

	public SgiParametroInexistenteException(Throwable arg0) {
		super(arg0);
	}

}
