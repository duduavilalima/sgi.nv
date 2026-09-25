package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.Date;

public interface ParcelaService extends CrudService<NotaSaidaParcelaTO> {
	void calcularValorLiquido(NotaSaidaParcelaPesquisaTO to);

	NotaSaidaParcelaTO updateParcela(NotaSaidaParcelaTO transferObj, Long id);

	NotaSaidaParcelaTO updateParcelaRecebimento(NotaSaidaParcelaTO transferObj, Long id) throws Exception;

	NotaSaidaParcelaTO updateAgendamento(NotaSaidaParcelaTO transferObj, Long id);

	void cancelarParcelaRecebimento(Long codigoNotaSaidaParcela);

	NotaSaidaParcelaTO marcarCobrancaCronica(NotaSaidaParcelaTO transferObj, Long codigoNotaSaidaParcela);

	NotaSaidaParcelaTO desmarcarCobrancaCronica(NotaSaidaParcelaTO transferObj, Long codigoNotaSaidaParcela);

	void salvarObservacao(NotaSaidaParcelaTO observacoesImpressao);

	Boolean parcelaDisponivelParaLote(Long id);
}
