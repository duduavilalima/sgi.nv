package br.com.araujoabreu.sgi.resources.comercial;

import br.com.araujoabreu.sgi.service.comercial.FormaContratacaoService;
import br.com.araujoabreu.sgi.service.comercial.transfer.FormaContratacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.FormaContratacaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/formacontratacao")
public class FormaContratacaoResource extends BaseCrudResource<FormaContratacaoTO, FormaContratacaoPesquisaTO, FormaContratacaoService> {

	@Autowired
	public FormaContratacaoResource(FormaContratacaoService service) {
		super(service);
	}
}
