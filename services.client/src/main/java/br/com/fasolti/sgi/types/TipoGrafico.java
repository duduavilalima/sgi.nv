package br.com.fasolti.sgi.types;

public enum TipoGrafico {

	BARRALINHA("Barra/linha", 0),
	LINHA("Linha", 1);

	private final String nome;
	private final int ordem;

	TipoGrafico(String nome, int ordem) {
		this.nome = nome;
		this.ordem = ordem;
	}

	public String getNome() {
		return nome;
	}

	public int getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
