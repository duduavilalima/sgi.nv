package br.com.araujoabreu.sgi.service.loteInterfaceFaturamento;

import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.fasolti.commons.client.service.CrudService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public interface LoteInterfaceFaturamentoService extends CrudService<LoteInterfaceTO> {
	LoteInterfaceTO reprocessarLote(Long id);

	LoteInterfaceTO processarLote(Long id);

	void baixarAnexo(String id, HttpServletRequest request, HttpServletResponse response) throws Exception;

	void verificarAnexo(String id) throws Exception;

	List<AnexoTO> getListaAnexo(Long id);

	LoteInterfaceTO qtdLote(LoteInterfaceTO loteInterfaceTO);

	void gerarLoteAPartirDeJobs(Long codigoLote);

	void confirmarEnvio(Long id);

	List<NotaTO> getNotasFiscaisLotePrefeitura(String id);

	List<NotaSaidaTO> getNotasPorTipoPrefeitura(LoteInterfaceTO loteInterfaceTO);

	Boolean notaFiscalDisponivelParaLoteFATN(Date dataEmissao, Long codigoMatrizUnidade);

	Boolean notaFiscalDisponivelParaLoteBaixasDiarias(Date data, Long codigoMatrizUnidade);

}
