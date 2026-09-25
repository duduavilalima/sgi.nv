package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.feriado.transfer.FeriadoPesquisaTO;
import br.com.araujoabreu.sgi.service.feriado.transfer.FeriadoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.DatasParcelaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/feriado")
public class FeriadoResource extends BaseCrudResource<FeriadoTO, FeriadoPesquisaTO, FeriadoService> {

	@Autowired
	public FeriadoResource(FeriadoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarTodos")
	public @ResponseBody
	List<FeriadoTO> buscarTodos() {
		return getService().buscarTodos();
	}

	@RequestMapping(method = RequestMethod.GET, value = "data/pagamento/agendamento")
	public @ResponseBody
	DatasParcelaTO getDataPagamentoDataAgendamento(@RequestParam String dataVencimento) {
		return getService().getDataPagamentoDataAgendamento(dataVencimento);
	}

}
