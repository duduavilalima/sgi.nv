package br.com.araujoabreu.sgi.inrf.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.inrf.domain.Processo.StatusProcesso;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PROCESSO_SITUACAO")
@SequenceGenerator(name = "SEQ_PROCESSO_SITUACAO", sequenceName = "SEQ_PROCESSO_SITUACAO", allocationSize = 1)
public class ProcessoSituacao extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -4852987527712704396L;
	private Processo processo;
	private Colaborador colaborador;
	private Boolean automatico;
	private StatusProcesso situacao;
	private Date dataSituacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PROCESSO")
	@NotNull
	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_COLABORADOR")
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@Column(name = "AUTOMATICO")
	@NotNull
	public Boolean getAutomatico() {
		return automatico;
	}

	public void setAutomatico(Boolean automatico) {
		this.automatico = automatico;
	}

	@Column(name = "SITUACAO")
	@NotNull
	public StatusProcesso getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusProcesso situacao) {
		this.situacao = situacao;
	}

	@Column(name = "DATA_SITUACAO")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	public Date getDataSituacao() {
		return dataSituacao;
	}

	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		ProcessoSituacao that = (ProcessoSituacao) o;
		return automatico.equals(that.automatico) &&
			dataSituacao.equals(that.dataSituacao);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), automatico, dataSituacao);
	}

	@Override
	public String toString() {
		return "ProcessoSituacao [codigo=" + getCodigo() + ", processo=" + processo
			+ ", colaborador=" + colaborador + ", automatico=" + automatico
			+ ", situacao=" + situacao + ", dataSituacao=" + dataSituacao
			+ "]";
	}

	@Transient
	public String getResponsavel() {
		if (automatico != null && automatico)
			return "<i>Processo automático</i>";
		if (colaborador != null && colaborador.getNome() != null)
			return colaborador.getNome();
		return "";
	}


}
