package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.relatorios.RelatoriosService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/relatorios")
public class RelatoriosServiceResource {

	@Autowired
	private RelatoriosService relatoriosService;

	@RequestMapping(method = RequestMethod.POST, value = "/buscargerenciasporunidade")
	public @ResponseBody
	List<CentroFinanceiroTO> buscaPorUnidade(@RequestBody UnidadeTO exampleObj) {
		return relatoriosService.buscaPorUnidade(exampleObj);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarcentrosfinanceirosporgerencia")
	public @ResponseBody
	List<CentroFinanceiroTO> buscaCcClPorGerencia(@RequestBody CentroFinanceiroTO exampleObj) {
		return relatoriosService.buscaCcClPorGerencia(exampleObj);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscartodasgerencias")
	public @ResponseBody
	List<CentroFinanceiroTO> buscarTodasGerencias() {
		return relatoriosService.buscarTodasGerencias();
	}

}
