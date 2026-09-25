package br.com.araujoabreu.sgi.service.tributo.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class TributoTO extends BaseTO {
	private String nome;
	private String descricao;
	private String tipo;

	public TributoTO() {
	}

	public TributoTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
