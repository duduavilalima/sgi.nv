package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.HistoricoConciliacaoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/tesouraria/historicoconciliacaobancaria")
public class HistoricoConciliacaoResource extends BaseCrudResource<HistoricoConciliacaoTO, HistoricoConciliacaoPesquisaTO, HistoricoConciliacaoService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HistoricoConciliacaoResource.class);

	@Autowired
	public HistoricoConciliacaoResource(HistoricoConciliacaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigo}/buscarhistoricoccc")
	public ResponseEntity buscarHistoricoCCC(@PathVariable String codigo) {
		try {
			return responseOk(getService().buscarHistoricoCCC(codigo));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarcontacontabil")
	public ResponseEntity buscarContaContabil(@RequestBody Long codigoHistoricoConciliacao) {
		try {
			return responseOk(getService().buscarContaContabil(codigoHistoricoConciliacao));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}
}
