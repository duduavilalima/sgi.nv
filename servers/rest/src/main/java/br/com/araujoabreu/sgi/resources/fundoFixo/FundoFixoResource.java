package br.com.araujoabreu.sgi.resources.fundoFixo;

import br.com.araujoabreu.sgi.service.fundoFixo.FundoFixoService;
import br.com.araujoabreu.sgi.service.fundoFixo.transfer.FundoFixoPesquisaTO;
import br.com.araujoabreu.sgi.service.fundoFixo.transfer.FundoFixoTO;
import br.com.araujoabreu.sgi.service.generico.ObjetoGenericoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/fundofixo")
public class FundoFixoResource extends BaseCrudResource<FundoFixoTO, FundoFixoPesquisaTO, FundoFixoService> {

	private final FundoFixoService fundoFixoService;

	@Autowired
	public FundoFixoResource(FundoFixoService service) {
		super(service);
		this.fundoFixoService = service;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarqtdfundofixoporcf/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity<ObjetoGenericoTO> buscarQtdFundoFixoPorCf(@PathVariable String id) {
		try {
			Long quantidade = fundoFixoService.buscarQtdFundoFixoPorCfId(Long.parseLong(id));

			ObjetoGenericoTO obj = new ObjetoGenericoTO();
			obj.setQtd(quantidade);

			return responseOk(obj);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
