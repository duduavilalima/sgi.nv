package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.pessoa.domain.Contato;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "contrato_contato", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo_contrato", "codigo_contato"}))
@SequenceGenerator(	name = "SEQ_CONTRATO_CONTATO",	sequenceName = "SEQ_CONTRATO_CONTATO",	allocationSize = 1)
public class ContratoContato extends BaseEntity {

	private static final long serialVersionUID = -2213812705985042583L;
	private Contato contato;
	private Contrato contrato;
	private int ordem;

	public ContratoContato() {
	}

	public ContratoContato(Long codigo) {
		super();
	}

	public ContratoContato(Contato contato, Contrato contrato, int ordem) {
		this.contato = contato;
		this.contrato = contrato;
		this.ordem = ordem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_contato", nullable = false)
	@NotNull
	public Contato getContato() {
		return this.contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
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

	@Column(name = "ordem", nullable = false)
	public int getOrdem() {
		return this.ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

}
