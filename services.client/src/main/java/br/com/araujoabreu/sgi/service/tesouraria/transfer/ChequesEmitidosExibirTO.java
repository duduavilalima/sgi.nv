package br.com.araujoabreu.sgi.service.tesouraria.transfer;


import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;

public class ChequesEmitidosExibirTO extends BaseTO {
	private Long codigoItemRelacao;
	private Long codigoRelacaoBancaria;
	private String numeroRelacaoBancaria;
	private Long chequeItem;
	private String numero;
	private PessoaTO credor;
	private BancoTO banco;
	private String docPagamento;
	private Date dataPagamento;
	private Integer qtdPagamentos;
	private BigDecimal total;
	private String status;
	private Date dataCompensacao;
	private Date criacao;

	public Long getCodigoRelacaoBancaria() {
		return codigoRelacaoBancaria;
	}

	public void setCodigoRelacaoBancaria(Long codigoRelacaoBancaria) {
		this.codigoRelacaoBancaria = codigoRelacaoBancaria;
	}

	public String getNumeroRelacaoBancaria() {
		return numeroRelacaoBancaria;
	}

	public void setNumeroRelacaoBancaria(String numeroRelacaoBancaria) {
		this.numeroRelacaoBancaria = numeroRelacaoBancaria;
	}

	public Long getCodigoItemRelacao() {
		return codigoItemRelacao;
	}

	public void setCodigoItemRelacao(Long codigoItemRelacao) {
		this.codigoItemRelacao = codigoItemRelacao;
	}

	public Long getChequeItem() {
		return chequeItem;
	}

	public void setChequeItem(Long chequeItem) {
		this.chequeItem = chequeItem;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}

	public BancoTO getBanco() {
		return banco;
	}

	public void setBanco(BancoTO banco) {
		this.banco = banco;
	}

	public String getDocPagamento() {
		return docPagamento;
	}

	public void setDocPagamento(String docPagamento) {
		this.docPagamento = docPagamento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Integer getQtdPagamentos() {
		return qtdPagamentos;
	}

	public void setQtdPagamentos(Integer qtdPagamentos) {
		this.qtdPagamentos = qtdPagamentos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDataCompensacao() {
		return dataCompensacao;
	}

	public void setDataCompensacao(Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}

	public Date getCriacao() {
		return criacao;
	}

	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}
}
