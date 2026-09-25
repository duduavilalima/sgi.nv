package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.apoio.EnumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/enumservice")
public class EnumResource {

	@Autowired
	private EnumService enumService;

	@RequestMapping(method = RequestMethod.GET, value = "/simples/{pacote}/{classe}/{nomeEnum}")
	public @ResponseBody
	String[] getEnumClassValues(@PathVariable String pacote, @PathVariable String classe, @PathVariable String nomeEnum) {
		return enumService.getEnumClassValues(pacote, classe, nomeEnum);
	}
}
