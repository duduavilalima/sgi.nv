package br.com.araujoabreu.sgi.resources.controlefiscal;

import br.com.araujoabreu.sgi.service.controlefiscal.ReinfService;
import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfPesquisaTO;
import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/reinf")
public class ReinfResource extends BaseCrudResource<ReinfTO, ReinfPesquisaTO, ReinfService> {

	@Autowired
	public ReinfResource(ReinfService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/gerar/{referencia}/{empresa:[0-9]+}")
	public @ResponseBody ReinfTO gerarReinf(@PathVariable String referencia, @PathVariable long empresa) {
		try {
			return getService().gerarReinf(referencia, empresa);
		} catch (Exception e) {
			e.printStackTrace();
			return new ReinfTO();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/gerar/{evento}/{referencia}/{empresa:[0-9]+}")
	public @ResponseBody ReinfTO gerarReinfPorEvento(@PathVariable String evento, @PathVariable String referencia, @PathVariable long empresa) {
		try {
			return getService().gerarReinfPorEvento(evento, referencia, empresa);
		} catch (Exception e) {
			e.printStackTrace();
			return new ReinfTO();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/processar/{referencia}/{empresa:[0-9]+}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void processarRetorno(@PathVariable String referencia, @PathVariable long empresa) throws Exception {
			getService().processarRetorno(referencia, empresa);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/processartodos/{ano:[0-9]+}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void processarRetorno(@PathVariable int ano) throws Exception {
		getService().processarTodosRetornos(ano);
	}
}
