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
@Table(name = "OPERADORA")
public class Operadora extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = 8541094434307844451L;
	private String nome;

	public Operadora() {
	}

	@Override
	@Column(name = "NOME", nullable = false)
	@NotNull
	@Length(max = 50)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Operadora [nome=" + nome + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Operadora)) return false;
		if (!super.equals(o)) return false;
		Operadora operadora = (Operadora) o;
		return Objects.equals(getNome(), operadora.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome());
	}
}
