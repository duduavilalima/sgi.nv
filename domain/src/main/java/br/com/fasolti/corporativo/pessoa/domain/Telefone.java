package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Table(name = "TELEFONE")
@SequenceGenerator(name = "SEQ_TELEFONE", sequenceName = "SEQ_TELEFONE", allocationSize = 1)
public class Telefone extends AuditEntity implements DomainObject, Detail, Comparable {

	private static final long serialVersionUID = -7367823371132970692L;
	private Pessoa pessoa;
	private Contato contato;
	private String ddd;
	private String numero;
	private String ramal;
	private TipoTelefone tipo;
	private Boolean principal;
	private Operadora operadora;
	private Boolean selecao;
	private Long codigoOperadora;

	public Telefone() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PESSOA")
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTATO")
	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	@Column(name = "DDD", nullable = false, length = 2)
	@NotNull
	@Length(max = 2)
	public String getDdd() {
		return this.ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	@Column(name = "NUMERO", nullable = false)
	@NotNull
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "RAMAL", length = 4)
	@Length(max = 4)
	public String getRamal() {
		return this.ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoTelefone getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoTelefone tipo) {
		this.tipo = tipo;
	}

	@Override
	@Column(name = "PRINCIPAL", nullable = false, length = 1)
	@NotNull
	public Boolean getPrincipal() {
		return principal;
	}

	@Override
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_OPERADORA")
	public Operadora getOperadora() {
		return operadora;
	}

	public void setOperadora(Operadora operadora) {
		this.operadora = operadora;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.ddd != null) {
			sb.append("(");
			sb.append(this.ddd);
			sb.append(")");
		}
		sb.append(" ");
		if (this.numero != null) {
			sb.append(this.numero);
		}
		return sb.toString();
	}

	@Transient
	public String getDddTelefone() {
		StringBuilder sb = new StringBuilder();
		if (numero != null) {
			if (ddd != null) {
				sb.append("(").append(ddd).append(") ");
			}
			sb.append(numero);
		}
		return sb.toString();
	}

	public String tratarTelefone() {
		StringBuilder sb = new StringBuilder();
		if (numero != null) {
			if (tipo != null) {
				sb.append(tipo);
			}
			if (ddd != null) {
				sb.append(" - (").append(ddd).append(") ").append(numero);
			}
			if (ramal != null && (!ramal.isEmpty())) {
				sb.append(" r. ").append(ramal);
			}
		}
		return sb.toString();
	}

	@Transient
	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	@Transient
	public Long getCodigoOperadora() {
		return codigoOperadora;
	}

	public void setCodigoOperadora(Long codigoOperadora) {
		this.codigoOperadora = codigoOperadora;
	}

	@Override
	public int compareTo(Object o) {
		return principal.compareTo(((Telefone) o).getPrincipal());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Telefone)) return false;
		if (!super.equals(o)) return false;
		Telefone telefone = (Telefone) o;
		return Objects.equals(getDdd(), telefone.getDdd()) &&
			Objects.equals(getNumero(), telefone.getNumero()) &&
			Objects.equals(getRamal(), telefone.getRamal()) &&
			Objects.equals(getPrincipal(), telefone.getPrincipal()) &&
			Objects.equals(getSelecao(), telefone.getSelecao()) &&
			Objects.equals(getCodigoOperadora(), telefone.getCodigoOperadora());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDdd(), getNumero(), getRamal(), getPrincipal(), getSelecao(), getCodigoOperadora());
	}

	public enum TipoTelefone {
		Telefone,
		Fax,
		Celular;

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Telefone";
				case 1:
					return "Fax";
				case 2:
					return "Celular";
				default:
					return "";
			}
		}
	}
}
