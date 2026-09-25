package br.com.araujoabreu.sgi.service.contaContabil.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class ContaContabilPesquisaTO extends BaseSearchTO {

	private String numero;
	private String descricao;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
