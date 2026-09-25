package br.com.araujoabreu.sgi.administracao.enums;

public enum StatusCodigoBarraLd {
	NAO_PREENCHIDO("Não preenchido"),
	PREENCHIDO("Preenchido");

	private final String nome;

	private StatusCodigoBarraLd(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
