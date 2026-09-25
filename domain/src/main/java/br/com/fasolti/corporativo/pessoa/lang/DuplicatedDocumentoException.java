package br.com.fasolti.corporativo.pessoa.lang;

import br.com.fasolti.corporativo.pessoa.domain.Pessoa;

/**
 * Indica que o documento já está registrado no sistema de armazenamento.
 *
 * @author daniel.oliveira
 * @see Pessoa
 */
public class DuplicatedDocumentoException extends Exception {

	private static final String DEFAULT_EXCEPTION_MESSAGE = "Esse documento já existe na base de dados.";

	private static final long serialVersionUID = 1L;

	/**
	 * Permite criar a exceção com uma mensagem diferente da mensagem defatut.
	 *
	 * @param message a nova mensagem
	 */
	public DuplicatedDocumentoException(String message) {
		super(message);
	}

	/**
	 * Construtor default. Cria a exceção com a mensagem default.
	 */
	public DuplicatedDocumentoException() {
		super(DEFAULT_EXCEPTION_MESSAGE);
	}

}
