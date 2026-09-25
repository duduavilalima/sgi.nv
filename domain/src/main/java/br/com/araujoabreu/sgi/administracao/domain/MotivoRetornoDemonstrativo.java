package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.fundoFixo.domain.Demonstrativo;
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
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_DEMONSTRATIVO")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_DEMONSTRATIVO", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_DEMONSTRATIVO", allocationSize = 1)
public class MotivoRetornoDemonstrativo extends BaseEntity implements Serializable, Comparable<MotivoRetornoDemonstrativo> {

	private static final long serialVersionUID = -7402028743035167549L;
	private MotivoRetornoDocumento motivoRetornoDocumento;
	private Demonstrativo demonstrativo;

	public MotivoRetornoDemonstrativo() {

	}

	public MotivoRetornoDemonstrativo(MotivoRetornoDocumento motivoRetornoDocumento, Demonstrativo demonstrativo) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.demonstrativo = demonstrativo;
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
	@JoinColumn(name = "CODIGO_DEMONSTRATIVO")
	@NotNull
	public Demonstrativo getDemonstrativo() {
		return demonstrativo;
	}

	public void setDemonstrativo(Demonstrativo demonstrativo) {
		this.demonstrativo = demonstrativo;
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
		if (!(MotivoRetornoDemonstrativo.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MotivoRetornoDemonstrativo other = (MotivoRetornoDemonstrativo) obj;
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
		return "MotivoRetornoDemonstrativo [codigo=" + getCodigo() + ", motivoRetornoDocumento=" + motivoRetornoDocumento + ", demonstrativo=" + demonstrativo + "]";
	}

	@Override
	public int compareTo(MotivoRetornoDemonstrativo o) {
		return motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
	}

}
