package br.com.araujoabreu.sgi.resources.contrato;

import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.contrato.ContratoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
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
@RequestMapping("/rest/contrato")
public class ContratoResource extends BaseCrudResource<ContratoTO, ContratoPesquisaTO, ContratoService> {

	@Autowired
	public ContratoResource(ContratoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoContrato}/buscarenderecos")
	public @ResponseBody
	List<ContratoEnderecoTO> buscarEnderecos(@PathVariable String codigoContrato) {
		return getService().buscarEnderecos(codigoContrato);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoContrato}/buscarcontato")
	public @ResponseBody
	List<ContratoContatoTO> buscarContato(@PathVariable String codigoContrato) {
		return getService().buscarContato(codigoContrato);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/buscarcentrolucro")
	public @ResponseBody
	List<CentroFinanceiroTO> buscarCentroLucro(@RequestBody ContratoTO contrato) throws Exception {
		return getService().buscarCentroLucro(contrato);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoContrato}/buscaraditivos")
	public @ResponseBody
	List<AditivoTO> buscarAditivos(@PathVariable String codigoContrato) {
		return getService().buscarAditivos(codigoContrato);
	}

	@RequestMapping(method = RequestMethod.GET, value = "{codigoContrato}/buscaranexos")
	public @ResponseBody
	List<ContratoAnexoTO> buscarAnexos(@PathVariable String codigoContrato) {
		return getService().buscarAnexos(codigoContrato);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/busca/cidadeunidade")
	public CidadeTO findCidadeUnidade(@RequestBody ContratoTO contratoTO) throws Exception {
		return getService().findCidadeUnidade(contratoTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/busca/cidadeunidadematriz")
	public CidadeTO findCidadeUnidadeMatriz(@RequestBody ContratoTO contratoTO) throws Exception {
		return getService().findCidadeUnidadeMatriz(contratoTO);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/cliente")
	public PessoaTO obterClienteContrato(@PathVariable String id) throws Exception {
		return getService().obterClienteContrato(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/reativarcontrato")
	public HttpEntity<Void> reativar(@PathVariable Long id) throws Exception {
		getService().reativar(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/encerrarcontrato")
	public HttpEntity<Void> encerrar(@PathVariable Long id) throws Exception {
		getService().encerrar(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/paralisarcontrato")
	public HttpEntity<Void> paralisar(@PathVariable Long id) throws Exception {
		getService().paralisar(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscartipologradouro")
	public @ResponseBody
	List<TipoLogradouroTO> buscarTipoLogradouro() {
		return getService().buscarTipoLogradouro();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/consultardocumento")
	public ResponseEntity<ContratoTO> consultarDocumento(@RequestBody ContratoTO contratoTO) {
		try {
			return responseOk(getService().consultarDocumento(contratoTO));
		} catch (Exception e) {
			return responseServerError(e);

		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/totalcentrosfinanceiros")
	public ContratoTO totalCentrosFinanceirosResource(@RequestBody ContratoTO contrato) throws Exception {
		return getService().totalCentrosFinanceirosResource(contrato);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/removecentrofinanceiro")
	public HttpEntity<Void> removeCentroFinanceiro(@PathVariable Long id) throws Exception {
		getService().removeCentroFinanceiro(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/calcularprazocontrato")
	public ContratoTO calcularPrazoContrato(@RequestBody ContratoTO contrato) {
		return getService().calcularPrazoContrato(contrato);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/atualizatotais")
	public HttpEntity<Void> atualizarTotalEhCustoMensal(@PathVariable Long id) {
		getService().atualizarCustoMensalEhTotal(id);
		return ResponseEntity.noContent().build();
	}
}
