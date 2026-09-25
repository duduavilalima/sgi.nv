package br.com.araujoabreu.sgi.administracao.enums;

public enum TipoAnexo {

	ADITIVO(0, "Aditivo"),
	PROPOSTA(1, "Proposta"),
	CONTRATO(2, "Contrato"),
	ORCAMENTO(3, "Orçamento"),
	OUTROS(4, "Outros");

	private final int ordem;
	private final String descricao;

	TipoAnexo(int ordem, String descricao) {
		this.ordem = ordem;
		this.descricao = descricao;
	}

	public int getOrdem() {
		return ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
}
