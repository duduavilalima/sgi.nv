package br.com.araujoabreu.sgi.resources.fecho;

import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/fechomaodeobra")
public class FechoMaoDeObraResource extends BaseCrudResource<FechoMaoDeObraTO, FechoMaoDeObraPesquisaTO, FechoMaoDeObraService> {

	@Autowired
	public FechoMaoDeObraResource(FechoMaoDeObraService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maoDeObraByCodFecho/{id:[0-9]+}")
	public @ResponseBody
	List<FechoMaoDeObraTO> maoDeObraByCodFecho(@PathVariable String id) throws Exception {
		return getService().maoDeObraByCodFecho(id);
	}
}
