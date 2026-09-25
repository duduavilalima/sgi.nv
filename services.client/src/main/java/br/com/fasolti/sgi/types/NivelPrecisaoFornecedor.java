package br.com.fasolti.sgi.types;

public enum NivelPrecisaoFornecedor {

	ORDEMCOMPRAAPROVADA("Ordem de compra aprovada", 0),
	NOTAFISCALORDEMCOMPRA("Nota fiscal/Ordem de compra", 1),
	NOTAFISCALAPROVADAGERENCIA("Notas fiscais aprovadas pela gerência", 2),
	NOTAFISCALAPROVADACONTASPAGAR("Notas fiscais aprovadas pelo contas a pagar", 3);

	private final String nome;
	private final int ordem;

	NivelPrecisaoFornecedor(String nome, int ordem) {
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
