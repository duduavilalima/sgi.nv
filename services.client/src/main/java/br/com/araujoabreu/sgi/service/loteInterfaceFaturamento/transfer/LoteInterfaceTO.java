package br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class LoteInterfaceTO extends BaseTO {
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
    private PessoaTO responsavel;
	private Long qtdLote;
	private String mensagemErro;
	private Date dataImportacao;
	private ColaboradorTO colaborador;

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

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
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

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public PessoaTO getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(PessoaTO responsavel) {
		this.responsavel = responsavel;
	}

	public Long getQtdLote() {
		return qtdLote;
	}

	public void setQtdLote(Long qtdLote) {
		this.qtdLote = qtdLote;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}
}
