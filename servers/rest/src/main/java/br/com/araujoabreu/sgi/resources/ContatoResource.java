package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.contato.ContatoService;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rest/contato")
public class ContatoResource extends BaseCrudResource<ContatoTO, ContatoPesquisaTO, ContatoService> {

	@Autowired
	public ContatoResource(ContatoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}
}
