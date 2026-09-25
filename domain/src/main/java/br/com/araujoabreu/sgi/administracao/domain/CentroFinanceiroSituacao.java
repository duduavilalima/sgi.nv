package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.SituacaoCentroFinanceiro;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alexandre Neves
 */
@Entity
@Table(name = "CENTRO_FINANCEIRO_SITUACAO")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_CENTRO_FINANCEIRO_SITUACAO", sequenceName = "SEQ_CENTRO_FINANCEIRO_SITUACAO", allocationSize = 1)
public class CentroFinanceiroSituacao extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = -996885294861962522L;
	private CentroFinanceiro centroFinanceiro;
	private Colaborador colaborador;
	private Date data;
	private SituacaoCentroFinanceiro situacao;

	public CentroFinanceiroSituacao() {
	}

	public CentroFinanceiroSituacao(CentroFinanceiro centroFinanceiro, Colaborador colaborador, Date data, SituacaoCentroFinanceiro situacao) {
		this.centroFinanceiro = centroFinanceiro;
		this.colaborador = colaborador;
		this.data = data;
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_COLABORADOR")
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_SITUACAO", updatable = false)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SITUACAO", nullable = false)
	public SituacaoCentroFinanceiro getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCentroFinanceiro situacao) {
		this.situacao = situacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
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
		CentroFinanceiroSituacao other = (CentroFinanceiroSituacao) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

}
