package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelacaoBancariaTO extends BaseTO {

	private String numero;
	private Date dataPagamento;
	private BigDecimal total;
	private Long chequeAnterior;
	private String meioPagamento;
	private String observacao;
	private ContaCorrenteTO contaCorrente;
	private ContaCorrenteTO contaCorrenteRecebimento;
	private UnidadeTO unidade;
	private Boolean pagamentoIndividual;
	private Boolean impressaoCheque = Boolean.FALSE;
	private Boolean impressaoEndosso = Boolean.FALSE;
	private Boolean impressao2via = Boolean.FALSE;
	private Integer qtdPagamentos;
	private String status;
	private Date dataEnvioIb;
	private Date dataRetornoIb;
	private String motivoBaixaManual;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private ColaboradorTO colaboradorEnvio;
	private ColaboradorTO colaboradorBaixaManual;
	private ColaboradorTO colaboradorEstornoBaixaManual = new ColaboradorTO();
	private ColaboradorTO colaboradorCancelamento;
	private Date dataEnvio;
	private Date dataBaixaManual;
	private Date dataEstornoBaixaManual;
	private Date dataCancelamento;
	private Boolean isBaixaManual;
	private List<RelacaoBancariaPagamentosVO> relacaoBancariaPagamentosVO;
	private List<RelacaoBancariaPagamentosVO> pesquisaPagamentosVO;
	private PesquisaPagamentoRBTO pesquisaPagamentoRBTO;
	private List<Long> itensRemovidos;
	private Boolean houverMudancaNoUltimoCheque;
	private String situacaoNovo;
	private Long totalItens;
	private Long totalPago;
	private Long valorTotal;

	public RelacaoBancariaTO() {

	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getChequeAnterior() {
		return chequeAnterior;
	}

	public void setChequeAnterior(Long chequeAnterior) {
		this.chequeAnterior = chequeAnterior;
	}

	public String getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(String meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public ContaCorrenteTO getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteTO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public ContaCorrenteTO getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(ContaCorrenteTO contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public Boolean getPagamentoIndividual() {
		return pagamentoIndividual;
	}

	public void setPagamentoIndividual(Boolean pagamentoIndividual) {
		this.pagamentoIndividual = pagamentoIndividual;
	}

	public Boolean getImpressaoCheque() {
		return impressaoCheque;
	}

	public void setImpressaoCheque(Boolean impressaoCheque) {
		this.impressaoCheque = impressaoCheque;
	}

	public Boolean getImpressaoEndosso() {
		return impressaoEndosso;
	}

	public void setImpressaoEndosso(Boolean impressaoEndosso) {
		this.impressaoEndosso = impressaoEndosso;
	}

	public Boolean getImpressao2via() {
		return impressao2via;
	}

	public void setImpressao2via(Boolean impressao2via) {
		this.impressao2via = impressao2via;
	}

	public Integer getQtdPagamentos() {
		return qtdPagamentos;
	}

	public void setQtdPagamentos(Integer qtdPagamentos) {
		this.qtdPagamentos = qtdPagamentos;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDataEnvioIb() {
		return dataEnvioIb;
	}

	public void setDataEnvioIb(Date dataEnvioIb) {
		this.dataEnvioIb = dataEnvioIb;
	}

	public Date getDataRetornoIb() {
		return dataRetornoIb;
	}

	public void setDataRetornoIb(Date dataRetornoIb) {
		this.dataRetornoIb = dataRetornoIb;
	}

	public String getMotivoBaixaManual() {
		return motivoBaixaManual;
	}

	public void setMotivoBaixaManual(String motivoBaixaManual) {
		this.motivoBaixaManual = motivoBaixaManual;
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

	public ColaboradorTO getColaboradorEnvio() {
		return colaboradorEnvio;
	}

	public void setColaboradorEnvio(ColaboradorTO colaboradorEnvio) {
		this.colaboradorEnvio = colaboradorEnvio;
	}

	public ColaboradorTO getColaboradorBaixaManual() {
		return colaboradorBaixaManual;
	}

	public void setColaboradorBaixaManual(ColaboradorTO colaboradorBaixaManual) {
		this.colaboradorBaixaManual = colaboradorBaixaManual;
	}

	public ColaboradorTO getColaboradorEstornoBaixaManual() {
		return colaboradorEstornoBaixaManual;
	}

	public void setColaboradorEstornoBaixaManual(ColaboradorTO colaboradorEstornoBaixaManual) {
		this.colaboradorEstornoBaixaManual = colaboradorEstornoBaixaManual;
	}

	public ColaboradorTO getColaboradorCancelamento() {
		return colaboradorCancelamento;
	}

	public void setColaboradorCancelamento(ColaboradorTO colaboradorCancelamento) {
		this.colaboradorCancelamento = colaboradorCancelamento;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Date getDataBaixaManual() {
		return dataBaixaManual;
	}

	public void setDataBaixaManual(Date dataBaixaManual) {
		this.dataBaixaManual = dataBaixaManual;
	}

	public Date getDataEstornoBaixaManual() {
		return dataEstornoBaixaManual;
	}

	public void setDataEstornoBaixaManual(Date dataEstornoBaixaManual) {
		this.dataEstornoBaixaManual = dataEstornoBaixaManual;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public Boolean getBaixaManual() {
		return isBaixaManual;
	}

	public void setBaixaManual(Boolean baixaManual) {
		isBaixaManual = baixaManual;
	}

	public List<RelacaoBancariaPagamentosVO> getRelacaoBancariaPagamentosVO() {
		return relacaoBancariaPagamentosVO;
	}

	public void setRelacaoBancariaPagamentosVO(List<RelacaoBancariaPagamentosVO> relacaoBancariaPagamentosVO) {
		this.relacaoBancariaPagamentosVO = relacaoBancariaPagamentosVO;
	}

	public List<RelacaoBancariaPagamentosVO> getPesquisaPagamentosVO() {
		return pesquisaPagamentosVO;
	}

	public void setPesquisaPagamentosVO(List<RelacaoBancariaPagamentosVO> pesquisaPagamentosVO) {
		this.pesquisaPagamentosVO = pesquisaPagamentosVO;
	}

	public PesquisaPagamentoRBTO getPesquisaPagamentoRBTO() {
		return pesquisaPagamentoRBTO;
	}

	public void setPesquisaPagamentoRBTO(PesquisaPagamentoRBTO pesquisaPagamentoRBTO) {
		this.pesquisaPagamentoRBTO = pesquisaPagamentoRBTO;
	}

	public List<Long> getItensRemovidos() {
		return itensRemovidos;
	}

	public void setItensRemovidos(List<Long> itensRemovidos) {
		this.itensRemovidos = itensRemovidos;
	}

	public Boolean getHouverMudancaNoUltimoCheque() {
		return houverMudancaNoUltimoCheque;
	}

	public void setHouverMudancaNoUltimoCheque(Boolean houverMudancaNoUltimoCheque) {
		this.houverMudancaNoUltimoCheque = houverMudancaNoUltimoCheque;
	}

	public String getSituacaoNovo() {
		return situacaoNovo;
	}

	public void setSituacaoNovo(String situacaoNovo) {
		this.situacaoNovo = situacaoNovo;
	}

	public Long getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(Long totalItens) {
		this.totalItens = totalItens;
	}

	public Long getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(Long totalPago) {
		this.totalPago = totalPago;
	}

	public Long getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Long valorTotal) {
		this.valorTotal = valorTotal;
	}
}
