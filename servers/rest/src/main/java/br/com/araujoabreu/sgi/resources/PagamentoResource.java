package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/pagamento")
public class PagamentoResource extends BaseCrudResource<PagamentoTO, PagamentoPesquisaTO, PagamentoService> {

	@Autowired
	public PagamentoResource(PagamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/popup/{id}/{tipo}/{codNota}")
	public @ResponseBody
	PagamentoPesquisaTO getPagamentoPopUp(@PathVariable Long id, @PathVariable String tipo, @PathVariable Long codNota) {
		return getService().getPagamentoPopUp(id, tipo, codNota);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/notapagamento/{codPagamento}")
	public @ResponseBody
	ParcelaNotaTO getNotaByCodigoPagamento(@PathVariable Long codPagamento) {
		return getService().getParcelaNotaByPagamento(codPagamento);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/codigonota/{codPagamento}")
	public ResponseEntity getCodigoNotaByCodigoPagamento(@PathVariable Long codPagamento) {
		ParcelaNotaTO parcelaNotaTO = new ParcelaNotaTO();
		parcelaNotaTO.setCodigo(getService().getCodigoNota(codPagamento));
		return responseOk(parcelaNotaTO);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/lote/custos")
	public @ResponseBody
	ParcelaNotaTO mockLoteCustos() {
		return getService().mockLoteCustos();
	}


}
