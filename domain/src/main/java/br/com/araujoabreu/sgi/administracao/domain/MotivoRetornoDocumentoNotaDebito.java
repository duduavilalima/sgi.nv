package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaDebito;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_NOTA_DEBITO")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_NOTA_DEBITO", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_NOTA_DEBITO", allocationSize = 1)
public class MotivoRetornoDocumentoNotaDebito extends BaseEntity implements Comparable<MotivoRetornoDocumentoNotaDebito>, Serializable {

	private static final long serialVersionUID = 4505544427729018715L;
	private NotaDebito notaDebito;
	private MotivoRetornoDocumento motivoRetornoDocumento;

	public MotivoRetornoDocumentoNotaDebito() {
	}

	public MotivoRetornoDocumentoNotaDebito(NotaDebito notaDebito, MotivoRetornoDocumento motivoRetornoDocumento) {
		this.notaDebito = notaDebito;
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota_debito", nullable = false)
	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "codigo_motivo_retorno_documento", nullable = false)
	public MotivoRetornoDocumento getMotivoRetornoDocumento() {
		return this.motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumento motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@Override
	public int compareTo(MotivoRetornoDocumentoNotaDebito o) {
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

		if (!(obj instanceof MotivoRetornoDocumentoNotaDebito)) {
			return false;
		}

		MotivoRetornoDocumentoNotaDebito other = (MotivoRetornoDocumentoNotaDebito) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

}
