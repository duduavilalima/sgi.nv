package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.etapasNaoFaturadas.EtapasNaoFaturadasService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalFaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.araujoabreu.sgi.util.ResponseSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest/faturamento/etapanaofaturada")
public class EtapasNaoFaturadasResource extends ResponseSupport {

	protected static final String ID = "/{id:[0-9]+}";
	protected static final String ID_NF = "/busca/notafaturamento/{id:[0-9]+}";
	protected static final String CREATE = "/create";
	protected static final String BUSCA_NS = "/busca/notaSaida";
	private static final Logger LOGGER = LoggerFactory.getLogger(EtapasNaoFaturadasResource.class);
	@Autowired
	private EtapasNaoFaturadasService etapasNaoFaturadasService;

	@RequestMapping(method = RequestMethod.GET, value = ID_NF)
	public @ResponseBody
	ResponseEntity findNotaFiscalFaturamento(@PathVariable String id) {
		try {
			return responseOk(etapasNaoFaturadasService.findNotaFiscalFaturamento(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = ID)
	public @ResponseBody
	ResponseEntity createEtapaNaoFaturada(@PathVariable String id) {
		try {
			return responseOk(etapasNaoFaturadasService.createEtapaNaoFaturada(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_NS)
	public @ResponseBody
	ResponseEntity findLastNotaSaidaByUnidadeCodigo(@RequestBody NotaFiscalFaturamentoTO exampleObj) {
		try {
			return responseOk(etapasNaoFaturadasService.findLastNotaSaidaByUnidadeCodigo(exampleObj));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/servico")
	public @ResponseBody
	ResponseEntity buscarServicos(@RequestBody ServicoTipoTO exampleObj) {
		try {
			return responseOk(etapasNaoFaturadasService.buscarServicos(exampleObj));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity save(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			return responseOk(etapasNaoFaturadasService.save(transferObj));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/validarCL/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity validarCL(@PathVariable Long id) {
		try {
			return responseOk(etapasNaoFaturadasService.validarCL(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

}
