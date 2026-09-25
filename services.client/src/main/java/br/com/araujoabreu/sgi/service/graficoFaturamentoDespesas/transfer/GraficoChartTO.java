package br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;

/**
 * TO de retorno de resultado, para a montagem de gráficos na tela "Custos > Gráfico Faturamento/Despesas"
 */
public class GraficoChartTO extends BaseTO {

	private List<Double> previsto;
	private List<Double> realizado;
	private List<Double> extra;
	private List<Double> previsaoMO;

	public List<Double> getPrevisaoMO() {
		return previsaoMO;
	}

	public void setPrevisaoMO(List<Double> previsaoMO) {
		this.previsaoMO = previsaoMO;
	}

	public List<Double> getPrevisto() {
		return previsto;
	}

	public void setPrevisto(List<Double> previsto) {
		this.previsto = previsto;
	}

	public List<Double> getRealizado() {
		return realizado;
	}

	public void setRealizado(List<Double> realizado) {
		this.realizado = realizado;
	}

	public List<Double> getExtra() {
		return extra;
	}

	public void setExtra(List<Double> extra) {
		this.extra = extra;
	}
}
