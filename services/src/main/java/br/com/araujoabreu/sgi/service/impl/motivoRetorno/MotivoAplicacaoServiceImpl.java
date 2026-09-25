package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumento;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoAplicacaoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class MotivoAplicacaoServiceImpl extends BaseSearchService<MotivoAplicacao, MotivoAplicacaoTO, MotivoAplicacaoPesquisaTO, MotivoAplicacaoRepository> implements MotivoAplicacaoService {

	private static final QMotivoAplicacao qMotivoAplicacao = QMotivoAplicacao.motivoAplicacao;

	@Autowired
	private MotivoRetornoDocumentoService motivoRetornoDocumentoService;

	@Autowired
	public MotivoAplicacaoServiceImpl(MotivoAplicacaoRepository repository) {
		super(repository);
	}

	public MotivoAplicacaoTO findMotivoAplicacaoByChave(String chave) {

		QBean qBean = Projections.bean(
			MotivoAplicacao.class,
			qMotivoAplicacao.codigo,
			qMotivoAplicacao.chave,
			qMotivoAplicacao.nome
		);

		JPAQuery<MotivoAplicacao> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qMotivoAplicacao)
			.where(qMotivoAplicacao.chave.eq(chave));

		MotivoAplicacao motivoAplicacao = jpaQuery.fetchOne();

		return map(motivoAplicacao);
	}

	@Override
	public MotivoAplicacaoTO buscarMotivoAplicacaoByChave(@PathVariable String chave) throws Exception {
		try {
			return findMotivoAplicacaoByChave(chave);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public MotivoAplicacaoTO findByCodMotivoRetornoDoc(Long codigo) {

		JPAQuery<MotivoRetornoDocumento> jpaQueryMotivoRetorno = motivoRetornoDocumentoService.getJpaQuery();

		QMotivoRetornoDocumento qMotivoRetornoDocumento = QMotivoRetornoDocumento.motivoRetornoDocumento;

		QBean qBeanMotivoRetorno = Projections.bean(
			MotivoRetornoDocumento.class,
			Projections.bean(
				MotivoAplicacao.class,
				qMotivoAplicacao.codigo,
				qMotivoAplicacao.chave,
				qMotivoAplicacao.nome
			).as("aplicacao")
		);

		jpaQueryMotivoRetorno
			.select(qBeanMotivoRetorno)
			.from(qMotivoRetornoDocumento).leftJoin(qMotivoRetornoDocumento.aplicacao, qMotivoAplicacao)
			.where(qMotivoRetornoDocumento.codigo.eq(codigo));

		MotivoRetornoDocumento motivoRetornoDocumento = jpaQueryMotivoRetorno.fetchOne();

		if (motivoRetornoDocumento != null && motivoRetornoDocumento.getAplicacao() != null) {
			return map(motivoRetornoDocumento.getAplicacao());
		} else {
			return new MotivoAplicacaoTO();
		}
	}
}
