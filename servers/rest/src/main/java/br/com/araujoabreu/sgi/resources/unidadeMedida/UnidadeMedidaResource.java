package br.com.araujoabreu.sgi.resources.unidadeMedida;

import br.com.araujoabreu.sgi.service.unidademedida.UnidadeMedidaService;
import br.com.araujoabreu.sgi.service.unidademedida.transfer.UnidadeMedidaPesquisaTO;
import br.com.araujoabreu.sgi.service.unidademedida.transfer.UnidadeMedidaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/unidademedida")
public class UnidadeMedidaResource extends BaseCrudResource<UnidadeMedidaTO, UnidadeMedidaPesquisaTO, UnidadeMedidaService> {

	@Autowired
	public UnidadeMedidaResource(UnidadeMedidaService service) {
		super(service);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/buscarTodasUnidadesMedida")
	public @ResponseBody
	ResponseEntity<List<UnidadeMedidaTO>> buscarTodasUnidadesMedida() {
		try {
			return responseOk(getService().buscarTodasUnidadesMedida());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
