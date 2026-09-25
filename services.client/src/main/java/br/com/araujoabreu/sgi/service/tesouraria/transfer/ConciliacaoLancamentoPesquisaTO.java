package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class ConciliacaoLancamentoPesquisaTO extends BaseSearchTO {

	private ConciliacaoBancariaTO conciliacaoBancaria;
	private Integer registro;
	private String natureza;
	private Date lancamento;
	private String situacao;
	private Double valor;
	private String debitoCredito;

	/**
	 * Atributo para carregar o número do Lançamento ou de uma Relação Bancária, quando
	 * houver uma associacao entre uma dessas duas entidades.
	 * Usado na tela de "Importar Conciliação Bancária".
	 */
	private String numeroPagamento;

	public Integer getRegistro() {
		return registro;
	}

	public void setRegistro(Integer registro) {
		this.registro = registro;
	}

	public Date getLancamento() {
		return lancamento;
	}

	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public ConciliacaoBancariaTO getConciliacaoBancaria() {
		return conciliacaoBancaria;
	}

	public void setConciliacaoBancaria(ConciliacaoBancariaTO conciliacaoBancaria) {
		this.conciliacaoBancaria = conciliacaoBancaria;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(String debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	public String getNumeroPagamento() {
		return numeroPagamento;
	}

	public void setNumeroPagamento(String numeroPagamento) {
		this.numeroPagamento = numeroPagamento;
	}
}
