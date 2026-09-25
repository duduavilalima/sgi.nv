package br.com.araujoabreu.sgi.service.impl.unidadeMedida;

import br.com.araujoabreu.sgi.compras.domain.QUnidadeMedida;
import br.com.araujoabreu.sgi.compras.domain.UnidadeMedida;
import br.com.araujoabreu.sgi.service.unidademedida.UnidadeMedidaService;
import br.com.araujoabreu.sgi.service.unidademedida.transfer.UnidadeMedidaPesquisaTO;
import br.com.araujoabreu.sgi.service.unidademedida.transfer.UnidadeMedidaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.unidadeMedida.UnidadeMedidaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnidadeMedidaServiceImpl extends BaseSearchService<UnidadeMedida, UnidadeMedidaTO, UnidadeMedidaPesquisaTO, UnidadeMedidaRepository> implements UnidadeMedidaService {

	private static final QUnidadeMedida qUnidadeMedida = QUnidadeMedida.unidadeMedida;

	@Autowired
	public UnidadeMedidaServiceImpl(UnidadeMedidaRepository repository) {
		super(repository);
	}

	public List<UnidadeMedidaTO> buscarTodasUnidadesMedida() {

		List<UnidadeMedida> listaUnidadeMedida = new ArrayList<>();

		try {
			QBean qBean = Projections.bean(
				UnidadeMedida.class,
				qUnidadeMedida.codigo,
				qUnidadeMedida.descricao);

			JPAQuery<UnidadeMedida> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qUnidadeMedida)
				.orderBy(qUnidadeMedida.nome.asc());

			listaUnidadeMedida = jpaQuery.fetch();
			return map(listaUnidadeMedida);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
