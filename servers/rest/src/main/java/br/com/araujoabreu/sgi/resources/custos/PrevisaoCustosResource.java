package br.com.araujoabreu.sgi.resources.custos;

import br.com.araujoabreu.sgi.service.custos.PrevisaoCustosService;
import br.com.araujoabreu.sgi.service.custos.transfer.PrevisaoCustosPesquisaTO;
import br.com.araujoabreu.sgi.service.custos.transfer.PrevisaoCustosTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/previsaocustos")
public class PrevisaoCustosResource extends BaseCrudResource<PrevisaoCustosTO, PrevisaoCustosPesquisaTO, PrevisaoCustosService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrevisaoCustosResource.class);

	@Autowired
	public PrevisaoCustosResource(PrevisaoCustosService previsaoCustosService) {
		super(previsaoCustosService);
	}
}
