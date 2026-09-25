package br.com.araujoabreu.sgi.service.impl.categoriaTributo;

import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.service.categoriaTributo.AliquotaService;
import br.com.araujoabreu.sgi.service.categoriaTributo.CategoriaTributoService;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
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
public class CategoriaTributoServiceImpl extends BaseSearchService<CategoriaTributo, CategoriaTributoTO, CategoriaTributoPesquisaTO, CategoriaTributoRepository> implements CategoriaTributoService {

	private static final QCategoriaTributo query = QCategoriaTributo.categoriaTributo;

	@Autowired
	private AliquotaService aliquotaService;

	@Autowired
	public CategoriaTributoServiceImpl(CategoriaTributoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<CategoriaTributo> query, CategoriaTributoPesquisaTO exampleObj) {
		query.leftJoin(QCategoriaTributo.categoriaTributo.cidade, QCidade.cidade);
	}

	@Override
	protected void buildLoadQuery(JPAQuery<CategoriaTributo> jpaQuery) {
		jpaQuery.leftJoin(QCategoriaTributo.categoriaTributo.cidade, QCidade.cidade);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CategoriaTributoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			if (Check.isAssigned(exampleObj.getCodigo())) {
				builder.and(query.codigo.eq(exampleObj.getCodigo()));
			}
		}

		if (Check.isAssigned(exampleObj.getTributoTO())) {
			if (Check.isAssigned(exampleObj.getTributoTO().getCodigo())) {
				builder.and(query.tributo.codigo.eq(exampleObj.getTributoTO().getCodigo()));
			}
		}

		if (Check.isAssigned(exampleObj.getCidade())) {
			if (Check.isAssigned(exampleObj.getCidade().getCodigo())) {
				builder.and(query.cidade.codigo.eq(exampleObj.getCidade().getCodigo()));
			}
			if (Check.isAssigned(exampleObj.getCidade().getUf())) {
				builder.and(query.cidade.uf.toLowerCase().eq(exampleObj.getCidade().getUf().toLowerCase()));
			}
		}

		if (Check.isAssigned(exampleObj.getPrincipal())) {
			builder.and(query.principal.eq(exampleObj.getPrincipal()));
		}

		if (Check.isAssigned(exampleObj.getHabilitaFaturamento())) {
			builder.and(query.habilitaFaturamento.eq(exampleObj.getHabilitaFaturamento()));
		}
	}

	@Override
	public CategoriaTributoTO getCategoriaTributoByCod(String id) throws Exception {
		try {
			return buscarCategoriaTributoById(id);
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	public CategoriaTributoTO buscarCategoriaTributoById(String id) {
		QTributo qTributo = QTributo.tributo;
		QCidade qCidade = QCidade.cidade;

		QBean qBean = Projections.bean(
			CategoriaTributo.class,
			query.codigo,
			query.descricao,
			Projections.bean(
				Tributo.class,
				qTributo.codigo,
				qTributo.nome
			).as("tributo"),
			Projections.bean(
				Cidade.class,
				qCidade.codigo,
				qCidade.uf,
				qCidade.nome
			).as("cidade")
		);

		JPAQuery<CategoriaTributo> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(query).join(query.tributo, qTributo).leftJoin(query.cidade, qCidade)
			.where(query.codigo.eq(Long.parseLong(id)));

		CategoriaTributo categoriaTributo = jpaQuery.fetchOne();

		CategoriaTributoTO to = getMapper().map(categoriaTributo, CategoriaTributoTO.class);
		BigDecimal pa = aliquotaService.getPercentualAliquotaByCodCategoriaTributo(to.getCodigo());
		to.setPercentualAliquota(pa.toString());

		return to;
	}

	@Override
	public List<CategoriaTributoTO> getListaCategoriaTributoByCodTributo(String id) {
		List<CategoriaTributo> lista = buscarListaCategoriaTributoByCodTributo(id);

		List<CategoriaTributoTO> listaTO = converterListaParaTO(lista);

		return listaTO;
	}

	public List<CategoriaTributo> buscarListaCategoriaTributoByCodTributo(String id) {

		QAliquota qAliquota = QAliquota.aliquota;

		QBean qBean = Projections.bean(
			CategoriaTributo.class,
			query.codigo,
			query.descricao
		);

		JPAQuery<CategoriaTributo> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(query).leftJoin(query.aliquotas, qAliquota)
			.where(query.tributo.codigo.eq(Long.parseLong(id)))
			.orderBy(query.descricao.asc(), qAliquota.percentual.asc(), query.tributo.codigo.asc());

		return jpaQuery.fetch();
	}

	@Override
	public List<CategoriaTributoTO> getListaCategoriaTributoByTributoUF(Long codTributo, String uf) {
		List<CategoriaTributo> lista = buscarListaPorCodTributoUF(codTributo, uf);

		List<CategoriaTributoTO> listaTO = converterListaParaTO(lista);

		return listaTO;
	}

	public List<CategoriaTributo> buscarListaPorCodTributoUF(Long codTributo, String uf) {

		QAliquota qAliquota = QAliquota.aliquota;
		QCidade qCidade = QCidade.cidade;
		QTributo qTributo = QTributo.tributo;

		QBean qBean = Projections.bean(
			CategoriaTributo.class,
			query.codigo,
			query.descricao,
			Projections.bean(
				Cidade.class,
				qCidade.codigo,
				qCidade.uf,
				qCidade.nome
			).as("cidade")
		);

		JPAQuery<CategoriaTributo> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(query)
			.innerJoin(query.tributo, qTributo)
			.leftJoin(query.aliquotas, qAliquota)
			.leftJoin(query.cidade, qCidade)
			.where(qTributo.codigo.eq(codTributo).and(qCidade.uf.containsIgnoreCase(uf)))
			.orderBy(qCidade.nome.asc(), qAliquota.percentual.asc());

		return jpaQuery.fetch();
	}

	private List<CategoriaTributoTO> converterListaParaTO(List<CategoriaTributo> lista) {
		List<CategoriaTributoTO> listaTO = new ArrayList<>();

		for (CategoriaTributo ct : lista) {
			CategoriaTributoTO to = getMapper().map(ct, CategoriaTributoTO.class);
			BigDecimal pa = aliquotaService.getPercentualAliquotaByCodCategoriaTributo(to.getCodigo());
			to.setPercentualAliquota(pa.toString());

			listaTO.add(to);
		}

		return listaTO;
	}

}
