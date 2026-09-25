package br.com.araujoabreu.sgi.service.impl.contasApagar;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;

import java.math.BigDecimal;
import java.util.Date;

public class LoteCustosTO {

	private Long codigoPagamento;
	private String numero;
	private Pagamento.TipoPagamento tipo;
	private Date dataPagamento;
	private BigDecimal valor;
	private Long cfCodigo;
	private String cfIdentificador;
	private CentroFinanceiro.TipoCentroFinanceiro cfTipo;
	private BigDecimal valorRateio;
	private Long ccCLCodigo;
	private String ccCLNumero;
	private Long ccCCCodigo;
	private String ccCCNumero;

	public LoteCustosTO() {
	}

	public LoteCustosTO(Long codigoPagamento, String numero, Pagamento.TipoPagamento tipo, Date dataPagamento, BigDecimal valor, Long cfCodigo, String cfIdentificador, CentroFinanceiro.TipoCentroFinanceiro cfTipo, BigDecimal valorRateio, Long ccCLCodigo, String ccCLNumero, Long ccCCCodigo, String ccCCNumero) {
		this.codigoPagamento = codigoPagamento;
		this.numero = numero;
		this.tipo = tipo;
		this.dataPagamento = dataPagamento;
		this.valor = valor;
		this.cfCodigo = cfCodigo;
		this.cfIdentificador = cfIdentificador;
		this.cfTipo = cfTipo;
		this.valorRateio = valorRateio;
		this.ccCLCodigo = ccCLCodigo;
		this.ccCLNumero = ccCLNumero;
		this.ccCCCodigo = ccCCCodigo;
		this.ccCCNumero = ccCCNumero;
	}

	public Long getCodigoPagamento() {
		return codigoPagamento;
	}

	public void setCodigoPagamento(Long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Pagamento.TipoPagamento getTipo() {
		return tipo;
	}

	public void setTipo(Pagamento.TipoPagamento tipo) {
		this.tipo = tipo;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getCfCodigo() {
		return cfCodigo;
	}

	public void setCfCodigo(Long cfCodigo) {
		this.cfCodigo = cfCodigo;
	}

	public String getCfIdentificador() {
		return cfIdentificador;
	}

	public void setCfIdentificador(String cfIdentificador) {
		this.cfIdentificador = cfIdentificador;
	}

	public CentroFinanceiro.TipoCentroFinanceiro getCfTipo() {
		return cfTipo;
	}

	public void setCfTipo(CentroFinanceiro.TipoCentroFinanceiro cfTipo) {
		this.cfTipo = cfTipo;
	}

	public BigDecimal getValorRateio() {
		return valorRateio;
	}

	public void setValorRateio(BigDecimal valorRateio) {
		this.valorRateio = valorRateio;
	}

	public Long getCcCLCodigo() {
		return ccCLCodigo;
	}

	public void setCcCLCodigo(Long ccCLCodigo) {
		this.ccCLCodigo = ccCLCodigo;
	}

	public String getCcCLNumero() {
		return ccCLNumero;
	}

	public void setCcCLNumero(String ccCLNumero) {
		this.ccCLNumero = ccCLNumero;
	}

	public Long getCcCCCodigo() {
		return ccCCCodigo;
	}

	public void setCcCCCodigo(Long ccCCCodigo) {
		this.ccCCCodigo = ccCCCodigo;
	}

	public String getCcCCNumero() {
		return ccCCNumero;
	}

	public void setCcCCNumero(String ccCCNumero) {
		this.ccCCNumero = ccCCNumero;
	}

	@Override
	public String toString() {
		return "LoteCustosTO{" +
			"codigoPagamento=" + codigoPagamento +
			", numero='" + numero + '\'' +
			", tipo=" + tipo +
			", dataPagamento=" + dataPagamento +
			", valor=" + valor +
			", cfCodigo=" + cfCodigo +
			", cfIdentificador='" + cfIdentificador + '\'' +
			", cfTipo='" + cfTipo + '\'' +
			", valorRateio=" + valorRateio +
			", ccCLCodigo=" + ccCLCodigo +
			", ccCLNumero='" + ccCLNumero + '\'' +
			", ccCCCodigo=" + ccCCCodigo +
			", ccCCNumero='" + ccCCNumero + '\'' +
			'}';
	}
}
