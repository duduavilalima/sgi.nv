package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class NotaDebitoPesquisaTO extends BaseSearchTO {

	private String numero;
	private String situacao;
	private UnidadeTO empresa;
	private ServicoTipoTO servicoTipo;
	private PessoaTO cliente;
	private CentroFinanceiroTO centroFinanceiro;
	private ContaCorrenteTO contaCorrentePrevista;
	private ContaCorrenteTO contaCorrenteRecebimento;
	private Date dataEmissao;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataAgendamento;
	private Date dataContabilizacao;
	private String tipoData;
	private Date dataDe;
	private Date dataAte;
	private String descricao;
	private BigDecimal valorBruto;
	private BigDecimal valorLiquidoRecebido;
	private String tipoFornecimento;
	private String origemPesquisa;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public UnidadeTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeTO empresa) {
		this.empresa = empresa;
	}

	public ServicoTipoTO getServicoTipo() {
		return servicoTipo;
	}

	public void setServicoTipo(ServicoTipoTO servicoTipo) {
		this.servicoTipo = servicoTipo;
	}

	public PessoaTO getCliente() {
		return cliente;
	}

	public void setCliente(PessoaTO cliente) {
		this.cliente = cliente;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoData() {
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public ContaCorrenteTO getContaCorrentePrevista() {
		return contaCorrentePrevista;
	}

	public void setContaCorrentePrevista(ContaCorrenteTO contaCorrentePrevista) {
		this.contaCorrentePrevista = contaCorrentePrevista;
	}

	public ContaCorrenteTO getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(ContaCorrenteTO contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getValorLiquidoRecebido() {
		return valorLiquidoRecebido;
	}

	public void setValorLiquidoRecebido(BigDecimal valorLiquidoRecebido) {
		this.valorLiquidoRecebido = valorLiquidoRecebido;
	}

	public String getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(String tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	public String getOrigemPesquisa() {
		return origemPesquisa;
	}

	public void setOrigemPesquisa(String origemPesquisa) {
		this.origemPesquisa = origemPesquisa;
	}
}
