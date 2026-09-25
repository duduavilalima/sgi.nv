package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.service.tesouraria.ConciliacaoLancamentoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPagamentosVO;
import br.com.araujoabreu.sgi.tesouraria.domain.*;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tesouraria.ChequeContaCorrenteItemRelacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ChequeContaCorrenteRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoLancamentoAssociacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoLancamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoPagamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.LancamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.RelacaoBancariaRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConciliacaoLancamentoServiceImpl extends BaseSearchService<ConciliacaoLancamento, ConciliacaoLancamentoTO, ConciliacaoLancamentoPesquisaTO, ConciliacaoLancamentoRepository> implements ConciliacaoLancamentoService {

	private static final QConciliacaoLancamento qConciliacaoLancamento = QConciliacaoLancamento.conciliacaoLancamento;

	private static final QConciliacaoLancamentoAssociacao qConciliacaoLancamentoAssociacao = QConciliacaoLancamentoAssociacao.conciliacaoLancamentoAssociacao;

	@Autowired
	private ConciliacaoLancamentoAssociacaoRepository conciliacaoLancamentoAssociacaoRepository;

	@Autowired
	private ItemRelacaoRepository itemRelacaoRepository;

	@Autowired
	private RelacaoBancariaRepository relacaoBancariaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private ChequeContaCorrenteRepository chequeContaCorrenteRepository;

	@Autowired
	private ChequeContaCorrenteItemRelacaoRepository chequeContaCorrenteItemRelacaoRepository;

	@Autowired
	private ItemRelacaoPagamentoRepository itemRelacaoPagamentoRepository;

	@Autowired
	public ConciliacaoLancamentoServiceImpl(ConciliacaoLancamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<ConciliacaoLancamento> jpaQuery, ConciliacaoLancamentoPesquisaTO exampleObj) {
		jpaQuery.from(qConciliacaoLancamento).orderBy(qConciliacaoLancamento.codigo.asc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ConciliacaoLancamentoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getConciliacaoBancaria())) {
			if (Check.isAssigned(exampleObj.getConciliacaoBancaria().getCodigo())) {
				builder.and(qConciliacaoLancamento.conciliacaoBancaria.codigo.eq(exampleObj.getConciliacaoBancaria().getCodigo()));
			}
		}
	}

	/**
	 * Retorna os Lançamentos que já estão associados a uma Conciliação Lançamento.
	 *
	 * @return
	 */
	@Override
	public List<LancamentoTO> getLancamentosAssociados() throws Exception {
		try {
			return pesquisarLancamentoAssociados();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private List<LancamentoTO> pesquisarLancamentoAssociados() {
		List<LancamentoTO> listaLancamento = new ArrayList<>();

		BooleanExpression booleanExpression = qConciliacaoLancamentoAssociacao.tipo.eq(ConciliacaoLancamentoAssociacao.TipoConciliacaoLancamentoAssociacao.ASSOCIACAO_COM_LANCAMENTO_TESOURARIA);
		Iterable<ConciliacaoLancamentoAssociacao> iterable = conciliacaoLancamentoAssociacaoRepository.findAll(booleanExpression);

		if (iterable != null) {
			List<ConciliacaoLancamentoAssociacao> listaAssociacao = Lists.newArrayList(iterable);

			if (listaAssociacao != null && !listaAssociacao.isEmpty()) {

				for (ConciliacaoLancamentoAssociacao associacao : listaAssociacao) {

					if (ConciliacaoLancamentoAssociacao.TipoConciliacaoLancamentoAssociacao.ASSOCIACAO_COM_LANCAMENTO_TESOURARIA.equals(associacao.getTipo())) {
						if (associacao.getLancamento() != null && associacao.getLancamento().getCodigo() != null) {
							Lancamento lancamento = lancamentoRepository.findById(associacao.getLancamento().getCodigo()).orElse(null);
							LancamentoTO lancamentoTO = getMapper().map(lancamento, LancamentoTO.class);
							listaLancamento.add(lancamentoTO);
						}
					}
				}
			}
		}

		return listaLancamento;
	}

	/**
	 * Retorna um cheque que tenha o mesmo número passado por parametro e é
	 * associado a um item relação com situação "Pago".
	 *
	 * @param numeroCheque
	 * @return ChequeContaCorrenteTO
	 */
	@Override
	public ChequeContaCorrenteTO getChequeAssociado(@RequestBody String numeroCheque) throws Exception {
		try {
			return pesquisarChequeAssociado(numeroCheque);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private ChequeContaCorrenteTO pesquisarChequeAssociado(String numeroCheque) {
		ChequeContaCorrenteTO chequeContaCorrenteTO = new ChequeContaCorrenteTO();

		if (numeroCheque != null && numeroCheque.length() > 0) {

			//remove caracteres e espaços em branco
			String chequeString = numeroCheque.replaceAll("[^0-9 ]", "").replaceAll(" ", "");
			Long chequeLong = Long.valueOf(chequeString);

			QChequeContaCorrente qChequeContaCorrente = QChequeContaCorrente.chequeContaCorrente;
			QChequeContaCorrenteItemRelacao qChequeContaCorrenteItemRelacao = QChequeContaCorrenteItemRelacao.chequeContaCorrenteItemRelacao;
			QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;

			JPAQuery jpaQueryChequeContaCorrente = select().from(qChequeContaCorrente);

			QBean qBean = Projections.bean(ChequeContaCorrente.class, qChequeContaCorrente.codigo, qChequeContaCorrente.cheque, qChequeContaCorrente.valor);

			jpaQueryChequeContaCorrente.select(qBean).from(qChequeContaCorrente, qChequeContaCorrenteItemRelacao, qItemRelacao)
				.where(
					qChequeContaCorrente.cheque.eq(chequeLong)
						.and(qChequeContaCorrente.codigo.eq(qChequeContaCorrenteItemRelacao.codigoChequeContaCorrente))
						.and(qChequeContaCorrenteItemRelacao.codigoItemRelacao.eq(qItemRelacao.codigo))
						.and(qItemRelacao.status.eq(ItemRelacao.StatusItemRelacao.CONFIRMADO))
				);

			ChequeContaCorrente chequeContaCorrente = (ChequeContaCorrente) jpaQueryChequeContaCorrente.fetchOne();

			if (Check.isAssigned(chequeContaCorrente) && Check.isAssigned(chequeContaCorrente.getCodigo())) {
				JPAQuery jpaQueryItemRelacao = select().from(qItemRelacao);

				QBean qBeanItemRelacao = Projections.bean(ItemRelacao.class, qItemRelacao.codigo);

				jpaQueryItemRelacao.select(qItemRelacao).from(qItemRelacao, qChequeContaCorrenteItemRelacao)
					.where(
						qChequeContaCorrenteItemRelacao.codigoChequeContaCorrente.eq(chequeContaCorrente.getCodigo())
							.and(qChequeContaCorrenteItemRelacao.codigoItemRelacao.eq(qItemRelacao.codigo))
							.and(qItemRelacao.status.eq(ItemRelacao.StatusItemRelacao.CONFIRMADO))
					);

				List<ItemRelacao> listaItemRelacao = jpaQueryItemRelacao.fetch();

				for (ItemRelacao itemRelacao : listaItemRelacao) {
					chequeContaCorrenteTO = getMapper().map(chequeContaCorrente, ChequeContaCorrenteTO.class);
					RelacaoBancariaPagamentosVO vo = getRelacaoBancariaDoCheque(itemRelacao);
					chequeContaCorrenteTO.setRelacaoBancariaPagamentosVO(vo);
					break;
				}
			}
		}

		return chequeContaCorrenteTO;
	}

	private RelacaoBancariaPagamentosVO getRelacaoBancariaDoCheque(ItemRelacao itemRelacao) {
		RelacaoBancariaPagamentosVO vo = new RelacaoBancariaPagamentosVO();

		if (Check.isAssigned(itemRelacao)) {
			vo.setCodigo(String.valueOf(itemRelacao.getCodigo()));

			Pagamento pagamento = getPagamentoByItemRelacao(itemRelacao);

			if (pagamento != null) {
				vo.setNumero(pagamento.getNumero());
			}

			ItemRelacaoTO itemRelacaoTO = new ItemRelacaoTO();
			itemRelacaoTO.setCodigo(itemRelacao.getCodigo());
			vo.setItemRelacao(itemRelacaoTO);
		}

		return vo;
	}

	private Pagamento getPagamentoByItemRelacao(ItemRelacao itemRelacao) {
		List<Pagamento> pagamentos = new ArrayList<>();

		QPagamento qPagamento = QPagamento.pagamento;
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;

		QBean qBean = Projections.bean(Pagamento.class, qPagamento.codigo, qPagamento.numero, qPagamento.tipoDocumentoPagamento, qPagamento.credor);
		JPAQuery jpaQueryPagamento = select().from(qPagamento);

		jpaQueryPagamento
			.select(qBean)
			.from(qItemRelacaoPagamento).join(qItemRelacaoPagamento.pagamento, qPagamento).join(qItemRelacaoPagamento.itemRelacao, qItemRelacao)
			.where(qItemRelacao.codigo.eq(itemRelacao.getCodigo()));

		return (Pagamento) jpaQueryPagamento.fetchOne();
	}

}
