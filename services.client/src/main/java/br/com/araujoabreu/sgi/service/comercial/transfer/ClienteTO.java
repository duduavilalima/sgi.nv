package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;

import java.util.Date;
import java.util.List;

public class ClienteTO extends PessoaTO {

	private RamoNegocioTO cnae;
	private String ufInscricaoEstadual;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;
	private String tipoPessoaJuridica;
	private String tipoPessoaFisica;
	private Boolean mei;


	/*Aba Contato*/
	private String canalPreferencial;
	/*SubAba Telefone*/
	private List<TelefoneTO> telefones;
	/*SubAba Endereço*/
	private EnderecoTO endereco;
	/*SubAba Email*/
	private String email;
	private String emailContato;

	/*Aba Controle*/
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String inativador;
	private Date dataInativacao;
	private String nomeCriador;
	private String nomeAtualizador;
	private String nomeInativador;

	public ClienteTO() {
	}

	public ClienteTO(Long codigo, String documento) {
		setCodigo(codigo);
		setDocumento(documento);
	}

	public RamoNegocioTO getCnae() {
		return cnae;
	}

	public void setCnae(RamoNegocioTO cnae) {
		this.cnae = cnae;
	}

	public String getUfInscricaoEstadual() {
		return ufInscricaoEstadual;
	}

	public void setUfInscricaoEstadual(String ufInscricaoEstadual) {
		this.ufInscricaoEstadual = ufInscricaoEstadual;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getCanalPreferencial() {
		return canalPreferencial;
	}

	public void setCanalPreferencial(String canalPreferencial) {
		this.canalPreferencial = canalPreferencial;
	}

	public List<TelefoneTO> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneTO> telefones) {
		this.telefones = telefones;
	}

	public EnderecoTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoTO endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
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

	public String getInativador() {
		return inativador;
	}

	public void setInativador(String inativador) {
		this.inativador = inativador;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public String getNomeCriador() {
		return nomeCriador;
	}

	public void setNomeCriador(String nomeCriador) {
		this.nomeCriador = nomeCriador;
	}

	public String getNomeAtualizador() {
		return nomeAtualizador;
	}

	public void setNomeAtualizador(String nomeAtualizador) {
		this.nomeAtualizador = nomeAtualizador;
	}

	public String getNomeInativador() {
		return nomeInativador;
	}

	public void setNomeInativador(String nomeInativador) {
		this.nomeInativador = nomeInativador;
	}

	public String getTipoPessoaJuridica() {
		return tipoPessoaJuridica;
	}

	public void setTipoPessoaJuridica(String tipoPessoaJuridica) {
		this.tipoPessoaJuridica = tipoPessoaJuridica;
	}

	public String getTipoPessoaFisica() {
		return tipoPessoaFisica;
	}

	public void setTipoPessoaFisica(String tipoPessoaFisica) {
		this.tipoPessoaFisica = tipoPessoaFisica;
	}

	public Boolean getMei() {
		return mei;
	}

	public void setMei(Boolean mei) {
		this.mei = mei;
	}
}
