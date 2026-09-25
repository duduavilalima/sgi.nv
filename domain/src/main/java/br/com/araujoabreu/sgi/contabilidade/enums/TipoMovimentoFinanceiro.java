package br.com.araujoabreu.sgi.contabilidade.enums;

public enum TipoMovimentoFinanceiro {
	CREDITO("Crédito", "C"),
	DEBITO("Débito", "D");

	private final String nome;
	private final String constante;

	TipoMovimentoFinanceiro(String nome, String constante) {
		this.nome = nome;
		this.constante = constante;
	}

	public String getConstante() {
		return constante;
	}

	public String getNome() {
		return nome;
	}
}
