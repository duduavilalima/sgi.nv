package br.com.araujoabreu.sgi.compras.domain;

import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RATEIO_NOTA_FISCAL")
@SequenceGenerator(name="SEQ_RATEIO_NOTA_FISCAL", sequenceName="SEQ_RATEIO_NOTA_FISCAL", allocationSize = 1)
public class RateioNotaFiscal extends Rateio {

	private static final long serialVersionUID = 4667121943244893480L;
	private ItemNotaFiscal itemNotaFiscal;
	private Long codigoCentroFinanceiro;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_ITEM_NOTA_FISCAL", nullable = false)
	public ItemNotaFiscal getItemNotaFiscal() {
		return itemNotaFiscal;
	}

	public void setItemNotaFiscal(ItemNotaFiscal itemNotaFiscal) {
		this.itemNotaFiscal = itemNotaFiscal;
	}

	@Column(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public Long getCodigoCentroFinanceiro() {
		return codigoCentroFinanceiro;
	}

	public void setCodigoCentroFinanceiro(Long codigoCentroFinanceiro) {
		this.codigoCentroFinanceiro = codigoCentroFinanceiro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
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
		if (!(RateioNotaFiscal.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		RateioNotaFiscal other = (RateioNotaFiscal) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}
