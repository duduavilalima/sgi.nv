package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.nota.ParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
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

@RestController
@RequestMapping("/rest/contasareceber/parcela")
public class ParcelaResource extends BaseCrudResource<NotaSaidaParcelaTO, NotaSaidaParcelaPesquisaTO, ParcelaService> {

	protected static final String ID_UPDATE = "/update/{id:[0-9]+}";

	protected static final String CANCELAR_RECEBIMENTO = "/recebimento/cancelar/{id:[0-9]+}";

	protected static final String VERIFICAR_LOTE = "/verificarLote/{id:[0-9]+}";

	@Autowired
	public FeriadoService feriadoService;

	@Autowired
	public ParcelaResource(ParcelaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.PUT, value = ID_UPDATE)
	public ResponseEntity updateParcela(@RequestBody NotaSaidaParcelaTO transferObj, @PathVariable Long id) {
		try {
			return responseOk(getService().updateParcela(transferObj, id));
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/recebimento/{id:[0-9]+}")
	public ResponseEntity updateParcelaRecebimento(@RequestBody NotaSaidaParcelaTO transferObj, @PathVariable Long id) {
		try {
			return responseOk(getService().updateParcelaRecebimento(transferObj, id));
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = CANCELAR_RECEBIMENTO)
	public HttpEntity<Void> cancelarRecebimentoParcela(@PathVariable String id) {
		try {
			getService().cancelarParcelaRecebimento(Long.parseLong(id));
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updateAgendamento/{id:[0-9]+}")
	public ResponseEntity updateAgendamento(@RequestBody NotaSaidaParcelaTO transferObj, @PathVariable Long id) {
		try {
			return responseOk(getService().updateAgendamento(transferObj, id));
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/confirmarOuAjustarDataAgendamento")
	public ResponseEntity confirmarOuAjustarDataAgendamento(@RequestBody NotaSaidaParcelaTO transferObj) {
		try {
			transferObj.setDataVencimento(transferObj.getDataVencimento());
			transferObj.setDataPagamento(feriadoService.devolveDataUtil(transferObj.getDataVencimento()));
			transferObj.setDataAgendamento(feriadoService.devolveDataUtil(transferObj.getDataPagamento(), 1));
			return responseOk(transferObj);
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updateMarcaCobrancaCronica/{id:[0-9]+}")
	public ResponseEntity marcarCobrancaCronica(@RequestBody NotaSaidaParcelaTO transferObj, @PathVariable Long id) {
		try {
			return responseOk(getService().marcarCobrancaCronica(transferObj, id));
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updateDesmarcaCobrancaCronica/{id:[0-9]+}")
	public ResponseEntity desmarcarCobrancaCronica(@RequestBody NotaSaidaParcelaTO transferObj, @PathVariable Long id) {
		try {
			return responseOk(getService().desmarcarCobrancaCronica(transferObj, id));
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvarObservacao")
	public ResponseEntity salvarObservacao(@RequestBody NotaSaidaParcelaTO transferObj) {
		try {
			getService().salvarObservacao(transferObj);
			return responseOk();
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = VERIFICAR_LOTE)
	public @ResponseBody
	ResponseEntity<NotaTO> verificarLote(@PathVariable String id) {
		try {
			Boolean resultado = getService().parcelaDisponivelParaLote(Long.valueOf(id));
			NotaTO notaTO = new NotaTO();
			notaTO.setSelecao(resultado);
			return responseOk(notaTO);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
