package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.EtapaFluxo;
import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.QColaborador;
import br.com.araujoabreu.sgi.administracao.domain.QEtapaFluxo;
import br.com.araujoabreu.sgi.administracao.domain.QFluxo;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoAplicacao;
import br.com.araujoabreu.sgi.service.administracao.EtapaFluxoService;
import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoPesquisaTO;
import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.QPerfil;
import br.com.fasolti.sgi.repository.etapafluxo.EtapaFluxoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtapaFluxoServiceImpl extends
	BaseSearchService
		<EtapaFluxo,
			EtapaFluxoTO,
			EtapaFluxoPesquisaTO,
			EtapaFluxoRepository> implements EtapaFluxoService {

	private static final QEtapaFluxo qEtapaFluxo = QEtapaFluxo.etapaFluxo;

	@Autowired
	public EtapaFluxoServiceImpl(EtapaFluxoRepository repository) {
		super(repository);
	}

	@Override
	public List<EtapaFluxoTO> obterEtapaFluxoColaboradorEPerfil() {
		QColaborador qColaborador = QColaborador.colaborador;
		QPerfil qPerfil = QPerfil.perfil;

		JPAQuery<EtapaFluxo> jpaQueryEtapaFluxo = select();

		QBean qBean = Projections.bean(
			EtapaFluxo.class,
			qEtapaFluxo.codigo,
			qEtapaFluxo.nome,
			qEtapaFluxo.ordem,
			qEtapaFluxo.statusAtual,
			qEtapaFluxo.statusBase,
			Projections.bean(Colaborador.class, qColaborador.codigo, qColaborador.nome).as("colaborador"),
			Projections.bean(Perfil.class, qPerfil.codigo, qPerfil.nome).as("perfil")
		);

		jpaQueryEtapaFluxo
			.select(qBean)
			.from(qEtapaFluxo).leftJoin(qEtapaFluxo.colaborador, qColaborador).leftJoin(qEtapaFluxo.perfil, qPerfil)
			.orderBy(qEtapaFluxo.codigo.asc());

		return map(jpaQueryEtapaFluxo.fetch());
	}

	@Override
	public EtapaFluxoTO obterEtapaFLuxoComMotivoAplicacao(Long idEtapaFLuxo) {
		QMotivoAplicacao qMotivoAplicacao = QMotivoAplicacao.motivoAplicacao;

		JPAQuery<EtapaFluxo> jpaQuery = select();

		QBean qBean = Projections.bean(
			EtapaFluxo.class,
			qEtapaFluxo.codigo,
			qEtapaFluxo.nome,
			qEtapaFluxo.statusBase,
			qEtapaFluxo.statusAnterior,
			Projections.bean(MotivoAplicacao.class, qEtapaFluxo.motivoAplicacao.codigo, qEtapaFluxo.motivoAplicacao.nome).as("motivoAplicacao")
		);

		jpaQuery
			.select(qBean)
			.from(qEtapaFluxo).leftJoin(qEtapaFluxo.motivoAplicacao, qMotivoAplicacao)
			.where(qEtapaFluxo.codigo.eq(idEtapaFLuxo));

		EtapaFluxo etapaFluxo = jpaQuery.fetchOne();

		return map(etapaFluxo);
	}

	@Override
	public EtapaFluxoTO findByCodFluxoAndStatusAtual(Long codigoFluxo, Integer statusAtual) {
		QFluxo qFluxo = QFluxo.fluxo;

		JPAQuery<EtapaFluxo> jpaQuery = select();

		QBean qBean = Projections.bean(
			EtapaFluxo.class,
			qEtapaFluxo.codigo,
			qEtapaFluxo.statusBase,
			qEtapaFluxo.statusAnterior
		);

		jpaQuery
			.select(qBean)
			.from(qEtapaFluxo).join(qEtapaFluxo.fluxo, qFluxo)
			.where(qFluxo.codigo.eq(codigoFluxo).and(qEtapaFluxo.statusAtual.eq(statusAtual)));

		EtapaFluxo etapaFluxo = jpaQuery.fetchOne();

		return map(etapaFluxo);
	}
}
