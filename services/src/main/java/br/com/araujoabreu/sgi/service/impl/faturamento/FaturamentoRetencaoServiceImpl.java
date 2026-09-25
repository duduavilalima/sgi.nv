package br.com.araujoabreu.sgi.service.impl.faturamento;

import br.com.araujoabreu.sgi.administracao.domain.QSituacao;
import br.com.araujoabreu.sgi.administracao.domain.QTipo;
import br.com.araujoabreu.sgi.administracao.domain.Situacao;
import br.com.araujoabreu.sgi.administracao.domain.Tipo;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoRetencao;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoRetencao;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoRetencaoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRetencaoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaturamentoRetencaoServiceImpl extends BaseSearchService<FaturamentoRetencao, FaturamentoRetencaoTO, FaturamentoRetencaoPesquisaTO, FaturamentoRetencaoRepository> implements FaturamentoRetencaoService {

	private final QFaturamentoRetencao qFaturamentoRetencao = QFaturamentoRetencao.faturamentoRetencao;

	@Autowired
	public FaturamentoRetencaoServiceImpl(FaturamentoRetencaoRepository repository) {
		super(repository);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<FaturamentoRetencaoTO> obterListaPorCodigoFaturamento(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery
			.select(Projections.fields(FaturamentoRetencao.class,
				QFaturamentoRetencao.faturamentoRetencao.codigo,
				QFaturamentoRetencao.faturamentoRetencao.codigoResponsavelBaixa,
				QFaturamentoRetencao.faturamentoRetencao.codigoResponsavelCancelamento,
				QFaturamentoRetencao.faturamentoRetencao.dataBaixa,
				QFaturamentoRetencao.faturamentoRetencao.dataCancelamento,
				QFaturamentoRetencao.faturamentoRetencao.percentual,
				Projections.fields(Tipo.class,
					QTipo.tipo.codigo,
					QTipo.tipo.descricao,
					QTipo.tipo.nome,
					QTipo.tipo.chave).as(QTipo.tipo),
				Projections.fields(Situacao.class,
					QSituacao.situacao.codigo,
					QSituacao.situacao.descricao,
					QSituacao.situacao.nome,
					QSituacao.situacao.chave).as(QSituacao.situacao)))
			.innerJoin(QFaturamentoRetencao.faturamentoRetencao.situacao, QSituacao.situacao)
			.innerJoin(QFaturamentoRetencao.faturamentoRetencao.tipo, QTipo.tipo)
			.where(QFaturamentoRetencao.faturamentoRetencao.faturamento.codigo.eq(codigo));

		List<FaturamentoRetencao> listRetencao = jpaQuery.fetch();

		return map(listRetencao);
	}

	@Override
	public JPAQuery getJpaQueryFaturamentoRetencao() {
		return select();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<FaturamentoRetencaoTO> obterPercentualListaPorCodigoFaturamento(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery
			.select(Projections.fields(FaturamentoRetencao.class,
				QFaturamentoRetencao.faturamentoRetencao.codigo,
				QFaturamentoRetencao.faturamentoRetencao.percentual))
			.where(QFaturamentoRetencao.faturamentoRetencao.faturamento.codigo.eq(codigo));

		List<FaturamentoRetencao> listRetencao = jpaQuery.fetch();

		return map(listRetencao);
	}

	@Override
	public Long deleteByFaturamentoCodigo(Long codigo) {
		return selectDelete(qFaturamentoRetencao.faturamento.codigo.eq(codigo)).execute();
	}
}
