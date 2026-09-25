package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.List;

public class ConciliacaoLancamentoTO extends BaseTO {
	private ConciliacaoBancariaTO conciliacaoBancaria;
	private Integer registro;
	private String registroExibicao;
	private String natureza;
	private Date lancamento;
	private String situacao;
	private Double valor;
	private String debitoCredito;
	private String historico;
	private LancamentoSimpleTO objLancamento;
	private List<RelacaoBancariaPagamentosVO> objPagamento;

	private String exibirNumero;
	private String exibirCredorContaFavorecidoTributo;
	private PessoaTO credor;

	private ChequeContaCorrenteTO cheque;
	private Boolean tipoCheque;

	public Integer getRegistro() {
		return registro;
	}

	public void setRegistro(Integer registro) {
		this.registro = registro;
	}

	public String getRegistroExibicao() {
		return registroExibicao;
	}

	public void setRegistroExibicao(String registroExibicao) {
		this.registroExibicao = registroExibicao;
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

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public LancamentoSimpleTO getObjLancamento() {
		return objLancamento;
	}

	public void setObjLancamento(LancamentoSimpleTO objLancamento) {
		this.objLancamento = objLancamento;
	}

	public List<RelacaoBancariaPagamentosVO> getObjPagamento() {
		return objPagamento;
	}

	public void setObjPagamento(List<RelacaoBancariaPagamentosVO> objPagamento) {
		this.objPagamento = objPagamento;
	}

	public String getExibirNumero() {
		return exibirNumero;
	}

	public void setExibirNumero(String exibirNumero) {
		this.exibirNumero = exibirNumero;
	}

	public String getExibirCredorContaFavorecidoTributo() {
		return exibirCredorContaFavorecidoTributo;
	}

	public void setExibirCredorContaFavorecidoTributo(String exibirCredorContaFavorecidoTributo) {
		this.exibirCredorContaFavorecidoTributo = exibirCredorContaFavorecidoTributo;
	}

	public ChequeContaCorrenteTO getCheque() {
		return cheque;
	}

	public void setCheque(ChequeContaCorrenteTO cheque) {
		this.cheque = cheque;
	}

	public Boolean getTipoCheque() {
		return tipoCheque;
	}

	public void setTipoCheque(Boolean tipoCheque) {
		this.tipoCheque = tipoCheque;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}
}
