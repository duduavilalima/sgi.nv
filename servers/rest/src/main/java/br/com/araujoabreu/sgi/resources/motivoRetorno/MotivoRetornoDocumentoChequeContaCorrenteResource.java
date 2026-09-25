package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoChequeContaCorrenteService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoChequeContaCorrentePesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoChequeContaCorrenteTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/chequecontacorrente")
public class MotivoRetornoDocumentoChequeContaCorrenteResource extends BaseCrudResource<MotivoRetornoDocumentoChequeContaCorrenteTO, MotivoRetornoDocumentoChequeContaCorrentePesquisaTO, MotivoRetornoDocumentoChequeContaCorrenteService> {

	@Autowired
	public MotivoRetornoDocumentoChequeContaCorrenteResource(MotivoRetornoDocumentoChequeContaCorrenteService service) {
		super(service);
	}

}
