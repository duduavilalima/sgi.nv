package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/motivo/retorno")
public class MotivoRetornoResource extends BaseCrudResource<MotivoRetornoTO, MotivoRetornoPesquisaTO, MotivoRetornoService> {

	@Autowired
	public MotivoRetornoResource(MotivoRetornoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarMotivoRetornoByChave/{chave}")
	public List<MotivoRetornoTO> buscarListaMotivoRetornoByChaveAplicacao(@PathVariable String chave) throws Exception {
		return getService().buscarListaMotivoRetornoByChaveAplicacao(chave);
	}
}
