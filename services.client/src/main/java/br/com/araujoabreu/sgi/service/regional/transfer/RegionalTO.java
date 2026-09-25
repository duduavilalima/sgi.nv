package br.com.araujoabreu.sgi.service.regional.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class RegionalTO extends BaseTO {

	private String descricao;
	private String situacao;
	private DiretoriaSimpleTO diretoria;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

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

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoriaTO) {
		this.diretoria = diretoriaTO;
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
