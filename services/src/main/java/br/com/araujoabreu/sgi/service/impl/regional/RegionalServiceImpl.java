package br.com.araujoabreu.sgi.service.impl.regional;

import br.com.araujoabreu.sgi.administracao.domain.QDiretoria;
import br.com.araujoabreu.sgi.administracao.domain.QRegional;
import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Regional;
import br.com.araujoabreu.sgi.service.regional.RegionalService;
import br.com.araujoabreu.sgi.service.regional.transfer.RegionalPesquisaTO;
import br.com.araujoabreu.sgi.service.regional.transfer.RegionalTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.administracao.RegionalRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.araujoabreu.sgi.administracao.domain.QRegional.regional;

@Service
public class RegionalServiceImpl extends BaseSearchService<Regional, RegionalTO, RegionalPesquisaTO, RegionalRepository> implements RegionalService {

	private static final QRegional qRegional = regional;

	@Autowired
	public RegionalServiceImpl(RegionalRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(Regional entity, RegionalTO object) {
		for (Regional.Situacao situacao : Regional.Situacao.values()) {
			if (object.getSituacao().equals(situacao.getNome())) {
				entity.setSituacao(situacao);
				break;
			}
		}
	}

	/**
	 * Buscar o código Regional de acordo com uma empresa selecionada.
	 *
	 * @param idEmpresa
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<RegionalTO> buscarPorCodigoUnidade(String idEmpresa) {
		QDiretoria qDiretoria = QDiretoria.diretoria;
		QUnidade qUnidade = QUnidade.unidade;

		try {
			JPAQuery<Regional> query = select(Projections.bean(
				Regional.class,
				qRegional.codigo,
				qRegional.descricao));

			query.from(qRegional)
				.join(qRegional.diretoria, qDiretoria)
				.join(qDiretoria.unidade, qUnidade);

			query.where(qUnidade.codigo.eq(Long.parseLong(idEmpresa)));

			query.orderBy(qRegional.descricao.asc());

			List<Regional> listaRegionais = query.fetch();

			return map(listaRegionais);
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}


	@Override
	public List<RegionalTO> buscarAtivas() {
		QDiretoria qDiretoria = QDiretoria.diretoria;
		QUnidade qUnidade = QUnidade.unidade;

		try {
			JPAQuery<Regional> query = select(Projections.bean(
				Regional.class,
				qRegional.codigo,
				qRegional.descricao));

			query.from(qRegional)
				.join(qRegional.diretoria, qDiretoria)
				.join(qDiretoria.unidade, qUnidade)
				.where(qRegional.situacao.eq(Regional.Situacao.ATIVO))
				.orderBy(qRegional.descricao.asc());

			List<Regional> listaRegionais = query.fetch();

			return map(listaRegionais);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
