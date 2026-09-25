package br.com.araujoabreu.sgi.compras.enums;

/**
 * TipoFrete para Ordem de compra.
 */
public enum TipoFreteOC {

	EMITENTE("Emitente", "Por conta do emitente"),
	DESTINATARIO("Destinatário", "Por conta do destinatário/remetente");

	private final String nome;
	private final String descricao;

	private TipoFreteOC(String nome, String descricao) {
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
