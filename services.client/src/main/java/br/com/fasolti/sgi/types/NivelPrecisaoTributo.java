package br.com.fasolti.sgi.types;

public enum NivelPrecisaoTributo {

	TRIBUTOSAGRUPAR("Tributos à agrupar", 0),
	TRIBUTOSAGRUPARLIBERADOS("Tributos agrupados/liberados", 1);

	private final String nome;
	private final int ordem;

	NivelPrecisaoTributo(String nome, int ordem) {
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
