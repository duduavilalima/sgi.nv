package br.com.araujoabreu.sgi.resources.contrato;

import br.com.araujoabreu.sgi.service.contrato.ContratoAnexoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/rest/contratoanexo")
public class ContratoAnexoResource extends BaseCrudResource<ContratoAnexoTO, ContratoAnexoPesquisaTO, ContratoAnexoService> {

	@Autowired
	public ContratoAnexoResource(ContratoAnexoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) throws Exception {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/baixaranexo")
	public HttpEntity<Void> baixarAnexo(@PathVariable String id, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		getService().baixarAnexo(id, request, response);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/verificaranexo")
	public ResponseEntity<AnexoTO> verificaranexo(@PathVariable Long id, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		return ResponseEntity.ok(getService().verificarAnexo(id));
	}
}
