package br.com.araujoabreu.sgi.resources.contasapagar;

import br.com.araujoabreu.sgi.service.nota.NotaFiscalService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/contasapagar/notafiscal")
public class NotaFiscalResource extends BaseCrudResource<NotaFiscalTO, NotaFiscalPesquisaTO, NotaFiscalService> {

	@Autowired
	public NotaFiscalResource(NotaFiscalService service) {
		super(service);
	}


}
