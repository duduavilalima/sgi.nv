package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.ConciliacaoBancariaService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaRelacaoBancariaPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.DadosConciliacaoBancariaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/tesouraria/conciliacaobancaria")
public class ConciliacaoBancariaResource extends BaseCrudResource<ConciliacaoBancariaTO, ConciliacaoBancariaPesquisaTO, ConciliacaoBancariaService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConciliacaoBancariaResource.class);

	@Autowired
	public ConciliacaoBancariaResource(ConciliacaoBancariaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/pesquisarelacaobancaria")
	public ResponseEntity pesquisarRelacaoBancaria(@RequestBody ConciliacaoBancariaRelacaoBancariaPesquisaTO filtro) {
		try {
			return responseOk(getService().pesquisarRelacaoBancaria(filtro));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvardados")
	public ResponseEntity salvarDados(@RequestBody DadosConciliacaoBancariaTO dados) {
		try {
			return responseOk(getService().salvarDados(dados));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validarunicidade")
	public HttpEntity<Void> validarUnicidade(@RequestBody ConciliacaoBancariaTO object) {
		List<ConciliacaoBancariaTO> lista = getService().validarUnicidade(object);
		if (lista != null && !lista.isEmpty()) {
			return ResponseEntity.badRequest().build();
		} else {
			return ResponseEntity.ok().build();
		}
	}
}
