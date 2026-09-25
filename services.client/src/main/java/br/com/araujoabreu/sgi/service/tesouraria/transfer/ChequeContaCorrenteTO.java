package br.com.araujoabreu.sgi.service.tesouraria.transfer;


import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;

public class ChequeContaCorrenteTO extends BaseTO {
	private Long cheque;
	private Long codigoContaCorrente;
	private Integer situacao;
	private BigDecimal valor;
	private Date dataCompensacao;
	private Date dataConciliacao;
	private Date dataCancelamento;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	private RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO;

	public Long getCheque() {
		return cheque;
	}

	public void setCheque(Long cheque) {
		this.cheque = cheque;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getDataCompensacao() {
		return dataCompensacao;
	}

	public void setDataCompensacao(Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}

	public Date getDataConciliacao() {
		return dataConciliacao;
	}

	public void setDataConciliacao(Date dataConciliacao) {
		this.dataConciliacao = dataConciliacao;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Long getCodigoContaCorrente() {
		return codigoContaCorrente;
	}

	public void setCodigoContaCorrente(Long codigoContaCorrente) {
		this.codigoContaCorrente = codigoContaCorrente;
	}

	public RelacaoBancariaPagamentosVO getRelacaoBancariaPagamentosVO() {
		return relacaoBancariaPagamentosVO;
	}

	public void setRelacaoBancariaPagamentosVO(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) {
		this.relacaoBancariaPagamentosVO = relacaoBancariaPagamentosVO;
	}
}
