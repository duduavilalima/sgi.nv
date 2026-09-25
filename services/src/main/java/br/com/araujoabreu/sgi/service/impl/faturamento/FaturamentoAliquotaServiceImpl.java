package br.com.araujoabreu.sgi.service.impl.faturamento;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoAliquota;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoAliquotaService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoAliquotaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaturamentoAliquotaServiceImpl extends BaseSearchService<FaturamentoAliquota, FaturamentoAliquotaTO, FaturamentoAliquotaPesquisaTO, FaturamentoAliquotaRepository> implements FaturamentoAliquotaService {
	private static final QFaturamentoAliquota qFaturamentoAliquota = QFaturamentoAliquota.faturamentoAliquota;

	@Autowired
	public FaturamentoAliquotaServiceImpl(FaturamentoAliquotaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<FaturamentoAliquota> jpaQuery, FaturamentoAliquotaPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigoFaturamento())) {
			QFaturamento qFaturamento = QFaturamento.faturamento;
			QAliquota qAliquota = QAliquota.aliquota;
			QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
			QTributo qTributo = QTributo.tributo;

			jpaQuery = select(Projections.fields(
				FaturamentoAliquota.class,
				qFaturamentoAliquota.codigo,
				qFaturamentoAliquota.baseCalculo,
				qFaturamentoAliquota.valor,
				Projections.fields(Faturamento.class,
					qFaturamento.codigo,
					qFaturamento.tipo,
					qFaturamento.situacao,
					qFaturamento.issRetidoCliente
				).as(qFaturamentoAliquota.faturamento),
				Projections.fields(Aliquota.class,
					qAliquota.codigo,
					qAliquota.percentual,
					Projections.fields(CategoriaTributo.class,
						qCategoriaTributo.codigo,
						qCategoriaTributo.codigoReceita,
						qCategoriaTributo.tipoAliquota,
						Projections.fields(Tributo.class,
							qTributo.codigo,
							qTributo.nome,
							qTributo.tipo
						).as(qCategoriaTributo.tributo)
					).as(qAliquota.categoriaTributo)
				).as(qFaturamentoAliquota.aliquota)
			));

			jpaQuery
				.from(qFaturamentoAliquota)
				.join(qFaturamentoAliquota.faturamento, qFaturamento)
				.join(qFaturamentoAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.join(qCategoriaTributo.tributo, qTributo);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FaturamentoAliquotaPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo()))
			builder.and((qFaturamentoAliquota.codigo.eq(exampleObj.getCodigo())));

		if (Check.isAssigned(exampleObj.getCodigoFaturamento())) {
			QFaturamento qFaturamento = QFaturamento.faturamento;
			builder.and((qFaturamento.codigo.eq(exampleObj.getCodigoFaturamento())));
		}
	}

	@Override
	protected void doBeforeUpdate(FaturamentoAliquota entity, FaturamentoAliquotaTO faturamentoAliquotaTO) throws ServiceException {
		if (faturamentoAliquotaTO.getCodigo() == null) {
			throw new ValidationException("ValidationException","Código obrigatório!");
		}
	}

	/**
	 * Busca uma lista de FaturamentoAliquota, usando como criterio de pesquisa o
	 * os atributos do FaturamentoAliquota do parametro.
	 *
	 * @param faturamentoAliquota
	 * @return
	 */
	@Override
	public List<FaturamentoAliquotaTO> pesquisarFaturamentoAliquota(FaturamentoAliquotaTO faturamentoAliquota) {

		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QFaturamento qFaturamento = QFaturamento.faturamento;

		QBean qBean = Projections.bean(
			FaturamentoAliquota.class,
			qFaturamentoAliquota.codigo,

			Projections.bean(
				Aliquota.class,
				qAliquota.codigo,
				qAliquota.percentual,
				Projections.bean(
					CategoriaTributo.class,
					qCategoriaTributo.codigo
				).as("categoriaTributo")
			).as("aliquota"),

			Projections.bean(Faturamento.class, qFaturamento.codigo).as("faturamento")
		);

		JPAQuery<FaturamentoAliquota> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qFaturamentoAliquota)
			.leftJoin(qFaturamentoAliquota.faturamento, qFaturamento)
			.leftJoin(qFaturamentoAliquota.aliquota, qAliquota).innerJoin(qAliquota.categoriaTributo, qCategoriaTributo)
			.where(filtarBusca(faturamentoAliquota))
			.orderBy(qFaturamentoAliquota.codigo.asc());

		List<FaturamentoAliquota> lista = jpaQuery.fetch();

		return map(lista);
	}

	/**
	 * Filtra a busca pelos atributos preenchidos que estão no objeto do parametro.
	 *
	 * @param faturamentoAliquota
	 * @return
	 */
	private Predicate filtarBusca(FaturamentoAliquotaTO faturamentoAliquota) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(faturamentoAliquota.getCodigo())) {
			builder.and(qFaturamentoAliquota.codigo.eq(faturamentoAliquota.getCodigo()));
		}

		if (Check.isAssigned(faturamentoAliquota.getFaturamento()) && Check.isAssigned(faturamentoAliquota.getFaturamento().getCodigo())) {
			builder.and(qFaturamentoAliquota.faturamento.codigo.eq(faturamentoAliquota.getFaturamento().getCodigo()));
		}

		return builder;
	}

	@Override
	public JPAQuery<FaturamentoAliquota> getJPAQuery() {
		return select();
	}

	@Transactional()
	@Override
	public void deletarFaturamentoAliquota(Long codigoFaturamento) {
		selectDelete()
			.where(QFaturamentoAliquota.faturamentoAliquota.faturamento.codigo.eq(codigoFaturamento))
			.execute();
	}

	@Override
	public JPAQuery<?> findFaturamentoAliquotaApenasPercentual(Long codigoFaturamento) {
		return select(Projections.fields(
			FaturamentoAliquota.class,
			QFaturamentoAliquota.faturamentoAliquota.codigo,
			Projections.fields(Aliquota.class,
				QAliquota.aliquota.codigo,
				QAliquota.aliquota.percentual,
				Projections.fields(CategoriaTributo.class,
					QCategoriaTributo.categoriaTributo.codigo,
					Projections.fields(
						Tributo.class,
						QTributo.tributo.codigo,
						QTributo.tributo.nome
					).as(QTributo.tributo)
				).as(QCategoriaTributo.categoriaTributo)
			).as(QAliquota.aliquota)
		))
			.innerJoin(QFaturamentoAliquota.faturamentoAliquota.aliquota, QAliquota.aliquota)
			.innerJoin(QAliquota.aliquota.categoriaTributo, QCategoriaTributo.categoriaTributo)
			.innerJoin(QCategoriaTributo.categoriaTributo.tributo, QTributo.tributo)
			.where(QFaturamentoAliquota.faturamentoAliquota.faturamento.codigo.eq(codigoFaturamento))
			.orderBy(QFaturamentoAliquota.faturamentoAliquota.codigo.asc());
	}
}
