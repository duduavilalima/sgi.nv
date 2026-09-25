package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.service.tesouraria.ItemRelacaoPagamentoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoPagamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoPagamentoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacaoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.QItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.QItemRelacaoPagamento;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoPagamentoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemRelacaoPagamentoServiceImpl extends BaseSearchService<ItemRelacaoPagamento, ItemRelacaoPagamentoTO, ItemRelacaoPagamentoPesquisaTO, ItemRelacaoPagamentoRepository> implements ItemRelacaoPagamentoService {

	@Autowired
	public ItemRelacaoPagamentoServiceImpl(ItemRelacaoPagamentoRepository repository) {
		super(repository);
	}

	@Override
	public List<ItemRelacaoPagamentoTO> obterItemRelacaoPagamentoPorCodigoItemRelacao(Long codigo) {
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;

		JPAQuery<ItemRelacaoPagamento> jpaQueryItemRelacaoPgto = select();
		jpaQueryItemRelacaoPgto.from(qItemRelacaoPagamento)
			.join(qItemRelacaoPagamento.pagamento, qPagamento)
			.join(qItemRelacaoPagamento.itemRelacao, qItemRelacao);
		QBean<?> qBean = Projections.bean(
			ItemRelacaoPagamento.class,
			qItemRelacaoPagamento.codigo,
			Projections.bean(
				Pagamento.class,
				qPagamento.codigo,
				qPagamento.numero
			).as("pagamento")
		);

		jpaQueryItemRelacaoPgto.distinct().where(new BooleanBuilder(qItemRelacaoPagamento.itemRelacao.codigo.eq(codigo))).select(qBean);

		return map(jpaQueryItemRelacaoPgto.fetch());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemRelacaoPagamentoTO> obterCodigoECodigoPagamentoPeloCodigoItemRelacao(Long codigoItemRelacao) {
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;
		JPAQuery<ItemRelacaoPagamento> jpaQuery = select(Projections.bean(ItemRelacaoPagamento.class, qItemRelacaoPagamento.codigo, Projections.bean(Pagamento.class, qPagamento.codigo).as("pagamento")));
		jpaQuery.join(qItemRelacaoPagamento.pagamento, qPagamento)
			.where(new BooleanBuilder().and(qItemRelacaoPagamento.itemRelacao.codigo.eq(codigoItemRelacao)));
		List<ItemRelacaoPagamento> listaItemRelacaoPagamento = jpaQuery.fetch();

		return map(listaItemRelacaoPagamento);
	}

	@Override
	public JPAQuery<?> jpaQueryItemRelacaoPagamento() {
		return select();
	}
}
