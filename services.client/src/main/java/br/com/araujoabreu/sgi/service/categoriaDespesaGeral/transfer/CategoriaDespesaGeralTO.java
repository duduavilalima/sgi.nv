package br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class CategoriaDespesaGeralTO extends BaseTO {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
