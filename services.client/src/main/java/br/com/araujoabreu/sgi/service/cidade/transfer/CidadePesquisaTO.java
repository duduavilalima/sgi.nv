package br.com.araujoabreu.sgi.service.cidade.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class CidadePesquisaTO extends BaseSearchTO {

	private String nome;
	private String uf;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
}
