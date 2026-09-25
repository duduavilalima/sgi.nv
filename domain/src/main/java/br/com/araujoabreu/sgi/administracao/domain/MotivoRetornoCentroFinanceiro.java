package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_CF")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_CF", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_CF", allocationSize = 1)
public class MotivoRetornoCentroFinanceiro extends BaseEntity implements Serializable, Comparable<MotivoRetornoCentroFinanceiro> {

	private static final long serialVersionUID = 7485408898210469994L;
	private MotivoRetornoDocumento motivoRetornoDocumento;
	private CentroFinanceiro centroFinanceiro;

	public MotivoRetornoCentroFinanceiro() {
	}

	public MotivoRetornoCentroFinanceiro(MotivoRetornoDocumento motivoRetornoDocumento, CentroFinanceiro centroFinanceiro) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.centroFinanceiro = centroFinanceiro;
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
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
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
		if (!(MotivoRetornoCentroFinanceiro.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MotivoRetornoCentroFinanceiro other = (MotivoRetornoCentroFinanceiro) obj;
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
		return "MotivoRetornoCentroFinanceiro [codigo=" + getCodigo() + ", motivoRetornoDocumento=" + motivoRetornoDocumento + ", centroFinanceiro=" + centroFinanceiro + "]";
	}

	@Override
	public int compareTo(MotivoRetornoCentroFinanceiro o) {
		return motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
	}

}
