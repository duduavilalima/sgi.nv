package br.com.araujoabreu.sgi.comercial.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "CATEGORIA_DESPESA_GERAL")
@SequenceGenerator(name="SEQ_CATEGORIA_DESPESA_GERAL", sequenceName="SEQ_CATEGORIA_DESPESA_GERAL", allocationSize=1)
public class CategoriaDespesaGeral extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = -7353336791914368180L;
	private String nome;

	@Column(name = "NOME", nullable = false)
	@NotNull
	public String getNome() {
		return nome;
	}

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
		if (!(CategoriaDespesaGeral.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		CategoriaDespesaGeral other = (CategoriaDespesaGeral) obj;
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
		return "CategoriaDespesaGeral [codigo=" + getCodigo() + ", nome=" + nome + "]";
	}

}
