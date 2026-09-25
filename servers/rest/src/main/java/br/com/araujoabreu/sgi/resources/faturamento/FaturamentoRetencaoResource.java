package br.com.araujoabreu.sgi.resources.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.FaturamentoRetencaoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/rest/faturamentoretencao")
public class FaturamentoRetencaoResource extends BaseCrudResource<FaturamentoRetencaoTO, FaturamentoRetencaoPesquisaTO, FaturamentoRetencaoService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FaturamentoRetencaoResource.class);

	@Autowired
	private FaturamentoRetencaoService faturamentoRetencaoService;

	@Autowired
	public FaturamentoRetencaoResource(FaturamentoRetencaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listarporfaturamento/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity listarPorFaturamento(@PathVariable String id) {
		try {
			List<FaturamentoRetencaoTO> ret = faturamentoRetencaoService.obterListaPorCodigoFaturamento(new Long(id));
			return responseOk(ret);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}
}
