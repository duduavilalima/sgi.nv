package br.com.araujoabreu.sgi.service.impl.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroSituacao;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiroSituacao;
import br.com.araujoabreu.sgi.administracao.domain.QColaborador;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroSituacaoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSituacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSituacaoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroSituacaoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentroFinanceiroSituacaoServiceImpl extends BaseSearchService<CentroFinanceiroSituacao, CentroFinanceiroSituacaoTO, CentroFinanceiroSituacaoPesquisaTO, CentroFinanceiroSituacaoRepository> implements CentroFinanceiroSituacaoService {

	private final QCentroFinanceiroSituacao qCentroFinanceiroSituacao = QCentroFinanceiroSituacao.centroFinanceiroSituacao;

	@Autowired
	public CentroFinanceiroSituacaoServiceImpl(CentroFinanceiroSituacaoRepository repository) {
		super(repository);
	}

	@Override
	public List<CentroFinanceiroSituacaoTO> obterCentroFinanceiroSituacao(Long codigo) {
		QColaborador qColaborador = QColaborador.colaborador;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		QBean qBean = Projections.bean(CentroFinanceiroSituacao.class, qCentroFinanceiroSituacao.codigo, qCentroFinanceiroSituacao.situacao, qCentroFinanceiroSituacao.data,
			Projections.bean(Colaborador.class, qColaborador.codigo, qColaborador.nome).as("colaborador"));

		JPAQuery<CentroFinanceiroSituacao> jpaQuerySituacao = select(qBean);
		jpaQuerySituacao
			.from(qCentroFinanceiroSituacao)
			.join(qCentroFinanceiroSituacao.centroFinanceiro, qCentroFinanceiro)
			.join(qCentroFinanceiroSituacao.colaborador, qColaborador)
			.where(qCentroFinanceiro.codigo.eq(codigo));

		List<CentroFinanceiroSituacao> listaSituacao = jpaQuerySituacao.fetch();

		return map(listaSituacao);
	}

	@Override
	public boolean ultimaSituacaoInativa(Long codigo) {
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		QBean qBean = Projections.bean(
			CentroFinanceiroSituacao.class,
			qCentroFinanceiroSituacao.codigo,
			qCentroFinanceiroSituacao.situacao,
			qCentroFinanceiroSituacao.data);

		JPAQuery<CentroFinanceiroSituacao> jpaQuerySituacao = select(qBean);

		jpaQuerySituacao
			.from(qCentroFinanceiroSituacao)
			.join(qCentroFinanceiroSituacao.centroFinanceiro, qCentroFinanceiro)
			.where(qCentroFinanceiro.codigo.eq(codigo))
		.orderBy(qCentroFinanceiroSituacao.data.desc());

		List<CentroFinanceiroSituacao> listaSituacao = jpaQuerySituacao.fetch();

		if (listaSituacao.isEmpty()) {
			return Boolean.FALSE;
		}

		return CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO.equals(listaSituacao.get(0).getSituacao());
	}
}
