package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "HISTORICO_CONCILIACAO_CONTA_CONTABIL")
@SequenceGenerator(name = "SEQ_HISTORICO_CONCILIACAO_CONTA_CONTABIL", sequenceName = "SEQ_HISTORICO_CONCILIACAO_CONTA_CONTABIL", allocationSize = 1)
public class HistoricoConciliacaoContaContabil extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = 1678724014069730012L;

	private HistoricoConciliacao historicoConciliacao;
	private ContaContabil contaContabil;
	private String numeroContaContabil;
	private Unidade unidade;

	public HistoricoConciliacaoContaContabil() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_HISTORICO_CONCILIACAO")
	public HistoricoConciliacao getHistoricoConciliacao() {
		return historicoConciliacao;
	}

	public void setHistoricoConciliacao(HistoricoConciliacao historicoConciliacao) {
		this.historicoConciliacao = historicoConciliacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Transient
	public String getNumeroContaContabil() {
		if (this.numeroContaContabil == null && this.contaContabil != null && this.contaContabil.getNumero() != null) {
			this.numeroContaContabil = this.contaContabil.getNumero() + (this.contaContabil.getDescricao() == null ? "" : (" - " + this.contaContabil.getDescricao()));
		}
		return numeroContaContabil;
	}

	@Transient
	public Unidade getUnidade() {
		if (contaContabil != null) {
			unidade = contaContabil.getEmpresa();
		}
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
}
