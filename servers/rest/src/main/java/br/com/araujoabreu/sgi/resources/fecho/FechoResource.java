package br.com.araujoabreu.sgi.resources.fecho;

import br.com.araujoabreu.sgi.service.fecho.FechoService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
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
@RequestMapping("/rest/fecho")
public class FechoResource extends BaseCrudResource<FechoTO, FechoPesquisaTO, FechoService> {

	@Autowired
	public FechoResource(FechoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fechoByCodCentroFinanceiro/{id:[0-9]+}")
	public @ResponseBody
	FechoTO fechoByCodCentroFinanceiro(@PathVariable String id) throws Exception {
		return getService().fechoByCodCentroFinanceiro(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listaFechoByCodCentroFinanceiro/{id:[0-9]+}")
	public @ResponseBody
	List<FechoTO> listaFechoByCodCentroFinanceiro(@PathVariable String id) throws Exception {
		return getService().listaFechoByCodCentroFinanceiro(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/removerReajusteByCodCentroFinanceiro/{id:[0-9]+}")
	public HttpEntity<Void> removerReajusteByCodCentroFinanceiro(@PathVariable String id) throws Exception {
		getService().removerReajusteByCodCentroFinanceiro(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addReajuste")
	public HttpEntity<Void> addReajuste(@RequestBody FechoTO fechoTO) throws Exception {
		getService().addReajuste(fechoTO);
		return ResponseEntity.noContent().build();
	}

}
