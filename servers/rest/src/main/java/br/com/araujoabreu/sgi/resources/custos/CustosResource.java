package br.com.araujoabreu.sgi.resources.custos;

import br.com.araujoabreu.sgi.service.custos.CustosService;
import br.com.araujoabreu.sgi.service.custos.transfer.ImportacaoOrcamentoEmpresarialTO;
import br.com.fasolti.commons.rest.ResponseSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rest/custos")
public class CustosResource extends ResponseSupport {

	@Autowired
	private CustosService custosService;

	@RequestMapping(method = RequestMethod.POST, value = "/upload/planilha/{fileName}/{extension}")
	public final ResponseEntity uploadPlanilha(@RequestBody ImportacaoOrcamentoEmpresarialTO importacaoOrcamentoEmpresarialTO, @PathVariable("fileName") String fileName, @PathVariable("extension") String extension, BindingResult result) {
		try {
			custosService.importacaoOrcamentoEmpresarial(importacaoOrcamentoEmpresarialTO);
		} catch (Exception e) {
			return responseServerError(e);
		}
		return responseOk();
	}

}
