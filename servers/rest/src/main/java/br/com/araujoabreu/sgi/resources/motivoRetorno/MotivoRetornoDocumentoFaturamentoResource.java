package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoFaturamentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/faturamento")
public class MotivoRetornoDocumentoFaturamentoResource extends BaseCrudResource<MotivoRetornoDocumentoFaturamentoTO, MotivoRetornoDocumentoFaturamentoPesquisaTO, MotivoRetornoDocumentoFaturamentoService> {

	@Autowired
	public MotivoRetornoDocumentoFaturamentoResource(MotivoRetornoDocumentoFaturamentoService service) {
		super(service);
	}
}
