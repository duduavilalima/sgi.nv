package br.com.araujoabreu.sgi.resources.tipoCobranca;

import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaService;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaPesquiasTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/tipoCobranca")
public class TipoCobrancaResource extends BaseCrudResource<TipoCobrancaTO, TipoCobrancaPesquiasTO, TipoCobrancaService> {

	@Autowired
	public TipoCobrancaResource(TipoCobrancaService service) {
		super(service);
	}
}
