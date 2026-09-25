package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/documento")
public class MotivoRetornoDocumentoResource extends BaseCrudResource<MotivoRetornoDocumentoTO, MotivoRetornoDocumentoPesquisaTO, MotivoRetornoDocumentoService> {

	@Autowired
	public MotivoRetornoDocumentoResource(MotivoRetornoDocumentoService service) {
		super(service);
	}
}
