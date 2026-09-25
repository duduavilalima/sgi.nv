package br.com.araujoabreu.sgi.resources.comercial;

import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.comercial.ClienteService;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClientePesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClienteTO;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/clientes")
public class ClienteResource extends BaseCrudResource<ClienteTO, ClientePesquisaTO, ClienteService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteResource.class);

	@Autowired
	public ClienteResource(ClienteService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoCliente}/buscarcontratos")
	public @ResponseBody
	ResponseEntity buscarContratos(@PathVariable String codigoCliente) {
		try {
			return responseOk(getService().buscarContratos(codigoCliente));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoCliente}/buscarsubclientes")
	public @ResponseBody
	ResponseEntity buscarSubclientes(@PathVariable String codigoCliente) {
		try {
			return responseOk(getService().buscarSubclientes(codigoCliente));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoCliente}/buscardadoscontabeis")
	public @ResponseBody
	ResponseEntity buscarDadosContabeis(@PathVariable String codigoCliente) {
		try {
			return responseOk(getService().buscarDadosContabeis(codigoCliente));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoCliente}/buscartelefones")
	public @ResponseBody
	ResponseEntity buscarTelefones(@PathVariable String codigoCliente) {
		try {
			return responseOk(getService().buscarTelefones(codigoCliente));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoCliente}/buscarcontatos")
	public @ResponseBody
	ResponseEntity<List<ContatoTO>> buscarContatos(@PathVariable String codigoCliente) {
		try {
			return responseOk(getService().buscarContatos(codigoCliente));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	public HttpEntity<Void> removerCliente(@PathVariable Long id) {
		try {
			getService().removerCliente(id);
			return responseOk(ResponseEntity.noContent().build());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarclassificacao")
	public @ResponseBody
	ResponseEntity buscarClassificacaoCliente() {
		try {
			return responseOk(getService().buscarClassificacaoCliente());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarrelacionamento")
	public @ResponseBody
	ResponseEntity buscarRelacionamento() {
		try {
			return responseOk(getService().buscarRelacionamento());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscartipologradouro")
	public @ResponseBody
	ResponseEntity buscarTipoLogradouro() {
		try {
			return responseOk(getService().buscarTipoLogradouro());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/consultardocumentopopup")
	public ResponseEntity<ValueTO> consultarDocumentoPopup(@RequestBody ClienteTO clienteTO) {
		try {
			return responseOk(getService().consultarDocumentoPopup(clienteTO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/consultardocumento")
	public ResponseEntity<Object> consultarDocumento(@RequestBody ClienteTO clienteTO) {
		try {
			return responseOk(getService().consultarDocumento(clienteTO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarfornecedor")
	public @ResponseBody
	ResponseEntity converterFornecedor(@RequestBody ClienteTO clienteTO) {
		try {
			return responseOk(getService().converterFornecedor(clienteTO));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return responseServerError(e);
		}
	}
}
