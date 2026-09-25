package br.com.araujoabreu.sgi.service.impl.gerencia;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.service.administracao.ColaboradorService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.gerencia.GerenciaService;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaPesquisaTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.colaboradorCentroFinanceiro.ColaboradorCentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.gerencia.GerenciaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GerenciaServiceImpl extends BaseSearchService<Gerencia, GerenciaTO, GerenciaPesquisaTO, GerenciaRepository> implements GerenciaService {

	private static final QGerencia qGerencia = QGerencia.gerencia;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private ColaboradorCentroFinanceiroRepository colaboradorCentroFinanceiroRepository;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	public GerenciaServiceImpl(GerenciaRepository repository) {
		super(repository);
	}

	public List<GerenciaTO> getCFsByUnidade(UnidadeTO unidade) throws Exception {
		try {

			List<GerenciaTO> listaGerenciaTO = new ArrayList<>();

			if (Check.isAssigned(unidade) && Check.isAssigned(unidade.getCodigo())) {

				JPAQuery<Gerencia> jpaQueryGerencia = select();

				QBean<Gerencia> qBean = Projections.bean(
					Gerencia.class,
					qGerencia.codigo,
					qGerencia.nome
				);

				jpaQueryGerencia
					.select(qBean)
					.from(qGerencia)
					.where(qGerencia.unidade.codigo.eq(unidade.getCodigo()));

				List<Gerencia> listaGerencia = jpaQueryGerencia.fetch();

				for (Gerencia gerencia : listaGerencia) {
					GerenciaTO to = getMapper().map(gerencia, GerenciaTO.class);
					listaGerenciaTO.add(to);
				}
			}

			return listaGerenciaTO;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<GerenciaTO> buscarTodasGerenciasTelaRelatorio() {
		try {

			JPAQuery<Gerencia> jpaQueryGerencia = select();

			QBean<Gerencia> qBean = Projections.bean(
				Gerencia.class,
				qGerencia.codigo,
				qGerencia.identificador,
				qGerencia.nome
			);

			jpaQueryGerencia.select(qBean);

			List<Gerencia> listaGerencia = jpaQueryGerencia.fetch();

			listaGerencia.sort((g1, g2) -> g1.getNome().compareTo(g2.getNome()));

			return map(listaGerencia);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<GerenciaTO> buscarGerenciasPorUnidadeTelaRelatorio(Long codigoUnidade) {
		try {

			JPAQuery<Gerencia> jpaQueryGerencia = select();

			QBean<Gerencia> qBean = Projections.bean(
				Gerencia.class,
				qGerencia.codigo,
				qGerencia.identificador,
				qGerencia.nome
			);

			jpaQueryGerencia.select(qBean);

			jpaQueryGerencia.where(qGerencia.unidade.codigo.eq(codigoUnidade)
				.and(qGerencia.habilitaFaturamento.isTrue()));

			List<Gerencia> listaGerencia = jpaQueryGerencia.fetch();

			listaGerencia.sort((g1, g2) -> g1.getNome().compareTo(g2.getNome()));

			return map(listaGerencia);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<GerenciaTO> buscarTodasGerenciasHabilitadasFaturamento() {
		List<Gerencia> listaGerencia;

		try {
			QBean<Gerencia> qBean = Projections.bean(
				Gerencia.class,
				qGerencia.codigo,
				qGerencia.nome,
				qGerencia.identificadorFinal,
				qGerencia.identificador
			);

			JPAQuery<Gerencia> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qGerencia)
				.where(
					qGerencia.situacao.eq(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO)
						.and(qGerencia.pai.isNull())
						.and(qGerencia.habilitaFaturamento.eq(true))
				)
				.orderBy(qGerencia.nome.asc());

			// FIXME: Usuário com esse perfil só pode ver notas dos CLs associados a ele.
			if (usuarioService.possuiUmPerfilLista("faturamento-cns-gerencia")) {
				List<CentroFinanceiroTO> list = usuarioService.getCentroFinanceiros();
				List<Long> codigos = new ArrayList<>();
				list.forEach(cf -> codigos.add(cf.getCodigo()));
				jpaQuery.where(qGerencia.codigo.in(codigos).or(qGerencia.pai.codigo.in(codigos)));
			}

			listaGerencia = jpaQuery.fetch();
			return map(listaGerencia);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GerenciaTO obterGerencia(Long codigoGerencia) {

		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Gerencia.class,
				qGerencia.codigo,
				qGerencia.nome,
				qGerencia.numero,
				Projections.bean(Colaborador.class, qGerencia.gerente.nome).as(qGerencia.gerente),
				Projections.bean(Diretoria.class,
					qGerencia.diretoria.numero,
					qGerencia.diretoria.descricao
				).as("diretoria"))
		);
		jpaQuery.where(qGerencia.codigo.eq(codigoGerencia));
		return map((Gerencia) jpaQuery.fetchOne());
	}

	@Override
	public List<GerenciaTO> getGerenciaSimplesAtivas() {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Gerencia.class,
				qGerencia.codigo,
				qGerencia.nome,
				qGerencia.numero,
				Projections.bean(Colaborador.class, qGerencia.gerente.nome).as(qGerencia.gerente),
				Projections.bean(Diretoria.class,
					qGerencia.diretoria.numero,
					qGerencia.diretoria.descricao
				).as(qGerencia.diretoria))
		);
		jpaQuery.where(qGerencia.situacao.eq(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO));
		jpaQuery.orderBy(qGerencia.nome.asc(), qGerencia.numero.asc());
		return map(jpaQuery.fetch());
	}

	@Override
	public List<GerenciaTO> findByCodigoDiretoria(long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Gerencia.class,
				qGerencia.codigo,
				qGerencia.nome,
				qGerencia.numero,
				Projections.bean(Colaborador.class, QColaborador.colaborador.nome, QColaborador.colaborador.codigo).as(qGerencia.gerente),
				Projections.bean(Diretoria.class,
					QDiretoria.diretoria.numero,
					QDiretoria.diretoria.descricao
				).as(qGerencia.diretoria))
		);
		jpaQuery.innerJoin(qGerencia.diretoria, QDiretoria.diretoria);
		jpaQuery.innerJoin(qGerencia.gerente, QColaborador.colaborador);
		jpaQuery.where(qGerencia.diretoria.codigo.eq(codigo));
		jpaQuery.orderBy(qGerencia.nome.asc(), qGerencia.numero.asc());
		return map(jpaQuery.fetch());
	}

	private List<Gerencia> buscarGerenciasPorCodigoDiretoria(long codigoDiretoria) {
		List<Gerencia> listaGerencia = new ArrayList<>();
		QDiretoria qDiretoria = QDiretoria.diretoria;

		QBean<Gerencia> qBeanGerencia = Projections.bean(Gerencia.class, qGerencia.codigo, qGerencia.nome, qGerencia.numero,
			Projections.bean(Diretoria.class, qDiretoria.codigo, qDiretoria.permiteContrato, qDiretoria.numero).as(qGerencia.diretoria));

		JPAQuery<Gerencia> jpaQueryGerencia = select();
		jpaQueryGerencia.select(qBeanGerencia).from(qGerencia).join(qGerencia.diretoria, qDiretoria).where(qDiretoria.codigo.eq(codigoDiretoria));
		listaGerencia = jpaQueryGerencia.fetch();

		return listaGerencia;
	}

	@Override
	public List<GerenciaTO> getGerenciaSimplesAtivasUsuarioLogado(long idDiretoria) {
		List<Gerencia> gerenciasDiretoria = buscarGerenciasPorCodigoDiretoria(idDiretoria);
		if (usuarioService.possuiPerfilLista(
			"admin-contrato",
			"aprov-contrato-dir",
			"aprov-contrato-rh",
			"aprov-contrato-fin",
			"aprov-contrato-cont",
			"aprov-contrato-com",
			"aprov-contrato-pres")) {
			return map(gerenciasDiretoria);
		}
		List<Gerencia> gerenciasUsuario = new ArrayList<>();

		List<Perfil> perfis = securityService.getUser().getPerfis();

		List<ColaboradorCentroFinanceiro> colaboradorCentroFinanceiro = colaboradorCentroFinanceiroRepository.findByColaboradorCodigo(securityService.getUser().getPessoaFisica().getId());

		perfis.forEach(perfil -> {
			if (perfil.getNome().equals("aprov-contrato-ger")) {
				for (Gerencia gerencia : gerenciasDiretoria) {
					for (ColaboradorCentroFinanceiro gerenciaUser : colaboradorCentroFinanceiro) {
						Optional<CentroFinanceiro> centroFinanceiro = centroFinanceiroRepository.findById(gerenciaUser.getCentroFinanceiro().getId());
						if (gerencia.getCodigo().equals(centroFinanceiro.get().getId())) {
							gerenciasUsuario.add(gerencia);
							break;
						}
					}
				}
			}
		});
		gerenciasUsuario.sort(Comparator.comparing(CentroFinanceiro::getNomeAbreviado));
		return map(gerenciasUsuario);
	}

}
