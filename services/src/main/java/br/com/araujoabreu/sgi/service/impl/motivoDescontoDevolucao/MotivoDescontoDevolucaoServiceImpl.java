package br.com.araujoabreu.sgi.service.impl.motivoDescontoDevolucao;

import br.com.araujoabreu.sgi.compras.domain.MotivoDescontoDevolucao;
import br.com.araujoabreu.sgi.compras.domain.QMotivoDescontoDevolucao;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.MotivoDescontoDevolucaoContaContabilService;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.MotivoDescontoDevolucaoService;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoContaContabilTO;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoDescontoDevolucao.MotivoDescontoDevolucaoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MotivoDescontoDevolucaoServiceImpl extends BaseSearchService<MotivoDescontoDevolucao, MotivoDescontoDevolucaoTO, MotivoDescontoDevolucaoPesquisaTO, MotivoDescontoDevolucaoRepository> implements MotivoDescontoDevolucaoService {

	private static final QMotivoDescontoDevolucao query = QMotivoDescontoDevolucao.motivoDescontoDevolucao;

	@Autowired
	private MotivoDescontoDevolucaoContaContabilService motivoDescontoDevolucaoContaContabilService;

	@Autowired
	public MotivoDescontoDevolucaoServiceImpl(MotivoDescontoDevolucaoRepository repository) {
		super(repository);
	}

	@Override
	public List<MotivoDescontoDevolucaoTO> listarDescontoPorMatrizNF(Long codigoNota) {

		List<MotivoDescontoDevolucaoContaContabilTO> listaMddcc = motivoDescontoDevolucaoContaContabilService.listarDescontoPorMatrizNF(codigoNota);
		Set<Long> listaCodigos = new HashSet<>();

		for (MotivoDescontoDevolucaoContaContabilTO to : listaMddcc) {
			listaCodigos.add(to.getMotivoDescontoDevolucao().getCodigo());
		}

		JPAQuery<MotivoDescontoDevolucao> jpaQuery = select();

		QBean qBean = Projections.bean(
			MotivoDescontoDevolucao.class,
			query.codigo,
			query.descricao
		);

		jpaQuery
			.select(qBean)
			.from(query)
			.where(query.codigo.in(listaCodigos));

		List<MotivoDescontoDevolucao> lista = jpaQuery.fetch();
		List<MotivoDescontoDevolucaoTO> listaTO = new ArrayList<>();

		for (MotivoDescontoDevolucao motivoDescontoDevolucao : lista) {
			MotivoDescontoDevolucaoTO to = getMapper().map(motivoDescontoDevolucao, MotivoDescontoDevolucaoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public List<MotivoDescontoDevolucaoTO> listarDevolucaoPorMatrizNF(Long codigoNota) {
		List<MotivoDescontoDevolucaoContaContabilTO> listaMddcc = motivoDescontoDevolucaoContaContabilService.listarDevolucaoPorMatrizNF(codigoNota);
		Set<Long> listaCodigos = new HashSet<>();

		for (MotivoDescontoDevolucaoContaContabilTO to : listaMddcc) {
			listaCodigos.add(to.getMotivoDescontoDevolucao().getCodigo());
		}

		JPAQuery<MotivoDescontoDevolucao> jpaQuery = select();

		QBean qBean = Projections.bean(
			MotivoDescontoDevolucao.class,
			query.codigo,
			query.descricao
		);

		jpaQuery
			.select(qBean)
			.from(query)
			.where(query.codigo.in(listaCodigos));

		List<MotivoDescontoDevolucao> lista = jpaQuery.fetch();
		List<MotivoDescontoDevolucaoTO> listaTO = new ArrayList<>();

		for (MotivoDescontoDevolucao motivoDescontoDevolucao : lista) {
			MotivoDescontoDevolucaoTO to = getMapper().map(motivoDescontoDevolucao, MotivoDescontoDevolucaoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}
}
