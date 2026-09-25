package br.com.araujoabreu.sgi.administracao.enums;

public enum TipoEnderecoContrato {

	PRESTACAO_SERVICO(0, "Prestação de serviço"),
	SO_COBRANCA(1, "Faturamento"),
	OUTROS(2, "Outros");

	private final int ordem;
	private final String descricao;

	TipoEnderecoContrato(int ordem, String descricao) {
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
