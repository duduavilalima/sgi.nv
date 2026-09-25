package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "TIPO_ITEM_COMPRA")
public class TipoItemCompra extends BaseEntity {

	private static final long serialVersionUID = 5347567143720814808L;
	private String nome;
	private Boolean servico;

	public TipoItemCompra() {
	}

	@Column(name = "NOME", insertable = false, updatable = false, nullable = false)
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "SERVICO", insertable = false, updatable = false, nullable = false)
	@NotNull
	public Boolean getServico() {
		return servico;
	}

	public void setServico(Boolean servico) {
		this.servico = servico;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TipoItemCompra)) return false;
		if (!super.equals(o)) return false;
		TipoItemCompra that = (TipoItemCompra) o;
		return Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getServico(), that.getServico());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getServico());
	}
}
