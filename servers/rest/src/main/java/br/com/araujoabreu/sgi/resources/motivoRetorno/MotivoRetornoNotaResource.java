package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoNotaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/nota")
public class MotivoRetornoNotaResource extends BaseCrudResource<MotivoRetornoNotaTO, MotivoRetornoNotaPesquisaTO, MotivoRetornoNotaService> {

	@Autowired
	public MotivoRetornoNotaResource(MotivoRetornoNotaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/cancelarNotaFiscal")
	public ResponseEntity saveCancelamentoNotaFiscal(@RequestBody MotivoRetornoNotaTO transferObj) {
		try {
			return responseOk(getService().saveNotaFiscal(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
