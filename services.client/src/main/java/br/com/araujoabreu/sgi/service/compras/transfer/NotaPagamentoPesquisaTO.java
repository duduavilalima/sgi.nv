package br.com.araujoabreu.sgi.service.compras.transfer;

import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.fundoFixo.transfer.FundoFixoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaPesquisaTO;

import java.math.BigDecimal;

public class NotaPagamentoPesquisaTO extends NotaPesquisaTO {

	private String processo;
	private FundoFixoTO fundoFixo;
	private NotaFiscalTO notaFiscal;
	private BigDecimal valor = new BigDecimal(0);
	private BigDecimal valorJuros = new BigDecimal(0);
	private BigDecimal valorMulta = new BigDecimal(0);
	private BigDecimal valorTributo = new BigDecimal(0);
	private BigDecimal saldoAdiantamento = new BigDecimal(0);
	private ContaContabilTO contaContabil;
	private boolean creditoFundoFixo;
	private StringBuffer logImportacao = new StringBuffer();
	private BigDecimal valorOutrosDescontos = new BigDecimal(0);

	public NotaPagamentoPesquisaTO() {
	}

	/**
	 * Retorna verdadeiro se a nota foi paga com parcela única.
	 *
	 * @return
	 */
	public boolean isParcelaUnica() {
		return "Parcela única".equalsIgnoreCase(getFormaPagamento());
	}

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public FundoFixoTO getFundoFixo() {
		return fundoFixo;
	}

	public void setFundoFixo(FundoFixoTO fundoFixo) {
		this.fundoFixo = fundoFixo;
	}

	public NotaFiscalTO getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscalTO notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	public BigDecimal getValor() {
		return valor;
	}

	@Override
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(BigDecimal valorTributo) {
		this.valorTributo = valorTributo;
	}

	public BigDecimal getSaldoAdiantamento() {
		return saldoAdiantamento;
	}

	public void setSaldoAdiantamento(BigDecimal saldoAdiantamento) {
		this.saldoAdiantamento = saldoAdiantamento;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public boolean isCreditoFundoFixo() {
		return creditoFundoFixo;
	}

	public void setCreditoFundoFixo(boolean creditoFundoFixo) {
		this.creditoFundoFixo = creditoFundoFixo;
	}

	public StringBuffer getLogImportacao() {
		return logImportacao;
	}

	public void setLogImportacao(StringBuffer logImportacao) {
		this.logImportacao = logImportacao;
	}

	public BigDecimal getValorOutrosDescontos() {
		return valorOutrosDescontos;
	}

	public void setValorOutrosDescontos(BigDecimal valorOutrosDescontos) {
		this.valorOutrosDescontos = valorOutrosDescontos;
	}
}
