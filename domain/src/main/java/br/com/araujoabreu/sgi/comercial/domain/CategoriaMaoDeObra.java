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
@Table(name = "CATEGORIA_MAO_OBRA")
@SequenceGenerator(name="SEQ_CATEGORIA_MAO_OBRA", sequenceName="SEQ_CATEGORIA_MAO_OBRA", allocationSize=1)
public class CategoriaMaoDeObra extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = 2794402018509258991L;
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
		if (!(CategoriaMaoDeObra.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		CategoriaMaoDeObra other = (CategoriaMaoDeObra) obj;
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
		return "ClassificacaoCliente [codigo=" + getCodigo() + ", nome=" + nome + "]";
	}


	public enum TipoCategoriaMaoDeObra {
		SALARIO("Salário"),
		OUTROS("Outros");

		private final String descricao;

		private TipoCategoriaMaoDeObra(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

}
