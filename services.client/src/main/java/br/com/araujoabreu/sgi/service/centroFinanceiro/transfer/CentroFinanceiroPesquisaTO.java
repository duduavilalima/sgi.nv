package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoSimpleTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaSimpleTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class CentroFinanceiroPesquisaTO extends BaseSearchTO {

	private Long codigoCliente;
	private String identificadorFinal;
	private String nome;
	private Long codigoContrato;
	private UnidadeSimpleTO unidade;
	private String situacao;

	private String identificador;
	private CentroFinanceiroSimpleTO pai;
	private ContratoSimpleTO contrato;
	private Boolean cumulativo;
	private GerenciaSimpleTO gerencia;

	private String UF;
	private CidadeTO cidade;
	private Boolean permiteClienteLivre;

	public Long getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getIdentificadorFinal() {
		return identificadorFinal;
	}

	public void setIdentificadorFinal(String identificadorFinal) {
		this.identificadorFinal = identificadorFinal;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public CentroFinanceiroSimpleTO getPai() {
		return pai;
	}

	public void setPai(CentroFinanceiroSimpleTO pai) {
		this.pai = pai;
	}

	public ContratoSimpleTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoSimpleTO contrato) {
		this.contrato = contrato;
	}

	public Boolean getCumulativo() {
		return cumulativo;
	}

	public void setCumulativo(Boolean cumulativo) {
		this.cumulativo = cumulativo;
	}

	public GerenciaSimpleTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(GerenciaSimpleTO gerencia) {
		this.gerencia = gerencia;
	}

	public String getUF() {
		return UF;
	}

	public void setUF(String UF) {
		this.UF = UF;
	}

	public CidadeTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeTO cidade) {
		this.cidade = cidade;
	}

	public Boolean getPermiteClienteLivre() {
		return permiteClienteLivre;
	}

	public void setPermiteClienteLivre(Boolean permiteClienteLivre) {
		this.permiteClienteLivre = permiteClienteLivre;
	}
}
