package br.com.fasolti.corporativo.security.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RECEPTOR")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Receptor extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -9004667615610425367L;
	private TipoReceptor tipo;

	public Receptor() {
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoReceptor getTipo() {
		return tipo;
	}

	public void setTipo(TipoReceptor tipo) {
		this.tipo = tipo;
	}

	@Transient
	public abstract String getChave();

	public enum TipoReceptor {
		PERFIL, USUARIO;

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "role";
				case 1:
					return "user";
				default:
					return "";
			}
		}
	}

}
