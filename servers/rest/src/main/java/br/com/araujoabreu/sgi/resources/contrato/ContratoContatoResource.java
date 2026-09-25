package br.com.araujoabreu.sgi.resources.contrato;

import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.ContratoContatoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/rest/contratocontato")
public class ContratoContatoResource extends BaseCrudResource<ContratoContatoTO, ContratoContatoPesquisaTO, ContratoContatoService> {

	@Autowired
	public ContratoContatoResource(ContratoContatoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarrelacionamento")
	public @ResponseBody
	List<RelacionamentoContatoTO> buscarRelacionamento() {
		return getService().buscarRelacionamento();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) throws Exception {
		getService().remover(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listaByCodContrato/{id:[0-9]+}")
	public List<ContratoContatoTO> listaContratoContatoByCodContrato(@PathVariable String id) throws Exception {
		return getService().listaContratoContatoByCodContrato(id);
	}
}
