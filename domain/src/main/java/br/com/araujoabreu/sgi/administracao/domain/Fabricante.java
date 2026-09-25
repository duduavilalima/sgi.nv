package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FABRICANTE")
@SequenceGenerator(name = "SEQ_FABRICANTE", sequenceName = "SEQ_FABRICANTE", allocationSize = 1)
public class Fabricante extends AuditEntity implements Dominio {

	private static final long serialVersionUID = -1170514042638850332L;
	private String nome;

	public Fabricante() {
	}

	public Fabricante(Long codigo, String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	@Override
	@Column(name = "nome", unique = true, nullable = false, length = 60)
	@NotNull
	@Length(max = 60)
	public String getNome() {
		return this.nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		if (!(Fabricante.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Fabricante other = (Fabricante) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (nome == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!nome.equals(other.getNome())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Fabricante [codigo=" + getCodigo() + ", nome=" + getNome() + "]";
	}

}
