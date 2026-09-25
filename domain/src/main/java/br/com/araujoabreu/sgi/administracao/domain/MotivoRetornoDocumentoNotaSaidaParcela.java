package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
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
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_NOTA_SAIDA_PARCELA")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_NOTA_SAIDA_PARCELA", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_NOTA_SAIDA_PARCELA", allocationSize = 1)
public class MotivoRetornoDocumentoNotaSaidaParcela extends BaseEntity implements Comparable<MotivoRetornoDocumentoNotaSaidaParcela>, Serializable {

	private static final long serialVersionUID = 4505544427729018715L;
	private NotaSaidaParcela notaSaidaParcela;
	private MotivoRetornoDocumento motivoRetornoDocumento;

	public MotivoRetornoDocumentoNotaSaidaParcela() {
	}

	public MotivoRetornoDocumentoNotaSaidaParcela(NotaSaidaParcela notaSaidaParcela, MotivoRetornoDocumento motivoRetornoDocumento) {
		this.notaSaidaParcela = notaSaidaParcela;
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota_saida_parcela", nullable = false)
	public NotaSaidaParcela getNotaSaidaParcela() {
		return notaSaidaParcela;
	}

	public void setNotaSaidaParcela(NotaSaidaParcela notaSaidaParcela) {
		this.notaSaidaParcela = notaSaidaParcela;
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
	public int compareTo(MotivoRetornoDocumentoNotaSaidaParcela o) {
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

		if (!(obj instanceof MotivoRetornoDocumentoNotaSaidaParcela)) {
			return false;
		}

		MotivoRetornoDocumentoNotaSaidaParcela other = (MotivoRetornoDocumentoNotaSaidaParcela) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

}
