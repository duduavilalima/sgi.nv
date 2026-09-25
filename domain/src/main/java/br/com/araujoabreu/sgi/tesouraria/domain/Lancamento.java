package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "LANCAMENTO")
@SequenceGenerator(name = "SEQ_LANCAMENTO", sequenceName = "SEQ_LANCAMENTO", allocationSize = 1)
public class Lancamento extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -6463653855182776258L;

	private ContaContabil contaContabil;
	private ContaCorrente contaCorrente;
	private Unidade unidade;
	private Colaborador colaboradorCancelamento;
	private String numero;
	private StatusLancamento status;
	private Date data;
	private BigDecimal valor;
	private Date dataCancelamento;
	private String debitoCredito;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	@NotNull
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
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
	@JoinColumn(name = "CODIGO_UNIDADE")
	@NotNull
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CANCELAMENTO")
	public Colaborador getColaboradorCancelamento() {
		return colaboradorCancelamento;
	}

	public void setColaboradorCancelamento(Colaborador colaboradorCancelamento) {
		this.colaboradorCancelamento = colaboradorCancelamento;
	}

	@Column(name = "NUMERO", length = 14)
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "SITUACAO")
	@NotNull
	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
	}

	@Column(name = "DATA")
	@NotNull
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column(name = "VALOR")
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "DATA_CANCELAMENTO")
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Column(name = "DEBITO_CREDITO")
	@NotNull
	public String getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(String debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Lancamento that = (Lancamento) o;
		return numero.equals(that.numero) &&
			data.equals(that.data) &&
			valor.equals(that.valor) &&
			dataCancelamento.equals(that.dataCancelamento) &&
			debitoCredito.equals(that.debitoCredito);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), numero, data, valor, dataCancelamento, debitoCredito);
	}

	public enum StatusLancamento {
		ABERTO("Aberto"),
		ENVIADO_CONTABILIDADE("Enviado para Contabilidade"),
		CANCELADO("Cancelado");

		private final String descricao;

		StatusLancamento(String descricao) {
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
