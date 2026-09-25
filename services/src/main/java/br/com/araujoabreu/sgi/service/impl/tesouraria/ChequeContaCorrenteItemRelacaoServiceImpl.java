package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.ChequeContaCorrenteItemRelacaoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteItemRelacaoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrenteItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.QChequeContaCorrenteItemRelacao;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChequeContaCorrenteItemRelacaoServiceImpl implements ChequeContaCorrenteItemRelacaoService {

	private final QChequeContaCorrenteItemRelacao qChequeContaCorrenteItemRelacao = QChequeContaCorrenteItemRelacao.chequeContaCorrenteItemRelacao;
	@Autowired
	protected Mapper mapper;
	@PersistenceContext
	private EntityManager entityManager;

	public JPAQuery<ChequeContaCorrenteItemRelacao> select() {
		return new JPAQueryFactory(this.entityManager).selectFrom(qChequeContaCorrenteItemRelacao);
	}

	public JPAQuery<ChequeContaCorrenteItemRelacao> select(Expression<ChequeContaCorrenteItemRelacao> expr) {
		return new JPAQueryFactory(this.entityManager).selectFrom(qChequeContaCorrenteItemRelacao).select(expr);
	}

	@Override
	public List<ChequeContaCorrenteItemRelacaoTO> obterChequeContaCorrenteItemRelacaoPorCodigoChequeContaCorrente(Long codigochequeContaCorrente) {
		JPAQuery<ChequeContaCorrenteItemRelacao> jpaQueryCCCItemRelacao = select();
		jpaQueryCCCItemRelacao.from(qChequeContaCorrenteItemRelacao);
		QBean qBean = Projections.bean(
			ChequeContaCorrenteItemRelacao.class,
			qChequeContaCorrenteItemRelacao.codigoItemRelacao,
			qChequeContaCorrenteItemRelacao.codigoChequeContaCorrente
		);

		jpaQueryCCCItemRelacao.where(qChequeContaCorrenteItemRelacao.codigoChequeContaCorrente.eq(codigochequeContaCorrente)).select(qBean);
		List<ChequeContaCorrenteItemRelacao> listaChequeContaCorrenteItemRelacao = jpaQueryCCCItemRelacao.fetch();

		List<ChequeContaCorrenteItemRelacaoTO> listaChequeContaCorrenteItemRelacaoTO = new ArrayList<>();

		for (ChequeContaCorrenteItemRelacao chequeContaCorrenteItemRelacao : listaChequeContaCorrenteItemRelacao) {
			listaChequeContaCorrenteItemRelacaoTO.add(mapper.map(chequeContaCorrenteItemRelacao, ChequeContaCorrenteItemRelacaoTO.class));
		}

		return listaChequeContaCorrenteItemRelacaoTO;
	}

	@Override
	public List<ChequeContaCorrenteItemRelacaoTO> obterChequeContaCorrenteItemRelacaoPorCodigoItemRelacao(Long codigoItemRelacao) {
		QChequeContaCorrenteItemRelacao qChequeContaCorrenteItemRelacao = QChequeContaCorrenteItemRelacao.chequeContaCorrenteItemRelacao;
		JPAQuery jpaQuery = select().from(qChequeContaCorrenteItemRelacao);
		jpaQuery.from(qChequeContaCorrenteItemRelacao)
			.select(Projections.bean(ChequeContaCorrenteItemRelacao.class, qChequeContaCorrenteItemRelacao.codigoItemRelacao, qChequeContaCorrenteItemRelacao.codigoChequeContaCorrente))
			.orderBy(qChequeContaCorrenteItemRelacao.codigoItemRelacao.asc())
			.where(new BooleanBuilder().and(qChequeContaCorrenteItemRelacao.codigoItemRelacao.eq(codigoItemRelacao)));
		List<ChequeContaCorrenteItemRelacao> listaChequeContaCorrenteItemRelacaos = jpaQuery.fetch();

		List<ChequeContaCorrenteItemRelacaoTO> listaChequeContaCorrenteItemRelacaoTO = new ArrayList<>();

		for (ChequeContaCorrenteItemRelacao chequeContaCorrenteItemRelacao : listaChequeContaCorrenteItemRelacaos) {
			listaChequeContaCorrenteItemRelacaoTO.add(mapper.map(chequeContaCorrenteItemRelacao, ChequeContaCorrenteItemRelacaoTO.class));
		}

		return listaChequeContaCorrenteItemRelacaoTO;

	}
}
