package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CHEQUE_CONTA_CORRENTE")
@SequenceGenerator(name = "SEQ_CHEQUE_CONTA_CORRENTE", sequenceName = "SEQ_CHEQUE_CONTA_CORRENTE", allocationSize = 1)
public class ChequeContaCorrente extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 598893266118075377L;

	private ContaCorrente contaCorrente;
	private Pessoa responsavelConciliacao;
	private Pessoa responsavelCancelamento;
	private Long cheque;
	private StatusChequeContaCorrente situacao;
	private BigDecimal valor;
	private Date dataCompensacao;
	private Date dataConciliacao;
	private Date dataCancelamento;

	public ChequeContaCorrente() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CORRENTE")
	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_CONCILIACAO")
	public Pessoa getResponsavelConciliacao() {
		return responsavelConciliacao;
	}

	public void setResponsavelConciliacao(Pessoa responsavelConciliacao) {
		this.responsavelConciliacao = responsavelConciliacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_CANCELAMENTO")
	public Pessoa getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(Pessoa responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	@Column(name = "cheque")
	public Long getCheque() {
		return cheque;
	}

	public void setCheque(Long cheque) {
		this.cheque = cheque;
	}

	public StatusChequeContaCorrente getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusChequeContaCorrente situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "data_compensacao")
	@Temporal(TemporalType.DATE)
	public Date getDataCompensacao() {
		return dataCompensacao;
	}

	public void setDataCompensacao(Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}

	@Column(name = "DATA_CONCILIACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataConciliacao() {
		return dataConciliacao;
	}

	public void setDataConciliacao(Date dataConciliacao) {
		this.dataConciliacao = dataConciliacao;
	}

	@Column(name = "DATA_CANCELAMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChequeContaCorrente)) return false;
		if (!super.equals(o)) return false;
		ChequeContaCorrente that = (ChequeContaCorrente) o;
		return Objects.equals(getCheque(), that.getCheque()) &&
			Objects.equals(getValor(), that.getValor()) &&
			Objects.equals(getDataCompensacao(), that.getDataCompensacao()) &&
			Objects.equals(getDataConciliacao(), that.getDataConciliacao()) &&
			Objects.equals(getDataCancelamento(), that.getDataCancelamento());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCheque(), getValor(), getDataCompensacao(), getDataConciliacao(), getDataCancelamento());
	}

	@Override
	public String toString() {
		return "ChequeContaCorrente{" +
			"cheque=" + cheque +
			", valor=" + valor +
			", dataCompensacao=" + dataCompensacao +
			", dataConciliacao=" + dataConciliacao +
			", dataCancelamento=" + dataCancelamento +
			'}';
	}

	public enum StatusChequeContaCorrente {
		EMITIDO("Emitido (a compensar)"),
		PAGO("Pago (compensado)"),
		ESTORNADO("Estornado (pagamento não aprovado pela instituição financeira)"),
		CANCELADO_USUARIO("Cancelado (pelo usuário)"),
		CANCELADO_REIMPRESSAO("Cancelado (por reimpressão)");

		private final String descricao;

		private StatusChequeContaCorrente(String descricao) {
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
