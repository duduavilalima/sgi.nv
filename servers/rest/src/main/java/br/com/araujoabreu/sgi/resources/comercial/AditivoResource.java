package br.com.araujoabreu.sgi.resources.comercial;

import br.com.araujoabreu.sgi.service.aditivo.AditivoPesquisaTO;
import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.comercial.AditivoService;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/aditivo")
public class AditivoResource extends BaseCrudResource<AditivoTO, AditivoPesquisaTO, AditivoService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteResource.class);

	@Autowired
	public AditivoResource(AditivoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public ResponseEntity remover(@PathVariable Long id) {
		try {
			getService().remover(id);
			return ResponseEntity.noContent().build();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}
}
