package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.regional.RegionalService;
import br.com.araujoabreu.sgi.service.regional.transfer.RegionalPesquisaTO;
import br.com.araujoabreu.sgi.service.regional.transfer.RegionalTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/regional")
public class RegionalResource extends BaseCrudResource<RegionalTO, RegionalPesquisaTO, RegionalService> {

	@Autowired
	public RegionalResource(RegionalService service) {
		super(service);
	}

	/**
	 * Buscar o código Regional de acordo com uma empresa selecionada.
	 *
	 * @param idEmpresa
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{idEmpresa:[0-9]+}/listar")
	public List<RegionalTO> buscarPorCodigoUnidade(@PathVariable String idEmpresa) {
		return getService().buscarPorCodigoUnidade(idEmpresa);
	}

	/**
	 * Buscar lista de Regionais ativas
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/listaAtiva")
	public List<RegionalTO> buscarListaAtiva() {
		return getService().buscarAtivas();
	}
}
