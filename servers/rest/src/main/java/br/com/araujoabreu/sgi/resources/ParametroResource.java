package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.parametro.ParametroService;
import br.com.araujoabreu.sgi.service.parametro.transfer.ParametroTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/parametro")
public class ParametroResource {
	protected static final String PAGE = "/page";

	@Autowired
	private ParametroService parametroService;

	@RequestMapping(method = RequestMethod.POST, value = PAGE)
	public @ResponseBody
	ParametroTO buscaPorExemplo(@RequestBody ParametroTO exampleObj) {
		return parametroService.buscaPorExemplo(exampleObj);
	}

}
