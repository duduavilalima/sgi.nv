package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "MOTIVO_RETORNO_MOTIVO_RETORNO_DOCUMENTO")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_MOTIVO_RETORNO_DOCUMENTO", sequenceName = "SEQ_MOTIVO_RETORNO_MOTIVO_RETORNO_DOCUMENTO", allocationSize = 1)
public class MotivoDocumento extends BaseEntity implements Serializable, Comparable<MotivoDocumento> {

	private static final long serialVersionUID = -3440382322735344823L;
	private MotivoRetornoDocumento motivoRetornoDocumento;
	private MotivoRetorno motivoRetorno;

	public MotivoDocumento() {

	}

	public MotivoDocumento(MotivoRetornoDocumento motivoRetornoDocumento, MotivoRetorno motivoRetorno) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.motivoRetorno = motivoRetorno;
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
	@JoinColumn(name = "CODIGO_MOTIVO_RETORNO")
	@NotNull
	public MotivoRetorno getMotivoRetorno() {
		return motivoRetorno;
	}

	public void setMotivoRetorno(MotivoRetorno motivoRetorno) {
		this.motivoRetorno = motivoRetorno;
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		MotivoDocumento other = (MotivoDocumento) obj;
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
		return "MotivoDocumento [codigo=" + getCodigo() + ", motivoRetornoDocumento=" + motivoRetornoDocumento + ", motivoRetorno=" + motivoRetorno + "]";
	}

	@Override
	public int compareTo(MotivoDocumento o) {
		if (this.motivoRetorno != null && o.getMotivoRetorno() != null) {
			return this.motivoRetorno.getMotivo().compareTo(o.getMotivoRetorno().getMotivo());
		}
		return 0;
	}
}
