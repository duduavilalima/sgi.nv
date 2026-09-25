package br.com.araujoabreu.sgi.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vw_custos_receita_prevista")
public class VwCustosReceitaPrevista implements Serializable {

	private static final long serialVersionUID = -3891331752330156533L;
	@Id
	@Column(name = "codigo_unidade", nullable = false, updatable = false, insertable = false)
	private Long codigoUnidade;

	@Column(name = "codigo_matriz", nullable = false, updatable = false, insertable = false)
	private Long codigoMatriz;

	@Column(name = "codigo_centro_financeiro", nullable = false, updatable = false, insertable = false)
	private Long codigoCentroFinanceiro;

	@Column(name = "codigo_gerencia", nullable = false, updatable = false, insertable = false)
	private Long codigoGerencia;

	@Column(name = "ano_vencimento", nullable = false, updatable = false, insertable = false)
	private Integer anoVencimento;

	@Column(name = "mes_vencimento", nullable = false, updatable = false, insertable = false)
	private Integer mesVenciemento;

	@Column(name = "valor_liquido", nullable = false, updatable = false, insertable = false)
	private BigDecimal valorLiquido;

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

	public VwCustosReceitaPrevista() {
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

	public Integer getAnoVencimento() {
		return anoVencimento;
	}

	public void setAnoVencimento(Integer anoVencimento) {
		this.anoVencimento = anoVencimento;
	}

	public Integer getMesVenciemento() {
		return mesVenciemento;
	}

	public void setMesVenciemento(Integer mesVenciemento) {
		this.mesVenciemento = mesVenciemento;
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
		if (!(o instanceof VwCustosReceitaPrevista)) return false;
		VwCustosReceitaPrevista that = (VwCustosReceitaPrevista) o;
		return Objects.equals(getCodigoUnidade(), that.getCodigoUnidade()) &&
			Objects.equals(getCodigoMatriz(), that.getCodigoMatriz()) &&
			Objects.equals(getCodigoCentroFinanceiro(), that.getCodigoCentroFinanceiro()) &&
			Objects.equals(getCodigoGerencia(), that.getCodigoGerencia()) &&
			Objects.equals(getAnoVencimento(), that.getAnoVencimento()) &&
			Objects.equals(getMesVenciemento(), that.getMesVenciemento()) &&
			Objects.equals(getValorLiquido(), that.getValorLiquido()) &&
			Objects.equals(getTipoFaturamento(), that.getTipoFaturamento());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigoUnidade(), getCodigoMatriz(), getCodigoCentroFinanceiro(), getCodigoGerencia(), getAnoVencimento(), getMesVenciemento(), getValorLiquido(), getTipoFaturamento());
	}
}
