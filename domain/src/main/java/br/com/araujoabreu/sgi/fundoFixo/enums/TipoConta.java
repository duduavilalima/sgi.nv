package br.com.araujoabreu.sgi.fundoFixo.enums;

public enum TipoConta {
	FUNDO_FIXO("Fundo Fixo", "FF"),
	REEMBOLSO("Reembolso", "RE"),
	ADIANTAMENTO("Adiantamento", "AD");

	private final String nome;
	private final String sigla;

	TipoConta(String nome, String sigla) {
		this.nome = nome;
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public String getSigla() {
		return sigla;
	}

}
