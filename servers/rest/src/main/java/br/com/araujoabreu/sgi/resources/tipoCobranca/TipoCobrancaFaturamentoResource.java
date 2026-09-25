package br.com.araujoabreu.sgi.resources.tipoCobranca;

import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaFaturamentoService;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaFaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaFaturamentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/tipocobrancafaturamento")
public class TipoCobrancaFaturamentoResource extends BaseCrudResource<TipoCobrancaFaturamentoTO, TipoCobrancaFaturamentoPesquisaTO, TipoCobrancaFaturamentoService> {

	@Autowired
	public TipoCobrancaFaturamentoResource(TipoCobrancaFaturamentoService service) {
		super(service);
	}
}
