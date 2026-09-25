package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MovimentoAutorizadoPesquisaTO extends BaseSearchTO {

	// Pesquisa

	private UnidadeTO empresa;
	private CentroFinanceiroTO gerencia;
	private ContaContabilTO contaContabil;
	private String numero;
	private BancoTO banco;
	private String nomeCredor;
	private Date pagamentoDe;
	private Date pagamentoAte;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	private List<TributoTO> tributoPesquisa;

	// Retorno

	private DadosBancariosTO dadosBancarios;
	private BigDecimal valorPagar;
	private BigDecimal valor;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private BigDecimal valorAdiantamento = BigDecimal.ZERO;
	private Long codigoNota;

	private PessoaTO credor;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataMovimento;
	private Date dataAprovador;
	private Date dataAutorizador;
	private Date dataLiberacao;
	private Date dataAnalista;
	private String status;
	private String tipo;
	private String tipoDocumentoPagamento;

	private TributoTO tributoExibicao;

	public MovimentoAutorizadoPesquisaTO() {
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

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

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

	public String getNomeCredor() {
		return nomeCredor;
	}

	public void setNomeCredor(String nomeCredor) {
		this.nomeCredor = nomeCredor;
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

	public List<TributoTO> getTributoPesquisa() {
		return tributoPesquisa;
	}

	public void setTributoPesquisa(List<TributoTO> tributoPesquisa) {
		this.tributoPesquisa = tributoPesquisa;
	}

	public DadosBancariosTO getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancariosTO dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	public BigDecimal getValorAdiantamento() {
		return valorAdiantamento;
	}

	public void setValorAdiantamento(BigDecimal valorAdiantamento) {
		this.valorAdiantamento = valorAdiantamento;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
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

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public Date getDataAprovador() {
		return dataAprovador;
	}

	public void setDataAprovador(Date dataAprovador) {
		this.dataAprovador = dataAprovador;
	}

	public Date getDataAutorizador() {
		return dataAutorizador;
	}

	public void setDataAutorizador(Date dataAutorizador) {
		this.dataAutorizador = dataAutorizador;
	}

	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Date getDataAnalista() {
		return dataAnalista;
	}

	public void setDataAnalista(Date dataAnalista) {
		this.dataAnalista = dataAnalista;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public TributoTO getTributoExibicao() {
		return tributoExibicao;
	}

	public void setTributoExibicao(TributoTO tributoExibicao) {
		this.tributoExibicao = tributoExibicao;
	}

	public Long getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Long codigoNota) {
		this.codigoNota = codigoNota;
	}
}
