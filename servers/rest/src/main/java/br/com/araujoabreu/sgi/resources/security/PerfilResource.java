package br.com.araujoabreu.sgi.resources.security;

import br.com.araujoabreu.sgi.service.security.PerfilService;
import br.com.araujoabreu.sgi.service.security.transfer.PerfilPesquisaTO;
import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/perfil")
public class PerfilResource extends BaseCrudResource<PerfilTO, PerfilPesquisaTO, PerfilService> {

	@Autowired
	public PerfilResource(PerfilService perfilService) {
		super(perfilService);
	}

	@RequestMapping(method = RequestMethod.GET, value = "listarAtivos")
	public @ResponseBody
	ResponseEntity<Iterable<PerfilTO>> listarPerfisAtivos() {
		try {
			Iterable<PerfilTO> resultado = getService().listarPerfisAtivos();
			return responseOk(resultado);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
