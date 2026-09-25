package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoContratoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoContratoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoContratoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/contrato")
public class MotivoRetornoDocumentoContratoResource extends BaseCrudResource<MotivoRetornoDocumentoContratoTO, MotivoRetornoDocumentoContratoPesquisaTO, MotivoRetornoDocumentoContratoService> {

	@Autowired
	public MotivoRetornoDocumentoContratoResource(MotivoRetornoDocumentoContratoService service) {
		super(service);
	}
}
