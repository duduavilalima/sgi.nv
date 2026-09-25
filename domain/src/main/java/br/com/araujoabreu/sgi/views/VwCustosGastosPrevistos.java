package br.com.araujoabreu.sgi.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vw_custos_gastos_previstos")
public class VwCustosGastosPrevistos implements Serializable {

	private static final long serialVersionUID = 2911152570601211918L;
	@Id
	@Column(name = "codigo", nullable = false, updatable = false, insertable = false)
	private Long codigo;

	@Column(name = "codigo_unidade", nullable = false, updatable = false, insertable = false)
	private Long codigoUnidade;

	@Column(name = "codigo_matriz", nullable = false, updatable = false, insertable = false)
	private Long codigoMatriz;

	@Column(name = "ano_pagamento", nullable = false, updatable = false, insertable = false)
	private Integer anoPagamento;

	@Column(name = "mes_pagamento", nullable = false, updatable = false, insertable = false)
	private Integer mesPagamento;

	@Column(name = "valor_total_nf", nullable = false, updatable = false, insertable = false)
	private BigDecimal valorTotalNf;

	@Column(name = "valor_liquido_pagamento", nullable = false, updatable = false, insertable = false)
	private BigDecimal valorLiquidoPagamento;

	@Column(name = "codigo_centro_financeiro", nullable = false, updatable = false, insertable = false)
	private Long codigoCentroFinanceiro;

	@Column(name = "codigo_gerencia", nullable = false, updatable = false, insertable = false)
	private Long codigoGerencia;

	@Column(name = "valor_recebimento", nullable = false, updatable = false, insertable = false)
	private BigDecimal valorRecebimento;

	@Column(name = "total_itens", nullable = false, updatable = false, insertable = false)
	private BigDecimal totalItens;

	@Column(name = "tipo_faturamento", nullable = false, updatable = false, insertable = false)
	private Integer tipoFaturamento;

	@Column(name = "ano_previsao_primeira_parcela", nullable = false, updatable = false, insertable = false)
	private Integer anoPrevisaoPrimeiraParcela;

	@Column(name = "mes_previsao_primeira_parcela", nullable = false, updatable = false, insertable = false)
	private Integer mesPrevisaoPrimeiraParcela;

	@Column(name = "ano_encerramento", nullable = false, updatable = false, insertable = false)
	private Integer anoEncerramento;

	@Column(name = "mesEncerramento", nullable = false, updatable = false, insertable = false)
	private Integer mesEncerramento;

	public VwCustosGastosPrevistos() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(Long codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	public Long getCodigoMatriz() {
		return codigoMatriz;
	}

	public void setCodigoMatriz(Long codigoMatriz) {
		this.codigoMatriz = codigoMatriz;
	}

	public Integer getAnoPagamento() {
		return anoPagamento;
	}

	public void setAnoPagamento(Integer anoPagamento) {
		this.anoPagamento = anoPagamento;
	}

	public Integer getMesPagamento() {
		return mesPagamento;
	}

	public void setMesPagamento(Integer mesPagamento) {
		this.mesPagamento = mesPagamento;
	}

	public BigDecimal getValorTotalNf() {
		return valorTotalNf;
	}

	public void setValorTotalNf(BigDecimal valorTotalNf) {
		this.valorTotalNf = valorTotalNf;
	}

	public BigDecimal getValorLiquidoPagamento() {
		return valorLiquidoPagamento;
	}

	public void setValorLiquidoPagamento(BigDecimal valorLiquidoPagamento) {
		this.valorLiquidoPagamento = valorLiquidoPagamento;
	}

	public Long getCodigoCentroFinanceiro() {
		return codigoCentroFinanceiro;
	}

	public void setCodigoCentroFinanceiro(Long codigoCentroFinanceiro) {
		this.codigoCentroFinanceiro = codigoCentroFinanceiro;
	}

	public Long getCodigoGerencia() {
		return codigoGerencia;
	}

	public void setCodigoGerencia(Long codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}

	public BigDecimal getValorRecebimento() {
		return valorRecebimento;
	}

	public void setValorRecebimento(BigDecimal valorRecebimento) {
		this.valorRecebimento = valorRecebimento;
	}

	public BigDecimal getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(BigDecimal totalItens) {
		this.totalItens = totalItens;
	}

	public Integer getTipoFaturamento() {
		return tipoFaturamento;
	}

	public void setTipoFaturamento(Integer tipoFaturamento) {
		this.tipoFaturamento = tipoFaturamento;
	}

	public Integer getAnoPrevisaoPrimeiraParcela() {
		return anoPrevisaoPrimeiraParcela;
	}

	public void setAnoPrevisaoPrimeiraParcela(Integer anoPrevisaoPrimeiraParcela) {
		this.anoPrevisaoPrimeiraParcela = anoPrevisaoPrimeiraParcela;
	}

	public Integer getMesPrevisaoPrimeiraParcela() {
		return mesPrevisaoPrimeiraParcela;
	}

	public void setMesPrevisaoPrimeiraParcela(Integer mesPrevisaoPrimeiraParcela) {
		this.mesPrevisaoPrimeiraParcela = mesPrevisaoPrimeiraParcela;
	}

	public Integer getAnoEncerramento() {
		return anoEncerramento;
	}

	public void setAnoEncerramento(Integer anoEncerramento) {
		this.anoEncerramento = anoEncerramento;
	}

	public Integer getMesEncerramento() {
		return mesEncerramento;
	}

	public void setMesEncerramento(Integer mesEncerramento) {
		this.mesEncerramento = mesEncerramento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VwCustosGastosPrevistos)) return false;
		VwCustosGastosPrevistos that = (VwCustosGastosPrevistos) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getCodigoUnidade(), that.getCodigoUnidade()) &&
			Objects.equals(getCodigoMatriz(), that.getCodigoMatriz()) &&
			Objects.equals(getAnoPagamento(), that.getAnoPagamento()) &&
			Objects.equals(getMesPagamento(), that.getMesPagamento()) &&
			Objects.equals(getValorTotalNf(), that.getValorTotalNf()) &&
			Objects.equals(getValorLiquidoPagamento(), that.getValorLiquidoPagamento()) &&
			Objects.equals(getCodigoCentroFinanceiro(), that.getCodigoCentroFinanceiro()) &&
			Objects.equals(getCodigoGerencia(), that.getCodigoGerencia()) &&
			Objects.equals(getValorRecebimento(), that.getValorRecebimento()) &&
			Objects.equals(getTotalItens(), that.getTotalItens()) &&
			Objects.equals(getTipoFaturamento(), that.getTipoFaturamento());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getCodigoUnidade(), getCodigoMatriz(), getAnoPagamento(), getMesPagamento(), getValorTotalNf(), getValorLiquidoPagamento(), getCodigoCentroFinanceiro(), getCodigoGerencia(), getValorRecebimento(), getTotalItens(), getTipoFaturamento());
	}
}
