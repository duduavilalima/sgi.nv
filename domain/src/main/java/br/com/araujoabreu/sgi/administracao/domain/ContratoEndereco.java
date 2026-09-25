package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.enums.TipoEnderecoContrato;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRATO_ENDERECO")
@SequenceGenerator(name = "SEQ_CONTRATO_ENDERECO", sequenceName = "SEQ_CONTRATO_ENDERECO", allocationSize = 1)
public class ContratoEndereco extends BaseEntity {

	private static final long serialVersionUID = 2535496126439418851L;
	private Contrato contrato;
	private Endereco endereco;
	private boolean principal;
	private TipoEnderecoContrato tipo;

	public ContratoEndereco() {
	}

	public ContratoEndereco(Contrato contrato, Endereco endereco, Boolean principal, TipoEnderecoContrato tipo) {
		this.contrato = contrato;
		this.endereco = endereco;
		this.principal = principal == null ? Boolean.FALSE : principal;
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_contrato", nullable = false)
	@NotNull
	public Contrato getContrato() {
		return this.contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_endereco", nullable = false)
	@NotNull
	public Endereco getEndereco() {
		return this.endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Column(name = "principal", nullable = false)
	public boolean isPrincipal() {
		return this.principal;
	}

	public void setPrincipal(boolean principal) {
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

}
