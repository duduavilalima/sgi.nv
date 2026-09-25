package br.com.fasolti.sgi.types;

public enum NivelPrecisaoPretacaoConta {

	APROVADAGERENCIA("Aprovada pela gerência", 0),
	HOMOLOGADO("Homologado", 1),
	EMPAGAMENTO("Em pagamento", 2);

	private final String nome;
	private final int ordem;

	NivelPrecisaoPretacaoConta(String nome, int ordem) {
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
