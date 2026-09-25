package br.com.araujoabreu.sgi.resources.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.FaturamentoAliquotaService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/faturamentoaliquota")
public class FaturamentoAliquotaResource extends BaseCrudResource<FaturamentoAliquotaTO, FaturamentoAliquotaPesquisaTO, FaturamentoAliquotaService> {

	@Autowired
	public FaturamentoAliquotaResource(FaturamentoAliquotaService service) {
		super(service);
	}
}
