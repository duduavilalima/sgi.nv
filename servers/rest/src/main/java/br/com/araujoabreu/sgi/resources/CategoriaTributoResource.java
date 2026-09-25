package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.categoriaTributo.CategoriaTributoService;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/categoriatributo")
public class CategoriaTributoResource extends BaseCrudResource<CategoriaTributoTO, CategoriaTributoPesquisaTO, CategoriaTributoService> {

	@Autowired
	public CategoriaTributoResource(CategoriaTributoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getCategoriaTributoByCod/{id:[0-9]+}")
	public @ResponseBody
	CategoriaTributoTO getCategoriaTributoByCod(@PathVariable String id) throws Exception {
		return getService().getCategoriaTributoByCod(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getListaCategoriaTributoByCodTributo/{id:[0-9]+}")
	public @ResponseBody
	List<CategoriaTributoTO> getListaCategoriaTributoByCodTributo(@PathVariable String id) {
		return getService().getListaCategoriaTributoByCodTributo(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getListaCategoriaTributoByTributoUF/{codTributo:[0-9]+}/{uf}")
	public @ResponseBody List<CategoriaTributoTO> getListaCategoriaTributoByTributoUF(@PathVariable String codTributo, @PathVariable String uf) {
		return getService().getListaCategoriaTributoByTributoUF(Long.parseLong(codTributo), uf);
	}

}
