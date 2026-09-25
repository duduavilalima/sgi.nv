package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.dadosBancarios.DadosBancariosService;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosPesquisaTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/dadosbancarios")
public class DadosBancariosResource extends BaseCrudResource<DadosBancariosTO, DadosBancariosPesquisaTO, DadosBancariosService> {

	@Autowired
	public DadosBancariosResource(DadosBancariosService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/homologardadosbancariosmanual")
	public DadosBancariosTO restHomologarDadosBancariosManual(@RequestBody DadosBancariosTO dbto) {
		return getService().restHomologarDadosBancariosManual(dbto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/popup/{idPessoa:[0-9]+}")
	public List<DadosBancariosTO> getDadosBancariosPessoa(@PathVariable Long idPessoa) {
		return getService().getDadosBancariosPessoa(idPessoa);
	}
}
