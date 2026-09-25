package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoEtapaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoEtapaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoEtapaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/faturamento/etapa")
public class MotivoRetornoDocumentoEtapaResource extends BaseCrudResource<MotivoRetornoDocumentoEtapaTO, MotivoRetornoDocumentoEtapaPesquisaTO, MotivoRetornoDocumentoEtapaService> {

	@Autowired
	public MotivoRetornoDocumentoEtapaResource(MotivoRetornoDocumentoEtapaService service) {
		super(service);
	}
}
