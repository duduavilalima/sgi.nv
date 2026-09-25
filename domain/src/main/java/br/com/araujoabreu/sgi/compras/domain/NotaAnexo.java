package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
@Table(name = "NOTA_ANEXO")
@SequenceGenerator(name="SEQ_NOTA_ANEXO", sequenceName="SEQ_NOTA_ANEXO", allocationSize=1)
public class NotaAnexo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4343716926761134582L;
	private Nota nota;
	private Anexo anexo;

	public NotaAnexo() {
		super();
	}

	public NotaAnexo(Nota nota, Anexo anexo) {
		super();
		this.nota = nota;
		this.anexo = anexo;
	}

	public NotaAnexo(Long codigo) {
		super();
		setCodigo(codigo);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "codigo_nota", nullable = false)
	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "codigo_anexo", nullable = false)
	@Cascade(value = CascadeType.DELETE)
	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
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
		if (!(obj instanceof NotaAnexo)) {
			return false;
		}
		NotaAnexo other = (NotaAnexo) obj;
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
		return "NotaAnexo [codigo=" + getCodigo() + "]";
	}

}
