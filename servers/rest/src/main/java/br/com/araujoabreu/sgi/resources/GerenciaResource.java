package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.gerencia.GerenciaService;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaPesquisaTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/gerencia")
public class GerenciaResource extends BaseCrudResource<GerenciaTO, GerenciaPesquisaTO, GerenciaService> {

	private static final String GET_GERENCIA_SIMPLES_BY_ID = "/getGerenciaSimplesById/{id:[0-9]+}";

	@Autowired
	public GerenciaResource(GerenciaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarbyunidade")
	public List<GerenciaTO> getCFsByUnidade(@RequestBody UnidadeTO unidade) throws Exception {
		return getService().getCFsByUnidade(unidade);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarTodasGerenciasTelaRelatorio")
	public ResponseEntity<List<GerenciaTO>> buscarTodasGerenciasTelaRelatorio() {
		try {
			return responseOk(getService().buscarTodasGerenciasTelaRelatorio());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscartodasgerenciasHabFat")
	public @ResponseBody
	ResponseEntity<List<CentroFinanceiroTO>> buscarTodasGerenciasHabilitadasFaturamento() {
		try {
			return responseOk(getService().buscarTodasGerenciasHabilitadasFaturamento());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = GET_GERENCIA_SIMPLES_BY_ID)
	public @ResponseBody
	ResponseEntity getGerenciaSimplesById(@PathVariable String id) {
		try {
			return responseOk(getService().obterGerencia(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getgerenciasimplesativas")
	public @ResponseBody ResponseEntity getGerenciaSimplesAtivas() {
		try {
			return responseOk(getService().getGerenciaSimplesAtivas());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/perfil/diretoria/{id:[0-9]+}")
	public List<GerenciaTO> buscarGerenciasPorPerfilUsuarioLogado(@PathVariable Long id) throws Exception {
		return getService().getGerenciaSimplesAtivasUsuarioLogado(id);
	}

}
