package br.com.araujoabreu.sgi.service.compras.transfer;

import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;

public class ParcelaNotaPesquisaTO extends PagamentoPesquisaTO {

	private NotaTO nota;
	private Integer ordem;
	private boolean parcelaAtual;

	public NotaTO getNota() {
		return nota;
	}

	public void setNota(NotaTO nota) {
		this.nota = nota;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public boolean isParcelaAtual() {
		return parcelaAtual;
	}

	public void setParcelaAtual(boolean parcelaAtual) {
		this.parcelaAtual = parcelaAtual;
	}
}
