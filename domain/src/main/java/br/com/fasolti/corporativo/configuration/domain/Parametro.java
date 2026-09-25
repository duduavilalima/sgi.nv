package br.com.fasolti.corporativo.configuration.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entidade que representa parâmetros de configuração armazenados em banco no formato chave-valor.
 *
 * @author leandro.oliveira
 */
@Entity
@Table(name = "PARAMETRO")
public class Parametro implements Serializable {

	private static final long serialVersionUID = 1L;

	private String chave;
	private String valor;

	@Id
	@Column(name = "CHAVE")
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	@Column(name = "VALOR")
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
