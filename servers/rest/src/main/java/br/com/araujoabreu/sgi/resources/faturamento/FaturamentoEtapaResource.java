package br.com.araujoabreu.sgi.resources.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaCheckDescricaoTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/faturamento/etapa")
public class FaturamentoEtapaResource extends BaseCrudResource<FaturamentoEtapaTO, FaturamentoEtapaPesquisaTO, FaturamentoEtapaService> {

	private static final String ULTIMO_NUMERO_ETAPA = "/proximonumeroetapa/{id:[0-9]+}";
	private static final String BUSCAR_BY_COD_NOTA = "/getByCodNota/{id:[0-9]+}";
	private static final String BUSCAR_BY_CODIGO = "/getbycodigo/{id:[0-9]+}";

	@Autowired
	public FaturamentoEtapaResource(FaturamentoEtapaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = ULTIMO_NUMERO_ETAPA)
	public @ResponseBody
	ResponseEntity buscarProximoNumeroEtapa(@PathVariable String id) {
		try {
			return responseOk(getService().obterProximoNumeroEtapaByFatCod(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = BUSCAR_BY_COD_NOTA)
	public @ResponseBody
	ResponseEntity buscarByCodNota(@PathVariable String id) {
		try {
			return responseOk(getService().getFaturamentoEtapaByNotaSaidaId(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/verificarDescricaoEtapaDeFaturamento")
	public @ResponseBody
	ResponseEntity verificarDescricaoEtapaDeFaturamento(@RequestBody FaturamentoEtapaCheckDescricaoTO faturamentoEtapaCheckDescricaoTO) {
		try {
			return responseOk(getService().verificarDescricaoEtapaDeFaturamento(faturamentoEtapaCheckDescricaoTO));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/etapaafaturarcheck/{id:[0-9]+}")
	public @ResponseBody ResponseEntity etapaAFaturarCheck(@PathVariable String id) {
		try {
			return responseOk(getService().etapaAFaturarCheck(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = BUSCAR_BY_CODIGO)
	public @ResponseBody ResponseEntity getByCodigo(@PathVariable String id) {
		try {
			return responseOk(getService().getFaturamentoEtapaByCodigo(Long.valueOf(id)));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
