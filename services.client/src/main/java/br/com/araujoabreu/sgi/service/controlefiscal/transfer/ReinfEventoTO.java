package br.com.araujoabreu.sgi.service.controlefiscal.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class ReinfEventoTO  extends BaseTO {
	private String tipoEvento;
	private Date dataEnvio;
	private PessoaTO prestador;
	private PessoaTO cliente;
	private ReinfTO reinf;
	private String idTransmissao;
	private String situacao;
	private String numeroRecibo;

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public PessoaTO getPrestador() {
		return prestador;
	}

	public void setPrestador(PessoaTO prestador) {
		this.prestador = prestador;
	}

	public PessoaTO getCliente() {
		return cliente;
	}

	public void setCliente(PessoaTO cliente) {
		this.cliente = cliente;
	}

	public ReinfTO getReinf() {
		return reinf;
	}

	public void setReinf(ReinfTO reinf) {
		this.reinf = reinf;
	}

	public String getIdTransmissao() {
		return idTransmissao;
	}

	public void setIdTransmissao(String idTransmissao) {
		this.idTransmissao = idTransmissao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNumeroRecibo() {
		return numeroRecibo;
	}

	public void setNumeroRecibo(String numeroRecibo) {
		this.numeroRecibo = numeroRecibo;
	}
}
