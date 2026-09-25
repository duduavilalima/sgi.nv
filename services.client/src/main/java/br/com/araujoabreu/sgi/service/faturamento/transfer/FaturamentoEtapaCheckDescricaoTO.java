package br.com.araujoabreu.sgi.service.faturamento.transfer;

public class FaturamentoEtapaCheckDescricaoTO {

	private Long codigoEtapa;
	private Long codigoFaturamento;
	private String descricao;
	private Integer numeroEtapa;
	private boolean descriptionExists = Boolean.FALSE;

	public Long getCodigoEtapa() {
		return codigoEtapa;
	}

	public void setCodigoEtapa(Long codigoEtapa) {
		this.codigoEtapa = codigoEtapa;
	}

	public Long getCodigoFaturamento() {
		return codigoFaturamento;
	}

	public void setCodigoFaturamento(Long codigoFaturamento) {
		this.codigoFaturamento = codigoFaturamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getNumeroEtapa() {
		return numeroEtapa;
	}

	public void setNumeroEtapa(Integer numeroEtapa) {
		this.numeroEtapa = numeroEtapa;
	}

	public boolean isDescriptionExists() {
		return descriptionExists;
	}

	public void setDescriptionExists(boolean descriptionExists) {
		this.descriptionExists = descriptionExists;
	}
}
