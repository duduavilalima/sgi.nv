package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.contaCorrente.ContaCorrenteService;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrentePesquisaTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequesEmitidosExibirTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaTO;
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
@RequestMapping("/rest/contacorrente")
public class ContaCorrenteResource extends BaseCrudResource<ContaCorrenteTO, ContaCorrentePesquisaTO, ContaCorrenteService> {

	@Autowired
	public ContaCorrenteResource(ContaCorrenteService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigo}/chequescontacorrente")
	public @ResponseBody
	ResponseEntity<List<ChequesEmitidosExibirTO>> buscarChequesContaCorrente(@PathVariable String codigo) {
		try {
			return responseOk(getService().buscarChequesContaCorrente(codigo));
		} catch (ValidationException e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saldos")
	public ResponseEntity<ConciliacaoBancariaTO> buscarSaldos(@RequestBody ConciliacaoBancariaTO to) throws Exception {
		try {
			return responseOk(getService().buscarSaldos(to));
		} catch (ValidationException e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigopessoa}/chequescontacorrentematriz")
	public @ResponseBody
	ResponseEntity<List<ContaCorrenteTO>> buscarChequesContaCorrenteMatriz(@PathVariable String codigopessoa) {
		try {
			return responseOk(getService().buscarChequesContaCorrenteMatriz(codigopessoa));
		} catch (ValidationException e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/contascorrentes")
	public @ResponseBody
	ResponseEntity<List<ContaCorrenteTO>> obterContasCorrentes(@RequestBody ContaCorrenteTO contaCorrenteTO) {
		try {
			return responseOk(getService().obterContasCorrentes(contaCorrenteTO));
		} catch (ValidationException e) {
			return responseServerError(e);
		}
	}
}
