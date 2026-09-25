package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class ClientePesquisaTO extends BaseSearchTO {
	private String documento;
	private String situacao;
	private String nome;
	private String nomeFantasia;
	private RamoNegocioTO ramoNegocio;
	private CidadeTO cidade;
	private String tipo;
	private String tipoPessoa;
	private Long codigoClientePrincipal;
	private ClassificacaoClienteTO classificacaoCliente;
	private Boolean faturamentoCobranca;
	private Boolean possuiContrato;
	private Integer conta;

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public RamoNegocioTO getRamoNegocio() {
		return ramoNegocio;
	}

	public void setRamoNegocio(RamoNegocioTO ramoNegocio) {
		this.ramoNegocio = ramoNegocio;
	}

	public CidadeTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeTO cidade) {
		this.cidade = cidade;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Long getCodigoClientePrincipal() {
		return codigoClientePrincipal;
	}

	public void setCodigoClientePrincipal(Long codigoClientePrincipal) {
		this.codigoClientePrincipal = codigoClientePrincipal;
	}

	public ClassificacaoClienteTO getClassificacaoCliente() {
		return classificacaoCliente;
	}

	public void setClassificacaoCliente(ClassificacaoClienteTO classificacaoCliente) {
		this.classificacaoCliente = classificacaoCliente;
	}

	public Boolean getFaturamentoCobranca() {
		return faturamentoCobranca;
	}

	public void setFaturamentoCobranca(Boolean faturamentoCobranca) {
		this.faturamentoCobranca = faturamentoCobranca;
	}

	public Boolean getPossuiContrato() {
		return possuiContrato;
	}

	public void setPossuiContrato(Boolean possuiContrato) {
		this.possuiContrato = possuiContrato;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}
}
