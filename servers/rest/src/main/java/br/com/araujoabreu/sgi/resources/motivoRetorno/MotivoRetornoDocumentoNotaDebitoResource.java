package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoNotaDebitoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoNotaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaDebitoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaDebitoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/notadebito")
public class MotivoRetornoDocumentoNotaDebitoResource extends BaseCrudResource<MotivoRetornoDocumentoNotaDebitoTO, MotivoRetornoDocumentoNotaDebitoPesquisaTO, MotivoRetornoDocumentoNotaDebitoService> {

	@Autowired
	public MotivoRetornoDocumentoNotaDebitoResource(MotivoRetornoDocumentoNotaDebitoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/retornarNotaDebito")
	public HttpEntity<Void> retornarNotaDebito(@RequestBody MotivoRetornoDocumentoNotaDebitoTO transferObj) {
		try {
			getService().retornarNotaDebito(transferObj);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
