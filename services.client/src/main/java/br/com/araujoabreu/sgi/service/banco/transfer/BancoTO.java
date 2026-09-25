package br.com.araujoabreu.sgi.service.banco.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.Objects;

public class BancoTO extends BaseTO {

	private String nome;
	private String descricao;
	private String situacao;
	private Boolean homologado;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public BancoTO() {
	}

	public BancoTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getHomologado() {
		return homologado;
	}

	public void setHomologado(Boolean homologado) {
		this.homologado = homologado;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BancoTO)) return false;
		if (!super.equals(o)) return false;
		BancoTO bancoTO = (BancoTO) o;
		return Objects.equals(getNome(), bancoTO.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome());
	}
}
