package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.MovimentoAutorizadoBoletosService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoBoletosPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoBoletosTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/tesouraria/movimentoautorizadoboletos")
public class MovimentoAutorizadoBoletosResource extends BaseCrudResource<MovimentoAutorizadoBoletosTO, MovimentoAutorizadoBoletosPesquisaTO, MovimentoAutorizadoBoletosService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovimentoAutorizadoBoletosResource.class);

	@Autowired
	public MovimentoAutorizadoBoletosResource(MovimentoAutorizadoBoletosService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/atualizarcodigobarras")
	public ResponseEntity salvarCodigoBarraLinhaDigitavel(@RequestBody MovimentoAutorizadoBoletosTO to) {
		try {
			return responseOk(getService().salvarCodigoBarraLinhaDigitavel(to));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
