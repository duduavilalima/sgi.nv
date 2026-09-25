package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoNotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaSaidaParcelaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/notasaidaparcela")
public class MotivoRetornoDocumentoNotaSaidaParcelaResource extends BaseCrudResource<MotivoRetornoDocumentoNotaSaidaParcelaTO, MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO, MotivoRetornoDocumentoNotaSaidaParcelaService> {

	protected static final String RECEBIMENTO = "/recebimento";

	@Autowired
	public MotivoRetornoDocumentoNotaSaidaParcelaResource(MotivoRetornoDocumentoNotaSaidaParcelaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = RECEBIMENTO)
	public HttpEntity<Void> cancelarRecebimentoParcela(@RequestBody MotivoRetornoDocumentoNotaSaidaParcelaTO to) {
		try {
			getService().salvarMotivoRetornoParcelaRecebimento(to);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

}
