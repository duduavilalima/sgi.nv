package br.com.araujoabreu.sgi.custos.enums;

/**
 * Operação a ser realizada na despesa "pai":
 * 0 = Soma
 * 1 = Subtração.
 */
public enum OperacaoDespesaCustos {

	SOMA(0, "Soma"),
	SUBTRACAO(1, "Subtração");

	private Integer code;
	private String description;

	OperacaoDespesaCustos(Integer code, String description) {
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
