package br.com.araujoabreu.sgi.service.custos.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class DespesaCustosSimpleTO extends BaseTO {

	private String descricao;
	private Integer ordem;
	private String situacao;
	private String tipo;
	private String operacao;

	public DespesaCustosSimpleTO() {
	}

	public DespesaCustosSimpleTO(String descricao, Integer ordem, String situacao, String tipo, String operacao) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.situacao = situacao;
		this.tipo = tipo;
		this.operacao = operacao;
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String descricao;
		private Integer ordem;
		private String situacao;
		private String tipo;
		private String operacao;

		public Builder() {
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

		public DespesaCustosSimpleTO build() {
			DespesaCustosSimpleTO obj = new DespesaCustosSimpleTO();
			obj.descricao = this.descricao;
			obj.ordem = this.ordem;
			obj.situacao = this.situacao;
			obj.tipo = this.tipo;
			obj.operacao = this.operacao;
			return obj;
		}

	}
}
