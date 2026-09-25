package br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas;


import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.FiltroGraficoCLTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.GraficoChartTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.GraficoFaturamentoDespesasTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.ViewCLTO;

import java.util.List;

public interface GraficoFaturamentoDespesasService {

	List<ViewCLTO> buscarCLsPorGerenciaUnidade(FiltroGraficoCLTO filtroGraficoCLTO);

	GraficoChartTO gerarGraficoFaturamento(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO);

	GraficoChartTO gerarGraficoReceitaLiquida(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO);

	GraficoChartTO gerarGraficoCustos(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO);

	GraficoChartTO gerarGraficoMargem(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO);

}
