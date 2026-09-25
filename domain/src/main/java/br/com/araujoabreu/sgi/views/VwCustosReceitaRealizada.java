package br.com.araujoabreu.sgi.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vw_custos_receita_realizada")
public class VwCustosReceitaRealizada implements Serializable {

	private static final long serialVersionUID = -5345490822738654021L;
	@Id
	@Column(name = "codigo_unidade", nullable = false, updatable = false, insertable = false)
	private Long codigoUnidade;

	@Column(name = "codigo_matriz", nullable = false, updatable = false, insertable = false)
	private Long codigoMatriz;

	@Column(name = "codigo_centro_financeiro", nullable = false, updatable = false, insertable = false)
	private Long codigoCentroFinanceiro;

	@Column(name = "codigo_gerencia", nullable = false, updatable = false, insertable = false)
	private Long codigoGerencia;

	@Column(name = "ano_pagamento", nullable = false, updatable = false, insertable = false)
	private Integer anoPagamento;

	@Column(name = "mes_pagamento", nullable = false, updatable = false, insertable = false)
	private Integer mesVPagamento;

	@Column(name = "valor_liquido", nullable = false, updatable = false, insertable = false)
	private BigDecimal valorLiquido;

	@Column(name = "tipo_faturamento", nullable = false, updatable = false, insertable = false)
	private Integer tipoFaturamento;

	public VwCustosReceitaRealizada() {
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

	public Integer getAnoPagamento() {
		return anoPagamento;
	}

	public void setAnoPagamento(Integer anoPagamento) {
		this.anoPagamento = anoPagamento;
	}

	public Integer getMesVPagamento() {
		return mesVPagamento;
	}

	public void setMesVPagamento(Integer mesVPagamento) {
		this.mesVPagamento = mesVPagamento;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Integer getTipoFaturamento() {
		return tipoFaturamento;
	}

	public void setTipoFaturamento(Integer tipoFaturamento) {
		this.tipoFaturamento = tipoFaturamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VwCustosReceitaRealizada)) return false;
		VwCustosReceitaRealizada that = (VwCustosReceitaRealizada) o;
		return Objects.equals(getCodigoUnidade(), that.getCodigoUnidade()) &&
			Objects.equals(getCodigoMatriz(), that.getCodigoMatriz()) &&
			Objects.equals(getCodigoCentroFinanceiro(), that.getCodigoCentroFinanceiro()) &&
			Objects.equals(getCodigoGerencia(), that.getCodigoGerencia()) &&
			Objects.equals(getAnoPagamento(), that.getAnoPagamento()) &&
			Objects.equals(getMesVPagamento(), that.getMesVPagamento()) &&
			Objects.equals(getValorLiquido(), that.getValorLiquido()) &&
			Objects.equals(getTipoFaturamento(), that.getTipoFaturamento());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigoUnidade(), getCodigoMatriz(), getCodigoCentroFinanceiro(), getCodigoGerencia(), getAnoPagamento(), getMesVPagamento(), getValorLiquido(), getTipoFaturamento());
	}
}
