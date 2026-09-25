package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/documento")
public class MotivoDocumentoResource extends BaseCrudResource<MotivoDocumentoTO, MotivoDocumentoPesquisaTO, MotivoDocumentoService> {

	@Autowired
	public MotivoDocumentoResource(MotivoDocumentoService service) {
		super(service);
	}
}
