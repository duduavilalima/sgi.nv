package br.com.araujoabreu.sgi.resources.motivoDescontoDevolucao;

import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.MotivoDescontoDevolucaoService;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/motivo/desconto/devolucao")
public class MotivoDescontoDevolucaoResource extends BaseCrudResource<MotivoDescontoDevolucaoTO, MotivoDescontoDevolucaoPesquisaTO, MotivoDescontoDevolucaoService> {

	@Autowired
	public MotivoDescontoDevolucaoResource(MotivoDescontoDevolucaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getListaDescontoByCodNota/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity<MotivoDescontoDevolucaoTO> buscarListaDescontoByCodNota(@PathVariable String id) {
		try {
			List<MotivoDescontoDevolucaoTO> lista = getService().listarDescontoPorMatrizNF(Long.parseLong(id));
			return responseOk(lista);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getListaDevolucaoByCodNota/{id:[0-9]+}")
	public @ResponseBody
	ResponseEntity<MotivoDescontoDevolucaoTO> buscarListaDevolucaoByCodNota(@PathVariable String id) {
		try {
			List<MotivoDescontoDevolucaoTO> lista = getService().listarDevolucaoPorMatrizNF(Long.parseLong(id));
			return responseOk(lista);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
