package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.MovimentoAutorizadoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/tesouraria/movimentoautorizado")
public class MovimentoAutorizadoResource extends BaseCrudResource<MovimentoAutorizadoTO, MovimentoAutorizadoPesquisaTO, MovimentoAutorizadoService> {

	@Autowired
	public MovimentoAutorizadoResource(MovimentoAutorizadoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/cancelar")
	public HttpEntity<Void> cancelar(@RequestBody Long codigo) {
		getService().cancelar(codigo);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/atualizarpagamento")
	public @ResponseBody
	PagamentoTO getPagamentoAtualizarMovimento(@RequestBody PagamentoTO pagamentoTO) {
		return getService().pagamentoAtualizarMovimento(pagamentoTO);
	}
}
