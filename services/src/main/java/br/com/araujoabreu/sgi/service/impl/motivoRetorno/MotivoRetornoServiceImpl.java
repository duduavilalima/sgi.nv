package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetorno;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoAplicacaoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class MotivoRetornoServiceImpl extends BaseSearchService<MotivoRetorno, MotivoRetornoTO, MotivoRetornoPesquisaTO, MotivoRetornoRepository> implements MotivoRetornoService {

	private static final QMotivoRetorno qMotivoRetorno = QMotivoRetorno.motivoRetorno;

	@Autowired
	private MotivoAplicacaoService motivoAplicacaoService;

	@Autowired
	public MotivoRetornoServiceImpl(MotivoRetornoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<MotivoRetorno> jpaQuery, MotivoRetornoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getMotivoAplicacao()) && Check.isAssigned(exampleObj.getMotivoAplicacao().getChave())) {
			jpaQuery.from(qMotivoRetorno)
				.join(qMotivoRetorno.motivoAplicacao, QMotivoAplicacao.motivoAplicacao);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoPesquisaTO exampleObj) {
		if (exampleObj.getMotivoAplicacao() != null && exampleObj.getMotivoAplicacao().getChave() != null && !exampleObj.getMotivoAplicacao().getChave().isEmpty()) {
			builder.and(qMotivoRetorno.motivoAplicacao.chave.eq(exampleObj.getMotivoAplicacao().getChave()));
		}
	}

	public List<MotivoRetornoTO> findMotivosRetornoByMotivoAplicacao(MotivoAplicacaoTO motivoAplicacao) {

		QMotivoAplicacao qMotivoAplicacao = QMotivoAplicacao.motivoAplicacao;

		QBean qBean = Projections.bean(
			MotivoRetorno.class,
			qMotivoRetorno.codigo,
			qMotivoRetorno.motivo,
			qMotivoRetorno.solucao
		);

		JPAQuery<MotivoRetorno> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qMotivoRetorno).join(qMotivoRetorno.motivoAplicacao, qMotivoAplicacao)
			.where(qMotivoAplicacao.codigo.eq(motivoAplicacao.getCodigo()))
			.orderBy(qMotivoRetorno.codigo.asc());

		List<MotivoRetorno> lista = jpaQuery.fetch();

		return map(lista);
	}

	public List<MotivoRetornoTO> findMotivoRetornoByChaveAplicacao(String chave) {
		MotivoAplicacaoTO motivoAplicacao = motivoAplicacaoService.findMotivoAplicacaoByChave(chave);
		return findMotivosRetornoByMotivoAplicacao(motivoAplicacao);
	}

	@Override
	public List<MotivoRetornoTO> buscarListaMotivoRetornoByChaveAplicacao(@PathVariable String chave) throws Exception {
		try {
			return findMotivoRetornoByChaveAplicacao(chave);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

}
