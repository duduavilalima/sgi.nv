package br.com.araujoabreu.sgi.comercial.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "CATEGORIA_MAO_OBRA_ADICIONAL")
@SequenceGenerator(name="SEQ_CATEGORIA_MAO_OBRA_ADICIONAL", sequenceName="SEQ_CATEGORIA_MAO_OBRA_ADICIONAL", allocationSize=1)
public class CategoriaMaoDeObraAdicional extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = -6213106360669791877L;
	private String nome;

	private List<FechoMaoDeObraAdicional> listaFechoMaoDeObraAdicional = new ArrayList<>();

	@Column(name = "NOME", nullable = false)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "categoriaMaoDeObraAdicional")
	public List<FechoMaoDeObraAdicional> getListaFechoMaoDeObraAdicional() {
		return listaFechoMaoDeObraAdicional;
	}

	public void setListaFechoMaoDeObraAdicional(List<FechoMaoDeObraAdicional> listaFechoMaoDeObraAdicional) {
		this.listaFechoMaoDeObraAdicional = listaFechoMaoDeObraAdicional;
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

		if (!(CategoriaMaoDeObraAdicional.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}

		CategoriaMaoDeObraAdicional other = (CategoriaMaoDeObraAdicional) obj;
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

}
