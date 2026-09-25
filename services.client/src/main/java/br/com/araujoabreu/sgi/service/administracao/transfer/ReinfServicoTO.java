package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ReinfServicoTO extends BaseTO {

	private String codigoClassificacao;

	public String getCodigoClassificacao() {
		return codigoClassificacao;
	}

	public void setCodigoClassificacao(String codigoClassificacao) {
		this.codigoClassificacao = codigoClassificacao;
	}
}
