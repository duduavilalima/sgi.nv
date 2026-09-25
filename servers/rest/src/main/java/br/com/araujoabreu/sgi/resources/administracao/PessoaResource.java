package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/pessoa")
public class PessoaResource extends BaseCrudResource<PessoaTO, PessoaPesquisaTO, PessoaService> {

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	public PessoaResource(PessoaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/autocomplete/{nome}")
	public @ResponseBody
	List<PessoaPesquisaTO> getPessoas(@PathVariable String nome) {
		return getService().getPessoas(nome);
	}

	@RequestMapping(method = RequestMethod.POST, value = "buscarNomeFantasiaDocumento")
	public @ResponseBody
	ResponseEntity buscarNomeFantasiaDocumento(@RequestBody PessoaPesquisaTO exampleObj) {
		try {
			return responseOk(getService().buscarNomeFantasiaDocumento(exampleObj));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return responseServerError(e);
		}
	}
}
