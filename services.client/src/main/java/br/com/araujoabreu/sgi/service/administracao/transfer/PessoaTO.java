package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.comercial.transfer.ClassificacaoClienteTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.List;

public class PessoaTO extends BaseTO {

	private String documento;
	private String nome;
	private String nomeFantasia;
	private String tipoPessoa;
	private String situacao;
	private Long codigoEndereco;
	private ClassificacaoClienteTO classificacaoCliente;
	private List<TelefoneTO> telefones;
	private String email;
	private PessoaTO clientePrincipal;
	private PessoaTO homologador;
	private Boolean homologado = false;
	private Date dataHomologacao;
	private Boolean faturamentoCobranca = false;
	private EnderecoTO endereco;
	private String inscricaoEstadualCliente;

	public PessoaTO() {
	}

	public PessoaTO(Long codigo, String documento, String nome, String tipoPessoa, String situacao) {
		this.setCodigo(codigo);
		this.documento = documento;
		this.nome = nome;
		this.tipoPessoa = tipoPessoa;
		this.situacao = situacao;
	}

	public PessoaTO(Long codigo, String nome) {
		this.setCodigo(codigo);
		this.nome = nome;
	}

	public PessoaTO(Long codigo) {
		this.setCodigo(codigo);
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Long getCodigoEndereco() {
		return codigoEndereco;
	}

	public void setCodigoEndereco(Long codigoEndereco) {
		this.codigoEndereco = codigoEndereco;
	}

	public ClassificacaoClienteTO getClassificacaoCliente() {
		return classificacaoCliente;
	}

	public void setClassificacaoCliente(ClassificacaoClienteTO classificacaoCliente) {
		this.classificacaoCliente = classificacaoCliente;
	}

	public List<TelefoneTO> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneTO> telefones) {
		this.telefones = telefones;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PessoaTO getClientePrincipal() {
		return clientePrincipal;
	}

	public void setClientePrincipal(PessoaTO clientePrincipal) {
		this.clientePrincipal = clientePrincipal;
	}

	public PessoaTO getHomologador() {
		return homologador;
	}

	public void setHomologador(PessoaTO homologador) {
		this.homologador = homologador;
	}

	public Boolean getHomologado() {
		return homologado;
	}

	public void setHomologado(Boolean homologado) {
		this.homologado = homologado;
	}

	public Date getDataHomologacao() {
		return dataHomologacao;
	}

	public void setDataHomologacao(Date dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	public Boolean getFaturamentoCobranca() {
		return faturamentoCobranca;
	}

	public void setFaturamentoCobranca(Boolean faturamentoCobranca) {
		this.faturamentoCobranca = faturamentoCobranca;
	}

	public EnderecoTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoTO endereco) {
		this.endereco = endereco;
	}

	public String getInscricaoEstadualCliente() {
		return inscricaoEstadualCliente;
	}

	public void setInscricaoEstadualCliente(String inscricaoEstadualCliente) {
		this.inscricaoEstadualCliente = inscricaoEstadualCliente;
	}
}
