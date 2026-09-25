package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;

import java.util.Date;

public class NotaPesquisaTO extends DespesaPesquisaTO {

	private String numero;
	private PessoaTO credor;
	private String naturezaPagamento;
	private String status;
	private String tipo;
	private CentroFinanceiroTO centroFinanceiro;
	private PessoaTO emissor;
	private Date dataEmissor;
	private String formaPagamento;
	private UnidadeTO empresa;

	private boolean conferenciaContabilidade;
	private PessoaTO conferenciaColaborador;
	private Date conferenciaDataAtualizacao;
	private PessoaTO gerente;

	private Date dataContrFiscal;
	private PessoaTO contrFiscal;

	private String duplicata;
	private String tipoDocumentoNota;
	private Integer numBoletoAnexo;

	private Date dataRetornoFinanceiro;
	private Boolean selecao;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}

	public String getNaturezaPagamento() {
		return naturezaPagamento;
	}

	public void setNaturezaPagamento(String naturezaPagamento) {
		this.naturezaPagamento = naturezaPagamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public PessoaTO getEmissor() {
		return emissor;
	}

	public void setEmissor(PessoaTO emissor) {
		this.emissor = emissor;
	}

	public Date getDataEmissor() {
		return dataEmissor;
	}

	public void setDataEmissor(Date dataEmissor) {
		this.dataEmissor = dataEmissor;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public UnidadeTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeTO empresa) {
		this.empresa = empresa;
	}

	public boolean isConferenciaContabilidade() {
		return conferenciaContabilidade;
	}

	public void setConferenciaContabilidade(boolean conferenciaContabilidade) {
		this.conferenciaContabilidade = conferenciaContabilidade;
	}

	public PessoaTO getConferenciaColaborador() {
		return conferenciaColaborador;
	}

	public void setConferenciaColaborador(PessoaTO conferenciaColaborador) {
		this.conferenciaColaborador = conferenciaColaborador;
	}

	public Date getConferenciaDataAtualizacao() {
		return conferenciaDataAtualizacao;
	}

	public void setConferenciaDataAtualizacao(Date conferenciaDataAtualizacao) {
		this.conferenciaDataAtualizacao = conferenciaDataAtualizacao;
	}

	public PessoaTO getGerente() {
		return gerente;
	}

	public void setGerente(PessoaTO gerente) {
		this.gerente = gerente;
	}

	public Date getDataContrFiscal() {
		return dataContrFiscal;
	}

	public void setDataContrFiscal(Date dataContrFiscal) {
		this.dataContrFiscal = dataContrFiscal;
	}

	public PessoaTO getContrFiscal() {
		return contrFiscal;
	}

	public void setContrFiscal(PessoaTO contrFiscal) {
		this.contrFiscal = contrFiscal;
	}

	public String getDuplicata() {
		return duplicata;
	}

	public void setDuplicata(String duplicata) {
		this.duplicata = duplicata;
	}

	public String getTipoDocumentoNota() {
		return tipoDocumentoNota;
	}

	public void setTipoDocumentoNota(String tipoDocumentoNota) {
		this.tipoDocumentoNota = tipoDocumentoNota;
	}

	public Integer getNumBoletoAnexo() {
		return numBoletoAnexo;
	}

	public void setNumBoletoAnexo(Integer numBoletoAnexo) {
		this.numBoletoAnexo = numBoletoAnexo;
	}

	public Date getDataRetornoFinanceiro() {
		return dataRetornoFinanceiro;
	}

	public void setDataRetornoFinanceiro(Date dataRetornoFinanceiro) {
		this.dataRetornoFinanceiro = dataRetornoFinanceiro;
	}

	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

}
