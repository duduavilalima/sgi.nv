package br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer;

import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;


public class LoteInterfacePesquisaTO extends BaseSearchTO {

	private String situacao;
	private String tipoLoteInterface;
	private UnidadeTO unidade;
	private Date inicio;
	private Date termino;
	private Date dataEvento;
	private Date dataEmissaoDe;
	private Date dataEmissaoAte;
	private Date dataCancelamentoDe;
	private Date dataCancelamentoAte;
	private Date dataExecucaoDe;
	private Date dataExecucaoAte;
	private String criador;
	private Date dataCriacao;
	private String numero;
	private String mensagemErro;
	private String ordem;


	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoLoteInterface() {
		return tipoLoteInterface;
	}

	public void setTipoLoteInterface(String tipoLoteInterface) {
		this.tipoLoteInterface = tipoLoteInterface;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getTermino() {
		return termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}

	public Date getDataEvento() {
		return dataEvento;
	}

	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}

	public Date getDataExecucaoDe() {
		return dataExecucaoDe;
	}

	public void setDataExecucaoDe(Date dataExecucaoDe) {
		this.dataExecucaoDe = dataExecucaoDe;
	}

	public Date getDataExecucaoAte() {
		return dataExecucaoAte;
	}

	public void setDataExecucaoAte(Date dataExecucaoAte) {
		this.dataExecucaoAte = dataExecucaoAte;
	}

	public Date getDataEmissaoDe() {
		return dataEmissaoDe;
	}

	public void setDataEmissaoDe(Date dataEmissaoDe) {
		this.dataEmissaoDe = dataEmissaoDe;
	}

	public Date getDataEmissaoAte() {
		return dataEmissaoAte;
	}

	public void setDataEmissaoAte(Date dataEmissaoAte) {
		this.dataEmissaoAte = dataEmissaoAte;
	}

	public Date getDataCancelamentoDe() {
		return dataCancelamentoDe;
	}

	public void setDataCancelamentoDe(Date dataCancelamentoDe) {
		this.dataCancelamentoDe = dataCancelamentoDe;
	}

	public Date getDataCancelamentoAte() {
		return dataCancelamentoAte;
	}

	public void setDataCancelamentoAte(Date dataCancelamentoAte) {
		this.dataCancelamentoAte = dataCancelamentoAte;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getNumero() {
		if (this.getCodigo() != null)
			return "LC/" + this.getCodigo();
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}
}
