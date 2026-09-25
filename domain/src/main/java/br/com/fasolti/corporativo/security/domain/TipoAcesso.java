package br.com.fasolti.corporativo.security.domain;

public enum TipoAcesso {
	INTERNO("Interno"), FORNECEDOR("Fornecedor");

	private final String nome;

	TipoAcesso(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
