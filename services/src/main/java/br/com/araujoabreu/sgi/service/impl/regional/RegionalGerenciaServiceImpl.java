package br.com.araujoabreu.sgi.service.impl.regional;

import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.QGerencia;
import br.com.araujoabreu.sgi.administracao.domain.QRegional;
import br.com.araujoabreu.sgi.administracao.domain.QRegionalGerencia;
import br.com.araujoabreu.sgi.administracao.domain.RegionalGerencia;
import br.com.araujoabreu.sgi.service.regional.RegionalGerenciaService;
import br.com.araujoabreu.sgi.service.regional.transfer.RegionalGerenciaPesquisaTO;
import br.com.araujoabreu.sgi.service.regional.transfer.RegionalGerenciaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.administracao.RegionalGerenciaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionalGerenciaServiceImpl extends BaseSearchService<RegionalGerencia, RegionalGerenciaTO, RegionalGerenciaPesquisaTO, RegionalGerenciaRepository> implements RegionalGerenciaService {

	private static final QRegionalGerencia qRegionalGerencia = QRegionalGerencia.regionalGerencia;

	@Autowired
	public RegionalGerenciaServiceImpl(RegionalGerenciaRepository repository) {
		super(repository);
	}

	@Override
	public List<Long> listaCodGerenciaPorCodRegional(Long codigoRegional) {
		try {
			QGerencia qGerencia = QGerencia.gerencia;
			QRegional qRegional = QRegional.regional;

			QBean qBean = Projections.bean(
				RegionalGerencia.class,
				qRegionalGerencia.codigo,
				Projections.bean(
					Gerencia.class,
					qGerencia.codigo,
					qGerencia.nome
				).as("gerencia")
			);

			JPAQuery<RegionalGerencia> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qRegionalGerencia)
				.join(qRegionalGerencia.gerencia, qGerencia)
				.join(qRegionalGerencia.regional, qRegional)
				.where(qRegional.codigo.eq(codigoRegional));

			List<RegionalGerencia> listaRG = jpaQuery.fetch();
			List<Long> listaGerenciaCodigos = new ArrayList<>();

			for (RegionalGerencia regionalGerencia : listaRG) {
				listaGerenciaCodigos.add(regionalGerencia.getGerencia().getCodigo());
			}

			return listaGerenciaCodigos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
