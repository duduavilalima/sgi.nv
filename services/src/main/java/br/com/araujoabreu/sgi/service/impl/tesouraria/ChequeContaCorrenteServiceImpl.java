package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.ChequeContaCorrenteService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrentePesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QChequeContaCorrente;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.sgi.repository.tesouraria.ChequeContaCorrenteRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChequeContaCorrenteServiceImpl extends BaseSearchService<ChequeContaCorrente, ChequeContaCorrenteTO, ChequeContaCorrentePesquisaTO, ChequeContaCorrenteRepository> implements ChequeContaCorrenteService {

	private final QChequeContaCorrente qChequeContaCorrente = QChequeContaCorrente.chequeContaCorrente;

	@Autowired
	public ChequeContaCorrenteServiceImpl(ChequeContaCorrenteRepository repository) {
		super(repository);
	}

	@Override
	public List<ChequeContaCorrenteTO> obterListaChequeContaCorrenteDeContaCorrente(Long codigoContaCorrente) {
		JPAQuery<ChequeContaCorrente> jpaQueryChequeCC = select();
		jpaQueryChequeCC.from(qChequeContaCorrente);
		QBean qBean = Projections.bean(
			ChequeContaCorrente.class,
			qChequeContaCorrente.codigo,
			qChequeContaCorrente.situacao,
			qChequeContaCorrente.dataCompensacao,
			qChequeContaCorrente.dataCriacao,
			qChequeContaCorrente.cheque
		);

		jpaQueryChequeCC.where(new BooleanBuilder().and(qChequeContaCorrente.contaCorrente.codigo.eq(codigoContaCorrente))).select(qBean);
		List<ChequeContaCorrente> lista = jpaQueryChequeCC.fetch();

		return map(lista);
	}

	@Override
	public List<ChequeContaCorrenteTO> obterListaChequeContaCorrenteDeContaCorrente(Long codigoContaCorrente, Long chequeAnterior, Long chequeFinal) {
		JPAQuery<ChequeContaCorrente> jpaQueryChequeCC = select();
		jpaQueryChequeCC.from(qChequeContaCorrente);
		QBean qBean = Projections.bean(
			ChequeContaCorrente.class,
			qChequeContaCorrente.codigo,
			qChequeContaCorrente.situacao,
			qChequeContaCorrente.dataCompensacao,
			qChequeContaCorrente.dataCriacao,
			qChequeContaCorrente.cheque
		);

		jpaQueryChequeCC
			.where(new BooleanBuilder().and(qChequeContaCorrente.contaCorrente.codigo.eq(codigoContaCorrente))
				.and(qChequeContaCorrente.cheque.goe(chequeAnterior))
				.and(qChequeContaCorrente.cheque.loe(chequeFinal)))
			.select(qBean);
		List<ChequeContaCorrente> lista = jpaQueryChequeCC.fetch();

		return map(lista);
	}
}
