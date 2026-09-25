package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Alexandre Neves
 */
@Entity
@Table(name = "CENTRO_FINANCEIRO_FLUXO_ETAPA")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_CENTRO_FINANCEIRO_FLUXO_ETAPA",	sequenceName = "SEQ_CENTRO_FINANCEIRO_FLUXO_ETAPA",	allocationSize = 1)
public class CentroFinanceiroFluxoEtapa extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7079433007842675190L;

	private CentroFinanceiro centroFinanceiro;
	private EtapaFluxo etapaFluxo;
	private Colaborador colaborador;
	private Date dataRealizacao;

	public CentroFinanceiroFluxoEtapa() {
	}

	public CentroFinanceiroFluxoEtapa(EtapaFluxo etapaFluxo) {
		this.etapaFluxo = etapaFluxo;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_REALIZADOR")
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_ETAPA_FLUXO")
	public EtapaFluxo getEtapaFluxo() {
		return etapaFluxo;
	}

	public void setEtapaFluxo(EtapaFluxo etapaFluxo) {
		this.etapaFluxo = etapaFluxo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_REALIZACAO")
	public Date getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(Date dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public boolean retornoObrigatorio() {
		if (etapaFluxo != null)
			return (etapaFluxo.getMotivoAplicacao() != null && etapaFluxo.getMotivoAplicacao().getCodigo() != null);
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centroFinanceiro == null) ? 0 : centroFinanceiro.hashCode());
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((colaborador == null) ? 0 : colaborador.hashCode());
		result = prime * result + ((dataRealizacao == null) ? 0 : dataRealizacao.hashCode());
		result = prime * result + ((etapaFluxo == null) ? 0 : etapaFluxo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(CentroFinanceiroFluxoEtapa.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		CentroFinanceiroFluxoEtapa other = (CentroFinanceiroFluxoEtapa) obj;
		if (getCentroFinanceiro() == null) {
			if (other.getCentroFinanceiro() != null) {
				return false;
			}
		} else if (!getCentroFinanceiro().equals(other.getCentroFinanceiro())) {
			return false;
		}
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getColaborador() == null) {
			if (other.getColaborador() != null) {
				return false;
			}
		} else if (!getColaborador().equals(other.getColaborador())) {
			return false;
		}
		if (getDataRealizacao() == null) {
			if (other.getDataRealizacao() != null) {
				return false;
			}
		} else if (!getDataRealizacao().equals(other.getDataRealizacao())) {
			return false;
		}
		if (getEtapaFluxo() == null) {
			if (other.getEtapaFluxo() != null) {
				return false;
			}
		} else if (!getEtapaFluxo().equals(other.getEtapaFluxo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CentroFinanceiroFluxoEtapa [codigo=" + getCodigo() + ", dataRealizacao=" + dataRealizacao + "]";
	}

}
