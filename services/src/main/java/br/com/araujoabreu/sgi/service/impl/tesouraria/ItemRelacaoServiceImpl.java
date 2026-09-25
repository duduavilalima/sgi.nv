package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.ItemRelacaoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.QItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemRelacaoServiceImpl extends BaseSearchService<ItemRelacao, ItemRelacaoTO, ItemRelacaoPesquisaTO, ItemRelacaoRepository> implements ItemRelacaoService {

	@Autowired
	public ItemRelacaoServiceImpl(ItemRelacaoRepository repository) {
		super(repository);
	}

	@Override
	public ItemRelacaoTO obterItemRelacaoComBanco(Long codigoItemRelacao) {
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;
		QBanco qBanco = QBanco.banco;

		JPAQuery<ItemRelacao> jpaQueryItemRelacao = select();
		jpaQueryItemRelacao.from(qItemRelacao).leftJoin(qItemRelacao.banco, qBanco).distinct();
		QBean<ItemRelacao> qBean = Projections.bean(
			ItemRelacao.class,
			qItemRelacao.codigo,
			qItemRelacao.cheque,
			qItemRelacao.qtdPagamentos,
			qItemRelacao.tipoDocumentoPagamento,
			qItemRelacao.dataPagamento,
			qItemRelacao.total,
			Projections.bean(
				RelacaoBancaria.class,
				qItemRelacao.relacaoBancaria.codigo,
				qItemRelacao.relacaoBancaria.numero,
				qItemRelacao.relacaoBancaria.status
			).as(qItemRelacao.relacaoBancaria),
			Projections.bean(
				Banco.class,
				qBanco.codigo,
				qBanco.nome
			).as(qItemRelacao.banco)
		);

		jpaQueryItemRelacao.where(new BooleanBuilder().and(qItemRelacao.codigo.eq(codigoItemRelacao))).select(qBean);

		return map(jpaQueryItemRelacao.fetchOne());
	}

	@Override
	public List<ItemRelacaoTO> obterItemCodigoEChequeCodigoRelacaoBancaria(Long codigoRb) {
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;

		JPAQuery<ItemRelacao> jpaQuery = select().from(qItemRelacao);
		jpaQuery.from(qItemRelacao)
			.select(Projections.bean(ItemRelacao.class, qItemRelacao.codigo, qItemRelacao.cheque))
			.where(qItemRelacao.relacaoBancaria.codigo.eq(codigoRb));

		List<ItemRelacao> itensRb = jpaQuery.fetch();

		return map(itensRb);
	}

	@Override
	public JPAQuery<ItemRelacao> jpaQueryItemRelacao() {
		return select();
	}
}
