package br.com.araujoabreu.sgi.lang;

public class SgiException extends RuntimeException {

	private static final long serialVersionUID = -101062583374118262L;

	public SgiException() {
		super();
	}

	public SgiException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SgiException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SgiException(String arg0) {
		super(arg0);
	}

	public SgiException(Throwable arg0) {
		super(arg0);
	}
}
