package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaBaixaRetencaoService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaBaixaRetencaoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/notaSaidaBaixaRetencao")
public class NotaSaidaParcelaBaixaRetencaoResource extends BaseCrudResource<NotaSaidaParcelaBaixaRetencaoTO, NotaSaidaParcelaBaixaRetencaoTO, NotaSaidaParcelaBaixaRetencaoService> {

	@Autowired
	public NotaSaidaParcelaBaixaRetencaoResource(NotaSaidaParcelaBaixaRetencaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/baixas/{id:[0-9]+}")
	public ResponseEntity obterBaixasRetencaoDaNotaSaidaParcela(@PathVariable Long id) {
		try {
			return responseOk(getService().obterBaixasRetencaoDaNotaSaidaParcela(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/baixas/remover/{id:[0-9]+}")
	public ResponseEntity removerBaixaRetencao(@PathVariable Long id) {
		try {
			getService().removerBaixaRetencao(id);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/verificarLoteBaixas/{id:[0-9]+}")
	public ResponseEntity verificarLoteBaixas(@PathVariable Long id) {
		try {
			return responseOk(getService().verificarLoteBaixas(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
