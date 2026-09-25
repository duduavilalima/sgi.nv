package br.com.araujoabreu.sgi.enumerators;

public enum Situacao {

	INATIVO("Inativo", "Inativa"),
	ATIVO("Ativo", "Ativa");

	private final String descricao;
	private String descricaoFeminina;

	Situacao(String descricao, String descricaoFeminina) {
		this.descricao = descricao;
		this.descricaoFeminina = descricaoFeminina;
	}

	private Situacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoFeminina() {
		return descricaoFeminina;
	}

	public String toString() {
		return descricao;
	}

}
