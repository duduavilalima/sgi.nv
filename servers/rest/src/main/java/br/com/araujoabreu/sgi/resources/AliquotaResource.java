package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.categoriaTributo.AliquotaService;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/aliquota")
public class AliquotaResource extends BaseCrudResource<AliquotaTO, AliquotaPesquisaTO, AliquotaService> {

	@Autowired
	public AliquotaResource(AliquotaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getListaAliquotaByCodCategoriaTributo/{id:[0-9]+}")
	public @ResponseBody
	List<AliquotaTO> getListaAliquotaByCodCategoriaTributo(@PathVariable String id) {
		return getService().getListaAliquotaByCodCategoriaTributo(Long.valueOf(id));
	}

}
