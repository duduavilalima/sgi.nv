package br.com.araujoabreu.sgi.resources.centroFinanceiro;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.CentroFinanceiroFechoTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
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
@RequestMapping("/rest/centrofinanceiro")
public class CentroFinanceiroResource extends BaseCrudResource<CentroFinanceiroTO, CentroFinanceiroPesquisaTO, CentroFinanceiroService> {

	@Autowired
	public CentroFinanceiroResource(CentroFinanceiroService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/gerencialogada")
	public @ResponseBody
	ResponseEntity<CentroFinanceiroTO> getGerenciaLogada() {
		try {
			return responseOk(getService().getGerenciaLogada());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


	@RequestMapping(method = RequestMethod.POST, value = "/buscarPorUnidade")
	public ResponseEntity<List<CentroFinanceiroTO>> getCFsByUnidade(@RequestBody CentroFinanceiroPesquisaTO exampleObj) {
		try {
			return responseOk(getService().getCFsByUnidade(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


	@RequestMapping(method = RequestMethod.POST, value = "/buscarPorUnidadeTelaRelatorio")
	public ResponseEntity<List<CentroFinanceiroTO>> getCFsByUnidadeTelaRelatorio(@RequestBody CentroFinanceiroPesquisaTO exampleObj) {
		try {
			return responseOk(getService().getCFsByUnidadeTelaRelatorio(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscargerenciasporunidade/{id:[0-9]+}")
	public ResponseEntity<List<GerenciaTO>> buscarTodasGerencias(@PathVariable("id") String id) {
		try {
			return responseOk(getService().buscarGerenciasPorUnidade(new Long(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscargerenciaspesquisa")
	public ResponseEntity<List<GerenciaTO>> buscarGerenciasPesquisa() {
		try {
			return responseOk(getService().buscarGerenciasPesquisa());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscargerenciasedicao")
	public ResponseEntity<List<GerenciaTO>> buscarGerenciasEdicao(@RequestBody CentroFinanceiroTO centroFinanceiroTO) {
		try {
			return responseOk(getService().buscarGerenciasEdicao(centroFinanceiroTO));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscargerenciaspordiretoria")
	public ResponseEntity<List<GerenciaTO>> buscarGerenciasPorDiretoria(@RequestBody DiretoriaSimpleTO diretoria) {
		try {
			return responseOk(getService().buscarGerenciasPorDiretoria(diretoria));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{idGerencia:[0-9]+}/buscargerenciascontratos")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarGerenciasPorContratos(@PathVariable String idGerencia) {
		try {
			return responseOk(getService().buscarGerenciasPorContratos(idGerencia));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarCFs")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarCFs(@RequestBody CentroFinanceiroTO exampleObj) {
		try {
			return responseOk(getService().buscarCFs(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarFaturamentoCFs")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarFaturamentoCFs(@RequestBody CentroFinanceiroTO exampleObj) {
		try {
			return responseOk(getService().buscarFaturamentoCFs(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inativarById/{id:[0-9]+}")
	public HttpEntity<Void> inativarById(@PathVariable String id) {
		try {
			getService().inativarById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/reativarById/{id:[0-9]+}")
	public HttpEntity<Void> reativarById(@PathVariable String id) {
		try {
			getService().reativarById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/paralisarById/{id:[0-9]+}")
	public HttpEntity<Void> paralisarById(@PathVariable String id) {
		try {
			getService().paralisarById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/encerrarById/{id:[0-9]+}")
	public HttpEntity<Void> encerrarById(@PathVariable String id) {
		try {
			getService().encerrarById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/encerrarFatById/{id:[0-9]+}")
	public HttpEntity<Void> encerrarFatById(@PathVariable String id) {
		try {
			getService().encerrarFatById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cancelarById/{id:[0-9]+}")
	public HttpEntity<Void> cancelarById(@PathVariable String id) {
		try {
			getService().cancelarById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/removerById/{id:[0-9]+}")
	public HttpEntity<Void> removerById(@PathVariable String id) {
		try {
			getService().removerById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarclporcliente/{id}")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarClPorCliente(@PathVariable("id") String id) {
		try {
			return responseOk(getService().buscarClPorCliente(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscartodasgerencias")
	public @ResponseBody
	ResponseEntity<List<CentroFinanceiroTO>> buscarTodasGerencias() {
		try {
			return responseOk(getService().buscarTodasGerencias());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarGerenciasPorRegional/{id:[0-9]+}")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarGerenciasPorRegional(@PathVariable("id") String id) {
		try {
			return responseOk(getService().buscarGerenciasPorRegional(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvarAlterarFecho")
	public ResponseEntity salvarAlterarFecho(@RequestBody CentroFinanceiroFechoTO centroFinanceiroFechoTO) {
		try {
			getService().salvarAlterarFecho(centroFinanceiroFechoTO);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/criarFecho")
	public ResponseEntity criarFecho(@RequestBody CentroFinanceiroFechoTO centroFinanceiroFechoTO) {
		try {
			getService().criarFecho(centroFinanceiroFechoTO);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/atualizarFecho")
	public ResponseEntity atualizarFecho(@RequestBody CentroFinanceiroFechoTO centroFinanceiroFechoTO) {
		try {
			getService().atualizarFecho(centroFinanceiroFechoTO);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/findAllGerencias")
	public @ResponseBody
	ResponseEntity<List<CentroFinanceiroTO>> findAllGerencias() {
		try {
			return responseOk(getService().findAllGerencias());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/obterlistacentroslucrosativos")
	public @ResponseBody
	ResponseEntity<List<CentroFinanceiroTO>> obterListaCentrosLucrosAtivos() {
		try {
			return responseOk(getService().obterListaCentrosLucrosAtivos());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarcfssimples")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarCFsSimples(@RequestBody CentroFinanceiroTO exampleObj) {
		try {
			return responseOk(getService().buscarCFsSimples(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarCFsPorPerfilFaturamento")
	public ResponseEntity<List<CentroFinanceiroTO>> buscarCFsPorPerfilFaturamento() {
		try {
			return responseOk(getService().getCentroFinanceiroPerfilFaturamento());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
