package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoParcelaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/parcela")
public class MotivoRetornoDocumentoParcelaResource extends BaseCrudResource<MotivoRetornoDocumentoFaturamentoTO, MotivoRetornoDocumentoFaturamentoPesquisaTO, MotivoRetornoDocumentoParcelaService> {

	@Autowired
	public MotivoRetornoDocumentoParcelaResource(MotivoRetornoDocumentoParcelaService service) {
		super(service);
	}
}
