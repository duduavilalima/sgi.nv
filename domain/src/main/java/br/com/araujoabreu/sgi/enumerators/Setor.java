package br.com.araujoabreu.sgi.enumerators;

public enum Setor {

	PUBLICO("Público"),
	PRIVADO("Privado"),
	TERCEIRO("Terceiro");

	private final String descricao;

	private Setor(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
}
