package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioPesquisaTO;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/usuario")
public class UsuarioResource extends BaseCrudResource<UsuarioTO, UsuarioPesquisaTO, UsuarioService> {

	@Autowired
	public UsuarioResource(UsuarioService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/usuariologado")
	public @ResponseBody
	ResponseEntity<UsuarioTO> getUsuarioLogado() {
		try {
			return responseOk(getService().getUsuarioLogado());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/possuitodosperfislista")
	public @ResponseBody
	ResponseEntity<Map<String, Boolean>> possuiTodosPerfisLista(@RequestParam String[] perfis) {
		try {
			return responseOk(getService().possuiTodosPerfisLista(perfis));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


	@RequestMapping(method = RequestMethod.POST, value = "/possuiumperfillista")
	public @ResponseBody
	ResponseEntity<Map<String, Boolean>> possuiUmPerfilLista(@RequestParam String[] perfis) {
		try {
			return responseOk(getService().possuiUmPerfilLista(perfis));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/apenasfaturamento")
	public @ResponseBody
	ResponseEntity<Map<String, Boolean>> apenasFaturamento() {
		try {
			return responseOk(getService().apenasFaturamento());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
