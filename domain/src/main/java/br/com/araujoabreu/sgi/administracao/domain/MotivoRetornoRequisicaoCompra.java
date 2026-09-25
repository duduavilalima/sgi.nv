package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.RequisicaoCompra;
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
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_RC")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_RC", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_RC", allocationSize = 1)
public class MotivoRetornoRequisicaoCompra extends BaseEntity implements Comparable<MotivoRetornoRequisicaoCompra> {

	private static final long serialVersionUID = 6268077462164275319L;
	private MotivoRetornoDocumento motivoRetornoDocumento;
	private RequisicaoCompra requisicaoCompra;

	public MotivoRetornoRequisicaoCompra() {

	}

	public MotivoRetornoRequisicaoCompra(MotivoRetornoDocumento motivoRetornoDocumento, RequisicaoCompra requisicaoCompra) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.requisicaoCompra = requisicaoCompra;
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
	@JoinColumn(name = "CODIGO_REQUISICAO_COMPRA")
	public RequisicaoCompra getRequisicaoCompra() {
		return requisicaoCompra;
	}

	public void setRequisicaoCompra(RequisicaoCompra requisicaoCompra) {
		this.requisicaoCompra = requisicaoCompra;
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
		if (!(MotivoRetornoRequisicaoCompra.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MotivoRetornoRequisicaoCompra other = (MotivoRetornoRequisicaoCompra) obj;
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
		return "MotivoRetornoRequisicaoCompra [codigo=" + getCodigo() + ", motivoRetornoDocumento=" + motivoRetornoDocumento + ", requisicaoCompra="
			+ requisicaoCompra + "]";
	}

	@Override
	public int compareTo(MotivoRetornoRequisicaoCompra o) {
		return this.motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
	}

}
