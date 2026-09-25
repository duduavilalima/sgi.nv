package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.compras.domain.QNota;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QNotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaRepository;
import br.com.fasolti.sgi.repository.tesouraria.RelacaoBancariaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotaTributoAliquotaServiceImpl extends BaseSearchService<NotaTributoAliquota, NotaTributoAliquotaTO, NotaTributoAliquotaPesquisaTO, NotaTributoAliquotaRepository> implements NotaTributoAliquotaService {

	private static final QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;

	@Autowired
	private CategoriaTributoRepository categoriaTributoRepository;

	@Autowired
	private AliquotaRepository aliquotaRepository;

	@Autowired
	private RelacaoBancariaRepository relacaoBancariaRepository;

	@Autowired
	public NotaTributoAliquotaServiceImpl(NotaTributoAliquotaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<NotaTributoAliquota> jpaQuery, NotaTributoAliquotaPesquisaTO exampleObj) {
		if (exampleObj.getNota() != null) {
			QNota qNota = QNota.nota;
			jpaQuery.join(qNotaTributoAliquota.nota, qNota);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, NotaTributoAliquotaPesquisaTO filter) {
		if (filter.getNota() != null) {
			if (filter.getNota().getCodigo() != null) {
				builder.and(qNotaTributoAliquota.nota.codigo.eq(filter.getNota().getCodigo()));
			}
		}
	}

	@Override
	protected void doAfterSearch(List<NotaTributoAliquota> entities, List<NotaTributoAliquotaPesquisaTO> result) throws ServiceException {
		for (NotaTributoAliquotaPesquisaTO pesquisaTO : result) {
			if (pesquisaTO.getAliquota() != null && pesquisaTO.getAliquota().getCodigo() != null) {
				Aliquota aliquota = aliquotaRepository.findById(pesquisaTO.getAliquota().getCodigo()).orElse(null);

				if (aliquota != null && aliquota.getCategoriaTributo() != null && aliquota.getCategoriaTributo().getCodigo() != null) {
					pesquisaTO.setAliquota(getMapper().map(aliquota, AliquotaTO.class));

					if (aliquota.getCategoriaTributo() != null && aliquota.getCategoriaTributo().getCodigo() != null) {
						categoriaTributoRepository.findById(aliquota.getCategoriaTributo().getCodigo()).ifPresent(categoriaTributo -> pesquisaTO.setCategoriaTributo(getMapper().map(categoriaTributo, CategoriaTributoTO.class)));

					}
				}
			}
		}
	}

	@Override
	public PagamentoTO buscarPagamentoById(Long codigoNotaTributoAliquota) {
		JPAQuery jpaQuery = select();
		QPagamento qPagamento = QPagamento.pagamento;

		QBean qBean = Projections.bean(
			NotaTributoAliquota.class,
			qNotaTributoAliquota.codigo,
			Projections.bean(
				Pagamento.class,
				qPagamento.codigo,
				qPagamento.status,
				qPagamento.dataVencimento,
				qPagamento.dataPagamento,
				qPagamento.tipo
			).as("pagamento")
		);

		jpaQuery
			.select(qBean)
			.from(qNotaTributoAliquota).leftJoin(qNotaTributoAliquota.pagamento, qPagamento)
			.where(qNotaTributoAliquota.codigo.eq(codigoNotaTributoAliquota));

		NotaTributoAliquota notaTributoAliquota = (NotaTributoAliquota) jpaQuery.fetchOne();
		Pagamento pagamento = notaTributoAliquota.getPagamento();

		return getMapper().map(pagamento, PagamentoTO.class);
	}

	@Override
	public JPAQuery<NotaTributoAliquota> getJPAQuery() {
		return select();
	}

	@Override
	public List<NotaTributoAliquotaTO> listarPorCodigoNota(Long codigoNota) {
		JPAQuery jpaQuery = select();
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;
		QCidade qCidade = QCidade.cidade;

		QBean qBean = Projections.fields(
			NotaTributoAliquota.class,
			qNotaTributoAliquota.codigo,
			qNotaTributoAliquota.baseCalculo,
			qNotaTributoAliquota.valor,
			qNotaTributoAliquota.percentual,
			Projections.fields(
				Aliquota.class,
				qAliquota.codigo,
				qAliquota.percentual,
				Projections.fields(
					CategoriaTributo.class,
					qCategoriaTributo.codigo,
					Projections.fields(
						Tributo.class,
						qTributo.codigo,
						qTributo.nome
					).as(QTributo.tributo),
					Projections.fields(
						Cidade.class,
						qCidade.codigo,
						qCidade.uf,
						qCidade.nome
					).as(qCidade)
				).as(QCategoriaTributo.categoriaTributo)
			).as(QAliquota.aliquota)
		);

		jpaQuery
			.select(qBean)
			.innerJoin(qNotaTributoAliquota.aliquota, qAliquota)
			.innerJoin(qAliquota.categoriaTributo, qCategoriaTributo)
			.innerJoin(qCategoriaTributo.tributo, qTributo)
			.leftJoin(qCategoriaTributo.cidade, qCidade)
			.where(qNotaTributoAliquota.nota.codigo.eq(codigoNota));

		List<NotaTributoAliquota> lista = jpaQuery.fetch();
		List<NotaTributoAliquotaTO> listaTO = new ArrayList<>();

		for (NotaTributoAliquota notaTributoAliquota : lista) {
			NotaTributoAliquotaTO to = getMapper().map(notaTributoAliquota, NotaTributoAliquotaTO.class);
			to.setAliquota(getMapper().map(notaTributoAliquota.getAliquota(), AliquotaTO.class));
			to.getAliquota().setCodigoTributo(notaTributoAliquota.getAliquota().getCategoriaTributo().getTributo().getCodigo());
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public List<NotaTributoAliquotaTO> listarPorCodigoNotaOrdem(Long codigoNota, Integer ordem) {
		JPAQuery jpaQuery = select();

		QBean qBean = Projections.fields(
			NotaTributoAliquota.class,
			qNotaTributoAliquota.codigo,
			qNotaTributoAliquota.baseCalculo,
			qNotaTributoAliquota.valor,
			qNotaTributoAliquota.percentual
		);

		jpaQuery.select(qBean)
			.innerJoin(qNotaTributoAliquota.aliquota, QAliquota.aliquota)
			.where(
				QNotaTributoAliquota.notaTributoAliquota.nota.codigo.eq(codigoNota).and(
					QNotaTributoAliquota.notaTributoAliquota.ordem.eq(ordem)
				)
			);

		return map(jpaQuery.fetch());
	}

	@Override
	public List<NotaTributoAliquotaTO> listarPorCodigoNotaPorOrdem(Long codigoNota, Integer ordem) {
		JPAQuery jpaQuery = select();
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;

		QBean qBean = Projections.fields(
			NotaTributoAliquota.class,
			qNotaTributoAliquota.codigo,
			qNotaTributoAliquota.baseCalculo,
			qNotaTributoAliquota.valor,
			qNotaTributoAliquota.percentual,
			Projections.fields(
				Aliquota.class,
				qAliquota.codigo,
				qAliquota.percentual,
				Projections.fields(
					CategoriaTributo.class,
					qCategoriaTributo.codigo,
					Projections.fields(
						Tributo.class,
						qTributo.codigo,
						qTributo.nome
					).as(QTributo.tributo)
				).as(QCategoriaTributo.categoriaTributo)
			).as(QAliquota.aliquota)
		);

		jpaQuery
			.select(qBean)
			.innerJoin(qNotaTributoAliquota.aliquota, qAliquota)
			.innerJoin(qAliquota.categoriaTributo, qCategoriaTributo)
			.innerJoin(qCategoriaTributo.tributo, qTributo)
			.where(qNotaTributoAliquota.nota.codigo.eq(codigoNota).and(qNotaTributoAliquota.ordem.eq(ordem)));

		List<NotaTributoAliquota> lista = jpaQuery.fetch();
		List<NotaTributoAliquotaTO> listaTO = new ArrayList<>();

		for (NotaTributoAliquota notaTributoAliquota : lista) {
			NotaTributoAliquotaTO to = getMapper().map(notaTributoAliquota, NotaTributoAliquotaTO.class);
			to.setAliquota(getMapper().map(notaTributoAliquota.getAliquota(), AliquotaTO.class));
			to.getAliquota().setCodigoTributo(notaTributoAliquota.getAliquota().getCategoriaTributo().getTributo().getCodigo());
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public void deletarNotaTributoAliquota(Long codigo) {
		selectDelete().where(qNotaTributoAliquota.codigo.eq(codigo)).execute();
	}

	@Override
	public void updateValor(Long codigo, BigDecimal valor) {
		selectUpdate().where(qNotaTributoAliquota.codigo.eq(codigo)).set(qNotaTributoAliquota.valor, valor).execute();
	}

	@Override
	public void updateBaseCalculo(Long codigo, BigDecimal baseCalculo) {
		selectUpdate().where(qNotaTributoAliquota.codigo.eq(codigo)).set(qNotaTributoAliquota.baseCalculo, baseCalculo).execute();
	}
}
