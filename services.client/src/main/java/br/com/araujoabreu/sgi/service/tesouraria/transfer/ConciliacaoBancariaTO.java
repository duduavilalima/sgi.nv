package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ConciliacaoBancariaTO extends BaseTO {
	private ContaCorrenteTO contaCorrente;
	private AnexoTO anexo;
	private Date data;
	private Integer situacao;
	private BigDecimal totalDebito;
	private BigDecimal totalCredito;
	private BigDecimal saldoInicial;
	private BigDecimal saldoFinal;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private List<ConciliacaoLancamentoTO> listaDeLancamentos;

	public ContaCorrenteTO getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteTO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public AnexoTO getAnexo() {
		return anexo;
	}

	public void setAnexo(AnexoTO anexo) {
		this.anexo = anexo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getTotalDebito() {
		return totalDebito;
	}

	public void setTotalDebito(BigDecimal totalDebito) {
		this.totalDebito = totalDebito;
	}

	public BigDecimal getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(BigDecimal totalCredito) {
		this.totalCredito = totalCredito;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getSaldoFinal() {
		return saldoFinal;
	}

	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldoFinal;
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

	public List<ConciliacaoLancamentoTO> getListaDeLancamentos() {
		return listaDeLancamentos;
	}

	public void setListaDeLancamentos(List<ConciliacaoLancamentoTO> listaDeLancamentos) {
		this.listaDeLancamentos = listaDeLancamentos;
	}

}
