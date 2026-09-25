package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.fasolti.framework.domain.BaseEntity;

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
@Table(name = "FATURAMENTO_ALIQUOTA")
@SequenceGenerator(name="SEQ_FATURAMENTO_ALIQUOTA", sequenceName="SEQ_FATURAMENTO_ALIQUOTA", allocationSize=1)
public class FaturamentoAliquota extends BaseEntity {

	private static final long serialVersionUID = 8019004567404790832L;
	private BigDecimal baseCalculo = new BigDecimal("0");
	private BigDecimal valor;
	private Aliquota aliquota;
	private Faturamento faturamento;

	private String uf;
	private Long codigoTributo;
	private Long codigoCategoriaTributo;
	private BigDecimal totalTributos = new BigDecimal("0");
	private BigDecimal percentualTributo;

	public FaturamentoAliquota() {
	}

	@Column(name = "BASE_CALCULO", nullable = false, precision = 14, scale = 2)
	public BigDecimal getBaseCalculo() {
		return this.baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}


	@Column(name = "VALOR", nullable = false, precision = 14, scale = 2)
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}


	//bi-directional many-to-one association to Aliquota
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ALIQUOTA")
	public Aliquota getAliquota() {
		return this.aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}


	//bi-directional many-to-one association to Faturamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FATURAMENTO", nullable = false)
	public Faturamento getFaturamento() {
		return this.faturamento;
	}

	public void setFaturamento(Faturamento faturamento) {
		this.faturamento = faturamento;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FaturamentoAliquota)) return false;
		if (!super.equals(o)) return false;

		FaturamentoAliquota that = (FaturamentoAliquota) o;

		if (!getBaseCalculo().equals(that.getBaseCalculo())) return false;
		if (!getValor().equals(that.getValor())) return false;
		if (!getUf().equals(that.getUf())) return false;
		if (!getCodigoTributo().equals(that.getCodigoTributo())) return false;
		if (!getCodigoCategoriaTributo().equals(that.getCodigoCategoriaTributo())) return false;
		if (!getTotalTributos().equals(that.getTotalTributos())) return false;
		return getPercentualTributo().equals(that.getPercentualTributo());

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + getBaseCalculo().hashCode();
		result = 31 * result + getValor().hashCode();
		result = 31 * result + getUf().hashCode();
		result = 31 * result + getCodigoTributo().hashCode();
		result = 31 * result + getCodigoCategoriaTributo().hashCode();
		result = 31 * result + getTotalTributos().hashCode();
		result = 31 * result + getPercentualTributo().hashCode();
		return result;
	}
}
