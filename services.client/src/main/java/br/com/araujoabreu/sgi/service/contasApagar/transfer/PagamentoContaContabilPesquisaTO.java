package br.com.araujoabreu.sgi.service.contasApagar.transfer;

import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class PagamentoContaContabilPesquisaTO extends BaseSearchTO {

	private PagamentoTO pagamento;
	private ContaContabilTO contaContabil;
	private String tipo;

	public PagamentoTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoTO pagamento) {
		this.pagamento = pagamento;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
