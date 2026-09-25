package br.com.araujoabreu.sgi.service.banco.transfer;


import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class BancoPesquisaTO extends BaseSearchTO {

	private String nome;
	private String descricao;
	private String situacao;
	private Boolean homologado;

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

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getHomologado() {
		return homologado;
	}

	public void setHomologado(Boolean homologado) {
		this.homologado = homologado;
	}
}
