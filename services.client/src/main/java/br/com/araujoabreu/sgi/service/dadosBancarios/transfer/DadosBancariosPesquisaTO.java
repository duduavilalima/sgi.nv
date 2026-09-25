package br.com.araujoabreu.sgi.service.dadosBancarios.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;


public class DadosBancariosPesquisaTO extends BaseSearchTO {
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
	private String situacao;
	private PessoaTO responsavelInativacao;
	private Date dataInativacao;

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

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public PessoaTO getResponsavelInativacao() {
		return responsavelInativacao;
	}

	public void setResponsavelInativacao(PessoaTO responsavelInativacao) {
		this.responsavelInativacao = responsavelInativacao;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}
}
