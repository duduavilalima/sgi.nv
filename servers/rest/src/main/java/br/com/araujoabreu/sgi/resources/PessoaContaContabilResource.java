package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.pessoaContaContabil.PessoaContaContabilService;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/pessoacontacontabil")
public class PessoaContaContabilResource extends BaseCrudResource<PessoaContaContabilTO, PessoaContaContabilPesquisaTO, PessoaContaContabilService> {

	@Autowired
	public PessoaContaContabilResource(PessoaContaContabilService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}
}
