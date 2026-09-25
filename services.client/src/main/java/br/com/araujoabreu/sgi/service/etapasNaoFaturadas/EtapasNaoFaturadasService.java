package br.com.araujoabreu.sgi.service.etapasNaoFaturadas;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalFaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;

import java.util.List;

public interface EtapasNaoFaturadasService {
	NotaSaidaTO findNotaFiscalFaturamento(String id);

	NotaFiscalFaturamentoTO createEtapaNaoFaturada(String id);

	NotaFiscalFaturamentoTO findLastNotaSaidaByUnidadeCodigo(NotaFiscalFaturamentoTO exampleObj);

	List<ServicoTipoPesquisaTO> buscarServicos(ServicoTipoTO exampleObj);

	NotaFiscalFaturamentoTO save(NotaFiscalFaturamentoTO transferObj);

	NotaFiscalFaturamentoTO validarCL(Long id);
}
