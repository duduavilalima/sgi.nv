package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "NOTA_SAIDA_PARCELA_BAIXA_RETENCAO")
@SequenceGenerator(name = "SEQ_NOTA_SAIDA_PARCELA_BAIXA_RETENCAO", sequenceName = "SEQ_NOTA_SAIDA_PARCELA_BAIXA_RETENCAO", allocationSize = 1)
public class NotaSaidaParcelaBaixaRetencao extends BaseEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -8686679688049053942L;

	private NotaSaidaParcela notaSaidaParcela;
	private ContaCorrente contaCorrente;
	private Date dataPagamento;
	private Date dataContabilizacao;
	private BigDecimal valorBaixa;
	private BigDecimal valorJuros;
	private Date dataCriacao;
	private String criador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_SAIDA_PARCELA", nullable = false)
	public NotaSaidaParcela getNotaSaidaParcela() {
		return notaSaidaParcela;
	}

	public void setNotaSaidaParcela(NotaSaidaParcela notaSaidaParcela) {
		this.notaSaidaParcela = notaSaidaParcela;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CORRENTE", nullable = false)
	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	@Column(name = "DATA_PAGAMENTO")
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Column(name = "DATA_CONTABILIZACAO")
	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	@Column(name = "VALOR_BAIXA")
	public BigDecimal getValorBaixa() {
		return valorBaixa;
	}

	public void setValorBaixa(BigDecimal valorBaixa) {
		this.valorBaixa = valorBaixa;
	}

	@Column(name = "VALOR_JUROS")
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", updatable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "CRIADOR", updatable = false)
	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Override
	public String toString() {
		return "NotaSaidaParcelaBaixaRetencao{" +
			"dataPagamento=" + dataPagamento +
			", dataContabilizacao=" + dataContabilizacao +
			", valorBaixa=" + valorBaixa +
			", valorJuros=" + valorJuros +
			'}';
	}
}
