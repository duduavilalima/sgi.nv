package br.com.araujoabreu.sgi.resources.centroFinanceiro;

import br.com.araujoabreu.sgi.lang.Response;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroFluxoEtapaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroFluxoEtapaPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroFluxoEtapaTO;
import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.RetornoAssinaturaTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/centrofinanceirofluxoetapa")
public class CentroFinanceiroFluxoEtapaResource extends BaseCrudResource<CentroFinanceiroFluxoEtapaTO, CentroFinanceiroFluxoEtapaPesquisaTO, CentroFinanceiroFluxoEtapaService> {

	@Autowired
	public CentroFinanceiroFluxoEtapaResource(CentroFinanceiroFluxoEtapaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscartodosetapafluxo")
	public @ResponseBody
	List<EtapaFluxoTO> buscarTodosEtapaFluxo() {
		return getService().buscarTodosEtapaFluxo();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/motivoAplicacaoByFluxoEtapaId/{id:[0-9]+}")
	public MotivoAplicacaoTO motivoAplicacaoByFluxoEtapaId(@PathVariable String id) throws Exception {
		return getService().motivoAplicacaoByFluxoEtapaId(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/assinarCentroFinanceiro/{codEtapaFluxo:[0-9]+}/{codCentroFinanceiro:[0-9]+}")
	public Response assinarCentroFinanceiro(@PathVariable String codEtapaFluxo, @PathVariable String codCentroFinanceiro) {

		Response.Builder response = new Response.Builder();
		MenssagemPadraoTO menssagemPadraoTO = getService().assinarCentroFinanceiro(Long.parseLong(codEtapaFluxo), Long.parseLong(codCentroFinanceiro));
		if (menssagemPadraoTO.getWarnings() != null && !menssagemPadraoTO.getWarnings().isEmpty()){
			response.warnings(menssagemPadraoTO.getWarnings());
		}
		return response.build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/retornarAssinatura")
	public ResponseEntity retornarAssinatura(@RequestBody RetornoAssinaturaTO retornoAssinaturaTO) {
		try {
			getService().retornarAssinaturaCentroFinanceiro(retornoAssinaturaTO);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/retornarAssinaturaSemMotivoRetorno")
	public ResponseEntity retornarAssinaturaSemMotivoRetorno(@RequestBody RetornoAssinaturaTO retornoAssinaturaTO) {
		try {
			getService().retornarAssinaturaSemMotivoRetorno(retornoAssinaturaTO);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
