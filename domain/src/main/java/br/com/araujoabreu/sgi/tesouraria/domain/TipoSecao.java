package br.com.araujoabreu.sgi.tesouraria.domain;

public enum TipoSecao {
	BOLETO_RELACAO_BANCARIA("Boleto"),
	BOLETO_OUTROS_BANCOS("Boleto outros bancos"),
	CONCESSIONARIA("Concessionárias"),
	DEPOSITO_CORRENTE("Depósito/transferência - Conta corrente"),
	DEPOSITO_POUPANCA("Depósito/transferência - Conta poupança"),
	DEPOSITO_SALARIO("Depósito/transferência - Conta salário"),
	TED("TED"),
	DOC("DOC"),
	GUIA("Guia"),
	OUTROS("Outros"),
	DDA("DDA");

	private final String descricao;

	TipoSecao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
