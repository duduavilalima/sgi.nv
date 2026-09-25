package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.BoletoService;
import br.com.fasolti.commons.lang.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/boleto")
public class BoletoResource {

	@Autowired
	private BoletoService boletoService;

	@RequestMapping(method = RequestMethod.GET, value = "/linhadigitavel/{tipoDocumentoPagamento}/{numero}")
	public Object validarLinhaDigitavel(@PathVariable String tipoDocumentoPagamento, @PathVariable String numero) throws ValidationException {
		try {
			return boletoService.validarLinhaDigitavel(tipoDocumentoPagamento, numero);
		} catch (Exception e) {
			throw new ValidationException("validarLinhaDigitavel", e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/codigobarras/{tipoDocumentoPagamento}/{numero}")
	public Object validarBoleto(@PathVariable String tipoDocumentoPagamento, @PathVariable String numero) {
		try {
			return boletoService.validarBoleto(tipoDocumentoPagamento, numero);
		} catch (Exception e) {
			throw new ValidationException("validarBoleto", e.getMessage());
		}
	}
}
