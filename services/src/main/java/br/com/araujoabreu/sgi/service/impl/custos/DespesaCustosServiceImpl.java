package br.com.araujoabreu.sgi.service.impl.custos;

import br.com.araujoabreu.sgi.custos.DespesaCustos;
import br.com.araujoabreu.sgi.custos.QDespesaCustos;
import br.com.araujoabreu.sgi.custos.enums.OperacaoDespesaCustos;
import br.com.araujoabreu.sgi.custos.enums.SituacaoDespesaCustos;
import br.com.araujoabreu.sgi.custos.enums.TipoDespesaCustos;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.custos.DespesaCustosService;
import br.com.araujoabreu.sgi.service.custos.transfer.DespesaCustosPesquisaTO;
import br.com.araujoabreu.sgi.service.custos.transfer.DespesaCustosTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.custos.DespesaCustosRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaCustosServiceImpl extends BaseSearchService<DespesaCustos, DespesaCustosTO, DespesaCustosPesquisaTO, DespesaCustosRepository> implements DespesaCustosService {

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	public DespesaCustosServiceImpl(DespesaCustosRepository repository) {
		super(repository);
	}

	@Override
	protected void doAfterLoad(DespesaCustosTO to) throws ServiceException {
		if (to != null && to.getCriador() != null) {
			to.setCriador(pessoaFisicaService.nomeUsuarioPorLogin(to.getCriador()));
		}

		if (to != null && to.getAtualizador() != null) {
			to.setAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(to.getAtualizador()));
		}
	}

	@Override
	protected void buildSearchQuery(JPAQuery<DespesaCustos> jpaQuery, DespesaCustosPesquisaTO exampleObj) {
		jpaQuery.orderBy(QDespesaCustos.despesaCustos.descricao.asc(), QDespesaCustos.despesaCustos.ordem.asc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, DespesaCustosPesquisaTO exampleObj) {
		if (exampleObj.getDescricao() != null && !exampleObj.getDescricao().trim().equals("")) {
			builder.and(QDespesaCustos.despesaCustos.descricao.containsIgnoreCase(exampleObj.getDescricao().trim()));
		}

		if (exampleObj.getOrdem() != null) {
			builder.and(QDespesaCustos.despesaCustos.ordem.eq(exampleObj.getOrdem()));
		}

		if (exampleObj.getOperacao() != null) {
			builder.and(QDespesaCustos.despesaCustos.operacao.eq(OperacaoDespesaCustos.valueOf(exampleObj.getOperacao())));
		}

		if (exampleObj.getSituacao() != null) {
			builder.and(QDespesaCustos.despesaCustos.situacao.eq(SituacaoDespesaCustos.valueOf(exampleObj.getSituacao())));
		}

		if (exampleObj.getTipo() != null) {
			builder.and(QDespesaCustos.despesaCustos.tipo.eq(TipoDespesaCustos.valueOf(exampleObj.getTipo())));
		}
	}

	@Override
	protected void doBeforeSave(DespesaCustos entity, DespesaCustosTO object) throws ServiceException {
		if (temDespesaCustoComMesmaDescricao(null, object.getDescricao()) > 0) {
			throw new ValidationException("temDespesaCustoComMesmaDescricao","Já existe uma despesa com a mesma descrição");
		}
		if (object.getPai() == null) {
			entity.setPai(null);
		}
	}

	@Override
	protected void doBeforeUpdate(DespesaCustos entity, DespesaCustosTO object) throws ServiceException {
		if (temDespesaCustoComMesmaDescricao(object.getCodigo(), object.getDescricao()) > 0) {
			throw new ValidationException("temDespesaCustoComMesmaDescricao","Já existe uma despesa com a mesma descrição");
		}
		if (object.getPai() == null) {
			entity.setPai(null);
		}
	}

	/**
	 * Verifica no banco se existe alguma despesa com a mesma descrição.
	 * Quando código igual a null, é verificado com todas as despesa, caso contrário,
	 * é verificado todos, menos a despesa do código informado.
	 * @param codigo Código da despesa que não será verificado.
	 * @param descricao A descrição para a verificação.
	 * @return A quantidade de despesas com a mesma descrição.
	 */
	@Override
	public long temDespesaCustoComMesmaDescricao(Long codigo, String descricao) {
		JPAQuery<DespesaCustos> query = select(Projections.fields(DespesaCustos.class, QDespesaCustos.despesaCustos.codigo));
		if (codigo != null) {
			query.where(QDespesaCustos.despesaCustos.codigo.eq(codigo).not());
		}
		query.where(QDespesaCustos.despesaCustos.descricao.equalsIgnoreCase(descricao.trim()));
		return query.fetchCount();
	}

	@Override
	public List<DespesaCustosTO> obterListaDespesasSimplesAtivas() {
		JPAQuery<DespesaCustos> query = select(Projections.fields(DespesaCustos.class,
			QDespesaCustos.despesaCustos.codigo,
			QDespesaCustos.despesaCustos.descricao,
			QDespesaCustos.despesaCustos.operacao,
			QDespesaCustos.despesaCustos.ordem,
			QDespesaCustos.despesaCustos.tipo,
			Projections.fields(DespesaCustos.class,
				QDespesaCustos.despesaCustos.pai.codigo,
				QDespesaCustos.despesaCustos.pai.descricao,
				QDespesaCustos.despesaCustos.pai.operacao,
				QDespesaCustos.despesaCustos.pai.ordem,
				QDespesaCustos.despesaCustos.pai.tipo)
				.as(QDespesaCustos.despesaCustos.pai)));
		query.where(QDespesaCustos.despesaCustos.situacao.eq(SituacaoDespesaCustos.ATIVA));
		query.orderBy(QDespesaCustos.despesaCustos.ordem.asc());
		return map(query.fetch());
	}
}
