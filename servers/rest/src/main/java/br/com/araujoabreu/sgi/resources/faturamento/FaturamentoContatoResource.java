package br.com.araujoabreu.sgi.resources.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.FaturamentoContatoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoContatoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/rest/faturamentocontato")
public class FaturamentoContatoResource extends BaseCrudResource<FaturamentoContatoTO, FaturamentoContatoPesquisaTO, FaturamentoContatoService> {

	private static final String URL_LISTA_POR_FATURAMENTO = "/listaByFatCod/{id:[0-9]+}";
	private static final String URL_REMOVER = "/remover/{id:[0-9]+}";

	@Autowired
	public FaturamentoContatoResource(FaturamentoContatoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = URL_LISTA_POR_FATURAMENTO)
	public @ResponseBody
	ResponseEntity listarPorFaturamento(@PathVariable String id) {
		try {
			List<FaturamentoContatoTO> listaContato = getService().listaPorCodFaturamento(Long.parseLong(id));
			return responseOk(listaContato);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = URL_REMOVER)
	public HttpEntity<Void> remover(@PathVariable String id) throws Exception {
		getService().remover(Long.parseLong(id));
		return ResponseEntity.noContent().build();
	}


}
