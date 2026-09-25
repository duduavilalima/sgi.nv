package br.com.araujoabreu.sgi.resources.categoriaMaoDeObraAdicional;

import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.CategoriaMaoDeObraAdicionalService;
import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.transfer.CategoriaMaoDeObraAdicionalPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.transfer.CategoriaMaoDeObraAdicionalTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/categoriamaodeobraadicional")
public class CategoriaMaoDeObraAdicionalResource extends BaseCrudResource<CategoriaMaoDeObraAdicionalTO, CategoriaMaoDeObraAdicionalPesquisaTO, CategoriaMaoDeObraAdicionalService> {

	@Autowired
	public CategoriaMaoDeObraAdicionalResource(CategoriaMaoDeObraAdicionalService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarTodasCategorias")
	public @ResponseBody
	ResponseEntity<List<CategoriaMaoDeObraAdicionalTO>> buscarTodasCategorias() {
		try {
			return responseOk(getService().buscarTodasCategorias());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
