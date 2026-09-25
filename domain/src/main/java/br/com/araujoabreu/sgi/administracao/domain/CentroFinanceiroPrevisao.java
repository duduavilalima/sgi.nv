package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Previsão de faturamento (contratual e extra contratual) dos centros financeiros.
 */
@Entity
@Table(name = "CENTRO_FINANCEIRO_PREVISAO")
@SequenceGenerator(name = "SEQ_CENTRO_FINANCEIRO_PREVISAO",	sequenceName = "SEQ_CENTRO_FINANCEIRO_PREVISAO",	allocationSize = 1)
public class CentroFinanceiroPrevisao extends AuditEntity implements DomainObject, Comparable<CentroFinanceiroPrevisao> {

	private static final long serialVersionUID = 4262692737047368530L;
	private CentroFinanceiro centroFinanceiro;
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

	public CentroFinanceiroPrevisao() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@NotNull
	@Length(max = 6)
	@Column(name = "COMPETENCIA", nullable = false, length = 6)
	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	@NotNull
	@Column(name = "VALOR_CONTRATUAL", nullable = false, precision = 2)
	public BigDecimal getValorContratual() {
		return valorContratual;
	}

	public void setValorContratual(BigDecimal valorContratual) {
		this.valorContratual = valorContratual;
	}

	@NotNull
	@Column(name = "VALOR_EXTRA", nullable = false, precision = 2)
	public BigDecimal getValorExtra() {
		return valorExtra;
	}

	public void setValorExtra(BigDecimal valorExtra) {
		this.valorExtra = valorExtra;
	}

	@Override
	public int compareTo(CentroFinanceiroPrevisao o) {
		return o.getCompetencia().compareTo(this.getCompetencia());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CentroFinanceiroPrevisao)) return false;
		if (!super.equals(o)) return false;
		CentroFinanceiroPrevisao that = (CentroFinanceiroPrevisao) o;
		return Objects.equals(getCentroFinanceiro(), that.getCentroFinanceiro()) &&
			Objects.equals(getCompetencia(), that.getCompetencia()) &&
			Objects.equals(getValorContratual(), that.getValorContratual()) &&
			Objects.equals(getValorExtra(), that.getValorExtra());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCentroFinanceiro(), getCompetencia(), getValorContratual(), getValorExtra());
	}
}
