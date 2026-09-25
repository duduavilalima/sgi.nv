package br.com.araujoabreu.sgi.resources.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroEnderecoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroEnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroEnderecoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/centrofinanceiroendereco")
public class CentroFinanceiroEnderecoResource extends BaseCrudResource<CentroFinanceiroEnderecoTO, CentroFinanceiroEnderecoPesquisaTO, CentroFinanceiroEnderecoService> {

	@Autowired
	public CentroFinanceiroEnderecoResource(CentroFinanceiroEnderecoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarListaCentroFinanceiroEndereco")
	public List<CentroFinanceiroEnderecoTO> buscarTelefones(@RequestBody CentroFinanceiroEnderecoTO centroFinanceiroEnderecoTO) {
		return getService().buscarTelefones(centroFinanceiroEnderecoTO);
	}
}
