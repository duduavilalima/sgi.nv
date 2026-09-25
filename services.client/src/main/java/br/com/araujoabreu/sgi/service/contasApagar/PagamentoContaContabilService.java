package br.com.araujoabreu.sgi.service.contasApagar;

import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

public interface PagamentoContaContabilService extends CrudService<PagamentoContaContabilTO> {
	JPAQuery jpaQueryPagamentoContaContabil();
}
