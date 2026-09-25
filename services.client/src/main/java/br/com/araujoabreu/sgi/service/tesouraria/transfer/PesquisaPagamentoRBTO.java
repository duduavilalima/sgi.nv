package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Eduardo Avila em 19/12/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PesquisaPagamentoRBTO extends BaseTO {
	//FIXME Tem ID?
	private String numero;
	private Long codigoBanco;
	private String nomeCredor;
	private Date dataPagamentoDe;
	private Date dataPagamentoAte;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	private Long codigoGerencia;
	private String tipoDocPagamento;

	private Long codigoPagamento;


	public PesquisaPagamentoRBTO() {

	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Long getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(Long codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNomeCredor() {
		return nomeCredor;
	}

	public void setNomeCredor(String nomeCredor) {
		this.nomeCredor = nomeCredor;
	}

	public Date getDataPagamentoDe() {
		return dataPagamentoDe;
	}

	public void setDataPagamentoDe(Date dataPagamentoDe) {
		this.dataPagamentoDe = dataPagamentoDe;
	}

	public Date getDataPagamentoAte() {
		return dataPagamentoAte;
	}

	public void setDataPagamentoAte(Date dataPagamentoAte) {
		this.dataPagamentoAte = dataPagamentoAte;
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

	public String getTipoDocPagamento() {
		return tipoDocPagamento;
	}

	public void setTipoDocPagamento(String tipoDocPagamento) {
		this.tipoDocPagamento = tipoDocPagamento;
	}

	public Long getCodigoGerencia() {
		return codigoGerencia;
	}

	public void setCodigoGerencia(Long codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}

	public Long getCodigoPagamento() {
		return codigoPagamento;
	}

	public void setCodigoPagamento(Long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}

	@Override
	public String toString() {
		return "PesquisaPagamentoRBTO{" +
			"numero='" + numero + '\'' +
			", codigoBanco=" + codigoBanco +
			", nomeCredor='" + nomeCredor + '\'' +
			", dataPagamentoDe=" + dataPagamentoDe +
			", dataPagamentoAte=" + dataPagamentoAte +
			", valorDe=" + valorDe +
			", valorAte=" + valorAte +
			", codigoGerencia=" + codigoGerencia +
			", tipoDocPagamento='" + tipoDocPagamento + '\'' +
			'}';
	}
}
