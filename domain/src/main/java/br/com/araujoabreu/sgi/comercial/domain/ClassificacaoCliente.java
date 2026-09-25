package br.com.araujoabreu.sgi.comercial.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "CLASSIFICACAO_CLIENTE")
@SequenceGenerator(name="SEQ_CLASSIFICACAO_CLIENTE", sequenceName="SEQ_CLASSIFICACAO_CLIENTE", allocationSize=1)
public class ClassificacaoCliente extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = -6025492953791673548L;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ClassificacaoCliente)) return false;
		if (!super.equals(o)) return false;
		ClassificacaoCliente that = (ClassificacaoCliente) o;
		return Objects.equals(getNome(), that.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome());
	}

	@Override
	public String toString() {
		return "ClassificacaoCliente{" +
			"nome='" + nome + '\'' +
			'}';
	}
}
