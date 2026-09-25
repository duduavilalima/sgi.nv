package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface ItemRelacaoService extends CrudService<ItemRelacaoTO> {
	ItemRelacaoTO obterItemRelacaoComBanco(Long codigoItemRelacao);

	List<ItemRelacaoTO> obterItemCodigoEChequeCodigoRelacaoBancaria(Long codigoRb);

	JPAQuery<?> jpaQueryItemRelacao();
}
