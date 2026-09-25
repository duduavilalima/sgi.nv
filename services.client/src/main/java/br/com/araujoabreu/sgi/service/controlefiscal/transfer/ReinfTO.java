package br.com.araujoabreu.sgi.service.controlefiscal.transfer;

import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.List;

public class ReinfTO extends BaseTO {
	private String referencia;
	private Date dataCriacao;
	private Date dataAtualizacao;
//	private List<ReinfEvento> eventos;
	private String reinfStatus;
	private String versao;

	private UnidadeTO unidade;

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getReinfStatus() {
		return reinfStatus;
	}

	public void setReinfStatus(String reinfStatus) {
		this.reinfStatus = reinfStatus;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}
}
