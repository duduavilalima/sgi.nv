package br.com.araujoabreu.sgi.resources.fluxoCaixa;

import br.com.araujoabreu.sgi.service.fluxoCaixa.DespesaFluxoCaixaService;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.DespesaFluxoCaixaPesquisaTO;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.DespesaFluxoCaixaTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/despesas")
public class DespesaFluxoCaixaResource extends BaseCrudResource<DespesaFluxoCaixaTO, DespesaFluxoCaixaPesquisaTO, DespesaFluxoCaixaService> {

	@Autowired
	public DespesaFluxoCaixaResource(DespesaFluxoCaixaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> remover(@PathVariable Long id) {
		try {
			getService().remover(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscapordescricao")
	public @ResponseBody
	ResponseEntity<List<DespesaFluxoCaixaTO>> buscarDespesaFluxoCaixaPorDescricao(@RequestBody String descricao) {
		try {
			return responseOk(getService().buscarDespesaFluxoCaixaPorDescricao(descricao));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete")
	public ResponseEntity<List<DespesaFluxoCaixaTO>> getDespesaFluxoCaixa(@RequestBody AutocompleteTO autocompleteTO) {
		try {
			return responseOk(getService().getDespesaFluxoCaixa(autocompleteTO));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
