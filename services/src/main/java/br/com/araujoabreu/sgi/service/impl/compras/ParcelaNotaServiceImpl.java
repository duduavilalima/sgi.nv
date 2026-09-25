package br.com.araujoabreu.sgi.service.impl.compras;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaPagamento;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QRateioNotaPagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.RateioNotaPagamento;
import br.com.araujoabreu.sgi.service.compras.ParcelaNotaService;
import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaPesquisaTO;
import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.compras.NotaFiscalRepository;
import br.com.fasolti.sgi.repository.contasApagar.ParcelaNotaRepository;
import br.com.fasolti.sgi.repository.rateio.ItemNotaFiscalRepository;
import br.com.fasolti.sgi.repository.rateio.ItemRecebimentoRepository;
import br.com.fasolti.sgi.repository.rateio.RateioItemRecebimentoRepository;
import br.com.fasolti.sgi.repository.rateio.RateioNotaFiscalRepository;
import br.com.fasolti.sgi.repository.rateio.RateioNotaPagamentoRepository;
import br.com.fasolti.sgi.repository.rateio.RecebimentoRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ParcelaNotaServiceImpl extends BaseSearchService<ParcelaNota, ParcelaNotaTO, ParcelaNotaPesquisaTO, ParcelaNotaRepository> implements ParcelaNotaService {

	private final QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
	private final QNota qNota = QNota.nota;

	@Autowired
	private RateioNotaPagamentoRepository rateioNotaPagamentoRepository;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Autowired
	private RateioNotaFiscalRepository rateioNotaFiscalRepository;

	@Autowired
	private RecebimentoRepository recebimentoRepository;

	@Autowired
	private ItemNotaFiscalRepository itemNotaFiscalRepository;

	@Autowired
	private ItemRecebimentoRepository itemRecebimentoRepository;

	@Autowired
	private RateioItemRecebimentoRepository rateioItemRecebimentoRepository;

	@Autowired
	private ParcelaNotaRepository parcelaNotaRepository;

	@Autowired
	public ParcelaNotaServiceImpl(ParcelaNotaRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(ParcelaNota entity, ParcelaNotaTO object) throws ServiceException {
		entity.setNaturezaPagamento(getMapper().map(object.getNaturezaPagamento(), NaturezaPagamento.class));
	}

	@Override
	public ParcelaNotaTO obterParcelaCodigoEHomologador(Long codigo) {
		JPAQuery jpaQueryParcelaNota = select().from(qParcelaNota);
		jpaQueryParcelaNota.from(qParcelaNota).join(qParcelaNota.nota, qNota);

		QBean qBean = Projections.bean(
			ParcelaNota.class,
			qParcelaNota.codigo,
			qParcelaNota.valor,
			qParcelaNota.numero,
			Projections.bean(
				Nota.class,
				qNota.codigo,
				qNota.valor,
				qNota.valorHomologado,
				qNota.dataCriacao
			).as("nota")
		);

		jpaQueryParcelaNota.select(qBean).where(qParcelaNota.codigo.eq(codigo));
		ParcelaNota parcelaNota = (ParcelaNota) jpaQueryParcelaNota.fetchOne();

		return map(parcelaNota);
	}

	@Override
	public JPAQuery jpaQueryParcelaNota() {
		return select();
	}

	@Override
	public BigDecimal obterPorCentroFinanceiroHomologadas(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte) {
		return obterPorCentroFinanceiro(codigoCf, dataPagamentoDe, dataPagamentoAte,
			Pagamento.Status.AUTORIZACAO,
			Pagamento.Status.AUTORIZADO,
			Pagamento.Status.PAGAMENTO,
			Pagamento.Status.RELACIONADO,
			Pagamento.Status.FINALIZADO);
	}

	@Override
	public BigDecimal obterPorCentroFinanceiroLancadas(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte) {
		return obterPorCentroFinanceiro(codigoCf, dataPagamentoDe, dataPagamentoAte,
			Pagamento.Status.ABERTO,
			Pagamento.Status.REABERTO,
			Pagamento.Status.AGUARDANDO_LIBERACAO_RN,
			Pagamento.Status.AJUSTE_FISCAL,
			Pagamento.Status.ANALISE,
			Pagamento.Status.AUTORIZACAO,
			Pagamento.Status.AUTORIZADO,
			Pagamento.Status.PAGAMENTO,
			Pagamento.Status.RELACIONADO,
			Pagamento.Status.FINALIZADO);
	}

	private JPAQuery<ParcelaNota> queryPagamento(Date dataPagamentoDe, Date dataPagamentoAte, Pagamento.Status ... status) {
		return select(
			Projections.fields(ParcelaNota.class,
				qParcelaNota.codigo,
				qParcelaNota.valor,
				qParcelaNota.valorJuros,
				qParcelaNota.valorAdiantamento,
				qParcelaNota.valorDesconto,
				qParcelaNota.valorDevolucao,
				qParcelaNota.valorMulta,
				qParcelaNota.status,
				Projections.fields(
					Nota.class,
					QNota.nota.codigo,
					QNota.nota.valor
				).as(QNota.nota)
			)
		)
		.distinct()
		.innerJoin(qParcelaNota.nota, qNota)
		.where(qParcelaNota.status.in(status))
		.where(qParcelaNota.dataPagamento.between(dataPagamentoDe, dataPagamentoAte));
	}

	private BigDecimal obterPorCentroFinanceiro(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte, Pagamento.Status ... status) {
		BigDecimal totalNotaPagamento = obterValorTotalNotaPagamento(codigoCf, dataPagamentoDe, dataPagamentoAte, status);

		BigDecimal totalRateioNotaFiscal = obterValorTotalRateioNotaFiscal(codigoCf, dataPagamentoDe, dataPagamentoAte, status);

		BigDecimal totalRateioItemRecebimento = obterValorTotalRateioItemRecebimento(codigoCf, dataPagamentoDe, dataPagamentoAte, status);

		BigDecimal totalRequisicaoCompra = BigDecimal.ZERO;

		if (totalRateioItemRecebimento.equals(BigDecimal.ZERO)) {
			totalRequisicaoCompra = obterValorTotalRequisicaoCompra(codigoCf, dataPagamentoDe, dataPagamentoAte, status);
		}

		return totalNotaPagamento.add(totalRateioNotaFiscal).add(totalRateioItemRecebimento).add(totalRequisicaoCompra).setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal obterValorTotalNotaPagamento(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte, Pagamento.Status ... status) {
		JPAQuery<RateioNotaPagamento> queryRateio = new JPAQuery<>(getEntityManager());
		QRateioNotaPagamento qRateioNotaPagamento = QRateioNotaPagamento.rateioNotaPagamento;
		QNotaPagamento qNotaPagamento = QNotaPagamento.notaPagamento;
		QContaContabil qContaContabil = QContaContabil.contaContabil;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;

		queryRateio
			.from(qRateioNotaPagamento)
			.join(qRateioNotaPagamento.notaPagamento, qNotaPagamento)
			.join(qRateioNotaPagamento.centroFinanceiro, qCentroFinanceiro)
			.join(qNotaPagamento.parcelas, qParcelaNota)
			.leftJoin(qNotaPagamento.contaContabil, qContaContabil)
			.where(
				qCentroFinanceiro.codigo.eq(codigoCf)
				.and(
					qContaContabil.tipoEspecial.ne(ContaContabil.TipoEspecial.RESCISAO).or(qContaContabil.tipoEspecial.isNull()).or(qContaContabil.codigo.isNull())
				)
				.and(qParcelaNota.status.in(status))
				.and(qParcelaNota.dataPagamento.between(dataPagamentoDe, dataPagamentoAte))
			);

		queryRateio
			.select(
				Projections.bean(
					RateioNotaPagamento.class,
					qRateioNotaPagamento.codigo,
					qRateioNotaPagamento.valor,
					Projections.bean(
						CentroFinanceiro.class,
						qCentroFinanceiro.codigo,
						qCentroFinanceiro.identificador
					).as("centroFinanceiro"),
					Projections.bean(
						NotaPagamento.class,
						qNotaPagamento.codigo,
						qNotaPagamento.valor
					).as("notaPagamento")
				)
			);

		List<RateioNotaPagamento> rateios = queryRateio.fetch();
		BigDecimal total = BigDecimal.ZERO;
		if (rateios != null && !rateios.isEmpty()) {
			total = queryRateio.fetch().stream().reduce(
				BigDecimal.ZERO, (soma, rateio) -> soma.add(rateio.getValor()
					.divide(new BigDecimal(parcelaNotaRepository.numParcelasByNota(rateio.getNotaPagamento().getCodigo())), 2, RoundingMode.HALF_UP)), BigDecimal::add);
		}

		return total.setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal obterValorTotalRateioNotaFiscal(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte, Pagamento.Status ... status) {
		JPAQuery<RateioNotaFiscal> queryRateio = new JPAQuery<>(getEntityManager());
		QRateioNotaFiscal qRateioNotaFiscal = QRateioNotaFiscal.rateioNotaFiscal;
		QItemNotaFiscal qItemNotaFiscal = QItemNotaFiscal.itemNotaFiscal;
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;

		queryRateio
			.from(qRateioNotaFiscal)
			.join(qRateioNotaFiscal.itemNotaFiscal, qItemNotaFiscal)
			.join(qRateioNotaFiscal.centroFinanceiro, qCentroFinanceiro)
			.join(qItemNotaFiscal.notaFiscal, qNotaFiscal)
			.join(qParcelaNota).on(qParcelaNota.nota.codigo.eq(qNotaFiscal.codigo))
			.where(
				qCentroFinanceiro.codigo.eq(codigoCf)
					.and(qParcelaNota.status.in(status))
					.and(qParcelaNota.dataPagamento.between(dataPagamentoDe, dataPagamentoAte))
			);

		queryRateio
			.select(
				Projections.bean(
					RateioNotaFiscal.class,
					qRateioNotaFiscal.codigo,
					qRateioNotaFiscal.valor,
					Projections.bean(
						CentroFinanceiro.class,
						qCentroFinanceiro.codigo,
						qCentroFinanceiro.identificador
					).as("centroFinanceiro"),
					Projections.bean(
						ItemNotaFiscal.class,
						qItemNotaFiscal.codigo,
						qItemNotaFiscal.preco,
						qItemNotaFiscal.quantidade,
						Projections.bean(
							NotaFiscal.class,
							qNotaFiscal.codigo
						).as("notaFiscal")
					).as("itemNotaFiscal")
				)
			);

		List<RateioNotaFiscal> rateios = queryRateio.fetch();
		BigDecimal total = BigDecimal.ZERO;
		if (rateios != null && !rateios.isEmpty()) {
			total = queryRateio.fetch().stream().reduce(
				BigDecimal.ZERO, (soma, rateio) -> soma.add(rateio.getValor()
					.divide(new BigDecimal(parcelaNotaRepository.numParcelasByNota(rateio.getItemNotaFiscal().getNotaFiscal().getCodigo())), 2, RoundingMode.HALF_UP)), BigDecimal::add);
		}

		return total.setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal obterValorTotalRateioItemRecebimento(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte, Pagamento.Status ... status) {
		JPAQuery<RateioItemRecebimento> queryRateio = new JPAQuery<>(getEntityManager());
		QRateioItemRecebimento qRateioItemRecebimento = QRateioItemRecebimento.rateioItemRecebimento;
		QItemRecebimento qItemRecebimento = QItemRecebimento.itemRecebimento;
		QRecebimento qRecebimento = QRecebimento.recebimento;
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;

		queryRateio
			.from(qRateioItemRecebimento)
			.join(qRateioItemRecebimento.itemRecebimento, qItemRecebimento)
			.join(qRateioItemRecebimento.centroFinanceiro, qCentroFinanceiro)
			.join(qItemRecebimento.recebimento, qRecebimento)
			.join(qRecebimento.notaFiscal, qNotaFiscal)
			.join(qParcelaNota).on(qParcelaNota.nota.codigo.eq(qNotaFiscal.codigo))
			.where(
				qCentroFinanceiro.codigo.eq(codigoCf)
					.and(qParcelaNota.status.in(status))
					.and(qParcelaNota.dataPagamento.between(dataPagamentoDe, dataPagamentoAte))
			);

		queryRateio
			.select(
				Projections.bean(
					RateioItemRecebimento.class,
					qRateioItemRecebimento.codigo,
					qRateioItemRecebimento.quantidade,
					Projections.bean(
						CentroFinanceiro.class,
						qCentroFinanceiro.codigo,
						qCentroFinanceiro.identificador
					).as("centroFinanceiro"),
					Projections.bean(
						ItemRecebimento.class,
						qItemRecebimento.codigo,
						qItemRecebimento.preco,
						Projections.bean(
							Recebimento.class,
							qRecebimento.codigo,
							Projections.bean(
								NotaFiscal.class,
								qNotaFiscal.codigo
							).as("notaFiscal")
						).as("recebimento")
					).as("itemRecebimento")
				)
			);


		List<RateioItemRecebimento> rateios = queryRateio.fetch();
		BigDecimal total = BigDecimal.ZERO;
		if (rateios != null && !rateios.isEmpty()) {
			total = rateios.stream().reduce(
				BigDecimal.ZERO, (soma, rateio) -> soma.add(
					rateio.getQuantidade().multiply(rateio.getItemRecebimento().getPreco())
						.divide(new BigDecimal(parcelaNotaRepository.numParcelasByNota(rateio.getItemRecebimento().getRecebimento().getNotaFiscal().getCodigo())), 2, RoundingMode.HALF_UP)), BigDecimal::add);
		}


		return total.setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal obterValorTotalRequisicaoCompra(Long codigoCf, Date dataPagamentoDe, Date dataPagamentoAte, Pagamento.Status ... status) {
		JPAQuery<ParcelaNota> queryPagamentoRequisicaoCompra = queryPagamento(dataPagamentoDe, dataPagamentoAte, status);

		queryPagamentoRequisicaoCompra.innerJoin(QNotaFiscal.notaFiscal).on(QNotaFiscal.notaFiscal.codigo.eq(qNota.codigo));
		queryPagamentoRequisicaoCompra.innerJoin(QNotaFiscal.notaFiscal.recebimentos, QRecebimento.recebimento);
		queryPagamentoRequisicaoCompra.innerJoin(QRecebimento.recebimento.pedido, QPedido.pedido);
		queryPagamentoRequisicaoCompra.innerJoin(QPedido.pedido.itens, QItemPedido.itemPedido);
		queryPagamentoRequisicaoCompra.innerJoin(QItemPedido.itemPedido.itens, QItemRecebimento.itemRecebimento);
		queryPagamentoRequisicaoCompra.innerJoin(QPedido.pedido.ordemCompra, QOrdemCompra.ordemCompra);
		queryPagamentoRequisicaoCompra.innerJoin(QOrdemCompra.ordemCompra.requisicoesCompraRefinadas, QRequisicaoCompraRefinada.requisicaoCompraRefinada);
		queryPagamentoRequisicaoCompra.innerJoin(QRequisicaoCompraRefinada.requisicaoCompraRefinada.requisicaoCompra, QRequisicaoCompra.requisicaoCompra);
		queryPagamentoRequisicaoCompra.innerJoin(QRequisicaoCompra.requisicaoCompra.centroFinanceiro, QCentroFinanceiro.centroFinanceiro);
		queryPagamentoRequisicaoCompra.where(QCentroFinanceiro.centroFinanceiro.codigo.eq(codigoCf));

		List<BigDecimal> listaValorItemQuantidade = new ArrayList<>();

		queryPagamentoRequisicaoCompra.fetch().forEach(parcelaNota -> {
			List<Recebimento> listRecebimento = recebimentoRepository.findByNotaFiscalCodigo(parcelaNota.getNota().getCodigo());
			listRecebimento.forEach(recebimento -> {
				List<ItemRecebimento> list = itemRecebimentoRepository.findByRecebimentoCodigo(recebimento.getCodigo());
				list.forEach(itemRecebimento -> {
					BigDecimal total = BigDecimal.ZERO;
					total = total.add(itemRecebimento.getQuantidade().multiply(itemRecebimento.getValor()));
					listaValorItemQuantidade.add(total);
				});
			});
		});

		BigDecimal total = listaValorItemQuantidade.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

		return total.setScale(2, RoundingMode.HALF_UP);
	}
}
