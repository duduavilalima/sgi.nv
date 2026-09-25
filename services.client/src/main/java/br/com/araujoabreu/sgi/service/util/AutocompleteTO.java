package br.com.araujoabreu.sgi.service.util;

import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;

public class AutocompleteTO extends BaseTO {

	private String nome;
	private UnidadeTO unidade;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}
}
