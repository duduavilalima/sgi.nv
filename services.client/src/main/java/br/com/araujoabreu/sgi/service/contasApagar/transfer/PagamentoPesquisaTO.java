package br.com.araujoabreu.sgi.service.contasApagar.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.NaturezaPagamentoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.compras.transfer.NotaPagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class PagamentoPesquisaTO extends BaseSearchTO {

	private BigDecimal valor = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private BigDecimal valorPagar = BigDecimal.ZERO;
	private Date data;
	private PessoaTO credor;
	private NaturezaPagamentoTO naturezaPagamento;
	private String numero;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataMovimento;
	private Date dataAprovador;
	private Date dataAutorizador;
	private Date dataLiberacao;
	private Date dataAnalista;
	private String status;
	private String tipo;
	private String descricao;
	private int checagem;
	private ColaboradorTO emissor;
	private ColaboradorTO supervisor;
	private ColaboradorTO aprovador;
	private ColaboradorTO analista;
	private ColaboradorTO autorizador;
	private ColaboradorTO liberador;
	private ColaboradorTO operadorTesouraria;
	private CentroFinanceiroTO centroFinanceiro;
	private Boolean existeBoleto;
	private Boolean selecao;
	private Boolean atualizadoTesouraria = Boolean.FALSE;
	private boolean linhaDigitavelInformada = false;
	private PagamentoTO tributoAcumulado;
	private DadosBancariosTO dadosBancarios;
	private Date dataTesouraria;
	private String tipoDocumentoPagamento;
	private String linhaDigitavel;
	private String codigoBarras;
	private BancoTO bancoBoleto;
	private BigDecimal valorAdiantamento = BigDecimal.ZERO;
	private NotaPagamentoTO npAdiantamento;
	private NotaTributoAliquotaTO listaNotaTributoAliquota;

	private BigDecimal valorTributo = BigDecimal.ZERO;
	private BigDecimal totalOutrosDescontos = BigDecimal.ZERO;
	private NotaFiscalTO notaFiscalTO;
	private BigDecimal valorFreteSeguro = BigDecimal.ZERO;
	private BigDecimal notaValorIRRF = BigDecimal.ZERO;
	private BigDecimal notaValorISS = BigDecimal.ZERO;
	private BigDecimal notaValorINSS = BigDecimal.ZERO;
	private BigDecimal notaValorPIS = BigDecimal.ZERO;
	private String issUF;
	private BigDecimal totalValorItens = BigDecimal.ZERO;
	private BigDecimal totalValorServicos = BigDecimal.ZERO;

	//Utilizado somente no popup de exibição do pagamentos associados a um item de relação bancária. Esse popup é utilizado em aba_pagamentos.html.
	private Date dataEmissao;

	public PagamentoPesquisaTO() {
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

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}

	public NaturezaPagamentoTO getNaturezaPagamento() {
		return naturezaPagamento;
	}

	public void setNaturezaPagamento(NaturezaPagamentoTO naturezaPagamento) {
		this.naturezaPagamento = naturezaPagamento;
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

	public Date getDataAprovador() {
		return dataAprovador;
	}

	public void setDataAprovador(Date dataAprovador) {
		this.dataAprovador = dataAprovador;
	}

	public Date getDataAutorizador() {
		return dataAutorizador;
	}

	public void setDataAutorizador(Date dataAutorizador) {
		this.dataAutorizador = dataAutorizador;
	}

	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Date getDataAnalista() {
		return dataAnalista;
	}

	public void setDataAnalista(Date dataAnalista) {
		this.dataAnalista = dataAnalista;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getChecagem() {
		return checagem;
	}

	public void setChecagem(int checagem) {
		this.checagem = checagem;
	}

	public ColaboradorTO getEmissor() {
		return emissor;
	}

	public void setEmissor(ColaboradorTO emissor) {
		this.emissor = emissor;
	}

	public ColaboradorTO getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(ColaboradorTO supervisor) {
		this.supervisor = supervisor;
	}

	public ColaboradorTO getAprovador() {
		return aprovador;
	}

	public void setAprovador(ColaboradorTO aprovador) {
		this.aprovador = aprovador;
	}

	public ColaboradorTO getAnalista() {
		return analista;
	}

	public void setAnalista(ColaboradorTO analista) {
		this.analista = analista;
	}

	public ColaboradorTO getAutorizador() {
		return autorizador;
	}

	public void setAutorizador(ColaboradorTO autorizador) {
		this.autorizador = autorizador;
	}

	public ColaboradorTO getLiberador() {
		return liberador;
	}

	public void setLiberador(ColaboradorTO liberador) {
		this.liberador = liberador;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Boolean getExisteBoleto() {
		return existeBoleto;
	}

	public void setExisteBoleto(Boolean existeBoleto) {
		this.existeBoleto = existeBoleto;
	}

	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	public Boolean getAtualizadoTesouraria() {
		return atualizadoTesouraria;
	}

	public void setAtualizadoTesouraria(Boolean atualizadoTesouraria) {
		this.atualizadoTesouraria = atualizadoTesouraria;
	}

	public boolean isLinhaDigitavelInformada() {
		return linhaDigitavelInformada;
	}

	public void setLinhaDigitavelInformada(boolean linhaDigitavelInformada) {
		this.linhaDigitavelInformada = linhaDigitavelInformada;
	}

	public PagamentoTO getTributoAcumulado() {
		return tributoAcumulado;
	}

	public void setTributoAcumulado(PagamentoTO tributoAcumulado) {
		this.tributoAcumulado = tributoAcumulado;
	}

	public DadosBancariosTO getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancariosTO dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	public ColaboradorTO getOperadorTesouraria() {
		return operadorTesouraria;
	}

	public void setOperadorTesouraria(ColaboradorTO operadorTesouraria) {
		this.operadorTesouraria = operadorTesouraria;
	}

	public Date getDataTesouraria() {
		return dataTesouraria;
	}

	public void setDataTesouraria(Date dataTesouraria) {
		this.dataTesouraria = dataTesouraria;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
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

	public BancoTO getBancoBoleto() {
		return bancoBoleto;
	}

	public void setBancoBoleto(BancoTO bancoBoleto) {
		this.bancoBoleto = bancoBoleto;
	}

	public BigDecimal getValorAdiantamento() {
		return valorAdiantamento;
	}

	public void setValorAdiantamento(BigDecimal valorAdiantamento) {
		this.valorAdiantamento = valorAdiantamento;
	}

	public NotaPagamentoTO getNpAdiantamento() {
		return npAdiantamento;
	}

	public void setNpAdiantamento(NotaPagamentoTO npAdiantamento) {
		this.npAdiantamento = npAdiantamento;
	}

	public NotaTributoAliquotaTO getListaNotaTributoAliquota() {
		return listaNotaTributoAliquota;
	}

	public void setListaNotaTributoAliquota(NotaTributoAliquotaTO listaNotaTributoAliquota) {
		this.listaNotaTributoAliquota = listaNotaTributoAliquota;
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

	public NotaFiscalTO getNotaFiscalTO() {
		return notaFiscalTO;
	}

	public void setNotaFiscalTO(NotaFiscalTO notaFiscalTO) {
		this.notaFiscalTO = notaFiscalTO;
	}


	public BigDecimal getValorFreteSeguro() {
		return valorFreteSeguro;
	}

	public void setValorFreteSeguro(BigDecimal valorFreteSeguro) {
		this.valorFreteSeguro = valorFreteSeguro;
	}

	public BigDecimal getNotaValorIRRF() {
		return notaValorIRRF;
	}

	public void setNotaValorIRRF(BigDecimal notaValorIRRF) {
		this.notaValorIRRF = notaValorIRRF;
	}

	public BigDecimal getNotaValorISS() {
		return notaValorISS;
	}

	public void setNotaValorISS(BigDecimal notaValorISS) {
		this.notaValorISS = notaValorISS;
	}

	public BigDecimal getNotaValorINSS() {
		return notaValorINSS;
	}

	public void setNotaValorINSS(BigDecimal notaValorINSS) {
		this.notaValorINSS = notaValorINSS;
	}

	public BigDecimal getNotaValorPIS() {
		return notaValorPIS;
	}

	public void setNotaValorPIS(BigDecimal notaValorPIS) {
		this.notaValorPIS = notaValorPIS;
	}

	public String getIssUF() {
		return issUF;
	}

	public void setIssUF(String issUF) {
		this.issUF = issUF;
	}

	public BigDecimal getTotalValorItens() {
		return totalValorItens;
	}

	public void setTotalValorItens(BigDecimal totalValorItens) {
		this.totalValorItens = totalValorItens;
	}

	public BigDecimal getTotalValorServicos() {
		return totalValorServicos;
	}

	public void setTotalValorServicos(BigDecimal totalValorServicos) {
		this.totalValorServicos = totalValorServicos;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
}
