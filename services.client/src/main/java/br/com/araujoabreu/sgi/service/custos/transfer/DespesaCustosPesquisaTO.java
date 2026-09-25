package br.com.araujoabreu.sgi.service.custos.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Objects;

public class DespesaCustosPesquisaTO extends BaseSearchTO {

	private DespesaCustosSimpleTO pai;
	private String descricao;
	private Integer ordem;
	private String situacao;
	private String tipo;
	private String operacao;

	public DespesaCustosPesquisaTO() {
	}

	public DespesaCustosPesquisaTO(DespesaCustosSimpleTO pai, String descricao, Integer ordem, String situacao, String tipo, String operacao) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DespesaCustosPesquisaTO)) return false;
		if (!super.equals(o)) return false;
		DespesaCustosPesquisaTO that = (DespesaCustosPesquisaTO) o;
		return Objects.equals(getDescricao(), that.getDescricao()) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			Objects.equals(getSituacao(), that.getSituacao()) &&
			Objects.equals(getTipo(), that.getTipo()) &&
			Objects.equals(getOperacao(), that.getOperacao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDescricao(), getOrdem(), getSituacao(), getTipo(), getOperacao());
	}

	@Override
	public String toString() {
		return "DespesaCustosPesquisaTO{" +
			"descricao='" + descricao + '\'' +
			", ordem=" + ordem +
			", situacao='" + situacao + '\'' +
			", tipo='" + tipo + '\'' +
			", operacao='" + operacao + '\'' +
			'}';
	}
}
