package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RelacaoBancariaPesquisaTO extends BaseSearchTO {

	private String numero;
	private String status;
	private Date pagamentoDe;
	private Date pagamentoAte;
	private Date dataPagamento;
	private UnidadeTO unidadePesquisa;
	private BancoTO bancoPesquisa;
	private String meioPagamento;
	private ContaCorrenteTO contaCorrente;
	private ItemRelacaoTO itemRelacao;
	//	private List<ItemRelacao> itens = new ArrayList<ItemRelacao>();
	private BigDecimal total;
	private Boolean pagamentoIndividual;
	private Boolean impressao2via = Boolean.FALSE;
	private Boolean impressaoEndosso = Boolean.FALSE;
	private Boolean impressaoCheque = Boolean.FALSE;
	private Integer qtdPagamentos;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private Date dataEnvio;
	private Date dataConciliacao;
	private Date dataCancelamento;
	private String numeroPagamento;
	private PessoaTO credor;
	private String nomeCredor;
	private List<TributoTO> tributos;

	private int totalPgtos;
	private int totalPgtosPagos;
	private BigDecimal totalPago;
	private int totalPgtosEstornados;
	private BigDecimal totalEstornados;
	private int totalPgtosRestantes;
	private BigDecimal totalRestantes;

	private int totalPgtosItens;
	private BigDecimal totalItens;

	public RelacaoBancariaPesquisaTO() {

	}

	public ItemRelacaoTO getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(ItemRelacaoTO itemRelacao) {
		this.itemRelacao = itemRelacao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}


	public BancoTO getBancoPesquisa() {
		return bancoPesquisa;
	}

	public void setBancoPesquisa(BancoTO bancoPesquisa) {
		this.bancoPesquisa = bancoPesquisa;
	}

	public String getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(String meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public UnidadeTO getUnidade() {
		return unidadePesquisa;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidadePesquisa = unidade;
	}

	public ContaCorrenteTO getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteTO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Boolean getPagamentoIndividual() {
		return pagamentoIndividual;
	}

	public void setPagamentoIndividual(Boolean pagamentoIndividual) {
		this.pagamentoIndividual = pagamentoIndividual;
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

	public UnidadeTO getUnidadePesquisa() {
		return unidadePesquisa;
	}

	public void setUnidadePesquisa(UnidadeTO unidadePesquisa) {
		this.unidadePesquisa = unidadePesquisa;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
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

	public String getNumeroPagamento() {
		return numeroPagamento;
	}

	public void setNumeroPagamento(String numeroPagamento) {
		this.numeroPagamento = numeroPagamento;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}

	public String getNomeCredor() {
		return nomeCredor;
	}

	public void setNomeCredor(String nomeCredor) {
		this.nomeCredor = nomeCredor;
	}

	public List<TributoTO> getTributos() {
		return tributos;
	}

	public void setTributos(List<TributoTO> tributos) {
		this.tributos = tributos;
	}

	public Boolean getImpressaoEndosso() {
		return impressaoEndosso;
	}

	public void setImpressaoEndosso(Boolean impressaoEndosso) {
		this.impressaoEndosso = impressaoEndosso;
	}

	public Boolean getImpressaoCheque() {
		return impressaoCheque;
	}

	public void setImpressaoCheque(Boolean impressaoCheque) {
		this.impressaoCheque = impressaoCheque;
	}

	public int getTotalPgtos() {
		return totalPgtos;
	}

	public void setTotalPgtos(int totalPgtos) {
		this.totalPgtos = totalPgtos;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public int getTotalPgtosEstornados() {
		return totalPgtosEstornados;
	}

	public void setTotalPgtosEstornados(int totalPgtosEstornados) {
		this.totalPgtosEstornados = totalPgtosEstornados;
	}

	public BigDecimal getTotalEstornados() {
		return totalEstornados;
	}

	public void setTotalEstornados(BigDecimal totalEstornados) {
		this.totalEstornados = totalEstornados;
	}

	public int getTotalPgtosPagos() {
		return totalPgtosPagos;
	}

	public void setTotalPgtosPagos(int totalPgtosPagos) {
		this.totalPgtosPagos = totalPgtosPagos;
	}

	public int getTotalPgtosRestantes() {
		return totalPgtosRestantes;
	}

	public void setTotalPgtosRestantes(int totalPgtosRestantes) {
		this.totalPgtosRestantes = totalPgtosRestantes;
	}

	public BigDecimal getTotalRestantes() {
		return totalRestantes;
	}

	public void setTotalRestantes(BigDecimal totalRestantes) {
		this.totalRestantes = totalRestantes;
	}

	public int getTotalPgtosItens() {
		return totalPgtosItens;
	}

	public void setTotalPgtosItens(int totalPgtosItens) {
		this.totalPgtosItens = totalPgtosItens;
	}

	public BigDecimal getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(BigDecimal totalItens) {
		this.totalItens = totalItens;
	}
}
