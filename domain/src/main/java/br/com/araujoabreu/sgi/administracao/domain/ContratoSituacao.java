package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.domain.Contrato.SituacaoContrato;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONTRATO_SITUACAO")
@SequenceGenerator(name = "SEQ_CONTRATO_SITUACAO", sequenceName = "SEQ_CONTRATO_SITUACAO", allocationSize = 1)
public class ContratoSituacao extends BaseEntity {

	private static final long serialVersionUID = 4690754438347898619L;
	private Colaborador colaborador;
	private Contrato contrato;
	private SituacaoContrato situacao;
	private Date dataSituacao;

	public ContratoSituacao() {
	}

	public ContratoSituacao(Long codigo) {
		setCodigo(codigo);
	}

	public ContratoSituacao(Colaborador colaborador, Contrato contrato, SituacaoContrato situacao, Date dataSituacao) {
		this.colaborador = colaborador;
		this.contrato = contrato;
		this.situacao = situacao;
		this.dataSituacao = dataSituacao;
	}

	public ContratoSituacao(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_COLABORADOR", nullable = false)
	@NotNull
	public Colaborador getColaborador() {
		return this.colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTRATO", nullable = false)
	@NotNull
	public Contrato getContrato() {
		return this.contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@Column(name = "SITUACAO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public SituacaoContrato getSituacao() {
		return this.situacao;
	}

	public void setSituacao(SituacaoContrato situacao) {
		this.situacao = situacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_SITUACAO", nullable = false, length = 29)
	@NotNull
	public Date getDataSituacao() {
		return this.dataSituacao;
	}

	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

}
