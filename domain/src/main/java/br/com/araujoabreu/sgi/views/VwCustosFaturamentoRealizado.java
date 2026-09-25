package br.com.araujoabreu.sgi.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vw_custos_faturamento_realizado")
public class VwCustosFaturamentoRealizado implements Serializable {

	private static final long serialVersionUID = -3599305166413776440L;
	@Id
	@Column(name = "codigo_unidade", nullable = false, updatable = false, insertable = false)
	private Long codigoUnidade;

	@Column(name = "codigo_matriz", nullable = false, updatable = false, insertable = false)
	private Long codigoMatriz;

	@Column(name = "codigo_centro_financeiro", nullable = false, updatable = false, insertable = false)
	private Long codigoCentroFinanceiro;

	@Column(name = "codigo_gerencia", nullable = false, updatable = false, insertable = false)
	private Long codigoGerencia;

	@Column(name = "ano_emissao", nullable = false, updatable = false, insertable = false)
	private Integer anoEmissao;

	@Column(name = "mes_emissao", nullable = false, updatable = false, insertable = false)
	private Integer mesEmissao;

	@Column(name = "valor_bruto", nullable = false, updatable = false, insertable = false)
	private BigDecimal valorBruto;

	@Column(name = "tipo_faturamento", nullable = false, updatable = false, insertable = false)
	private Integer tipoFaturamento;

	public VwCustosFaturamentoRealizado() {
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

	public Integer getAnoEmissao() {
		return anoEmissao;
	}

	public void setAnoEmissao(Integer anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	public Integer getMesEmissao() {
		return mesEmissao;
	}

	public void setMesEmissao(Integer mesEmissao) {
		this.mesEmissao = mesEmissao;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
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
		if (!(o instanceof VwCustosFaturamentoRealizado)) return false;
		VwCustosFaturamentoRealizado that = (VwCustosFaturamentoRealizado) o;
		return Objects.equals(getCodigoUnidade(), that.getCodigoUnidade()) &&
			Objects.equals(getCodigoMatriz(), that.getCodigoMatriz()) &&
			Objects.equals(getCodigoCentroFinanceiro(), that.getCodigoCentroFinanceiro()) &&
			Objects.equals(getCodigoGerencia(), that.getCodigoGerencia()) &&
			Objects.equals(getAnoEmissao(), that.getAnoEmissao()) &&
			Objects.equals(getMesEmissao(), that.getMesEmissao()) &&
			Objects.equals(getValorBruto(), that.getValorBruto()) &&
			Objects.equals(getTipoFaturamento(), that.getTipoFaturamento());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigoUnidade(), getCodigoMatriz(), getCodigoCentroFinanceiro(), getCodigoGerencia(), getAnoEmissao(), getMesEmissao(), getValorBruto(), getTipoFaturamento());
	}
}
