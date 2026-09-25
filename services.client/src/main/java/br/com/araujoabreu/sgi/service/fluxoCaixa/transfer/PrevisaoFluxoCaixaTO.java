package br.com.araujoabreu.sgi.service.fluxoCaixa.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class PrevisaoFluxoCaixaTO extends BaseTO {

	private UnidadeSimpleTO unidade;
	private DiretoriaSimpleTO diretoria;
	private DespesaFluxoCaixaTO despesaFluxoCaixa;
	private Date data;
	private Double valor;

	/*Aba Controle*/
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String nomeCriador;
	private String nomeAtualizador;
	private String nomeInativador;


	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public DespesaFluxoCaixaTO getDespesaFluxoCaixa() {
		return despesaFluxoCaixa;
	}

	public void setDespesaFluxoCaixa(DespesaFluxoCaixaTO despesaFluxoCaixa) {
		this.despesaFluxoCaixa = despesaFluxoCaixa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
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
}
