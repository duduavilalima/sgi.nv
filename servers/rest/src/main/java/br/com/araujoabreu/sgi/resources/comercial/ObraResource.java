package br.com.araujoabreu.sgi.resources.comercial;

import br.com.araujoabreu.sgi.service.comercial.ObraService;
import br.com.araujoabreu.sgi.service.comercial.transfer.ObraPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ObraTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.TipoObraTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/obras")
public class ObraResource extends BaseCrudResource<ObraTO, ObraPesquisaTO, ObraService> {

	@Autowired
	public ObraResource(ObraService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscartipoobra")
	public List<TipoObraTO> buscarTipoObra() {
		return getService().buscarTipoObra();
	}
}
