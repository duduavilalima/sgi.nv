package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "COLABORADOR_CENTRO_FINANCEIRO")
@SequenceGenerator(name = "SEQ_COLABORADOR_CENTRO_FINANCEIRO",	sequenceName = "SEQ_COLABORADOR_CENTRO_FINANCEIRO",	allocationSize = 1)
public class ColaboradorCentroFinanceiro extends BaseEntity implements Serializable, DomainObject {

	private static final long serialVersionUID = 5582963834409475556L;
	private CentroFinanceiro centroFinanceiro;
	private Colaborador colaborador;
	private CentroFinanceiro pai;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO", nullable = false)
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_COLABORADOR", nullable = false)
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@Transient
	public CentroFinanceiro getPai() {
		return pai;
	}

	public void setPai(CentroFinanceiro pai) {
		this.pai = pai;
	}

}

