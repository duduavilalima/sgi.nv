package br.com.araujoabreu.sgi.enumerators;

public enum PermiteContrato {

	SIM("Sim"),
	NAO("Não");

	private final String descricao;

	private PermiteContrato(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
