package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
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
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_FATURAMENTO")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_FATURAMENTO", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_FATURAMENTO", allocationSize = 1)
public class MotivoRetornoDocumentoFaturamento extends BaseEntity implements Comparable<MotivoRetornoDocumentoFaturamento>, Serializable {

	private static final long serialVersionUID = 7811819518932090896L;
	private Faturamento faturamento;
	private MotivoRetornoDocumento motivoRetornoDocumento;

	public MotivoRetornoDocumentoFaturamento() {
	}

	public MotivoRetornoDocumentoFaturamento(Faturamento faturamento, MotivoRetornoDocumento motivoRetornoDocumento) {
		this.faturamento = faturamento;
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_faturamento", nullable = false)
	public Faturamento getFaturamento() {
		return this.faturamento;
	}

	public void setFaturamento(Faturamento faturamento) {
		this.faturamento = faturamento;
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
	public int compareTo(MotivoRetornoDocumentoFaturamento o) {
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

		if (!(obj instanceof MotivoRetornoDocumentoFaturamento)) {
			return false;
		}

		MotivoRetornoDocumentoFaturamento other = (MotivoRetornoDocumentoFaturamento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MotivoRetornoDocumentoFaturamento [codigo=" + getCodigo() + ", faturamento=" + faturamento + ", motivoRetornoDocumento=" + motivoRetornoDocumento + "]";
	}

}
