package br.com.araujoabreu.sgi.service.impl.tipoCobranca;

import br.com.araujoabreu.sgi.faturamento.domain.QTipoCobrancaFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.TipoCobrancaFaturamento;
import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaFaturamentoService;
import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaVencimentoService;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaFaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaFaturamentoTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaVencimentoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tipoCobranca.TipoCobrancaFaturamentoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoCobrancaFaturamentoServiceImpl extends BaseSearchService<TipoCobrancaFaturamento, TipoCobrancaFaturamentoTO, TipoCobrancaFaturamentoPesquisaTO, TipoCobrancaFaturamentoRepository> implements TipoCobrancaFaturamentoService {

	public static final QTipoCobrancaFaturamento query = QTipoCobrancaFaturamento.tipoCobrancaFaturamento;

	@Autowired
	private TipoCobrancaFaturamentoRepository tipoCobrancaFaturamentoRepository;

	@Autowired
	private TipoCobrancaVencimentoService tipoCobrancaVencimentoService;

	@Autowired
	public TipoCobrancaFaturamentoServiceImpl(TipoCobrancaFaturamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, TipoCobrancaFaturamentoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(query.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getDescricao())) {
			builder.and(query.descricao.lower().eq(exampleObj.getDescricao().toLowerCase()));
		}
	}

	@Override
	public TipoCobrancaFaturamentoTO obterTipoCobrancaFaturamentoSimpleTelaNotaFiscal(Long codigoTipoCobranca) {
		JPAQuery<TipoCobrancaFaturamento> jpaQuery = select();
		jpaQuery.select(
			Projections.fields(TipoCobrancaFaturamento.class,
				query.codigo,
				query.descricao)
		);
		jpaQuery.where(query.codigo.eq(codigoTipoCobranca));

		TipoCobrancaFaturamento tipoCobrancaFaturamento = jpaQuery.fetchOne();

		TipoCobrancaFaturamentoTO tipoCobrancaFaturamentoTO = getMapper().map(tipoCobrancaFaturamento, TipoCobrancaFaturamentoTO.class);

		List<TipoCobrancaVencimentoTO> listaTO = tipoCobrancaVencimentoService.findListaByCodCobranca(codigoTipoCobranca);
		tipoCobrancaFaturamentoTO.setTipoCobrancaVencimentos(listaTO);

		return tipoCobrancaFaturamentoTO;

	}
}
