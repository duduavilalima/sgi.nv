package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MovimentoAutorizadoBoletosPesquisaTO extends BaseSearchTO {

	//Pesquisa
	private UnidadeTO empresa;
	private Date pagamentoDe;
	private Date pagamentoAte;
	private String nomeCredor;
	private BancoTO banco;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	private CentroFinanceiroTO gerencia;
	private String statusCodBarraLd;
	private String linhaDigitavel;
	private String codigoBarras;
	private String infoBoleto;

	//Retorno
	private String numero;
	private PessoaTO credor;
	private Date dataPagamento;
	private BigDecimal valorPagar;
	private String codBarrasLD;
	private DadosBancariosTO dadosBancarios;
	private boolean linhaDigitavelInformada;
	private String tipoDocumentoPagamento;
	private Long codigoNota;
	private String tipo;

	private String msgValidarValorEDataPagamento;

	private Boolean dataPagamentoInvalido;

	public String getCodBarrasLD() {
		return codBarrasLD;
	}

	public void setCodBarrasLD(String codBarrasLD) {
		this.codBarrasLD = codBarrasLD;
	}

	public UnidadeTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeTO empresa) {
		this.empresa = empresa;
	}

	public Date getPagamentoDe() {
		return pagamentoDe;
	}

	public void setPagamentoDe(Date pagamentoDe) {
		this.pagamentoDe = pagamentoDe;
	}

	public Date getPagamentoAte() {
		return pagamentoAte;
	}

	public void setPagamentoAte(Date pagamentoAte) {
		this.pagamentoAte = pagamentoAte;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNomeCredor() {
		return nomeCredor;
	}

	public void setNomeCredor(String nomeCredor) {
		this.nomeCredor = nomeCredor;
	}

	public BancoTO getBanco() {
		return banco;
	}

	public void setBanco(BancoTO banco) {
		this.banco = banco;
	}

	public BigDecimal getValorDe() {
		return valorDe;
	}

	public void setValorDe(BigDecimal valorDe) {
		this.valorDe = valorDe;
	}

	public BigDecimal getValorAte() {
		return valorAte;
	}

	public void setValorAte(BigDecimal valorAte) {
		this.valorAte = valorAte;
	}

	public CentroFinanceiroTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(CentroFinanceiroTO gerencia) {
		this.gerencia = gerencia;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public String getInfoBoleto() {
		return infoBoleto;
	}

	public void setInfoBoleto(String infoBoleto) {
		this.infoBoleto = infoBoleto;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}

	public Date getDataPagamento() {
		if (this.dataPagamento != null) {
			Calendar c = new GregorianCalendar();
			c.setTime(dataPagamento);
			c.set(Calendar.HOUR, 6);
			dataPagamento = new Date(c.getTimeInMillis());
		}

		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public DadosBancariosTO getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancariosTO dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}


	public String getStatusCodBarraLd() {
		return statusCodBarraLd;
	}

	public void setStatusCodBarraLd(String statusCodBarraLd) {
		this.statusCodBarraLd = statusCodBarraLd;
	}

	public boolean isLinhaDigitavelInformada() {
		return linhaDigitavelInformada;
	}

	public void setLinhaDigitavelInformada(boolean linhaDigitavelInformada) {
		this.linhaDigitavelInformada = linhaDigitavelInformada;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public Long getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Long codigoNota) {
		this.codigoNota = codigoNota;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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
