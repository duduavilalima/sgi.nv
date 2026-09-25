package br.com.araujoabreu.sgi.resources.categoriaDespesaGeral;

import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.CategoriaDespesaGeralService;
import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer.CategoriaDespesaGeralPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer.CategoriaDespesaGeralTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/categoriadespesageral")
public class CategoriaDespesaGeralResource extends BaseCrudResource<CategoriaDespesaGeralTO, CategoriaDespesaGeralPesquisaTO, CategoriaDespesaGeralService> {

	@Autowired
	public CategoriaDespesaGeralResource(CategoriaDespesaGeralService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarTodasCategorias")
	public @ResponseBody
	ResponseEntity<List<CategoriaDespesaGeralTO>> buscarTodasCategorias() {
		try {
			return responseOk(getService().buscarTodasCategorias());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
