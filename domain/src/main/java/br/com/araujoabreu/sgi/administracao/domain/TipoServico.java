package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_SERVICO")
@SequenceGenerator(name = "SEQ_TIPO_SERVICO", sequenceName = "SEQ_TIPO_SERVICO", allocationSize = 1)
public class TipoServico extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = -2683810039894664494L;
	private String sigla;
	private String nome;

	public TipoServico() {
	}

	@Column(name = "SIGLA", insertable = false, updatable = false, nullable = false)
	@Length(max = 30)
	@NotNull
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "NOME", insertable = false, updatable = false, nullable = false)
	@Length(max = 1000)
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
		if (!(TipoServico.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		TipoServico other = (TipoServico) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}
}
