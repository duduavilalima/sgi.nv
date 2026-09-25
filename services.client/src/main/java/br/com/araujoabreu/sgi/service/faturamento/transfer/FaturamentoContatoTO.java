package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class FaturamentoContatoTO extends BaseTO {

	private FaturamentoTO faturamento;
	private ContatoTO contato;
	private Integer ordem;

	public FaturamentoTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoTO faturamento) {
		this.faturamento = faturamento;
	}

	public ContatoTO getContato() {
		return contato;
	}

	public void setContato(ContatoTO contato) {
		this.contato = contato;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
