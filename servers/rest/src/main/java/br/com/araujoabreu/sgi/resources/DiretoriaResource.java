package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.Diretoria.DiretoriaService;
import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaPesquisaTO;
import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/diretoria")
public class DiretoriaResource extends BaseCrudResource<DiretoriaSimpleTO, DiretoriaPesquisaTO, DiretoriaService> {

	@Autowired
	public DiretoriaResource(DiretoriaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarDiretoriasDeContrato")
	public List<DiretoriaSimpleTO> buscarDiretoriasDeContratos() {
		return getService().buscarDiretoriasDeContratos();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarDiretoria")
	public List<DiretoriaSimpleTO> buscarDiretoriaDeContratosByColaborador() {
		return getService().buscarDiretoriaDeContratosByColaborador();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{idEmpresa:[0-9]+}/buscarDiretoriaPorEmpresa")
	public List<DiretoriaSimpleTO> buscarDiretoriaPorEmpresa(@PathVariable String idEmpresa) throws Exception {
		return getService().buscarDiretoriaPorEmpresa(idEmpresa);
	}
}
