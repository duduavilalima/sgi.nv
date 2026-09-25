package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteSimpleTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.math.BigDecimal;
import java.util.Date;

public class NotaSaidaParcelaBaixaRetencaoTO extends BaseTO implements SearchObject {

	private NotaSaidaParcelaTO notaSaidaParcela;
	private ContaCorrenteTO contaCorrente;
	private Date dataPagamento;
	private Date dataContabilizacao;
	private BigDecimal valorBaixa;
	private BigDecimal valorJuros;
	private Date dataCriacao;
	private String criador;

	public NotaSaidaParcelaTO getNotaSaidaParcela() {
		return notaSaidaParcela;
	}

	public void setNotaSaidaParcela(NotaSaidaParcelaTO notaSaidaParcela) {
		this.notaSaidaParcela = notaSaidaParcela;
	}

	public ContaCorrenteTO getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteTO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	public BigDecimal getValorBaixa() {
		return valorBaixa;
	}

	public void setValorBaixa(BigDecimal valorBaixa) {
		this.valorBaixa = valorBaixa;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Override
	public String toString() {
		return "NotaSaidaParcelaBaixaRetencaoTO{" +
			"dataPagamento=" + dataPagamento +
			", dataContabilizacao=" + dataContabilizacao +
			", valorBaixa=" + valorBaixa +
			", valorJuros=" + valorJuros +
			'}';
	}
}
