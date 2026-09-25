package br.com.fasolti.sgi.job.despesascl;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;

import java.util.List;

public class DespesasCLEmailBean {

	private Integer total;
	private String dataDespesas;
	private String despesasLimite;
	private List<CentroFinanceiroTO> cFList;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getDataDespesas() {
		return dataDespesas;
	}

	public void setDataDespesas(String dataDespesas) {
		this.dataDespesas = dataDespesas;
	}

	public String getDespesasLimite() {
		return despesasLimite;
	}

	public void setDespesasLimite(String despesasLimite) {
		this.despesasLimite = despesasLimite;
	}

	public List<CentroFinanceiroTO> getcFList() {
		return cFList;
	}

	public void setcFList(List<CentroFinanceiroTO> cFList) {
		this.cFList = cFList;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("DespesasCLEmailBean{");
		sb.append("dataDespesas='").append(dataDespesas).append('\'');
		sb.append(", despesasLimite='").append(despesasLimite).append('\'');
		sb.append(", cFList=").append(cFList);
		sb.append('}');
		return sb.toString();
	}
}
