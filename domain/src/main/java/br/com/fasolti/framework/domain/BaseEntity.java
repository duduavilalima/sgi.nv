package br.com.fasolti.framework.domain;

import br.com.fasolti.commons.domain.DomainObject;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class BaseEntity implements DomainObject {

	private static final long serialVersionUID = 7643030563973061177L;

	private Long codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODIGO", nullable = false)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	@Transient
	public Long getId() {
		return getCodigo();
	}

	@Override
	public void setId(Long id) {
		setCodigo(id);
	}

	@Transient
	public boolean isNew() {
		return null == getCodigo();
	}

	@Override
	public String toString() {
		return String.format("Entidade do tipo %s com codigo: %s", this.getClass().getName(), getCodigo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		BaseEntity other = (BaseEntity) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;

		return true;
	}
}
