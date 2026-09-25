package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/pessoafisica")
public class PessoaFisicaResource extends BaseCrudResource<PessoaFisicaTO, PessoaFisicaPesquisaTO, PessoaFisicaService> {

	@Autowired
	public PessoaFisicaResource(PessoaFisicaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarListaPessoaFisica")
	public ResponseEntity<List<PessoaFisicaTO>> buscarPessoasFisicas(@RequestBody PessoaFisicaTO pessoaFisicaTO) {
		return responseOk(getService().buscarPessoasFisicas(pessoaFisicaTO));
	}

	/**
	 * O objeto PessoaFisicaTO só esta sendo usado para transportar o nome do usuário.
	 *
	 * @param login
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/buscarNomeByLogin")
	public PessoaFisicaTO buscarPessoasFisicas(@RequestBody String login) {
		PessoaFisicaTO to = new PessoaFisicaTO();
		to.setNome(getService().nomeUsuarioPorLogin(login));
		return to;
	}
}
