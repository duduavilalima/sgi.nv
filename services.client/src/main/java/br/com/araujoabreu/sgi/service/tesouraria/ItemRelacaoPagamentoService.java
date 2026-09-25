package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoPagamentoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface ItemRelacaoPagamentoService extends CrudService<ItemRelacaoPagamentoTO> {
	List<ItemRelacaoPagamentoTO> obterItemRelacaoPagamentoPorCodigoItemRelacao(Long codigo);

	List<ItemRelacaoPagamentoTO> obterCodigoECodigoPagamentoPeloCodigoItemRelacao(Long codigoItemRelacao);

	JPAQuery jpaQueryItemRelacaoPagamento();
}
