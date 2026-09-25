package br.com.araujoabreu.sgi.resources.administracao;

import br.com.araujoabreu.sgi.service.administracao.ColaboradorService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/colaborador")
public class ColaboradorResource extends BaseCrudResource<ColaboradorTO, ColaboradorPesquisaTO, ColaboradorService> {

	@Autowired
	public ColaboradorResource(ColaboradorService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarListaColaborador")
	public List<ColaboradorTO> buscarColaboradores(@RequestBody ColaboradorTO colaboradorTO) {
		return getService().buscarColaboradores(colaboradorTO);
	}
}
