package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/notatributoaliquota")
public class NotaTributoAliquotaResource extends BaseCrudResource<NotaTributoAliquotaTO, NotaTributoAliquotaPesquisaTO, NotaTributoAliquotaService> {

	@Autowired
	public NotaTributoAliquotaResource(NotaTributoAliquotaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listarPorNota/{id:[0-9]+}")
	public ResponseEntity listarPorNota(@PathVariable Long id) {
		try {
			return responseOk(getService().listarPorCodigoNota(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
