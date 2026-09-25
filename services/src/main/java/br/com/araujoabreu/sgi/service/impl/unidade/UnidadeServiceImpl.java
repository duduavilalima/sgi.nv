package br.com.araujoabreu.sgi.service.impl.unidade;

import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadePesquisaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UnidadeServiceImpl extends BaseSearchService<Unidade, UnidadeTO, UnidadePesquisaTO, UnidadeRepository> implements UnidadeService {
	private static final QUnidade qUnidade = QUnidade.unidade;

	@Autowired
	public UnidadeServiceImpl(UnidadeRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, UnidadePesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(qUnidade.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getTipoUnidade())) {
			builder.and(qUnidade.tipoUnidade.eq((Unidade.TipoUnidade) Util.getEnumByString(Unidade.TipoUnidade.class, exampleObj.getTipoUnidade())));
		}

		if (Check.isAssigned(exampleObj.getCodnome())) {
			builder.and(qUnidade.codnome.contains((exampleObj.getCodnome())));
		}
	}

	@Override
	public List<UnidadeTO> listarUnidadesPorEmpresa() {
		return listarUnidadesPorEmpresaExceto(Collections.emptyList());
	}

	@Override
	public List<UnidadeTO> listarUnidadesPorEmpresaExceto(List<Long> codigosUnidadesIgnoradas) {

		JPAQuery jpaQuery = select(Projections.bean(Unidade.class, qUnidade.codigo, qUnidade.codnome, qUnidade.situacao, qUnidade.emitirNfFaturamento, qUnidade.tipoUnidade));
		BooleanExpression predicadoUnidades =
			qUnidade.situacao.eq(Pessoa.Situacao.ENABLED)
				.and(qUnidade.tipoUnidade.eq(Unidade.TipoUnidade.MATRIZ));

		if (codigosUnidadesIgnoradas != null && codigosUnidadesIgnoradas.size() > 0) {
			predicadoUnidades = predicadoUnidades.and(qUnidade.codigo.in(codigosUnidadesIgnoradas).not());
		}

		jpaQuery.where(predicadoUnidades).orderBy(qUnidade.codigo.asc(), qUnidade.nome.asc());
		List<Unidade> matrizes = jpaQuery.fetch();
		List<Unidade> matrizesComFiliais = new ArrayList<>();

		for (Unidade unidade : matrizes) {
			BooleanExpression predicadoFiliais =
				qUnidade.situacao.eq(Pessoa.Situacao.ENABLED)
					.and(qUnidade.tipoUnidade.eq(Unidade.TipoUnidade.FILIAL))
					.and(qUnidade.matriz.codigo.eq(unidade.getCodigo()));

			if (codigosUnidadesIgnoradas != null && codigosUnidadesIgnoradas.size() > 0) {
				predicadoFiliais = predicadoFiliais.and(qUnidade.codigo.in(codigosUnidadesIgnoradas).not());
			}

			jpaQuery = select(Projections.bean(Unidade.class, qUnidade.codigo, qUnidade.codnome, qUnidade.situacao, qUnidade.matriz, qUnidade.emitirNfFaturamento))
				.where(predicadoFiliais);

			matrizesComFiliais.add(unidade);
			matrizesComFiliais.addAll(jpaQuery.fetch());
		}

		return map(matrizesComFiliais);
	}

	public UnidadeTO obterUnidadeSimples(Long codigo) {
		QUnidade qUnidade = QUnidade.unidade;

		QBean<Unidade> qBean = Projections.bean(
			Unidade.class,
			qUnidade.codigo,
			qUnidade.nome,
			qUnidade.codnome,
			qUnidade.tipoUnidade,
			Projections.bean(
				Unidade.class,
				qUnidade.matriz.codigo,
				qUnidade.matriz.nome,
				qUnidade.matriz.codnome,
				qUnidade.matriz.tipoUnidade
			).as("matriz")
		);

		JPAQuery<Unidade> jpaQuery = select();
		jpaQuery.from(qUnidade).where(new BooleanBuilder().and(qUnidade.codigo.eq(codigo))).select(qBean);

		Unidade unidade = jpaQuery.fetchOne();

		if (unidade != null) {
			return map(unidade);
		}

		return null;
	}

	@Override
	public String obterCodNomeUnidade(Long codigo) {
		JPAQuery<Unidade> unidadeJPAQuery = select(
			Projections.fields(
				Unidade.class,
				QUnidade.unidade.codnome
			)
		);
		unidadeJPAQuery.where(QUnidade.unidade.codigo.eq(codigo));
		return unidadeJPAQuery.fetchFirst().getCodnome();
	}

	/**
	 * Filtrar para as empresas que estão habilitadas para o faturamento (campo "emite_nf_faturamento", tabela "unidade")
	 *
	 * @return
	 */
	@Override
	public List<UnidadeTO> listarUnidadesFaturamento() {

		List<UnidadeTO> listaUnidade = listarUnidadesPorEmpresa();
		List<UnidadeTO> listaUnidadeFaturamento = new ArrayList<>();

		for (UnidadeTO to : listaUnidade) {
			if (to.getEmitirNfFaturamento()) {
				listaUnidadeFaturamento.add(to);
			}
		}

		return listaUnidadeFaturamento;
	}

	@Override
	public List<UnidadeTO> listarUnidadeMatrizAtiva() {
		QBean<Unidade> qBean = Projections.bean(
			Unidade.class,
			qUnidade.codigo,
			qUnidade.nome,
			qUnidade.codnome,
			qUnidade.tipoUnidade,
			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.nome,
				qUnidade.codnome,
				qUnidade.tipoUnidade
			).as("matriz")
		);

		JPAQuery<Unidade> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qUnidade)
			.where(
				qUnidade.tipoUnidade.eq(Unidade.TipoUnidade.MATRIZ)
					.and(qUnidade.situacao.eq(Pessoa.Situacao.ENABLED))
			)
			.orderBy(qUnidade.codigo.asc());

		List<Unidade> listaUnidade = jpaQuery.fetch();
		List<UnidadeTO> listaTO = new ArrayList<>();

		for (Unidade unidade : listaUnidade) {
			listaTO.add(getMapper().map(unidade, UnidadeTO.class));
		}

		return listaTO;
	}

	@Override
	public boolean zerarNumeracaoNfAno(Long codigoUndiade) {
		JPAQuery jpaQuery = select(
			Projections.bean(
				Unidade.class,
				qUnidade.zerarNumeracaoNfAno)).where(qUnidade.codigo.eq(codigoUndiade));
		Unidade unidade = (Unidade) jpaQuery.fetchOne();
		return unidade == null ? false : unidade.getZerarNumeracaoNfAno();
	}

	@Override
	public UnidadeTO obterUnidadeParaVerificacaoDeNumeracao(Long codigoUndiade) {
		JPAQuery jpaQuery = select(
			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.proximaNf,
				qUnidade.proximaNfMaterial,
				qUnidade.anoProximaNf,
				qUnidade.proximaNd,
				qUnidade.zerarNumeracaoNfAno)).where(qUnidade.codigo.eq(codigoUndiade));
		Unidade unidade = (Unidade) jpaQuery.fetchOne();
		if (unidade == null) {
			return null;
		} else {
			return map(unidade);
		}
	}

	@Override
	@Transactional
	public void atualizarNumeracaoProximaNd(Long codigo, Integer numeracao, Short ano) {
		getRepository().updateProximaNd(codigo, numeracao);
		getRepository().updateAnoProximaNf(codigo, ano);
	}

	@Override
	public void atualizarNumeroProximaNd(Long codigo, Integer numero) {
		getRepository().updateProximaNd(codigo, numero);
	}

	@Override
	@Transactional
	public void atualizarNumeracaoProximaNf(Long codigo, Integer numeracao, Short ano) {
		getRepository().updateProximaNf(codigo, numeracao);
		getRepository().updateProximaNfMaterial(codigo, numeracao);
		getRepository().updateAnoProximaNf(codigo, ano);
	}

	@Override
	@Transactional
	public void atualizarNumeroProximaNf(Long codigo, Integer numeracao) {
		getRepository().updateProximaNf(codigo, numeracao);
	}

	@Override
	@Transactional
	public void atualizarNumeroProximaNfMaterial(Long codigo, Integer numeracao) {
		getRepository().updateProximaNfMaterial(codigo, numeracao);
	}

		@Override
	public UnidadeTO obterUnidade(Long codigoUndiade) {
		JPAQuery jpaQuery = select(
			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.proximaNf,
				qUnidade.codnome,
				qUnidade.nome,
				qUnidade.anoProximaNf,
				qUnidade.zerarNumeracaoNfAno,
				Projections.fields(Endereco.class,
					qUnidade.endereco.codigo,
					Projections.fields(Cidade.class,
						qUnidade.endereco.cidade.codigo,
						qUnidade.endereco.cidade.interfacePrefeitura)
						.as("cidade"))
					.as("endereco"),
				Projections.fields(
					Unidade.class,
					qUnidade.matriz.codigo,
					qUnidade.matriz.codnome
				).as("matriz")
			))
			.where(qUnidade.codigo.eq(codigoUndiade));
		Unidade unidade = (Unidade) jpaQuery.fetchOne();
		if (unidade == null) {
			return null;
		} else {
			return map(unidade);
		}
	}

	@Override
	public boolean obterUnidadeInterfacePrefeitura(Long codigo) {
		QUnidade qUnidade = QUnidade.unidade;

		QBean<Unidade> qBean = Projections.bean(
			Unidade.class,
			qUnidade.codigo,
			Projections.bean(
				Endereco.class,
				QUnidade.unidade.endereco.codigo,
				Projections.fields(
					Cidade.class,
					QUnidade.unidade.endereco.cidade.interfacePrefeitura
				).as("cidade")
			).as("endereco")
		);

		JPAQuery<Unidade> jpaQuery = select();
		jpaQuery.from(qUnidade).where(qUnidade.codigo.eq(codigo)).select(qBean);

		Unidade unidade = jpaQuery.fetchOne();

		if (unidade != null) {
			return unidade.getEndereco().getCidade().getInterfacePrefeitura();
		}

		return Boolean.FALSE;
	}

	@Override
	public JPAQuery getSelect() {
		return select();
	}

}
