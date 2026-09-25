package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.rest.BaseCrudResource;
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
@RequestMapping("/rest/pessoajuridica")
public class PessoaJuridicaResource extends BaseCrudResource<PessoaJuridicaTO, PessoaJuridicaPesquisaTO, PessoaJuridicaService> {

	protected static final String BUSCA_LISTA_POR_NOME_FANTASIA = "/buscarListaPorNomeFantasia";

	@Autowired
	public PessoaJuridicaResource(PessoaJuridicaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/popup/{id}/{tipo}")
	public @ResponseBody
	PessoaJuridicaPesquisaTO getPessoaPopUp(@PathVariable Long id, @PathVariable String tipo) {
		return getService().getPessoaPopUp(id, tipo);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/popup/faturamento/{id}/{tipo}/{idCl}")
	public @ResponseBody
	PessoaJuridicaPesquisaTO getPessoaPopUpFaturamento(@PathVariable Long id, @PathVariable String tipo, @PathVariable Long idCl) {
		return getService().getPessoaPopUp(id, tipo, idCl);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cnpj/{documento}")
	public ResponseEntity<PessoaJuridicaTO> findPessoaByCnpj(@PathVariable String documento) {
		try {
			return responseOk(getService().findPessoaByCnpj(documento));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/autocomplete/cliente/{nome}")
	public List<PessoaJuridicaPesquisaTO> getPessoaJuridicaCliente(@PathVariable String nome) {
		return getService().getPessoaJuridicaCliente(nome);
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_LISTA_POR_NOME_FANTASIA)
	public @ResponseBody
	List<PessoaJuridicaTO> buscarCidades(@RequestBody String nome) {
		return getService().buscarListaPorNomeFantasia(nome);
	}
}
