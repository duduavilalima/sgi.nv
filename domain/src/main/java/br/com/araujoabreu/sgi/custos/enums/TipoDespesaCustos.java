package br.com.araujoabreu.sgi.custos.enums;

/**
 * Tipo da despesa:
 * 0 = Recebimento
 * 1 = Custo
 * 2 = Despesa de centro de custos (não disponível para CL, somente gerências, diretorias e empresas)
 * 3 = Resultado (soma dos recebimentos e custos).
 */
public enum TipoDespesaCustos {

	RECEBIMENTO(0, "Recebimento"),
	CUSTO(1, "Custo"),
	DESPESA_CENTRO_CUSTOS(2, "Despesa de centro de custos (não disponível para CL, somente gerências, diretorias e empresas)"),
	RESULTADO(3, "Resultado (soma dos recebimentos e custos)");

	private Integer code;
	private String description;

	TipoDespesaCustos(Integer code, String description) {
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
