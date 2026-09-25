package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.enums.TipoEnderecoContrato;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CENTRO_FINANCEIRO_ENDERECO")
@SequenceGenerator(name = "SEQ_CENTRO_FINANCEIRO_ENDERECO", sequenceName = "SEQ_CENTRO_FINANCEIRO_ENDERECO", allocationSize = 1)
public class CentroFinanceiroEndereco extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = 4302593408272985091L;
	private CentroFinanceiro centroFinanceiro;
	private Endereco endereco;
	private Boolean principal;
	private TipoEnderecoContrato tipo;

	@ManyToOne
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "CODIGO_ENDERECO")
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Column(name = "PRINCIPAL", nullable = false)
	@NotNull
	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	@Column(name = "tipo", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public TipoEnderecoContrato getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoEnderecoContrato tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
			* result
			+ ((centroFinanceiro == null) ? 0 : centroFinanceiro.hashCode());
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result
			+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
			+ ((principal == null) ? 0 : principal.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CentroFinanceiroEndereco other = (CentroFinanceiroEndereco) obj;
		if (centroFinanceiro == null) {
			if (other.centroFinanceiro != null)
				return false;
		} else if (!centroFinanceiro.equals(other.centroFinanceiro))
			return false;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CentroFinanceiroEndereco [codigo=" + getCodigo() + ", principal=" + principal + ", tipo=" + tipo + "]";
	}

}
