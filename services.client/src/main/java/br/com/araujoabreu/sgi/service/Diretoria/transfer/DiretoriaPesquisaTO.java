package br.com.araujoabreu.sgi.service.Diretoria.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class DiretoriaPesquisaTO extends BaseSearchTO {

	private String nome;
	private String descricao;

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
}
