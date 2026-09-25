package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.tributo.TributoService;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoPesquisaTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/tributo")
public class TributoResource extends BaseCrudResource<TributoTO, TributoPesquisaTO, TributoService> {

	@Autowired
	public TributoResource(TributoService service) {
		super(service);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/buscarTributoByExibeNota")
	public @ResponseBody
	ResponseEntity<List<TributoTO>> buscarTributoByExibeNota() {
		try {
			return responseOk(getService().buscarTributoByExibeNota());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
