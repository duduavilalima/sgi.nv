package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "CONCILIACAO_BANCARIA")
@SequenceGenerator(name = "SEQ_CONCILIACAO_BANCARIA", sequenceName = "SEQ_CONCILIACAO_BANCARIA", allocationSize = 1)
public class ConciliacaoBancaria extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 2281061093238102534L;

	private ContaCorrente contaCorrente;
	private Anexo anexo;
	private Date data;
	private StatusConciliacaoBancaria situacao;
	private Double totalDebito = 0.0d;
	private Double totalCredito = 0.0d;
	private Double saldoInicial = 0.0d;
	private Double saldoFinal = 0.0d;

	public ConciliacaoBancaria() {

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CORRENTE")
	@NotNull
	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ANEXO")
	@NotNull
	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@Column(name = "DATA")
	@NotNull
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column(name = "situacao")
	@NotNull
	public StatusConciliacaoBancaria getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusConciliacaoBancaria situacao) {
		this.situacao = situacao;
	}

	@Column(name = "total_debito")
	@NotNull
	public Double getTotalDebito() {
		return totalDebito;
	}

	public void setTotalDebito(Double totalDebito) {
		this.totalDebito = totalDebito;
	}

	@Column(name = "total_credito")
	@NotNull
	public Double getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
	}

	@Column(name = "saldo_inicial")
	@NotNull
	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	@Column(name = "saldo_final")
	@NotNull
	public Double getSaldoFinal() {
		return saldoFinal;
	}

	public void setSaldoFinal(Double saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public enum StatusConciliacaoBancaria {
		CONCILIADA_COM_PENDENCIAS("Conciliada com pendências"),
		CONCILIADA_SEM_PENDENCIAS("Conciliada sem pendências"),
		CANCELADO("Cancelado");

		private final String descricao;

		private StatusConciliacaoBancaria(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return this.descricao;
		}
	}
}
