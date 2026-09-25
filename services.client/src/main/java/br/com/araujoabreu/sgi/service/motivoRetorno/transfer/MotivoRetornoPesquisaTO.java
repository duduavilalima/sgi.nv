package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class MotivoRetornoPesquisaTO extends BaseSearchTO {

	private String motivo;
	private String solucao;
	private MotivoAplicacaoTO motivoAplicacao;
	private PessoaTO inativador;
	private Date dataInativacao;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getSolucao() {
		return solucao;
	}

	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}

	public MotivoAplicacaoTO getMotivoAplicacao() {
		return motivoAplicacao;
	}

	public void setMotivoAplicacao(MotivoAplicacaoTO motivoAplicacao) {
		this.motivoAplicacao = motivoAplicacao;
	}

	public PessoaTO getInativador() {
		return inativador;
	}

	public void setInativador(PessoaTO inativador) {
		this.inativador = inativador;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
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
