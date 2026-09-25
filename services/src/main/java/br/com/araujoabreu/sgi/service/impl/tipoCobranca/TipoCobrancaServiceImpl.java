package br.com.araujoabreu.sgi.service.impl.tipoCobranca;

import br.com.araujoabreu.sgi.compras.domain.QTipoCobranca;
import br.com.araujoabreu.sgi.compras.domain.TipoCobranca;
import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaService;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaPesquiasTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tipoCobranca.TipoCobrancaRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoCobrancaServiceImpl extends BaseSearchService<TipoCobranca, TipoCobrancaTO, TipoCobrancaPesquiasTO, TipoCobrancaRepository> implements TipoCobrancaService {

	public static final QTipoCobranca query = QTipoCobranca.tipoCobranca;

	@Autowired
	public TipoCobrancaServiceImpl(TipoCobrancaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, TipoCobrancaPesquiasTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(query.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getChave())) {
			builder.and(query.chave.eq(exampleObj.getChave()));
		}

		if (Check.isAssigned(exampleObj.getNome())) {
			builder.and(query.nome.lower().eq(exampleObj.getNome().toLowerCase()));
		}
	}

}
