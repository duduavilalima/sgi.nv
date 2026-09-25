package br.com.araujoabreu.sgi.service.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSituacaoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CentroFinanceiroSituacaoService extends CrudService<CentroFinanceiroSituacaoTO> {
	List<CentroFinanceiroSituacaoTO> obterCentroFinanceiroSituacao(Long codigo);

    boolean ultimaSituacaoInativa(Long codigo);
}
