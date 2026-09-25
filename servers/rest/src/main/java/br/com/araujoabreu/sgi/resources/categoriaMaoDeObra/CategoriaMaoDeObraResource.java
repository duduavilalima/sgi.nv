package br.com.araujoabreu.sgi.resources.categoriaMaoDeObra;

import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.CategoriaMaoDeObraService;
import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.transfer.CategoriaMaoDeObraPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.transfer.CategoriaMaoDeObraTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/categoriamaodeobra")
public class CategoriaMaoDeObraResource extends BaseCrudResource<CategoriaMaoDeObraTO, CategoriaMaoDeObraPesquisaTO, CategoriaMaoDeObraService> {

	@Autowired
	public CategoriaMaoDeObraResource(CategoriaMaoDeObraService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarTodasCategorias")
	public @ResponseBody
	ResponseEntity<List<CategoriaMaoDeObraTO>> buscarTodasCategorias() {
		try {
			return responseOk(getService().buscarTodasCategorias());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
