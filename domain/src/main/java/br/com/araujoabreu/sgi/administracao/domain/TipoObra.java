package br.com.araujoabreu.sgi.administracao.domain;

// Generated 02/06/2014 18:05:26 by Hibernate Tools 3.4.0.CR1

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "TIPO_OBRA")
@SequenceGenerator(name = "SEQ_TIPO_OBRA", sequenceName = "SEQ_TIPO_OBRA", allocationSize = 1)
public class TipoObra extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = -6134087645830202733L;
	private String nome;

	public TipoObra() {
	}


	@Column(name = "nome", unique = true, nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TipoObra)) return false;
		if (!super.equals(o)) return false;
		TipoObra tipoObra = (TipoObra) o;
		return Objects.equals(getNome(), tipoObra.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome());
	}
}
