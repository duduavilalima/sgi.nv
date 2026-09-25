package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.LancamentoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/lancamentos")
public class LancamentoResource extends BaseCrudResource<LancamentoTO, LancamentoPesquisaTO, LancamentoService> {

	@Autowired
	public LancamentoResource(LancamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/cancelar")
	public HttpEntity<Void> cancelar(@RequestBody Long codigo) {
		getService().cancelar(codigo);
		return ResponseEntity.noContent().build();
	}

}
