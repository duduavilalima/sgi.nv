package br.com.araujoabreu.sgi.administracao.enums;

public enum TipoInformacaoNumeroBoleto {
	LINHA_DIGITAVEL("Linha digitável"),
	CODIGO_BARRAS("Código de barras");

	private final String nome;

	TipoInformacaoNumeroBoleto(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return nome;
	}

}
