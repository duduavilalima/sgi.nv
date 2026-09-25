package br.com.araujoabreu.sgi.resources.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.RelacaoBancariaService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPagamentosVO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest/tesouraria/relacaobancaria")
public class RelacaoBancariaResource extends BaseCrudResource<RelacaoBancariaTO, RelacaoBancariaPesquisaTO, RelacaoBancariaService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelacaoBancariaResource.class);

	@Autowired
	public RelacaoBancariaResource(RelacaoBancariaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/cancelar")
	public ResponseEntity cancelarRelacaoBancaria(@PathVariable Long id) {
		try {
			return responseOk(getService().cancelarRelacaoBancaria(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/enviar")
	public ResponseEntity enviarRelacaoBancaria(@PathVariable Long id) {
		try {
			return responseOk(getService().enviarRelacaoBancaria(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/itemrelacao/{id}/remover")
	public ResponseEntity removerPagamentoRB(@PathVariable Long id) {
		try {
			getService().removerPagamentoRB(id);
			return responseOk();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/itemrelacao/editar")
	public ResponseEntity editarPagamentoRB(@RequestBody RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) {
		try {
			return responseOk(getService().editarPagamentoRB(relacaoBancariaPagamentosVO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/realizarbaixabancaria")
	public HttpEntity<Void> realizarBaixaBancaria(@RequestBody RelacaoBancariaTO relacaoBancariaTO) {
		try {
			getService().realizarBaixaBancaria(relacaoBancariaTO);
			return responseOk();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/baixamanual")
	public ResponseEntity realizarBaixaBancariaManual(@RequestBody RelacaoBancariaTO to) {
		try {
			return responseOk(getService().realizarBaixaBancariaManual(to));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/estornobaixamanual")
	public ResponseEntity estornarBaixaBancariaManual(@RequestBody RelacaoBancariaTO to) {
		try {
			return responseOk(getService().estornarBaixaBancariaManual(to));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/adicionarpagamentos")
	public @ResponseBody
	ResponseEntity adicionarPagamentos(@RequestBody RelacaoBancariaTO rbTO) {
		try {
			return responseOk(getService().adicionarPagamentos(rbTO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
		return responseServerError(error.getBindingResult());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/pesquisarpagamentos", consumes = "application/json")
	public @ResponseBody
	ResponseEntity pesquisarPagamentos(@RequestBody RelacaoBancariaTO rbTO) {
		try {
			return responseOk(getService().pesquisarPagamentos(rbTO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/retornobancario/estornado")
	public ResponseEntity obterRetornosBancarios() {
		try {
			return responseOk(getService().obterRetornosBancarios());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/buscarultimocheque")
	public ResponseEntity obterUltimoChequeRB(@PathVariable Long id) {
		try {
			return responseOk(getService().obterUltimoChequeRB(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/imprimircheques/{tipo}")
	public HttpEntity<Void> imprimirCheques(@RequestBody RelacaoBancariaTO to, @PathVariable String tipo) {
		try {
			getService().imprimirCheques(to, tipo);
			return responseOk();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/itemrelacao/{id}")
	public ResponseEntity obterItensRelacao(@PathVariable Long id) {
		try {
			return responseOk(getService().obterItensRelacao(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/itensrelacao")
	public ResponseEntity obterItensDaRelacaoBancaria(@PathVariable Long id) {
		try {
			return responseOk(getService().obterItensDaRelacaoBancaria(id));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/baixarComprovante/existe/{id:[0-9]+}")
	public ResponseEntity baixarComprovante(@PathVariable String id) {
		try {
			getService().baixarComprovanteExiste(id);
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/baixarComprovante/{id:[0-9]+}")
	public ResponseEntity baixarComprovante(@PathVariable String id, final HttpServletRequest request, final HttpServletResponse response) {
		try {
			getService().baixarComprovante(id, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return responseServerError(e);
		}
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/itemRelacao/vo")
	public @ResponseBody
	ResponseEntity obterItemRelacaoVO(@RequestBody RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) {
		try {
			return responseOk(getService().criarRelacaoBancariaPagamentosVO(relacaoBancariaPagamentosVO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}
}
