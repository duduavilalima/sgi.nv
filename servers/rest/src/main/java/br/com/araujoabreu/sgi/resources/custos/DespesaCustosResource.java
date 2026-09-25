package br.com.araujoabreu.sgi.resources.custos;

import br.com.araujoabreu.sgi.service.custos.DespesaCustosService;
import br.com.araujoabreu.sgi.service.custos.transfer.DespesaCustosPesquisaTO;
import br.com.araujoabreu.sgi.service.custos.transfer.DespesaCustosTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest/despesacustos")
public class DespesaCustosResource extends BaseCrudResource<DespesaCustosTO, DespesaCustosPesquisaTO, DespesaCustosService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DespesaCustosResource.class);

	@Autowired
	public DespesaCustosResource(DespesaCustosService despesaCustosService) {
		super(despesaCustosService);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/obterListaDespesasSimplesAtivas")
	public @ResponseBody ResponseEntity obterListaDespesasSimplesAtivas() {
		try {
			return responseOk(getService().obterListaDespesasSimplesAtivas());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
