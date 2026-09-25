package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.OrdemCompra;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_OC")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_OC", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_OC", allocationSize = 1)
public class MotivoRetornoOrdemCompra extends BaseEntity implements Comparable<MotivoRetornoOrdemCompra> {

	private static final long serialVersionUID = -2634844410032335782L;
	private MotivoRetornoDocumento motivoRetornoDocumento;
	private OrdemCompra ordemCompra;

	public MotivoRetornoOrdemCompra() {

	}

	public MotivoRetornoOrdemCompra(MotivoRetornoDocumento motivoRetornoDocumento, OrdemCompra ordemCompra) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.ordemCompra = ordemCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MOTIVO_RETORNO_DOCUMENTO")
	@NotNull
	public MotivoRetornoDocumento getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumento motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ORDEM_COMPRA")
	public OrdemCompra getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(OrdemCompra ordemCompra) {
		this.ordemCompra = ordemCompra;
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
		if (!(MotivoRetornoOrdemCompra.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MotivoRetornoOrdemCompra other = (MotivoRetornoOrdemCompra) obj;
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
		return "MotivoRetornoOrdemCompra [codigo=" + getCodigo() + ", motivoRetornoDocumento=" + motivoRetornoDocumento + ", ordemCompra=" + ordemCompra + "]";
	}

	@Override
	public int compareTo(MotivoRetornoOrdemCompra o) {
		return motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
	}

}
