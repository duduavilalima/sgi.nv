package br.com.araujoabreu.sgi.service.contaCorrente.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class ContaCorrentePesquisaTO extends BaseSearchTO {

	private PessoaTO pessoa;
	private BancoTO banco;
	private String agencia;
	private String digitoAgencia;
	private String conta;
	private String digito;
	private Boolean principal = false;
	private Boolean homologado = false;
	private Boolean selecao;
	private Date dataHomologacao;
	private String tipoHomologacao;
	private String tipoConta;
	private String tipoContaCorrente;
	private Boolean internetBanking;
	private Boolean boletoFaturamento = Boolean.FALSE;
	private Boolean chequeCarbono;
	private Boolean conciliacao;

	private UnidadeTO unidade;

	// Pesquisa
	private UnidadeTO unidadePesquisa;
	private DadosBancariosTO dadosBancario;
	private String tipoUnidade;

	public PessoaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaTO pessoa) {
		this.pessoa = pessoa;
	}

	public BancoTO getBanco() {
		return banco;
	}

	public void setBanco(BancoTO banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public Boolean getHomologado() {
		return homologado;
	}

	public void setHomologado(Boolean homologado) {
		this.homologado = homologado;
	}

	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	public Date getDataHomologacao() {
		return dataHomologacao;
	}

	public void setDataHomologacao(Date dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	public String getTipoHomologacao() {
		return tipoHomologacao;
	}

	public void setTipoHomologacao(String tipoHomologacao) {
		this.tipoHomologacao = tipoHomologacao;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getTipoContaCorrente() {
		return tipoContaCorrente;
	}

	public void setTipoContaCorrente(String tipoContaCorrente) {
		this.tipoContaCorrente = tipoContaCorrente;
	}

	public Boolean getInternetBanking() {
		return internetBanking;
	}

	public void setInternetBanking(Boolean internetBanking) {
		this.internetBanking = internetBanking;
	}

	public Boolean getBoletoFaturamento() {
		return boletoFaturamento;
	}

	public void setBoletoFaturamento(Boolean boletoFaturamento) {
		this.boletoFaturamento = boletoFaturamento;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public UnidadeTO getUnidadePesquisa() {
		return unidadePesquisa;
	}

	public void setUnidadePesquisa(UnidadeTO unidadePesquisa) {
		this.unidadePesquisa = unidadePesquisa;
	}

	public DadosBancariosTO getDadosBancario() {
		return dadosBancario;
	}

	public void setDadosBancario(DadosBancariosTO dadosBancario) {
		this.dadosBancario = dadosBancario;
	}

	public String getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(String tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}

	public Boolean getChequeCarbono() {
		return chequeCarbono;
	}

	public void setChequeCarbono(Boolean chequeCarbono) {
		this.chequeCarbono = chequeCarbono;
	}

	public Boolean getConciliacao() {
		return conciliacao;
	}

	public void setConciliacao(Boolean conciliacao) {
		this.conciliacao = conciliacao;
	}
}
