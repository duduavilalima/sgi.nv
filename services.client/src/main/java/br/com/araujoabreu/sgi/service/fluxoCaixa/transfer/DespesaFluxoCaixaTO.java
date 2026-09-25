package br.com.araujoabreu.sgi.service.fluxoCaixa.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

/**
 * Created by vanessa.marcia on 27/04/17.
 */
public class DespesaFluxoCaixaTO extends BaseTO {

	public String sigla;
	public String descricao;
	public String situacao;

	/* Aba Controles*/
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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
