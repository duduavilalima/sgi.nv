package br.com.araujoabreu.sgi.resources.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroAliquotaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/centrofinanceiroaliquota")
public class CentroFinanceiroAliquotaResource extends BaseCrudResource<CentroFinanceiroAliquotaTO, CentroFinanceiroAliquotaPesquisaTO, CentroFinanceiroAliquotaService> {

	@Autowired
	public CentroFinanceiroAliquotaResource(CentroFinanceiroAliquotaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getListByCodCentroFinanceiro/{id:[0-9]+}")
	public @ResponseBody
	List<CentroFinanceiroAliquotaTO> getListByCodCentroFinanceiro(@PathVariable String id) throws Exception {
		return getService().getListByCodCentroFinanceiro(id);
	}
}
