package br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;
import java.util.Objects;

public class FiltroGraficoCLTO extends BaseTO {

	private Long codigoCentroFinanceiro;
	private List<Long> listaCodEmpresa;
	private Integer ano;

	public FiltroGraficoCLTO() {
	}

	public Long getCodigoCentroFinanceiro() {
		return codigoCentroFinanceiro;
	}

	public void setCodigoCentroFinanceiro(Long codigoCentroFinanceiro) {
		this.codigoCentroFinanceiro = codigoCentroFinanceiro;
	}

	public List<Long> getListaCodEmpresa() {
		return listaCodEmpresa;
	}

	public void setListaCodEmpresa(List<Long> listaCodEmpresa) {
		this.listaCodEmpresa = listaCodEmpresa;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FiltroGraficoCLTO)) return false;
		if (!super.equals(o)) return false;
		FiltroGraficoCLTO that = (FiltroGraficoCLTO) o;
		return Objects.equals(getCodigoCentroFinanceiro(), that.getCodigoCentroFinanceiro()) &&
			Objects.equals(getListaCodEmpresa(), that.getListaCodEmpresa()) &&
			Objects.equals(getAno(), that.getAno());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigoCentroFinanceiro(), getListaCodEmpresa(), getAno());
	}
}
