package br.com.araujoabreu.sgi.administracao.domain;


import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Table(name = "OBRA_CLIENTE")
@Access(AccessType.PROPERTY)
@SequenceGenerator(name = "SEQ_OBRA_CLIENTE", sequenceName = "SEQ_OBRA_CLIENTE", allocationSize = 1)
public class ObraCliente extends BaseEntity implements Comparable {

	private static final long serialVersionUID = -6214963053418232323L;
	private Pessoa pessoa;
	private Obra obra;
	private Boolean principal;
	private int percFaturamento;

	public ObraCliente() {

	}

	public ObraCliente(Pessoa pessoa, boolean principal) {
		this.pessoa = pessoa;
		this.principal = principal;
	}

	public ObraCliente(Pessoa pessoa, boolean principal, int percFaturamento) {
		this.pessoa = pessoa;
		this.principal = principal;
		this.percFaturamento = percFaturamento;
	}

	public ObraCliente(Long codigo, Pessoa pessoa, Obra obra, boolean principal, int percFaturamento) {
		setCodigo(codigo);
		this.pessoa = pessoa;
		this.obra = obra;
		this.principal = principal;
		this.percFaturamento = percFaturamento;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_cliente", nullable = false)
	@NotNull
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_obra", nullable = false)
	@NotNull
	public Obra getObra() {
		return this.obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	@Column(name = "principal", nullable = false)
	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	@Transient
	public Boolean isPrincipal() {
		return this.principal;
	}

	@Column(name = "perc_faturamento", nullable = false)
	public int getPercFaturamento() {
		return this.percFaturamento;
	}

	public void setPercFaturamento(int percFaturamento) {
		this.percFaturamento = percFaturamento;
	}

	@Override
	public int compareTo(Object o) {
		return principal.compareTo(((ObraCliente) o).isPrincipal());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ObraCliente)) return false;
		if (!super.equals(o)) return false;

		ObraCliente that = (ObraCliente) o;

		if (getPercFaturamento() != that.getPercFaturamento()) return false;
		if (getPessoa() != null ? !getPessoa().equals(that.getPessoa()) : that.getPessoa() != null) return false;
		if (getObra() != null ? !getObra().equals(that.getObra()) : that.getObra() != null) return false;
		return Objects.equals(principal, that.principal);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getPessoa() != null ? getPessoa().hashCode() : 0);
		result = 31 * result + (getObra() != null ? getObra().hashCode() : 0);
		result = 31 * result + (principal != null ? principal.hashCode() : 0);
		result = 31 * result + getPercFaturamento();
		return result;
	}
}
