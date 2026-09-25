package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class CategoriaContratoPesquisaTO extends BaseSearchTO {

	private String tipo;
	private String descricao;
	private String criador;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}
}
