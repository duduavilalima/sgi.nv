package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Fluxo;
import br.com.araujoabreu.sgi.administracao.domain.QFluxo;
import br.com.araujoabreu.sgi.service.administracao.FluxoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.FluxoPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.FluxoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.administracao.FluxoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FluxoServiceImpl extends BaseSearchService<Fluxo, FluxoTO, FluxoPesquisaTO, FluxoRepository> implements FluxoService {

	@Autowired
	public FluxoServiceImpl(FluxoRepository repository) {
		super(repository);
	}

	@Override
	public List<FluxoTO> findFluxoByNome(String nome) {
		QFluxo qFluxo = QFluxo.fluxo;

		List<Fluxo> lista = new ArrayList<>();

		JPAQuery<Fluxo> jpaQueryFluxo = select();

		QBean qBean = Projections.bean(
			Fluxo.class,
			qFluxo.codigo,
			qFluxo.nome
		);

		jpaQueryFluxo.select(qBean).from(qFluxo).where(qFluxo.nome.eq(nome));
		lista = jpaQueryFluxo.fetch();

		return map(lista);
	}
}
