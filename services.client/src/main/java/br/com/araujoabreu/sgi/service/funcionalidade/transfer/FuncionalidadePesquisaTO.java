package br.com.araujoabreu.sgi.service.funcionalidade.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class FuncionalidadePesquisaTO extends BaseSearchTO {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
