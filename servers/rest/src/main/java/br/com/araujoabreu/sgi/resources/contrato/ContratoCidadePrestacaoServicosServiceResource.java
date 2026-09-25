package br.com.araujoabreu.sgi.resources.contrato;

import br.com.araujoabreu.sgi.service.contrato.ContratoCidadePrestacaoServicosService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/contrato/cidade/faturamento")
public class ContratoCidadePrestacaoServicosServiceResource extends BaseCrudResource<ContratoCidadePrestacaoServicosTO, ContratoCidadePrestacaoServicosPesquisaTO, ContratoCidadePrestacaoServicosService> {

	@Autowired
	public ContratoCidadePrestacaoServicosServiceResource(ContratoCidadePrestacaoServicosService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "buscarTodos")
	private @ResponseBody
	ResponseEntity<Iterable<ContratoCidadePrestacaoServicosPesquisaTO>> buscarTodosPorExemplo(
		@RequestBody ContratoCidadePrestacaoServicosPesquisaTO parametros
	) {
		try {
			return responseOk(getService().buscarTodosPorExemplo(parametros));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
