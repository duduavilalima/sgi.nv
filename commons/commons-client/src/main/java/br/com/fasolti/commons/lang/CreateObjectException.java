package br.com.fasolti.commons.lang;

public class CreateObjectException extends ServiceException {

	public CreateObjectException(Throwable cause) {
		super("Erro criando objeto", cause);
	}

}
