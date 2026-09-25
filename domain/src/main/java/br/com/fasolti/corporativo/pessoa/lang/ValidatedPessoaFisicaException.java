package br.com.fasolti.corporativo.pessoa.lang;

public class ValidatedPessoaFisicaException extends Exception {

	private static final long serialVersionUID = -7506885146870943688L;

	public ValidatedPessoaFisicaException() {
		super();
	}

	public ValidatedPessoaFisicaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidatedPessoaFisicaException(String message) {
		super(message);
	}

	public ValidatedPessoaFisicaException(Throwable cause) {
		super(cause);
	}

}
