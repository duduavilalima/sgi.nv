package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_ETAPA")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_ETAPA", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_ETAPA", allocationSize = 1)
public class MotivoRetornoDocumentoEtapa extends BaseEntity implements Comparable<MotivoRetornoDocumentoEtapa>, Serializable {

	private static final long serialVersionUID = -3246768010867577773L;
	private FaturamentoEtapa faturamentoEtapa;
	private MotivoRetornoDocumento motivoRetornoDocumento;

	public MotivoRetornoDocumentoEtapa() {

	}

	public MotivoRetornoDocumentoEtapa(MotivoRetornoDocumento motivoRetornoDocumento, FaturamentoEtapa faturamentoEtapa) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.faturamentoEtapa = faturamentoEtapa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_etapa", nullable = false)
	public FaturamentoEtapa getFaturamentoEtapa() {
		return this.faturamentoEtapa;
	}

	public void setFaturamentoEtapa(FaturamentoEtapa faturamentoEtapa) {
		this.faturamentoEtapa = faturamentoEtapa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_motivo_retorno_documento", nullable = false)
	public MotivoRetornoDocumento getMotivoRetornoDocumento() {
		return this.motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumento motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@Override
	public int compareTo(MotivoRetornoDocumentoEtapa o) {
		return this.motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
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

		if (!(obj instanceof MotivoRetornoDocumentoEtapa)) {
			return false;
		}

		MotivoRetornoDocumentoEtapa other = (MotivoRetornoDocumentoEtapa) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "MotivoRetornoDocumentoEtapa [codigo=" + getCodigo() + ", faturamentoEtapa=" + faturamentoEtapa + ", motivoRetornoDocumento=" + motivoRetornoDocumento + "]";
	}

}
