package br.com.araujoabreu.sgi.service.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface FaturamentoRetencaoService extends CrudService<FaturamentoRetencaoTO> {

	JPAQuery getJpaQueryFaturamentoRetencao();

	List<FaturamentoRetencaoTO> obterListaPorCodigoFaturamento(Long codigo);

	List<FaturamentoRetencaoTO> obterPercentualListaPorCodigoFaturamento(Long codigo);

	Long deleteByFaturamentoCodigo(Long codigo);
}
