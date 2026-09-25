package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovimentoAutorizadoBoletosTO extends BaseTO {
	private BigDecimal valor = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private Date data;
	private String numero;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataMovimento;

	private Date dataTesouraria;
	private String linhaDigitavel;
	private String codigoBarras;
	private BigDecimal valorAdiantamento = BigDecimal.ZERO;
	private PessoaTO credor;
	private CentroFinanceiroTO gerencia;
	private String tipoDocumentoPagamento;
	private BigDecimal valorPagar;
	private BigDecimal valorLiquido;
	private BigDecimal valorTributo;
	private BigDecimal totalOutrosDescontos;
	private String tipo;
	private BigDecimal tributoMaisOutrosDesc;

	private DadosBancariosTO dadosBancarios;
	private PagamentoTO ultimaParcela;
	private String infoBoleto;
	private BancoTO bancoBoleto;

	private boolean linhaDigitavelInformada;

	private List<PagamentoContaContabilTO> listaPgtoCC = new ArrayList<>();

	private String msgValidarValorEDataPagamento;

	private Boolean dataPagamentoInvalido;

	public MovimentoAutorizadoBoletosTO() {
	}

	public BigDecimal getValor() {
		return valor;
	}

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

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public Date getDataTesouraria() {
		return dataTesouraria;
	}

	public void setDataTesouraria(Date dataTesouraria) {
		this.dataTesouraria = dataTesouraria;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public BigDecimal getValorAdiantamento() {
		return valorAdiantamento;
	}

	public void setValorAdiantamento(BigDecimal valorAdiantamento) {
		this.valorAdiantamento = valorAdiantamento;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}


	public CentroFinanceiroTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(CentroFinanceiroTO gerencia) {
		this.gerencia = gerencia;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public BigDecimal getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(BigDecimal valorTributo) {
		this.valorTributo = valorTributo;
	}

	public BigDecimal getTotalOutrosDescontos() {
		return totalOutrosDescontos;
	}

	public void setTotalOutrosDescontos(BigDecimal totalOutrosDescontos) {
		this.totalOutrosDescontos = totalOutrosDescontos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getTributoMaisOutrosDesc() {
		return tributoMaisOutrosDesc;
	}

	public void setTributoMaisOutrosDesc(BigDecimal tributoMaisOutrosDesc) {
		this.tributoMaisOutrosDesc = tributoMaisOutrosDesc;
	}

	public DadosBancariosTO getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancariosTO dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	public PagamentoTO getUltimaParcela() {
		return ultimaParcela;
	}

	public void setUltimaParcela(PagamentoTO ultimaParcela) {
		this.ultimaParcela = ultimaParcela;
	}

	public String getInfoBoleto() {
		return infoBoleto;
	}

	public void setInfoBoleto(String infoBoleto) {
		this.infoBoleto = infoBoleto;
	}

	public BancoTO getBancoBoleto() {
		return bancoBoleto;
	}

	public void setBancoBoleto(BancoTO bancoBoleto) {
		this.bancoBoleto = bancoBoleto;
	}

	public List<PagamentoContaContabilTO> getListaPgtoCC() {
		return listaPgtoCC;
	}

	public void setListaPgtoCC(List<PagamentoContaContabilTO> listaPgtoCC) {
		this.listaPgtoCC = listaPgtoCC;
	}

	public boolean getLinhaDigitavelInformada() {
		return linhaDigitavelInformada;
	}

	public boolean isLinhaDigitavelInformada() {
		return linhaDigitavelInformada;
	}

	public void setLinhaDigitavelInformada(boolean linhaDigitavelInformada) {
		this.linhaDigitavelInformada = linhaDigitavelInformada;
	}

	public String getMsgValidarValorEDataPagamento() {
		return msgValidarValorEDataPagamento;
	}

	public void setMsgValidarValorEDataPagamento(String msgValidarValorEDataPagamento) {
		this.msgValidarValorEDataPagamento = msgValidarValorEDataPagamento;
	}

	public Boolean getDataPagamentoInvalido() {
		return dataPagamentoInvalido;
	}

	public void setDataPagamentoInvalido(Boolean dataPagamentoInvalido) {
		this.dataPagamentoInvalido = dataPagamentoInvalido;
	}
}
