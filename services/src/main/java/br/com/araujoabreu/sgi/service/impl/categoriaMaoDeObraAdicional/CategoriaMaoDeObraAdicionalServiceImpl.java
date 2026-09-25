package br.com.araujoabreu.sgi.service.impl.categoriaMaoDeObraAdicional;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaMaoDeObraAdicional;
import br.com.araujoabreu.sgi.comercial.domain.QCategoriaMaoDeObraAdicional;
import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.CategoriaMaoDeObraAdicionalService;
import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.transfer.CategoriaMaoDeObraAdicionalPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.transfer.CategoriaMaoDeObraAdicionalTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.categoriaMaoDeObraAdicional.CategoriaMaoDeObraAdicionalRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaMaoDeObraAdicionalServiceImpl extends BaseSearchService<CategoriaMaoDeObraAdicional, CategoriaMaoDeObraAdicionalTO, CategoriaMaoDeObraAdicionalPesquisaTO, CategoriaMaoDeObraAdicionalRepository> implements CategoriaMaoDeObraAdicionalService {

	private static final QCategoriaMaoDeObraAdicional qCategoriaMaoDeObraAdicional = QCategoriaMaoDeObraAdicional.categoriaMaoDeObraAdicional;

	@Autowired
	public CategoriaMaoDeObraAdicionalServiceImpl(CategoriaMaoDeObraAdicionalRepository repository) {
		super(repository);
	}

	@Override
	public List<CategoriaMaoDeObraAdicionalTO> buscarTodasCategorias() {

		List<CategoriaMaoDeObraAdicional> listaCategoria = new ArrayList<>();

		try {
			QBean qBean = Projections.bean(
				CategoriaMaoDeObraAdicional.class,
				qCategoriaMaoDeObraAdicional.codigo,
				qCategoriaMaoDeObraAdicional.nome);

			JPAQuery<CategoriaMaoDeObraAdicional> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCategoriaMaoDeObraAdicional)
				.orderBy(qCategoriaMaoDeObraAdicional.nome.asc());

			listaCategoria = jpaQuery.fetch();
			return map(listaCategoria);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
