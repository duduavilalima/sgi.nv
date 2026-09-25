package br.com.fasolti.sgi.types;

public enum NivelPrecisaoReceita {

	AFATURAR("A faturar", 0),
	FATURADO("Faturado", 1),
	ARECEBER("A receber", 2);

	private final String nome;
	private final int ordem;

	NivelPrecisaoReceita(String nome, int ordem) {
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
