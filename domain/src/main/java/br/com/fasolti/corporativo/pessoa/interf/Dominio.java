package br.com.fasolti.corporativo.pessoa.interf;

import java.io.Serializable;

/**
 * Interface que determina uma entidade básica utilizada para dominio de codigo-valor.
 *
 * @author Marcus Lacerda
 * @author Sidharta Noleto
 */
public interface Dominio extends Serializable {

	/**
	 * Retorna o código da entidade
	 *
	 * @return
	 */
	Long getCodigo();

	/**
	 * Define o código da entidade.
	 *
	 * @param codigo
	 */
	void setCodigo(Long codigo);

	/**
	 * Retorna o nome da entidade.
	 *
	 * @return
	 */
	String getNome();

	/**
	 * Define o nome da entidade.
	 *
	 * @param nome
	 */
	void setNome(String nome);
}
