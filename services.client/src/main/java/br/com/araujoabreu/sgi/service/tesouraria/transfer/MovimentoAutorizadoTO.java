package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovimentoAutorizadoTO extends BaseTO {

	private String numero;
	private UnidadeTO credor;
	private CentroFinanceiroTO gerencia;
	private String tipoDocumentoPagamento;
	private String tipo;

	private String descricao;
	private int checagem;

	private ContaContabilTO contaContabil;
	private BancoTO banco;

	private Date data;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataMovimento;
	private Date dataAprovador;
	private Date dataAutorizador;
	private Date dataLiberacao;
	private Date dataAnalista;

	private DadosBancariosTO dadosBancarios;

	private BigDecimal tributoMaisOutrosDescNP;

	private TributoTO tributoExibicao;

	private BigDecimal valor = BigDecimal.ZERO;
	private BigDecimal valorNota = BigDecimal.ZERO;
	private BigDecimal valorAPagar = BigDecimal.ZERO;
	private BigDecimal valor_liquido = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private BigDecimal valorAdiantamento = BigDecimal.ZERO;
	private BigDecimal valorTributo = BigDecimal.ZERO;
	private BigDecimal totalOutrosDescontos = BigDecimal.ZERO;

	private Boolean selecao;
	private Boolean atualizadoTesouraria = Boolean.FALSE;
	private boolean linhaDigitavelInformada = false;

	private Date dataTesouraria;
	private String linhaDigitavel;
	private String codigoBarras;
	private String infoBoleto;
	private BancoTO bancoBoleto;
	private PagamentoTO ultimaParcela;
	private List<PagamentoContaContabilTO> listaPgtoCC = new ArrayList<>();

	public MovimentoAutorizadoTO() {
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public UnidadeTO getCredor() {
		return credor;
	}

	public void setCredor(UnidadeTO credor) {
		this.credor = credor;
	}

	public CentroFinanceiroTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(CentroFinanceiroTO gerencia) {
		this.gerencia = gerencia;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getChecagem() {
		return checagem;
	}

	public void setChecagem(int checagem) {
		this.checagem = checagem;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public BancoTO getBanco() {
		return banco;
	}

	public void setBanco(BancoTO banco) {
		this.banco = banco;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public DadosBancariosTO getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancariosTO dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	public BigDecimal getTributoMaisOutrosDescNP() {
		return tributoMaisOutrosDescNP;
	}

	public void setTributoMaisOutrosDescNP(BigDecimal tributoMaisOutrosDescNP) {
		this.tributoMaisOutrosDescNP = tributoMaisOutrosDescNP;
	}

	public TributoTO getTributoExibicao() {
		return tributoExibicao;
	}

	public void setTributoExibicao(TributoTO tributoExibicao) {
		this.tributoExibicao = tributoExibicao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorNota() {
		return valorNota;
	}

	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	public BigDecimal getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(BigDecimal valorPagar) {
		this.valorAPagar = valorPagar;
	}

	public BigDecimal getValor_liquido() {
		return valor_liquido;
	}

	public void setValor_liquido(BigDecimal valor_liquido) {
		this.valor_liquido = valor_liquido;
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

	public BigDecimal getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(BigDecimal valorTributo) {
		this.valorTributo = valorTributo;
	}

	public BigDecimal getTotalOutrosDescontos() {
		return totalOutrosDescontos;
	}

	public void setTotalOutrosDescontos(BigDecimal totalOutrosDescontos) {
		this.totalOutrosDescontos = totalOutrosDescontos;
	}

	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	public Boolean getAtualizadoTesouraria() {
		return atualizadoTesouraria;
	}

	public void setAtualizadoTesouraria(Boolean atualizadoTesouraria) {
		this.atualizadoTesouraria = atualizadoTesouraria;
	}

	public boolean isLinhaDigitavelInformada() {
		return linhaDigitavelInformada;
	}

	public void setLinhaDigitavelInformada(boolean linhaDigitavelInformada) {
		this.linhaDigitavelInformada = linhaDigitavelInformada;
	}

	public Date getDataTesouraria() {
		return dataTesouraria;
	}

	public void setDataTesouraria(Date dataTesouraria) {
		this.dataTesouraria = dataTesouraria;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getInfoBoleto() {
		return infoBoleto;
	}

	public void setInfoBoleto(String infoBoleto) {
		this.infoBoleto = infoBoleto;
	}

	public BancoTO getBancoBoleto() {
		return bancoBoleto;
	}

	public void setBancoBoleto(BancoTO bancoBoleto) {
		this.bancoBoleto = bancoBoleto;
	}

	public PagamentoTO getUltimaParcela() {
		return ultimaParcela;
	}

	public void setUltimaParcela(PagamentoTO ultimaParcela) {
		this.ultimaParcela = ultimaParcela;
	}

	public List<PagamentoContaContabilTO> getListaPgtoCC() {
		return listaPgtoCC;
	}

	public void setListaPgtoCC(List<PagamentoContaContabilTO> listaPgtoCC) {
		this.listaPgtoCC = listaPgtoCC;
	}
}
