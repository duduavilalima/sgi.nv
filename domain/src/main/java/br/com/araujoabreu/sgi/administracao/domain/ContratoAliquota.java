package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "CONTRATO_ALIQUOTA")
@SequenceGenerator(	name = "SEQ_CONTRATO_ALIQUOTA",	sequenceName = "SEQ_CONTRATO_ALIQUOTA",	allocationSize = 1)
public class ContratoAliquota extends AuditEntity {

	private static final long serialVersionUID = 5101220133663281796L;
	private Contrato contrato;
	private Aliquota aliquota;
	private BigDecimal percentualBaseCalculo; //percentual_base_calculo
	private Long codigoTributo;
	private Long codigoCategoriaTributo;
	private BigDecimal totalTributos;
	private BigDecimal percentualTributo;
	private String uf;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTRATO")
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
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

	@Transient
	public Long getCodigoCategoriaTributo() {
		return codigoCategoriaTributo;
	}

	public void setCodigoCategoriaTributo(Long codigoCategoriaTributo) {
		this.codigoCategoriaTributo = codigoCategoriaTributo;
	}

	@Transient
	public Long getCodigoTributo() {
		return codigoTributo;
	}

	public void setCodigoTributo(Long codigoTributo) {
		this.codigoTributo = codigoTributo;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContratoAliquota)) return false;
		if (!super.equals(o)) return false;
		ContratoAliquota that = (ContratoAliquota) o;
		return Objects.equals(getPercentualBaseCalculo(), that.getPercentualBaseCalculo()) &&
			Objects.equals(getCodigoTributo(), that.getCodigoTributo()) &&
			Objects.equals(getCodigoCategoriaTributo(), that.getCodigoCategoriaTributo()) &&
			Objects.equals(getTotalTributos(), that.getTotalTributos()) &&
			Objects.equals(getPercentualTributo(), that.getPercentualTributo()) &&
			Objects.equals(getUf(), that.getUf());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getPercentualBaseCalculo(), getCodigoTributo(), getCodigoCategoriaTributo(), getTotalTributos(), getPercentualTributo(), getUf());
	}

	@Override
	public String toString() {
		return "ContratoAliquota{" +
			"percentualBaseCalculo=" + percentualBaseCalculo +
			", codigoTributo=" + codigoTributo +
			", codigoCategoriaTributo=" + codigoCategoriaTributo +
			", totalTributos=" + totalTributos +
			", percentualTributo=" + percentualTributo +
			", uf='" + uf + '\'' +
			'}';
	}
}
