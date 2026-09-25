package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class SituacaoTO extends BaseTO {

	private String atualizador;
	private String chave;
	private String criador;
	private Date dataAtualizacao;
	private Date dataCriacao;
	private String descricao;
	private String nome;
	private Integer ordem;
	private Boolean permissaoCancelar;
	private Boolean permissaoEditar;
	private Boolean permissaoReativar;
	private Boolean permissaoSuspender;
	private String subsituacao;
	private String tabela;

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getPermissaoCancelar() {
		return permissaoCancelar;
	}

	public void setPermissaoCancelar(Boolean permissaoCancelar) {
		this.permissaoCancelar = permissaoCancelar;
	}

	public Boolean getPermissaoEditar() {
		return permissaoEditar;
	}

	public void setPermissaoEditar(Boolean permissaoEditar) {
		this.permissaoEditar = permissaoEditar;
	}

	public Boolean getPermissaoReativar() {
		return permissaoReativar;
	}

	public void setPermissaoReativar(Boolean permissaoReativar) {
		this.permissaoReativar = permissaoReativar;
	}

	public Boolean getPermissaoSuspender() {
		return permissaoSuspender;
	}

	public void setPermissaoSuspender(Boolean permissaoSuspender) {
		this.permissaoSuspender = permissaoSuspender;
	}

	public String getSubsituacao() {
		return subsituacao;
	}

	public void setSubsituacao(String subsituacao) {
		this.subsituacao = subsituacao;
	}

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
}
