package br.com.araujoabreu.sgi.service.compras;

import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.math.BigDecimal;
import java.util.Date;

public interface ParcelaNotaService extends CrudService<ParcelaNotaTO> {
	ParcelaNotaTO obterParcelaCodigoEHomologador(Long codigo);

	JPAQuery jpaQueryParcelaNota();

	BigDecimal obterPorCentroFinanceiroHomologadas(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte);

	BigDecimal obterPorCentroFinanceiroLancadas(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte);
}
