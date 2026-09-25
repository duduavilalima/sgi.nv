package br.com.araujoabreu.sgi.service.security.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class PerfilPesquisaTO extends BaseSearchTO {

	private String nome;
	private String descricao;
	private String tipoAcesso; // opcional
	private Boolean obrigatorioGerencia; // opcional
	private Boolean obrigatorioBanco; // opcional

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(String tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	public Boolean getObrigatorioGerencia() {
		return obrigatorioGerencia;
	}

	public void setObrigatorioGerencia(Boolean obrigatorioGerencia) {
		this.obrigatorioGerencia = obrigatorioGerencia;
	}

	public Boolean getObrigatorioBanco() {
		return obrigatorioBanco;
	}

	public void setObrigatorioBanco(Boolean obrigatorioBanco) {
		this.obrigatorioBanco = obrigatorioBanco;
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


}
