package br.com.fasolti.sgi.types;

public enum TipoPeriodo {
	DIARIO("Diário", 0),
	SEMANAL("Semanal", 1);

	private final String nome;
	private final int ordem;

	TipoPeriodo(String nome, int ordem) {
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
