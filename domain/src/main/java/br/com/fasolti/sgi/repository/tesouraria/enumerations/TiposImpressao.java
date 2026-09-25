package br.com.fasolti.sgi.repository.tesouraria.enumerations;

/**
 * Tipos de Impressão.<br/>
 * Usar para quando um certo tipo de impressão tiver um tratamento diferente.
 */
public enum TiposImpressao {

	CHEQUE("Cheque"),
	ENDOSSO("Endosso"),
	COPIA("Cópia (2ª via)");

	private final String tipo;

	private TiposImpressao(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return tipo;
	}

}
