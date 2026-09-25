package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.HistoricoConciliacaoContaContabilService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/tesouraria/historicoconciliacontacontabil")
public class HistoricoConciliacaoContaContabilResource extends BaseCrudResource<HistoricoConciliacaoContaContabilTO, HistoricoConciliacaoContaContabilPesquisaTO, HistoricoConciliacaoContaContabilService> {

	@Autowired
	public HistoricoConciliacaoContaContabilResource(HistoricoConciliacaoContaContabilService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) throws Exception {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}
}
