package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ConciliacaoLancamentoAssociacaoTO extends BaseTO {
	private ConciliacaoLancamentoTO conciliacaoLancamento;
	private ItemRelacaoTO itemRelacao;
	private NotaSaidaParcelaTO notaSaidaParcela;
	private String tipo;

	public ConciliacaoLancamentoTO getConciliacaoLancamento() {
		return conciliacaoLancamento;
	}

	public void setConciliacaoLancamento(ConciliacaoLancamentoTO conciliacaoLancamento) {
		this.conciliacaoLancamento = conciliacaoLancamento;
	}

	public ItemRelacaoTO getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(ItemRelacaoTO itemRelacao) {
		this.itemRelacao = itemRelacao;
	}

	public NotaSaidaParcelaTO getNotaSaidaParcela() {
		return notaSaidaParcela;
	}

	public void setNotaSaidaParcela(NotaSaidaParcelaTO notaSaidaParcela) {
		this.notaSaidaParcela = notaSaidaParcela;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
