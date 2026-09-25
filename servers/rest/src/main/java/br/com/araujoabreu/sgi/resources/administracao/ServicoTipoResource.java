package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.administracao.ServicoTipoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ReinfServicoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/servico_tipo")
public class ServicoTipoResource extends BaseCrudResource<ServicoTipoTO, ServicoTipoPesquisaTO, ServicoTipoService> {

	@Autowired
	public ServicoTipoResource(ServicoTipoService service) {
		super(service);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/buscarservicotipo")
	public @ResponseBody
	ResponseEntity<ServicoTipoTO> obterServicoTipoPelaSiglaTO(String sigla) {
		try {
			return responseOk(getService().obterServicoTipoPelaSiglaTO(sigla));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarReinfServicoByCodServicoTipo/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity<ReinfServicoTO> buscarReinfServicoByCodServicoTipo(@PathVariable String id) {
		try {
			return responseOk(getService().buscarReinfServicoByCodServicoTipo(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
