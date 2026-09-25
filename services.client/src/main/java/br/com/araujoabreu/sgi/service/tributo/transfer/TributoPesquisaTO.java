package br.com.araujoabreu.sgi.service.tributo.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class TributoPesquisaTO extends BaseSearchTO {

	private String nome;
	private String descricao;
	private String tipo;
	private Boolean exibeNota;

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

	public Boolean getExibeNota() {
		return exibeNota;
	}

	public void setExibeNota(Boolean exibeNota) {
		this.exibeNota = exibeNota;
	}
}
