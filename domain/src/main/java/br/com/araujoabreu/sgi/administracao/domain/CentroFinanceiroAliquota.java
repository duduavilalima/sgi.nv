package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Table(name = "CENTRO_FINANCEIRO_ALIQUOTA")
@SequenceGenerator(name = "SEQ_CENTRO_FINANCEIRO_ALIQUOTA",	sequenceName = "SEQ_CENTRO_FINANCEIRO_ALIQUOTA", allocationSize = 1)
public class CentroFinanceiroAliquota extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -6913428949409705020L;

	private CentroFinanceiro centroFinanceiro;
	private Aliquota aliquota;
	private String uf;
	private Long codigoTributo;
	private Long codigoCategoriaTributo;
	private BigDecimal percentualBaseCalculo; //percentual_base_calculo
	private BigDecimal totalTributos;
	private BigDecimal percentualTributo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ALIQUOTA")
	public Aliquota getAliquota() {
		return aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}

	@Column(name = "PERCENTUAL_BASE_CALCULO", nullable = false)
	public BigDecimal getPercentualBaseCalculo() {
		return percentualBaseCalculo;
	}

	public void setPercentualBaseCalculo(BigDecimal percentualBaseCalculo) {
		this.percentualBaseCalculo = percentualBaseCalculo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof CentroFinanceiroAliquota)) {
			return false;
		}

		CentroFinanceiroAliquota other = (CentroFinanceiroAliquota) obj;

		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ContratoAliquota [codigo=" + getCodigo() + ", centroFinanceiro=" + centroFinanceiro
			+ ", aliquota=" + aliquota + ", percentualBaseCalculo="
			+ percentualBaseCalculo + "]";
	}

	@Transient
	public Long getCodigoTributo() {
		return codigoTributo;
	}

	public void setCodigoTributo(Long codigoTributo) {
		this.codigoTributo = codigoTributo;
	}

	@Transient
	public Long getCodigoCategoriaTributo() {
		return codigoCategoriaTributo;
	}

	public void setCodigoCategoriaTributo(Long codigoCategoriaTributo) {
		this.codigoCategoriaTributo = codigoCategoriaTributo;
	}

	@Transient
	public BigDecimal getTotalTributos() {
		return totalTributos;
	}

	public void setTotalTributos(BigDecimal totalTributos) {
		this.totalTributos = totalTributos;
	}

	@Transient
	public BigDecimal getPercentualTributo() {
		return percentualTributo;
	}

	public void setPercentualTributo(BigDecimal percentualTributo) {
		this.percentualTributo = percentualTributo;
	}

	@Transient
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
