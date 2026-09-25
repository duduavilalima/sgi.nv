package br.com.araujoabreu.sgi.service.custos.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;
import br.com.fasolti.commons.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DespesaCustosTO extends BaseTO {

	private DespesaCustosSimpleTO pai;
	private String descricao;
	private Integer ordem;
	private String situacao;
	private String tipo;
	private String operacao;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public DespesaCustosTO() {
	}

	public DespesaCustosTO(DespesaCustosSimpleTO pai, String descricao, Integer ordem, String situacao, String tipo, String operacao) {
		this.pai = pai;
		this.descricao = descricao;
		this.ordem = ordem;
		this.situacao = situacao;
		this.tipo = tipo;
		this.operacao = operacao;
	}

	public DespesaCustosSimpleTO getPai() {
		return pai;
	}

	public void setPai(DespesaCustosSimpleTO pai) {
		this.pai = pai;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private DespesaCustosSimpleTO pai;
		private String descricao;
		private Integer ordem;
		private String situacao;
		private String tipo;
		private String operacao;

		public Builder() {
		}

		public Builder pai(DespesaCustosSimpleTO despesaCustos) {
			this.pai = despesaCustos;
			return this;
		}

		public Builder descricao(String descricao) {
			this.descricao = descricao;
			return this;
		}

		public Builder ordem(Integer ordem) {
			this.ordem = ordem;
			return this;
		}

		public Builder situacao(String situacao) {
			this.situacao = situacao;
			return this;
		}

		public Builder tipo(String tipo) {
			this.tipo = tipo;
			return this;
		}

		public Builder operacao(String operacao) {
			this.operacao = operacao;
			return this;
		}

		public DespesaCustosTO build() {
			DespesaCustosTO obj = new DespesaCustosTO();
			obj.pai = this.pai;
			obj.descricao = this.descricao;
			obj.ordem = this.ordem;
			obj.situacao = this.situacao;
			obj.tipo = this.tipo;
			obj.operacao = this.operacao;
			return obj;
		}

	}
}
