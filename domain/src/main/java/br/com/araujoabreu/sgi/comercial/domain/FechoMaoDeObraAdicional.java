package br.com.araujoabreu.sgi.comercial.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

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
@Table(name = "FECHO_MAO_OBRA_ADICIONAL")
@SequenceGenerator(name="SEQ_FECHO_MAO_OBRA_ADICIONAL", sequenceName="SEQ_FECHO_MAO_OBRA_ADICIONAL", allocationSize=1)
public class FechoMaoDeObraAdicional extends BaseEntity {

	private static final long serialVersionUID = -6677054041915134937L;
	private FechoMaoDeObra fechoMaoDeObra;
	private CategoriaMaoDeObraAdicional categoriaMaoDeObraAdicional;
	private Long codigoCategoriaMaoDeObraAdicional;
	private BigDecimal valorUnitario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FECHO_MAO_OBRA", nullable = false)
	@NotNull
	public FechoMaoDeObra getFechoMaoDeObra() {
		return fechoMaoDeObra;
	}

	public void setFechoMaoDeObra(FechoMaoDeObra fechoMaoDeObra) {
		this.fechoMaoDeObra = fechoMaoDeObra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CATEGORIA_MAO_OBRA_ADICIONAL", nullable = false)
	@NotNull
	public CategoriaMaoDeObraAdicional getCategoriaMaoDeObraAdicional() {
		return categoriaMaoDeObraAdicional;
	}

	public void setCategoriaMaoDeObraAdicional(CategoriaMaoDeObraAdicional categoriaMaoDeObraAdicional) {
		this.categoriaMaoDeObraAdicional = categoriaMaoDeObraAdicional;
	}

	@Column(name = "VALOR_UNITARIO", nullable = false)
	@NotNull
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Transient
	public Long getCodigoCategoriaMaoDeObraAdicional() {
		return codigoCategoriaMaoDeObraAdicional;
	}

	public void setCodigoCategoriaMaoDeObraAdicional(Long codigoCategoriaMaoDeObraAdicional) {
		this.codigoCategoriaMaoDeObraAdicional = codigoCategoriaMaoDeObraAdicional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaMaoDeObraAdicional == null) ? 0 : categoriaMaoDeObraAdicional.hashCode());
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((fechoMaoDeObra == null) ? 0 : fechoMaoDeObra.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (!(FechoMaoDeObraAdicional.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}

		FechoMaoDeObraAdicional other = (FechoMaoDeObraAdicional) obj;
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
		return "FechoMaoDeObraAdicional [codigo=" + getCodigo() + ", fecho=" + fechoMaoDeObra
			+ ", categoriaMaoDeObraAdicional="
			+ categoriaMaoDeObraAdicional + ", valorUnitario="
			+ valorUnitario + "]";
	}

	@Override
	public FechoMaoDeObraAdicional clone() {
		FechoMaoDeObraAdicional fechoMaoDeObraAdicional = new FechoMaoDeObraAdicional();
		fechoMaoDeObraAdicional.setFechoMaoDeObra(getFechoMaoDeObra());
		fechoMaoDeObraAdicional.setCategoriaMaoDeObraAdicional(getCategoriaMaoDeObraAdicional());
		fechoMaoDeObraAdicional.setCodigoCategoriaMaoDeObraAdicional(getCodigoCategoriaMaoDeObraAdicional());
		fechoMaoDeObraAdicional.setValorUnitario(getValorUnitario());
		return fechoMaoDeObraAdicional;
	}
}
