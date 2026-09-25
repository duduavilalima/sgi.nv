package br.com.araujoabreu.sgi.resources.contato;

import br.com.araujoabreu.sgi.service.contato.RelacionamentoContatoService;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/relacionamentocontato")
public class RelacionamentoContatoResource extends BaseCrudResource<RelacionamentoContatoTO, RelacionamentoContatoPesquisaTO, RelacionamentoContatoService> {

	private static final String URL_BUSCAR_TODOS = "/buscarTodos";

	@Autowired
	public RelacionamentoContatoResource(RelacionamentoContatoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = URL_BUSCAR_TODOS)
	public @ResponseBody
	ResponseEntity listarTodos() {
		try {
			List<RelacionamentoContatoTO> lista = getService().buscarTodos();
			return responseOk(lista);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
