package br.com.araujoabreu.sgi.resources.faturamento;

import br.com.araujoabreu.sgi.lang.Response;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalExibicaoTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/faturamento")
public class FaturamentoResource extends BaseCrudResource<FaturamentoTO, FaturamentoPesquisaTO, FaturamentoService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FaturamentoResource.class);

	@Autowired
	public FaturamentoResource(FaturamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/busca/notafiscal")
	public ResponseEntity<NotaFiscalExibicaoTO> findNotaFiscalFaturamento(@RequestBody FaturamentoEtapaTO etapa) {
		try {
			return responseOk(getService().findNotaFiscalFaturamento(etapa));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/faturamentoanterior/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity faturamentoAnterior(@PathVariable String id) {
		try {
			return responseOk(getService().faturamentoAnterior(new Long(id)));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/busca/notafiscal/paginado")
	public ResponseEntity<Iterable<NotaFiscalExibicaoTO>> buscaPorExemploNotasFiscaisFaturamento(
		@RequestBody FaturamentoTO exampleObj,
		@RequestParam(defaultValue = "0") Integer pageNumber,
		@RequestParam(defaultValue = "asc") String sortDir,
		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
		@RequestParam(required = false) String sortField,
		@RequestParam(required = false, defaultValue = "false") String sortFieldString) {

		Sort sort = null;
		if (sortField != null) {
			Sort.Direction dir = Sort.Direction.fromOptionalString(sortDir).orElse(null);
			if (dir != null) {
				sort = new Sort(dir, sortField);
			}
		}

		try {
			return ResponseEntity.ok(getService().montarNotaFiscalFaturamento(exampleObj, PageRequest.of(pageNumber, pageSize, sort)));
		} catch (ValidationException var9) {
			return this.responseServerError(var9);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/busca/notadebito/paginado")
	public ResponseEntity<Iterable<NotaDebitoTO>> buscaPorExemploNotasDebitoFaturamento(
		@RequestBody FaturamentoTO exampleObj,
		@RequestParam(defaultValue = "0") Integer pageNumber,
		@RequestParam(defaultValue = "asc") String sortDir,
		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
		@RequestParam(required = false) String sortField,
		@RequestParam(required = false, defaultValue = "false") String sortFieldString) {

		Sort sort = null;
		if (sortField != null) {
			Sort.Direction dir = Sort.Direction.fromOptionalString(sortDir).orElse(null);
			if (dir != null) {
				sort = new Sort(dir, sortField);
			}
		}

		try {
			return ResponseEntity.ok(getService().montarNotaDebitoFaturamento(exampleObj, PageRequest.of(pageNumber, pageSize, sort)));
		} catch (ValidationException var9) {
			return this.responseServerError(var9);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/notafiscalabertacheck/{id:[0-9]+}")
	public @ResponseBody ResponseEntity notaFiscalAbertaCheck(@PathVariable String id) {
		try {
			return responseOk(getService().notaFiscalAbertaCheck(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/verificarContaContabilCliente/{codigoFaturamento:[0-9]+}")
	public Response verificarContaContabilCliente(@PathVariable String codigoFaturamento) {

		Response.Builder response = new Response.Builder();
		MenssagemPadraoTO menssagemPadraoTO = getService().possuiContaContabilAssociada(Long.parseLong(codigoFaturamento));
		if (menssagemPadraoTO.getWarnings() != null && !menssagemPadraoTO.getWarnings().isEmpty()){
			response.warnings(menssagemPadraoTO.getWarnings());
		}
		return response.build();
	}
}
