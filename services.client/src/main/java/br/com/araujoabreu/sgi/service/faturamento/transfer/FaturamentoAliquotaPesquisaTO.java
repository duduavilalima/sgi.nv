package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class FaturamentoAliquotaPesquisaTO extends BaseSearchTO {

	private Long codigoFaturamento;
	private Double baseCalculo;
	private Double valor;
	private AliquotaTO aliquota;
	private FaturamentoTO faturamento;
	private String uf;
	private Long codigoTributo;
	private Long codigoCategoriaTributo;
	private Double totalTributos = 0d;
	private Double percentualTributo;

	public Long getCodigoFaturamento() {
		return codigoFaturamento;
	}

	public void setCodigoFaturamento(Long codigoFaturamento) {
		this.codigoFaturamento = codigoFaturamento;
	}

	public Double getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(Double baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public AliquotaTO getAliquota() {
		return aliquota;
	}

	public void setAliquota(AliquotaTO aliquota) {
		this.aliquota = aliquota;
	}

	public FaturamentoTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoTO faturamento) {
		this.faturamento = faturamento;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Long getCodigoTributo() {
		return codigoTributo;
	}

	public void setCodigoTributo(Long codigoTributo) {
		this.codigoTributo = codigoTributo;
	}

	public Long getCodigoCategoriaTributo() {
		return codigoCategoriaTributo;
	}

	public void setCodigoCategoriaTributo(Long codigoCategoriaTributo) {
		this.codigoCategoriaTributo = codigoCategoriaTributo;
	}

	public Double getTotalTributos() {
		return totalTributos;
	}

	public void setTotalTributos(Double totalTributos) {
		this.totalTributos = totalTributos;
	}

	public Double getPercentualTributo() {
		return percentualTributo;
	}

	public void setPercentualTributo(Double percentualTributo) {
		this.percentualTributo = percentualTributo;
	}
}
