package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoCentroFinanceiroService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoCentroFinanceiroPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoCentroFinanceiroTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/retorno/centrofinanceiro")
public class MotivoRetornoCentroFinanceiroResource extends BaseCrudResource<MotivoRetornoCentroFinanceiroTO, MotivoRetornoCentroFinanceiroPesquisaTO, MotivoRetornoCentroFinanceiroService> {

	@Autowired
	public MotivoRetornoCentroFinanceiroResource(MotivoRetornoCentroFinanceiroService service) {
		super(service);
	}
}
