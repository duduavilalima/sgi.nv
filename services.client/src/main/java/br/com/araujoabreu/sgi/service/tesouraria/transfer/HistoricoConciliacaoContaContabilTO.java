package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

/**
 * Created by vanessa.marcia on 18/04/17.
 */
public class HistoricoConciliacaoContaContabilTO extends BaseTO {

	public HistoricoConciliacaoTO historicoConciliacao;
	public UnidadeSimpleTO unidade;
	public ContaContabilTO contaContabil;
	private String numeroContaContabil;

	public HistoricoConciliacaoTO getHistoricoConciliacao() {
		return historicoConciliacao;
	}

	public void setHistoricoConciliacao(HistoricoConciliacaoTO historicoConciliacao) {
		this.historicoConciliacao = historicoConciliacao;
	}

	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public String getNumeroContaContabil() {
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}
}
