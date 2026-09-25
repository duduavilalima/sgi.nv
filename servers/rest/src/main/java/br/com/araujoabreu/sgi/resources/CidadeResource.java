package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.cidade.CidadeService;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadePesquisaTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/cidade")
public class CidadeResource extends BaseCrudResource<CidadeTO, CidadePesquisaTO, CidadeService> {

	@Autowired
	public CidadeResource(CidadeService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarcidadespornome")
	public List<CidadeTO> buscarCidadesByNome(@RequestBody String nomeCidade) {
		return getService().buscarCidadesByNome(nomeCidade);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscapornome")
	public @ResponseBody
	List<CidadeTO> buscarCidades(@RequestBody String nome) {
		return getService().buscarCidades(nome);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete")
	public List<CidadeTO> getCidade(@RequestBody AutocompleteTO autocompleteTO) {
		return getService().getCidade(autocompleteTO);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarUFs")
	public List<String> buscarUFs() {
		return getService().buscarUFs();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarcidadesiss/{uf}")
	public List<CidadeTO> getCidadesISS(@PathVariable String uf) {
		return getService().getCidadesISS(uf);
	}
}
