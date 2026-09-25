package br.com.araujoabreu.sgi.service.comercial.transfer;


import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;

public class ObraTO extends BaseTO {

	private TipoObraTO tipoObra;
	private EnderecoTO endereco;
	private String nome;
	private String situacao;
	private String setor;
	private Date inicioVigencia;
	private Date fimVigencia;
	private BigDecimal m2Total;
	private Short numPavimentos;
	private Short numSalas;
	private Short numLojas;
	private String descricao;
	private Date dataInativacao;
	private Short prazo;
	private ObraClienteTO clientePrincipal;
	private ObraClienteTO clienteSecundario;

	public TipoObraTO getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(TipoObraTO tipoObra) {
		this.tipoObra = tipoObra;
	}

	public EnderecoTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoTO endereco) {
		this.endereco = endereco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public Date getInicioVigencia() {
		return inicioVigencia;
	}

	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public Date getFimVigencia() {
		return fimVigencia;
	}

	public void setFimVigencia(Date fimVigencia) {
		this.fimVigencia = fimVigencia;
	}

	public BigDecimal getM2Total() {
		return m2Total;
	}

	public void setM2Total(BigDecimal m2Total) {
		this.m2Total = m2Total;
	}

	public Short getNumPavimentos() {
		return numPavimentos;
	}

	public void setNumPavimentos(Short numPavimentos) {
		this.numPavimentos = numPavimentos;
	}

	public Short getNumSalas() {
		return numSalas;
	}

	public void setNumSalas(Short numSalas) {
		this.numSalas = numSalas;
	}

	public Short getNumLojas() {
		return numLojas;
	}

	public void setNumLojas(Short numLojas) {
		this.numLojas = numLojas;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public Short getPrazo() {
		return prazo;
	}

	public void setPrazo(Short prazo) {
		this.prazo = prazo;
	}

	public ObraClienteTO getClientePrincipal() {
		return clientePrincipal;
	}

	public void setClientePrincipal(ObraClienteTO clientePrincipal) {
		this.clientePrincipal = clientePrincipal;
	}

	public ObraClienteTO getClienteSecundario() {
		return clienteSecundario;
	}

	public void setClienteSecundario(ObraClienteTO clienteSecundario) {
		this.clienteSecundario = clienteSecundario;
	}
}
