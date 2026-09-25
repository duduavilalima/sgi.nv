package br.com.araujoabreu.sgi.compras.enums;

/**
 * TipoFrete para Nota fiscal.
 */
public enum TipoFrete {

	EMITENTE("Emitente", "Por conta do emitente"),
	DESTINATARIO("Destinatário", "Por conta do destinatário/remetente"),
	TERCEIROS("Terceiros", "Por conta de terceiros"),
	SEM_FRETE("Sem frete", "Sem frete");

	private final String nome;
	private final String descricao;

	private TipoFrete(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}

}
