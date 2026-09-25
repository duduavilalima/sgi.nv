package br.com.araujoabreu.sgi.administracao.enums;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoDocumentoPagamento;

public enum TipoDocumentoPagamentoPesquisa {
	BOLETOS("Boleto (a vencer/vencidos)", TipoDocumentoPagamento.BOLETO.ordinal()),
	BOLETO_VENCER("Boleto (a vencer)", 999),
	BOLETO_VENCIDO("Boleto (vencidos)", 998),
	DEPOSITO("Depósito em conta/Transferência", TipoDocumentoPagamento.DEPOSITO.ordinal()),
	DDA("DDA", TipoDocumentoPagamento.DDA.ordinal()),
	GUIA("Guia", TipoDocumentoPagamento.GUIA.ordinal()),
	DOC("DOC", TipoDocumentoPagamento.DOC.ordinal()),
	TED("TED", TipoDocumentoPagamento.TED.ordinal()),
	CONCESSIONARIA("Concessionária", TipoDocumentoPagamento.CONCESSIONARIA.ordinal()),
	OUTROS("Outros", TipoDocumentoPagamento.OUTROS.ordinal());


	private final String descricao;
	private final int valor;

	TipoDocumentoPagamentoPesquisa(String descricao, int valor) {
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getValor() {
		return valor;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
}
