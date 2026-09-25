package br.com.araujoabreu.sgi.resources.fecho;

import br.com.araujoabreu.sgi.service.fecho.FechoDespesaGeralService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/fechodespesageral")
public class FechoDespesaGeralResource extends BaseCrudResource<FechoDespesaGeralTO, FechoDespesaGeralPesquisaTO, FechoDespesaGeralService> {

	@Autowired
	public FechoDespesaGeralResource(FechoDespesaGeralService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/despesaByCodFecho/{id:[0-9]+}")
	public @ResponseBody
	List<FechoDespesaGeralTO> despesaByCodFecho(@PathVariable String id) throws Exception {
		return getService().despesaByCodFecho(id);
	}
}
