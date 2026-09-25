package br.com.araujoabreu.sgi.service.impl.comercial;

import br.com.araujoabreu.sgi.service.comercial.RamoNegocioService;
import br.com.araujoabreu.sgi.service.comercial.transfer.RamoNegocioPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.RamoNegocioTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cnae;
import br.com.fasolti.corporativo.pessoa.domain.QCnae;
import br.com.fasolti.sgi.repository.comercial.RamoNegocioRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RamoNegocioServiceImpl extends BaseSearchService<Cnae, RamoNegocioTO, RamoNegocioPesquisaTO, RamoNegocioRepository> implements RamoNegocioService {
	private static final QCnae qRamoNegocio = QCnae.cnae;

	@Autowired
	public RamoNegocioServiceImpl(RamoNegocioRepository repository) {
		super(repository);
	}

	@Override
	public List<RamoNegocioTO> buscarRamoNegocioPorSiglaouDescricao(String siglaOuDescricao) {

		String siglaOuDescricaoUp = siglaOuDescricao.toUpperCase().trim();

		JPAQuery jpaQuery = select();
		jpaQuery.from(qRamoNegocio)
			.where(qRamoNegocio.descricao.upper().contains(siglaOuDescricaoUp)
				.or(qRamoNegocio.sigla.upper().contains(siglaOuDescricaoUp)))
			.orderBy(qRamoNegocio.descricao.asc());

		return map(jpaQuery.fetch());
	}

	@Override
	public List<RamoNegocioTO> getRamoNegocio(AutocompleteTO autocompleteTO) {
		return buscarRamoNegocioPorSiglaouDescricao(autocompleteTO.getNome());
	}
}
