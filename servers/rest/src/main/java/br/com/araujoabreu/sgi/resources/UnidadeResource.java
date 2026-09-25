package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadePesquisaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/unidade")
public class UnidadeResource extends BaseCrudResource<UnidadeTO, UnidadePesquisaTO, UnidadeService> {

	@Autowired
	public UnidadeResource(UnidadeService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/unidadesporempresa")
	public @ResponseBody
	List<UnidadeTO> listarUnidadesPorEmpresa() {
		return getService().listarUnidadesPorEmpresa();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/unidadesporempresaexceto")
	public @ResponseBody
	ResponseEntity<List<UnidadeTO>> listarUnidadesPorEmpresaExceto(@RequestBody List<Long> codigosUnidadesIgnoradas) {
		try {
			return ResponseEntity.ok(getService().listarUnidadesPorEmpresaExceto(codigosUnidadesIgnoradas));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/unidadesfaturamento")
	public @ResponseBody
	ResponseEntity<List<UnidadeTO>> listarUnidadesFaturamento() {
		try {
			return ResponseEntity.ok(getService().listarUnidadesFaturamento());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/unidadesmatrizativas")
	public @ResponseBody
	ResponseEntity<List<UnidadeTO>> listarUnidadeMatrizAtiva() {
		try {
			return ResponseEntity.ok(getService().listarUnidadeMatrizAtiva());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}


}
