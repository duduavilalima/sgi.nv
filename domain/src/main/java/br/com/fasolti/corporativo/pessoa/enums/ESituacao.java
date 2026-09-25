package br.com.fasolti.corporativo.pessoa.enums;

public enum ESituacao {

	INATIVO("Inativo"),
	ATIVO("Ativo");

	private final String descricao;

	private ESituacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
