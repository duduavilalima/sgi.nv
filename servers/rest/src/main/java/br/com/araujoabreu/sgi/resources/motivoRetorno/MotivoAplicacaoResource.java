package br.com.araujoabreu.sgi.resources.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoAplicacaoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/motivo/aplicacao")
public class MotivoAplicacaoResource extends BaseCrudResource<MotivoAplicacaoTO, MotivoAplicacaoPesquisaTO, MotivoAplicacaoService> {

	@Autowired
	public MotivoAplicacaoResource(MotivoAplicacaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarByChave/{chave}")
	public MotivoAplicacaoTO buscarMotivoAplicacaoByChave(@PathVariable String chave) throws Exception {
		return getService().buscarMotivoAplicacaoByChave(chave);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarByCodMotivoRetornoDocumento/{id:[0-9]+}")
	public @ResponseBody
	MotivoAplicacaoTO buscarMotivoAplicacaoByCodMotivoRetornoDoc(@PathVariable String id) throws Exception {
		return getService().findByCodMotivoRetornoDoc(Long.valueOf(id));
	}
}
