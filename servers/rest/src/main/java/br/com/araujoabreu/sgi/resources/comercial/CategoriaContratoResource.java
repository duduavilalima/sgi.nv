package br.com.araujoabreu.sgi.resources.comercial;

import br.com.araujoabreu.sgi.service.comercial.CategoriaContratoService;
import br.com.araujoabreu.sgi.service.comercial.transfer.CategoriaContratoPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.CategoriaContratoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/categoriacontrato")
public class CategoriaContratoResource extends BaseCrudResource<CategoriaContratoTO, CategoriaContratoPesquisaTO, CategoriaContratoService> {

	@Autowired
	public CategoriaContratoResource(CategoriaContratoService service) {
		super(service);
	}
}
