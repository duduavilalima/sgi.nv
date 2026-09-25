package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ItemRelacaoPagamentoTO extends BaseTO {
	private PagamentoTO pagamento;
	private ItemRelacaoTO itemRelacao;

	public ItemRelacaoPagamentoTO() {
	}

	public ItemRelacaoPagamentoTO(PagamentoTO pagamento, ItemRelacaoTO itemRelacao) {
		this.pagamento = pagamento;
		this.itemRelacao = itemRelacao;
	}

	public PagamentoTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoTO pagamento) {
		this.pagamento = pagamento;
	}

	public ItemRelacaoTO getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(ItemRelacaoTO itemRelacao) {
		this.itemRelacao = itemRelacao;
	}
}
