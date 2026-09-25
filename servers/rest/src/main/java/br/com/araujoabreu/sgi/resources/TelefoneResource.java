package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.telefone.TelefoneService;
import br.com.araujoabreu.sgi.service.telefone.transfer.OperadoraTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefonePesquisaTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/telefone")
public class TelefoneResource extends BaseCrudResource<TelefoneTO, TelefonePesquisaTO, TelefoneService> {

	@Autowired
	public TelefoneResource(TelefoneService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscaroperadora")
	public @ResponseBody
	List<OperadoraTO> buscarOperadora() {
		return getService().buscarOperadora();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarListaTelefone")
	public List<TelefoneTO> buscarTelefones(@RequestBody TelefoneTO telefoneTO) throws Exception {
		return getService().buscarTelefones(telefoneTO);
	}
}
