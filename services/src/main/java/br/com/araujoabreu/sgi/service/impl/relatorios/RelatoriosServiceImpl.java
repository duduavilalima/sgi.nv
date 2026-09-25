package br.com.araujoabreu.sgi.service.impl.relatorios;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.relatorios.RelatoriosService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RelatoriosServiceImpl implements RelatoriosService {

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private Mapper mapper;

	@Override
	public List<CentroFinanceiroTO> buscaPorUnidade(UnidadeTO exampleObj) {
		List<CentroFinanceiro> list = centroFinanceiroRepository.findGerenciasBySituacaoCfAndUnidade(exampleObj.getCodigo());

		List<CentroFinanceiroTO> centrosFinanceirosTO = new ArrayList<>();

		for (CentroFinanceiro cf : list) {
			centrosFinanceirosTO.add(new CentroFinanceiroTO(
				cf.getCodigo(),
				cf.getNome(),
				cf.getIdentificador(),
				cf.getIdentificadorFinal())
			);
		}

		return centrosFinanceirosTO;
	}

	@Override
	public List<CentroFinanceiroTO> buscaCcClPorGerencia(CentroFinanceiroTO exampleObj) {

		JPAQuery jpaQuery = centroFinanceiroService.getJpaQuery();

		jpaQuery.select(Projections.fields(CentroFinanceiro.class,
			QCentroFinanceiro.centroFinanceiro.codigo,
			QCentroFinanceiro.centroFinanceiro.nome,
			QCentroFinanceiro.centroFinanceiro.identificador,
			QCentroFinanceiro.centroFinanceiro.identificadorFinal,
			QCentroFinanceiro.centroFinanceiro.situacao,
			Projections.bean(Unidade.class, QCentroFinanceiro.centroFinanceiro.unidade.codigo).as("unidade")
		));

		jpaQuery.where(
			QCentroFinanceiro.centroFinanceiro.situacao.eq(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO),
			QCentroFinanceiro.centroFinanceiro.pai.isNotNull(),
			QCentroFinanceiro.centroFinanceiro.pai.codigo.eq(exampleObj.getCodigo())
		);

		List<CentroFinanceiro> centrosFinanceiros = jpaQuery.fetch();

		centrosFinanceiros.sort((o1, o2) -> o1.getIdentificadorFinal().compareTo(o2.getIdentificadorFinal()));

		List<CentroFinanceiroTO> centrosFinanceirosTO = new ArrayList<>();

		for (CentroFinanceiro centroFinanceiro : centrosFinanceiros) {
			centrosFinanceirosTO.add(mapper.map(centroFinanceiro, CentroFinanceiroTO.class));
		}

		return centrosFinanceirosTO;
	}

	@Override
	public List<CentroFinanceiroTO> buscarTodasGerencias() {
		return centroFinanceiroService.buscarTodasGerencias();
	}


}
