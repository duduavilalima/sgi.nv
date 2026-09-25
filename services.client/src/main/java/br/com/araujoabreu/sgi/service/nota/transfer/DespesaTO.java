package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.math.BigDecimal;
import java.util.Date;

public class DespesaTO extends BaseTO implements SearchObject {
	private BigDecimal valor = new BigDecimal(0);
	private BigDecimal valorHomologado = new BigDecimal(0);
	private String descricao;
	private Date data;
	private int version;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorHomologado() {
		return valorHomologado;
	}

	public void setValorHomologado(BigDecimal valorHomologado) {
		this.valorHomologado = valorHomologado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
