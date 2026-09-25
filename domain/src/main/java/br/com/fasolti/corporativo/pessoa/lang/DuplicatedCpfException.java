package br.com.fasolti.corporativo.pessoa.lang;

/**
 * Indica que o CPF já está registrado no sistema de armazenamento.
 *
 * @author Rodrigo Barreiros
 */
public class DuplicatedCpfException extends Exception {

	private static final String DEFAULT_EXCEPTION_MESSAGE = "Esse CPF já existe na base de dados.";

	private static final long serialVersionUID = 1L;

	/**
	 * Permite criar a exceção com uma mensagem diferente da mensagem defatut.
	 *
	 * @param message a nova mensagem
	 */
	public DuplicatedCpfException(String message) {
		super(message);
	}

	/**
	 * Construtor default. Cria a exceção com a mensagem default.
	 */
	public DuplicatedCpfException() {
		super(DEFAULT_EXCEPTION_MESSAGE);
	}

}
