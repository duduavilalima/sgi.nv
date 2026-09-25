package br.com.araujoabreu.sgi.custos.enums;

/**
 * Situação da despesa:
 * 0 = Inativa
 * 1 = Ativa.
 */
public enum SituacaoDespesaCustos {

	INATIVA(0, "Inativa"),
	ATIVA(1, "Ativa");

	private Integer code;
	private String description;

	SituacaoDespesaCustos(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
