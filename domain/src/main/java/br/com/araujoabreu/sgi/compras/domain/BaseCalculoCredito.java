package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "BASE_CALCULO_CREDITO")
@SequenceGenerator(name="SEQ_BASE_CALCULO_CREDITO", sequenceName="SEQ_BASE_CALCULO_CREDITO", allocationSize=1)
public class BaseCalculoCredito extends BaseEntity {

	private static final long serialVersionUID = 3013791273807359020L;
	private String nome;
	private String sigla;

	@Column(name = "NOME", insertable = false, updatable = false)
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "SIGLA", insertable = false, updatable = false)
	@Length(max = 12)
	@NotNull
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BaseCalculoCredito)) return false;
		if (!super.equals(o)) return false;
		BaseCalculoCredito that = (BaseCalculoCredito) o;
		return Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getSigla(), that.getSigla());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getSigla());
	}
}
