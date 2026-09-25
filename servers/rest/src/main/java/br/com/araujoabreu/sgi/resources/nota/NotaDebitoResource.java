package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.service.nota.NotaDebitoService;
import br.com.araujoabreu.sgi.service.nota.transfer.*;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/faturamento/notadebito/")
public class NotaDebitoResource extends BaseCrudResource<NotaDebitoTO, NotaDebitoPesquisaTO, NotaDebitoService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotaDebitoResource.class);

	protected static final String BUSCA_NUMERO_ND = "/busca/numero/notadebito";
	protected static final String NUMERAR_ND = "/numerarnd";
	protected static final String CANCELAR_NUMERACAO_ND = "/cancelarnumeracaond";
	protected static final String REMOVER_ND = "/removernd";
	protected static final String SALVAR_ND = "/salvarnd";
	protected static final String BUSCA_ST = "/servicotipo";
	protected static final String BUSCA_ND_IMPRESSAO = "/buscarndimpressao/{id:[0-9]+}";
	protected static final String MONTAR = "/montar/{idEtapa:[0-9]+}";

	@Autowired
	public NotaDebitoResource(NotaDebitoService service) { super(service); }

	@RequestMapping(method = RequestMethod.GET, value = "/buscarporid/{id:[0-9]+}")
	public ResponseEntity<NotaDebitoTO> buscarNotaDebitoById(@PathVariable Long id) {
		try {
			return responseOk(getService().buscarNotaDebitoById(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/receber")
	public ResponseEntity<NotaDebitoTO> receberNotaDebito(@RequestBody NotaDebitoTO notaDebitoTO) {
		try {
			return responseOk(getService().receberNotaDebito(notaDebitoTO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_NUMERO_ND)
	public @ResponseBody
	ResponseEntity<NotaDebitoTO> findLastNotaDebitoByUnidadeCodigo(@RequestBody NotaDebitoTO exampleObj) {
		try {
			return responseOk(getService().findLastNotaDebitoByUnidadeCodigo(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = MONTAR)
	public @ResponseBody
	ResponseEntity<NotaDebitoTO> novaNotaDebito(@PathVariable Long idEtapa) {
		try {
			return responseOk(getService().montarNovaNotaDebito(idEtapa));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = NUMERAR_ND)
	public ResponseEntity<NotaDebitoTO> numerarND(@RequestBody NotaDebitoTO transferObj) {
		try {
			return responseOk(getService().numerarND(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = CANCELAR_NUMERACAO_ND)
	public ResponseEntity<NotaDebitoTO> cancelarNumeracaoND(@RequestBody NotaDebitoTO transferObj) {
		try {
			return responseOk(getService().cancelarNumeracaoND(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvarProtocolo")
	public HttpEntity<Void> salvarProtocolo(@RequestBody NotaDebitoTO transferObj) {
		try {
			return responseOk(getService().salvarProtocolo(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = REMOVER_ND)
	public HttpEntity<Void> removerND(@RequestBody NotaDebitoTO transferObj) {
		try {
			getService().removerND(transferObj);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = SALVAR_ND)
	public ResponseEntity<NotaDebitoTO> saveND(@RequestBody NotaDebitoTO transferObj) {
		try {
			return responseOk(getService().saveND(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_ST)
	public @ResponseBody
	ResponseEntity buscarServicos(@RequestBody ServicoTipoTO exampleObj) {
		try {
			return responseOk(getService().buscarServicos(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = BUSCA_ND_IMPRESSAO)
	public @ResponseBody
	ResponseEntity<NotaDebitoTO> findNotaDebitoParaImpressaoByCodigo(@PathVariable Long id) {
		try {
			return responseOk(getService().buscarNotaDebitoById(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
