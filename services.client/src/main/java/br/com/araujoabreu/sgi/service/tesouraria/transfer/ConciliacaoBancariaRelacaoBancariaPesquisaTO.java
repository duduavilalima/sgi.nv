package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class ConciliacaoBancariaRelacaoBancariaPesquisaTO extends BaseSearchTO {

	private String numero;
	private BancoTO banco;
	private String credor;
	private Date pagamentoDe;
	private Date pagamentoAte;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	private CentroFinanceiroTO gerencia;
	private String docPagamento;
	private UnidadeTO empresa;
	private Long codigoContaCorrenteConciliacao;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BancoTO getBanco() {
		return banco;
	}

	public void setBanco(BancoTO banco) {
		this.banco = banco;
	}

	public String getCredor() {
		return credor;
	}

	public void setCredor(String credor) {
		this.credor = credor;
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

	public String getDocPagamento() {
		return docPagamento;
	}

	public void setDocPagamento(String docPagamento) {
		this.docPagamento = docPagamento;
	}

	public UnidadeTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeTO empresa) {
		this.empresa = empresa;
	}

	public CentroFinanceiroTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(CentroFinanceiroTO gerencia) {
		this.gerencia = gerencia;
	}

	public Long getCodigoContaCorrenteConciliacao() {
		return codigoContaCorrenteConciliacao;
	}

	public void setCodigoContaCorrenteConciliacao(Long codigoContaCorrenteConciliacao) {
		this.codigoContaCorrenteConciliacao = codigoContaCorrenteConciliacao;
	}
}
