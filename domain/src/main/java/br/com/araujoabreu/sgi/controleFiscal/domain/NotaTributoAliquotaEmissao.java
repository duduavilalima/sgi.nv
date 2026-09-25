package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.fasolti.framework.domain.BaseEntity;
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

@Entity
@Table(name = "NOTA_TRIBUTO_ALIQUOTA_EMISSAO")
@SequenceGenerator(name = "SEQ_NOTA_TRIBUTO_ALIQUOTA_EMISSAO", sequenceName = "SEQ_NOTA_TRIBUTO_ALIQUOTA_EMISSAO", allocationSize = 1)
public class NotaTributoAliquotaEmissao extends BaseEntity {

	private static final long serialVersionUID = 4129482941619926148L;
	private Nota nota;
	private Aliquota aliquota;
	private BigDecimal baseCalculo;
	private BigDecimal percentual;
	private BigDecimal valor;

	public NotaTributoAliquotaEmissao() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA")
	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ALIQUOTA")
	public Aliquota getAliquota() {
		return aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}

	@Column(name = "BASE_CALCULO")
	@NotNull
	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	@Column(name = "PERCENTUAL")
	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	@Column(name = "VALOR")
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		NotaTributoAliquotaEmissao that = (NotaTributoAliquotaEmissao) o;
		return Objects.equals(nota, that.nota) &&
			Objects.equals(aliquota, that.aliquota) &&
			Objects.equals(baseCalculo, that.baseCalculo) &&
			Objects.equals(percentual, that.percentual) &&
			Objects.equals(valor, that.valor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nota, aliquota, baseCalculo, percentual, valor);
	}

	@Override
	public String toString() {
		return "NotaTributoAliquotaEmissao{" +
			"nota=" + nota +
			", aliquota=" + aliquota +
			", baseCalculo=" + baseCalculo +
			", percentual=" + percentual +
			", valor=" + valor +
			'}';
	}
}
