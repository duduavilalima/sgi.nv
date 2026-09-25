package br.com.araujoabreu.sgi.resources.fluxoCaixa;

import br.com.araujoabreu.sgi.service.fluxoCaixa.PrevisaoFluxoCaixaService;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.PrevisaoFluxoCaixaPesquisaTO;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.PrevisaoFluxoCaixaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/previsao")
public class PrevisaoFluxoCaixaResource extends BaseCrudResource<PrevisaoFluxoCaixaTO, PrevisaoFluxoCaixaPesquisaTO, PrevisaoFluxoCaixaService> {

	@Autowired
	public PrevisaoFluxoCaixaResource(PrevisaoFluxoCaixaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}
}
