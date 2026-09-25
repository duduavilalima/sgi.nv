package br.com.araujoabreu.sgi.service.impl.tipoCobranca;

import br.com.araujoabreu.sgi.faturamento.domain.QTipoCobrancaVencimento;
import br.com.araujoabreu.sgi.faturamento.domain.TipoCobrancaVencimento;
import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaVencimentoService;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaVencimentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaVencimentoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tipoCobranca.TipoCobrancaVencimentoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TipoCobrancaVencimentoServiceImpl extends BaseSearchService<TipoCobrancaVencimento, TipoCobrancaVencimentoTO, TipoCobrancaVencimentoPesquisaTO, TipoCobrancaVencimentoRepository> implements TipoCobrancaVencimentoService {

	public static final QTipoCobrancaVencimento query = QTipoCobrancaVencimento.tipoCobrancaVencimento;

	@Autowired
	public TipoCobrancaVencimentoServiceImpl(TipoCobrancaVencimentoRepository repository) {
		super(repository);
	}

	@Override
	public List<TipoCobrancaVencimentoTO> findListaByCodCobranca(Long id) {
		JPAQuery<TipoCobrancaVencimento> jpaQuery = select();
		jpaQuery.select(
			Projections.fields(TipoCobrancaVencimento.class,
				query.codigo,
				query.dataReferencia,
				query.diaVencimento,
				query.mesReferencia,
				query.solicitaData,
				query.diaVencimentoTipo)
		);

		jpaQuery.where(query.tipoCobrancaFaturamento.codigo.eq(id));

		List<TipoCobrancaVencimento> lista = jpaQuery.fetch();
		List<TipoCobrancaVencimentoTO> listaTO = new ArrayList<>();

		for (TipoCobrancaVencimento tipoCobrancaVencimento : lista) {
			TipoCobrancaVencimentoTO to = getMapper().map(tipoCobrancaVencimento, TipoCobrancaVencimentoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}
}
