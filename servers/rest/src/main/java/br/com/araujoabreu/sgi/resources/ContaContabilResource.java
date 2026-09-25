package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.contaContabil.ContaContabilService;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.util.AutoCompleteContaContabilTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/rest/contacontabil")
public class ContaContabilResource extends BaseCrudResource<ContaContabilTO, ContaContabilPesquisaTO, ContaContabilService> {

	@Autowired
	public ContaContabilResource(ContaContabilService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscapornomeounumero")
	public @ResponseBody
	List<ContaContabilTO> buscarContaContabilPorNomeOuNumero(@RequestBody String nomeOuNumero) {
		return getService().buscarContaContabilPorNomeOuNumero(nomeOuNumero);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete")
	public List<ContaContabilTO> getContaContabil(@RequestBody AutocompleteTO autocompleteTO) {
		return getService().getContaContabil(autocompleteTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete/empresa")
	public List<ContaContabilTO> getContaContabilPorEmpresa(@RequestBody AutocompleteTO autocompleteTO) {
		return getService().getContaContabilPorEmpresa(autocompleteTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete/empresa/classificacao")
	public List<ContaContabilTO> getContaContabilPorEmpresaClassificacao(@RequestBody AutoCompleteContaContabilTO autocompleteTO) {
		return getService().getContaContabilPorEmpresaClassificacao(autocompleteTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete/empresa/tipoEspecialClassificacao")
	public List<ContaContabilTO> getContaContabilPorEmpresaTipoEspecialClassificacao(@RequestBody AutoCompleteContaContabilTO autocompleteTO) {
		return getService().getContaContabilPorEmpresaTipoEspecialClassificacao(autocompleteTO);
	}
}
