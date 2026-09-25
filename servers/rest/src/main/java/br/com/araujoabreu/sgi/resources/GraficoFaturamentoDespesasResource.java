package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.GraficoFaturamentoDespesasService;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.FiltroGraficoCLTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.GraficoChartTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.GraficoFaturamentoDespesasTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.ViewCLTO;
import br.com.araujoabreu.sgi.util.ResponseSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/graficofaturamentodespesas")
public class GraficoFaturamentoDespesasResource extends ResponseSupport {

	private static final String BUSCAR_CLS = "/buscarCLs";
	private static final String GERAR_GRAFICO_FATURAMENTO = "/gerarGraficoFaturamento";
	private static final String GERAR_GRAFICO_RECEITA_LIQUIDA = "/gerarGraficoReceitaLiquida";
	private static final String GERAR_GRAFICO_CUSTOS = "/gerarGraficoCustos";
	private static final String GERAR_GRAFICO_MARGEM = "/gerarGraficoMargem";

	@Autowired
	private GraficoFaturamentoDespesasService graficoFaturamentoDespesasService;


	@RequestMapping(method = RequestMethod.POST, value = BUSCAR_CLS)
	public ResponseEntity<List<ViewCLTO>> buscarCLs(@RequestBody FiltroGraficoCLTO filtroGraficoCLTO) {
		try {
			return responseOk(graficoFaturamentoDespesasService.buscarCLsPorGerenciaUnidade(filtroGraficoCLTO));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


	@RequestMapping(method = RequestMethod.POST, value = GERAR_GRAFICO_FATURAMENTO)
	public ResponseEntity<GraficoChartTO> gerarGraficoFaturamento(@RequestBody GraficoFaturamentoDespesasTO exampleObj) {
		try {
			return responseOk(graficoFaturamentoDespesasService.gerarGraficoFaturamento(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = GERAR_GRAFICO_RECEITA_LIQUIDA)
	public ResponseEntity<GraficoChartTO> gerarGraficoReceitaLiquida(@RequestBody GraficoFaturamentoDespesasTO exampleObj) {
		try {
			return responseOk(graficoFaturamentoDespesasService.gerarGraficoReceitaLiquida(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = GERAR_GRAFICO_CUSTOS)
	public ResponseEntity<GraficoChartTO> gerarGraficoCustos(@RequestBody GraficoFaturamentoDespesasTO exampleObj) {
		try {
			return responseOk(graficoFaturamentoDespesasService.gerarGraficoCustos(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = GERAR_GRAFICO_MARGEM)
	public ResponseEntity<GraficoChartTO> gerarGraficoMargem(@RequestBody GraficoFaturamentoDespesasTO exampleObj) {
		try {
			return responseOk(graficoFaturamentoDespesasService.gerarGraficoMargem(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
