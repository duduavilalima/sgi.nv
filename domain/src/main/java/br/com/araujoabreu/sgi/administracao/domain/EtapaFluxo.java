package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ETAPA_FLUXO")
@SequenceGenerator(name = "SEQ_ETAPA_FLUXO", sequenceName = "SEQ_ETAPA_FLUXO", allocationSize = 1)
public class EtapaFluxo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2534124970274396913L;
	private String nome;
	private Fluxo fluxo;
	private Perfil perfil;
	private Colaborador colaborador;
	private MotivoAplicacao motivoAplicacao;
	private Integer ordem;
	private Integer statusAnterior;
	private Integer statusAtual;
	private Integer proximoStatus;
	private Integer statusBase;
	private Boolean validarContaContabilCliente;

	public EtapaFluxo() {
	}

	public EtapaFluxo(Fluxo fluxo, Integer statusAtual) {
		this.fluxo = fluxo;
		this.statusAtual = statusAtual;
	}

	@Column(name = "NOME", length = 30, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FLUXO")
	public Fluxo getFluxo() {
		return fluxo;
	}

	public void setFluxo(Fluxo fluxo) {
		this.fluxo = fluxo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PERFIL")
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_COLABORADOR")
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_APLICACAO_MOTIVO_RETORNO")
	public MotivoAplicacao getMotivoAplicacao() {
		return motivoAplicacao;
	}

	public void setMotivoAplicacao(MotivoAplicacao motivoAplicacao) {
		this.motivoAplicacao = motivoAplicacao;
	}

	@Column(name = "ORDEM")
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(name = "SITUACAO_ANTERIOR")
	public Integer getStatusAnterior() {
		return statusAnterior;
	}

	public void setStatusAnterior(Integer statusAnterior) {
		this.statusAnterior = statusAnterior;
	}

	@Column(name = "SITUACAO_ATUAL")
	public Integer getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(Integer statusAtual) {
		this.statusAtual = statusAtual;
	}

	@Column(name = "PROXIMO_SITUACAO")
	public Integer getProximoStatus() {
		return proximoStatus;
	}

	public void setProximoStatus(Integer proximoStatus) {
		this.proximoStatus = proximoStatus;
	}

	@Column(name = "SITUACAO_BASE")
	public Integer getStatusBase() {
		return statusBase;
	}

	public void setStatusBase(Integer statusBase) {
		this.statusBase = statusBase;
	}

	@Column(name = "validar_conta_contabil_cliente", nullable = false)
	public Boolean getValidarContaContabilCliente() {
		return validarContaContabilCliente;
	}

	public void setValidarContaContabilCliente(Boolean validarContaContabilCliente) {
		this.validarContaContabilCliente = validarContaContabilCliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result + ((proximoStatus == null) ? 0 : proximoStatus.hashCode());
		result = prime * result + ((statusAnterior == null) ? 0 : statusAnterior.hashCode());
		result = prime * result + ((statusAtual == null) ? 0 : statusAtual.hashCode());
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
		if (!(EtapaFluxo.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		EtapaFluxo other = (EtapaFluxo) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getNome() == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!getNome().equals(other.getNome())) {
			return false;
		}
		if (getOrdem() == null) {
			if (other.getOrdem() != null) {
				return false;
			}
		} else if (!getOrdem().equals(other.getOrdem())) {
			return false;
		}
		if (getProximoStatus() == null) {
			if (other.getProximoStatus() != null) {
				return false;
			}
		} else if (!getProximoStatus().equals(other.getProximoStatus())) {
			return false;
		}
		if (getStatusAnterior() == null) {
			if (other.getStatusAnterior() != null) {
				return false;
			}
		} else if (!getStatusAnterior().equals(other.getStatusAnterior())) {
			return false;
		}
		if (getStatusAtual() == null) {
			if (other.getStatusAtual() != null) {
				return false;
			}
		} else if (!getStatusAtual().equals(other.getStatusAtual())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EtapaFluxo{" +
			"nome='" + nome + '\'' +
			", ordem=" + ordem +
			", statusAnterior=" + statusAnterior +
			", statusAtual=" + statusAtual +
			", proximoStatus=" + proximoStatus +
			", statusBase=" + statusBase +
			'}';
	}
}
