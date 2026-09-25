package br.com.araujoabreu.sgi.resources.fecho;

import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraAdicionalService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/fechomaodeobraadicional")
public class FechoMaoDeObraAdicionalResource extends BaseCrudResource<FechoMaoDeObraAdicionalTO, FechoMaoDeObraAdicionalPesquisaTO, FechoMaoDeObraAdicionalService> {

	@Autowired
	public FechoMaoDeObraAdicionalResource(FechoMaoDeObraAdicionalService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/adicionalByCodMaoDeObra/{id:[0-9]+}")
	public @ResponseBody
	List<FechoMaoDeObraAdicionalTO> adicionalByCodMaoDeObra(@PathVariable String id) throws Exception {
		return getService().adicionalByCodMaoDeObra(id);
	}
}
