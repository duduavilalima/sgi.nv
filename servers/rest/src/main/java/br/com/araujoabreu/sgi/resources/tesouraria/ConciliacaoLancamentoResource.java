package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.ConciliacaoLancamentoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/tesouraria/conciliacaolancamento")
public class ConciliacaoLancamentoResource extends BaseCrudResource<ConciliacaoLancamentoTO, ConciliacaoLancamentoPesquisaTO, ConciliacaoLancamentoService> {

	@Autowired
	public ConciliacaoLancamentoResource(ConciliacaoLancamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getlancamentosassociados")
	public List<LancamentoTO> getLancamentosAssociados() throws Exception {
		return getService().getLancamentosAssociados();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/getchequeassociado")
	public ChequeContaCorrenteTO getChequeAssociado(@RequestBody String numeroCheque) throws Exception {
		return getService().getChequeAssociado(numeroCheque);
	}
}
