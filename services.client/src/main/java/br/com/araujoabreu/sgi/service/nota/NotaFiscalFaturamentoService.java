package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalFaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;

import java.math.BigDecimal;
import java.util.List;

public interface NotaFiscalFaturamentoService extends CrudService<NotaSaidaTO> {
	NotaFiscalFaturamentoTO createNotaFiscal(NotaFiscalFaturamentoTO exampleObj);

	NotaFiscalFaturamentoTO findLastNotaFiscalByUnidadeCodigo(NotaFiscalFaturamentoTO exampleObj);

	NotaFiscalFaturamentoTO findNotaFiscalByCodigo(NotaFiscalFaturamentoTO exampleObj) throws ValidationException;

	List<ServicoTipoPesquisaTO> buscarServicos(ServicoTipoTO exampleObj);

	NotaFiscalFaturamentoTO saveNF(NotaFiscalFaturamentoTO transferObj) throws Exception;

	NotaFiscalFaturamentoTO numerarNF(NotaFiscalFaturamentoTO transferObj) throws Exception;

	NotaFiscalFaturamentoTO cancelarNumeracaoNF(NotaFiscalFaturamentoTO transferObj) throws Exception;

	NotaSaidaTO cancelarNf(NotaSaidaTO to) throws Exception;

	NotaFiscalFaturamentoTO confirmaCadastroNF(NotaFiscalFaturamentoTO transferObj) throws Exception;

	void removerNF(NotaFiscalFaturamentoTO transferObj) throws Exception;

	NotaFiscalFaturamentoTO findNotaFiscalParaImpressaoByCodigo(String id);

	void alterarValorBruto(NotaFiscalFaturamentoTO transferObj);

	NotaFiscalFaturamentoTO salvarProtocolo(NotaFiscalFaturamentoTO transferObj);

	List<NotaSaidaTO> pesquisaPorNumeroAutoComplete(NotaFiscalFaturamentoTO to);

	void atualizarAssociacaoNfSubstituta(NotaFiscalFaturamentoTO to);

	Boolean nfDisponivelParaLote(Long id);

	BigDecimal calcularValorLiquido(NotaSaidaTO notaSaidaTO);

	MenssagemPadraoTO possuiContaContabilAssociada(Long codigoNotaFiscal);
}
