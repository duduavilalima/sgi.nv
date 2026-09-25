package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "SUB_TIPO_SERVICO")
public class SubTipoServico extends BaseEntity {

	private static final long serialVersionUID = -3883930173041987254L;
	private TipoServico tipoServico;
	private String sigla;
	private String nome;

	public SubTipoServico() {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_SERVICO")
	@NotNull
	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SubTipoServico)) return false;
		if (!super.equals(o)) return false;
		SubTipoServico that = (SubTipoServico) o;
		return Objects.equals(getSigla(), that.getSigla()) &&
			Objects.equals(getNome(), that.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSigla(), getNome());
	}
}
