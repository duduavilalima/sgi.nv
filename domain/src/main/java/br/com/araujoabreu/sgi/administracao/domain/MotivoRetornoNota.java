package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.Nota;
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
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_NOTA")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_NOTA", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_NOTA", allocationSize = 1)
public class MotivoRetornoNota extends BaseEntity implements Comparable<MotivoRetornoNota> {

	private static final long serialVersionUID = 1492762150459670274L;
	private MotivoRetornoDocumento motivoRetornoDocumento;
	private Nota nota;

	public MotivoRetornoNota() {
		super();
	}

	public MotivoRetornoNota(MotivoRetornoDocumento motivoRetornoDocumento, Nota nota) {
		super();
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.nota = nota;
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
	@JoinColumn(name = "CODIGO_NOTA")
	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
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
		if (!(MotivoRetornoNota.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MotivoRetornoNota other = (MotivoRetornoNota) obj;
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
		return "MotivoRetornoNota [codigo=" + getCodigo() + ", motivoRetornoDocumento=" + motivoRetornoDocumento + ", nota=" + nota + "]";
	}

	@Override
	public int compareTo(MotivoRetornoNota o) {
		return motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
	}

}
