package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaRetencaoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.math.BigDecimal;

public interface NotaSaidaParcelaRetencaoService extends CrudService<NotaSaidaParcelaRetencaoTO> {
	void salvarNovaNotaSaidaParcelaRetencaoACESSORIA(Long codigoNotaSaidaParcela, BigDecimal valorRetido);

	void salvarNovaNotaSaidaParcelaRetencaoCONTRATUAL(Long codigoNotaSaidaParcela, BigDecimal valorRetido);

	void updateValorNotaSaidaParcelaRetencao(Long codigo, BigDecimal valor);

	long deleteNotaSaidaParcelaRetencaoDaParcela(Long codigo);
}
