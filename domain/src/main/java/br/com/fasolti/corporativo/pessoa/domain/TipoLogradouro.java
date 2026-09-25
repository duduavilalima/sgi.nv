package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "TIPO_LOGRADOURO")
public class TipoLogradouro extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = 2915765711983866858L;
	private String nome;

	public TipoLogradouro() {
		super();
	}

	@Override
	@Column(name = "NOME", length = 60)
	@Length(max = 60)
	@NotNull
	public String getNome() {
		return nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "TipoLogradouro [nome=" + nome + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TipoLogradouro)) return false;
		if (!super.equals(o)) return false;
		TipoLogradouro that = (TipoLogradouro) o;
		return Objects.equals(getNome(), that.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome());
	}
}
