package br.com.araujoabreu.sgi.resources.nota;

import br.com.araujoabreu.sgi.lang.Response;
import br.com.araujoabreu.sgi.service.nota.NotaFiscalFaturamentoService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalFaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
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
@RequestMapping("/rest/faturamento/notafiscal")
public class NotaFiscalFaturamentoResource extends BaseCrudResource<NotaSaidaTO, NotaSaidaPesquisaTO, NotaFiscalFaturamentoService> {

	protected static final String MONTAR = "/montar";

	protected static final String BUSCA_NUMERO_NF = "/busca/numero/notafiscal";

	protected static final String BUSCA_NF = "/busca/notafiscal";

	protected static final String BUSCA_NF_IMPRESSAO = "/buscarnfimpressao/{id:[0-9]+}";

	protected static final String BUSCA_ST = "/servicotipo";

	protected static final String SALVAR_NF = "/salvarnf";

	protected static final String NUMERAR_NF = "/numerarnf";

	protected static final String CANCELAR_NUMERACAO_NF = "/cancelarnumeracaonf";

	protected static final String CONFIRMAR_CADASTRO_NF = "/confirmarcadastronf";

	protected static final String REMOVER_NF = "/removernf";

	protected static final String ALTERAR_VALOR_BRUTO = "/alterarValorBruto";

	protected static final String VERIFICAR_NF_LOTE = "/verificarNfLote/{id:[0-9]+}";

	@Autowired
	public NotaFiscalFaturamentoResource(NotaFiscalFaturamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = MONTAR)
	public @ResponseBody
	ResponseEntity<NotaFiscalFaturamentoTO> createNotaFiscal(@RequestBody NotaFiscalFaturamentoTO exampleObj) {
		try {
			return responseOk(getService().createNotaFiscal(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_NUMERO_NF)
	public @ResponseBody
	ResponseEntity<NotaFiscalFaturamentoTO> findLastNotaFiscalByUnidadeCodigo(@RequestBody NotaFiscalFaturamentoTO exampleObj) {
		try {
			return responseOk(getService().findLastNotaFiscalByUnidadeCodigo(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_NF)
	public @ResponseBody
	ResponseEntity<NotaFiscalFaturamentoTO> findNotaFiscalByCodigo(@RequestBody NotaFiscalFaturamentoTO exampleObj) {
		try {
			return responseOk(getService().findNotaFiscalByCodigo(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = BUSCA_ST)
	public @ResponseBody
	ResponseEntity buscarServicos(@RequestBody ServicoTipoTO exampleObj) {
		try {
			return responseOk(getService().buscarServicos(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = SALVAR_NF)
	public ResponseEntity<NotaFiscalFaturamentoTO> saveNF(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			return responseOk(getService().saveNF(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = NUMERAR_NF)
	public ResponseEntity<NotaFiscalFaturamentoTO> numerarNF(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			return responseOk(getService().numerarNF(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = CANCELAR_NUMERACAO_NF)
	public ResponseEntity<NotaFiscalFaturamentoTO> cancelarNumeracaoNF(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			return responseOk(getService().cancelarNumeracaoNF(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "cancelarnotafiscal")
	public ResponseEntity<NotaSaidaTO> cancelarNf(@RequestBody NotaSaidaTO to) {
		try {
			return responseOk(getService().cancelarNf(to));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = CONFIRMAR_CADASTRO_NF)
	public ResponseEntity<NotaFiscalFaturamentoTO> confirmaCadastroNF(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			return responseOk(getService().confirmaCadastroNF(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = REMOVER_NF)
	public HttpEntity<Void> removerNF(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			getService().removerNF(transferObj);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = BUSCA_NF_IMPRESSAO)
	public @ResponseBody
	ResponseEntity<NotaFiscalFaturamentoTO> findNotaFiscalParaImpressaoByCodigo(@PathVariable String id) {
		try {
			return responseOk(getService().findNotaFiscalParaImpressaoByCodigo(id));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = ALTERAR_VALOR_BRUTO)
	public HttpEntity<Void> alterarValorBruto(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			getService().alterarValorBruto(transferObj);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvarProtocolo")
	public HttpEntity<Void> salvarProtocolo(@RequestBody NotaFiscalFaturamentoTO transferObj) {
		try {
			return responseOk(getService().salvarProtocolo(transferObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/pesquisaPorNumeroAutoComplete")
	public @ResponseBody
	ResponseEntity<List<NotaSaidaTO>> pesquisaPorNumeroAutoComplete(@RequestBody NotaFiscalFaturamentoTO exampleObj) {
		try {
			return responseOk(getService().pesquisaPorNumeroAutoComplete(exampleObj));
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/atualizarAssociacaoNfSubstituta")
	public @ResponseBody
	ResponseEntity atualizarAssociacaoNfSubstituta(@RequestBody NotaFiscalFaturamentoTO exampleObj) {
		try {
			getService().atualizarAssociacaoNfSubstituta(exampleObj);
			return responseOk();
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = VERIFICAR_NF_LOTE)
	public @ResponseBody
	ResponseEntity<NotaTO> verificarNfLote(@PathVariable String id) {
		try {
			Boolean resultado = getService().nfDisponivelParaLote(Long.valueOf(id));
			NotaTO notaTO = new NotaTO();
			notaTO.setSelecao(resultado);
			return responseOk(notaTO);
		} catch (Exception e) {
			return responseServerError(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/verificarContaContabilCliente/{codigoNotaFiscal:[0-9]+}")
	public Response verificarContaContabilCliente(@PathVariable String codigoNotaFiscal) {
		Response.Builder response = new Response.Builder();
		MenssagemPadraoTO menssagemPadraoTO = getService().possuiContaContabilAssociada(Long.parseLong(codigoNotaFiscal));
		if (menssagemPadraoTO.getWarnings() != null && !menssagemPadraoTO.getWarnings().isEmpty()){
			response.warnings(menssagemPadraoTO.getWarnings());
		}
		return response.build();
	}

}
