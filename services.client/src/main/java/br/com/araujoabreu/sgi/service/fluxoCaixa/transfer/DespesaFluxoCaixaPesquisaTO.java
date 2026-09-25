package br.com.araujoabreu.sgi.service.fluxoCaixa.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

/**
 * Created by vanessa.marcia on 27/04/17.
 */
public class DespesaFluxoCaixaPesquisaTO extends BaseSearchTO {

	public String sigla;
	public String descricao;
	public String situacao;

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}
