package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.banco.BancoService;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoPesquisaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.util.ResponseSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/banco")
public class BancoResource extends ResponseSupport {

	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String ID = "/{id:[0-9]+}";
	protected static final String BUSCA = "/busca";
	protected static final String BUSCA_POR_EXEMPLO = "/buscaexemplo";
	protected static final String TODOS = "/todos";

	@Autowired
	private BancoService bancoService;

	public BancoResource() {
	}

	@RequestMapping(method = RequestMethod.GET, value = "/todosordenado")
	public @ResponseBody
	List<BancoTO> buscaTodosOrdenado() {
		return bancoService.buscaTodosOrdenado();
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_POR_EXEMPLO)
	public @ResponseBody
	Iterable<BancoTO> buscaPorExemplo(BancoTO exampleObj) {
		return bancoService.buscaPorExemploMethod(exampleObj);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	public @ResponseBody
	ResponseEntity buscaPorExemplo(
		@RequestBody BancoPesquisaTO exampleObj,
		@RequestParam(defaultValue = "0") Integer pageNumber,
		@RequestParam(defaultValue = "asc") String sortDir,
		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
		@RequestParam(required = false) String sortField,
		@RequestParam(required = false, defaultValue = "false") String sortFieldString) {
		try {
			return responseOk(bancoService.buscaPorExemploMethod(exampleObj, pageNumber, sortDir, pageSize, sortField, sortFieldString));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = ID)
	public @ResponseBody
	ResponseEntity buscaPorId(@PathVariable String id) {
		try {
			return responseOk(bancoService.buscaPorIdMethod(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = TODOS)
	public @ResponseBody
	Iterable<BancoTO> buscaTodos() {
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<BancoTO> create() {
		try {
			return responseOk(bancoService.createMethod());
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity save(@RequestBody BancoTO transferObj) {
		try {
			return responseOk(bancoService.saveMethod(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = ID)
	public ResponseEntity update(@RequestBody BancoTO transferObj, @PathVariable Long id) {
		try {
			return responseOk(bancoService.updateMethod(transferObj, id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = ID)
	public @ResponseBody
	ResponseEntity delete(@PathVariable String id) {
		try {
			bancoService.deleteMethod(id);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
