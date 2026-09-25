package br.com.araujoabreu.sgi.resources.contrato;

import br.com.araujoabreu.sgi.service.contrato.ContratoEnderecoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rest/contratoendereco")
public class ContratoEnderecoServiceResource extends BaseCrudResource<ContratoEnderecoTO, ContratoEnderecoPesquisaTO, ContratoEnderecoService> {

	@Autowired
	public ContratoEnderecoServiceResource(ContratoEnderecoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) throws Exception {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}
}
