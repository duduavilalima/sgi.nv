package br.com.araujoabreu.sgi.compras.enums;

public enum TipoSeguro {

	EMITENTE("Emitente", "Por conta do emitente"),
	DESTINATARIO("Destinatário", "Por conta do destinatário/remetente");

	private final String nome;
	private final String descricao;

	TipoSeguro(String nome, String descricao) {
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
		return nome;
	}

}
