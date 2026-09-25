package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class CentroFinanceiroPrevisaoPesquisaTO extends BaseSearchTO {

	/**
	 * Competência (ano e mês) da previsão do centro financeiro
	 * (formato: AAAAMM, onde AAAA é o ano com 4 dígitos e MM é o mês com 2 dígitos).
	 */
	private String competencia;

	private String tipoFaturamento;
	private Long codigoGerencia;
	private Long codigoUnidade;
	private Long codigoCL;

//	private CentroFinanceiroTO centroFinanceiro;

	//	private BigDecimal valorContratual = BigDecimal.ZERO;

	/**
	 * Valor da previsão de faturamento extra contratual do centro financeiro.
	 */
//	private BigDecimal valorExtra = BigDecimal.ZERO;

	/*public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}*/
	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	/*public BigDecimal getValorContratual() {
		return valorContratual;
	}

	public void setValorContratual(BigDecimal valorContratual) {
		this.valorContratual = valorContratual;
	}

	public BigDecimal getValorExtra() {
		return valorExtra;
	}

	public void setValorExtra(BigDecimal valorExtra) {
		this.valorExtra = valorExtra;
	}*/

	public String getTipoFaturamento() {
		return tipoFaturamento;
	}

	public void setTipoFaturamento(String tipoFaturamento) {
		this.tipoFaturamento = tipoFaturamento;
	}

	public Long getCodigoGerencia() {
		return codigoGerencia;
	}

	public void setCodigoGerencia(Long codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}

	public Long getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(Long codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	public Long getCodigoCL() {
		return codigoCL;
	}

	public void setCodigoCL(Long codigoCL) {
		this.codigoCL = codigoCL;
	}
}
