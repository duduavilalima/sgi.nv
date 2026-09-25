package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class CentroFinanceiroPrevisaoTO extends BaseTO {

	private CentroFinanceiroTO centroFinanceiro;
	/**
	 * Competência (ano e mês) da previsão do centro financeiro
	 * (formato: AAAAMM, onde AAAA é o ano com 4 dígitos e MM é o mês com 2 dígitos).
	 */
	private String competencia;
	/**
	 * Valor da previsão de faturamento contratual do centro financeiro.
	 */
	private BigDecimal valorContratual = BigDecimal.ZERO;
	/**
	 * Valor da previsão de faturamento extra contratual do centro financeiro.
	 */
	private BigDecimal valorExtra = BigDecimal.ZERO;

	/**
	 * Valor da OS Inicial. Apenas para exibição. Não é persistido no banco de dados.
	 */
	private BigDecimal valorOSInicial = BigDecimal.ZERO;

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public BigDecimal getValorContratual() {
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
	}

	public BigDecimal getValorOSInicial() {
		return valorOSInicial;
	}

	public void setValorOSInicial(BigDecimal valorOSInicial) {
		this.valorOSInicial = valorOSInicial;
	}
}
