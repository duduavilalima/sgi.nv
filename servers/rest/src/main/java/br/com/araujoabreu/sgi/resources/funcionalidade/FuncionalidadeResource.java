package br.com.araujoabreu.sgi.resources.funcionalidade;

import br.com.araujoabreu.sgi.service.funcionalidade.FuncionalidadeService;
import br.com.araujoabreu.sgi.service.funcionalidade.transfer.FuncionalidadePesquisaTO;
import br.com.araujoabreu.sgi.service.funcionalidade.transfer.FuncionalidadeTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/funcionalidades")
public class FuncionalidadeResource extends BaseCrudResource<FuncionalidadeTO, FuncionalidadePesquisaTO, FuncionalidadeService> {

	@Autowired
	public FuncionalidadeResource(FuncionalidadeService service) {
		super(service);
	}
}
