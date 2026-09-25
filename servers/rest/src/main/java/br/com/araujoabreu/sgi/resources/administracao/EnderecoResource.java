package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.administracao.EnderecoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/endereco")
public class EnderecoResource extends BaseCrudResource<EnderecoTO, EnderecoPesquisaTO, EnderecoService> {

	protected static final String BUSCA_POR_PESSOA = "/busca/pessoa";

	@Autowired
	public EnderecoResource(EnderecoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_POR_PESSOA)
	public @ResponseBody
	EnderecoPesquisaTO findLastNotaFiscalByUnidadeCodigo(@RequestBody EnderecoPesquisaTO exampleObj) {
		return getService().findLastNotaFiscalByUnidadeCodigo(exampleObj);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscartipologradouro")
	public List<TipoLogradouroTO> buscarTipoLogradouro() {
		return getService().buscarTipoLogradouro();
	}

}
