package br.com.araujoabreu.sgi.resources.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroPrevisaoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPrevisaoPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPrevisaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/centrofinanceiroprevisao")
public class CentroFinanceiroPrevisaoResource extends BaseCrudResource<CentroFinanceiroPrevisaoTO, CentroFinanceiroPrevisaoPesquisaTO, CentroFinanceiroPrevisaoService> {

	private static final String BUSCAR_PREVISOES = "buscarPrevisoes";
	private static final String UPDATE_PREVISOES = "updatePrevisoes";

	@Autowired
	public CentroFinanceiroPrevisaoResource(CentroFinanceiroPrevisaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCAR_PREVISOES)
	public @ResponseBody
	ResponseEntity buscarPrevisoes(@RequestBody CentroFinanceiroPrevisaoPesquisaTO exampleObj) {
		try {
			return responseOk(getService().buscarPrevisoes(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = UPDATE_PREVISOES)
	public @ResponseBody
	ResponseEntity updatePrevisoes(@RequestBody List<CentroFinanceiroPrevisaoTO> lista) {
		try {
			getService().updatePrevisoes(lista);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
