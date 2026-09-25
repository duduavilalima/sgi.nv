package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaEmissaoService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaEmissaoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaEmissaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/notatributoaliquotaemissao")
public class NotaTributoAliquotaEmissaoResource extends BaseCrudResource<NotaTributoAliquotaEmissaoTO, NotaTributoAliquotaEmissaoPesquisaTO, NotaTributoAliquotaEmissaoService> {

	@Autowired
	public NotaTributoAliquotaEmissaoResource(NotaTributoAliquotaEmissaoService service) {
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
