package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class FaturamentoAliquotaTO extends BaseTO {

	private Long codigoFaturamento;
	private BigDecimal baseCalculo = new BigDecimal("0");
	private BigDecimal valor;
	private AliquotaTO aliquota;
	private FaturamentoTO faturamento;
	private String uf;
	private Long codigoTributo;
	private Long codigoCategoriaTributo;
	private BigDecimal totalTributos = new BigDecimal("0");
	private BigDecimal percentualTributo;

	public Long getCodigoFaturamento() {
		return codigoFaturamento;
	}

	public void setCodigoFaturamento(Long codigoFaturamento) {
		this.codigoFaturamento = codigoFaturamento;
	}

	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
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

	public BigDecimal getTotalTributos() {
		return totalTributos;
	}

	public void setTotalTributos(BigDecimal totalTributos) {
		this.totalTributos = totalTributos;
	}

	public BigDecimal getPercentualTributo() {
		return percentualTributo;
	}

	public void setPercentualTributo(BigDecimal percentualTributo) {
		this.percentualTributo = percentualTributo;
	}
}
