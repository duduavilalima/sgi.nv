package br.com.araujoabreu.sgi.resources;

import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceFaturamentoService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfacePesquisaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest/faturamento/interfaces")
public class LoteInterfaceFaturamentoResource extends BaseCrudResource<LoteInterfaceTO, LoteInterfacePesquisaTO, LoteInterfaceFaturamentoService> {

	@Autowired
	public LoteInterfaceFaturamentoResource(LoteInterfaceFaturamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/reprocessar")
	public ResponseEntity reprocessarLote(@PathVariable Long id) {
		try {
			return responseOk(getService().reprocessarLote(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/processar")
	public ResponseEntity processarLote(@PathVariable Long id) {
		try {
			return responseOk(getService().processarLote(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/baixaranexo/{id:[0-9]+}")
	public ResponseEntity baixarAnexo(@PathVariable String id, final HttpServletRequest request, final HttpServletResponse response) {
		try {
			getService().baixarAnexo(id, request, response);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/verificaranexo")
	public ResponseEntity verificarAnexo(@RequestBody String id) {
		try {
			getService().verificarAnexo(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/anexolista")
	public ResponseEntity getListaAnexo(@PathVariable Long id) {
		try {
			return responseOk(getService().getListaAnexo(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/qtdlote")
	public ResponseEntity qtdLote(@RequestBody LoteInterfaceTO loteInterfaceTO) {
		try {
			return responseOk(getService().qtdLote(loteInterfaceTO));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/confirmarEnvio")
	public ResponseEntity confirmarEnvio(@PathVariable Long id) {
		try {
			getService().confirmarEnvio(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listaNotaFiscalPrefeitura/{id:[0-9]+}")
	public ResponseEntity getNotasFiscaisLotePrefeitura(@PathVariable String id) {
		try {
			return ResponseEntity.ok(getService().getNotasFiscaisLotePrefeitura(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/verificarNotaFiscalTipoPrefeitura")
	public ResponseEntity verificarNotasFiscaisLoteTipoPrefeitura(@RequestBody LoteInterfaceTO loteInterfaceTO) {
		try {
			return ResponseEntity.ok(getService().getNotasPorTipoPrefeitura(loteInterfaceTO));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}
}
