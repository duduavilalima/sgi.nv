package br.com.araujoabreu.sgi.service.impl.tributo;


import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.service.tributo.TributoService;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoPesquisaTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tributo.TributoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TributoServiceImpl extends BaseSearchService<Tributo, TributoTO, TributoPesquisaTO, TributoRepository> implements TributoService {

	private static final QTributo query = QTributo.tributo;

	@Autowired
	public TributoServiceImpl(TributoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, TributoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getTipo())) {
			for (Tributo.Tipo value : Tributo.Tipo.values()) {
				if (exampleObj.getTipo().equalsIgnoreCase(value.toString())) {
					builder.and(query.tipo.eq(value));
					break;
				}
			}
		}

		if (Check.isAssigned(exampleObj.getExibeNota())) {
			builder.and(query.exibeNota.eq(exampleObj.getExibeNota()));
		}
	}

	@Override
	public List<TributoTO> buscarTributoByExibeNota() {
		List<Tributo> listaTributo = new ArrayList<>();

		try {
			QBean qBean = Projections.bean(
				Tributo.class,
				query.codigo,
				query.nome,
				query.descricao);

			JPAQuery<Tributo> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(query)
				.where(query.exibeNota.eq(Boolean.TRUE))
				.orderBy(query.nome.asc());

			listaTributo = jpaQuery.fetch();
			return map(listaTributo);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}


