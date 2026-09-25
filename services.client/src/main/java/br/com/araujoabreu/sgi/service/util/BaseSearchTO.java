package br.com.araujoabreu.sgi.service.util;

import br.com.fasolti.commons.client.transfer.SearchObject;

public abstract class BaseSearchTO extends BaseTO implements SearchObject {
	private String ordenacao;

	public String getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
}
