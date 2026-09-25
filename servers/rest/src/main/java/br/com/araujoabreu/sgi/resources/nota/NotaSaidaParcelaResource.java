package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/notafiscalfaturamento/parcela")
public class NotaSaidaParcelaResource extends BaseCrudResource<NotaSaidaParcelaTO, NotaSaidaParcelaPesquisaTO, NotaSaidaParcelaService> {

	@Autowired
	public NotaSaidaParcelaResource(NotaSaidaParcelaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listarPorCodNotaSaida/{id:[0-9]+}")
	public ResponseEntity listaPorNota(@PathVariable Long id) {
		try {
			return responseOk(getService().getListaParcelaByNotaSaidaId(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/verificarLoteLoteContabilDiario/{id:[0-9]+}")
	public ResponseEntity verificarLoteLoteContabilDiario(@PathVariable Long id) {
		try {
			return responseOk(getService().verificarLoteLoteContabilDiario(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
