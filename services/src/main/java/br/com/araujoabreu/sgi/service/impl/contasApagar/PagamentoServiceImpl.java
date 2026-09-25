package br.com.araujoabreu.sgi.service.impl.contasApagar;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contabilidade.domain.ItemLoteContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.*;
import br.com.araujoabreu.sgi.service.compras.transfer.NotaPagamentoTO;
import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.impl.loteInterfaceFaturamento.LoteInterfaceNotaServiceImpl;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.araujoabreu.sgi.tesouraria.domain.QItemRelacaoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.corporativo.pessoa.domain.QDadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.contasApagar.ParcelaNotaRepository;
import br.com.fasolti.sgi.repository.feriado.FeriadoRepository;
import br.com.fasolti.sgi.repository.loteInterface.LoteInterfaceRepository;
import br.com.fasolti.sgi.repository.notaDesconto.NotaDescontoRepository;
import br.com.fasolti.sgi.repository.notaPagamento.NotaPagamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaRepository;
import br.com.fasolti.sgi.repository.tesouraria.DadosBancariosRepository;
import br.com.fasolti.sgi.repository.tesouraria.RelacaoBancariaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static br.com.araujoabreu.sgi.compras.domain.QNota.nota;

@Service
public class PagamentoServiceImpl extends BaseSearchService<Pagamento, PagamentoTO, PagamentoPesquisaTO, PagamentoRepository> implements PagamentoService {

	private static final QPagamento qPagamento = QPagamento.pagamento;

	@Autowired
	private ParcelaNotaRepository parcelaNotaRepository;

	@Autowired
	private DadosBancariosRepository dadosBancariosRepository;

	@Autowired
	private NotaDescontoRepository notaDescontoRepository;

	@Autowired
	private NotaTributoAliquotaRepository notaTributoAliquotaRepository;

	@Autowired
	private NotaPagamentoRepository notaPagamentoRepository;

	@Autowired
	private FeriadoRepository feriadoRepository;

	@Autowired
	private RelacaoBancariaRepository relacaoBancariaRepository;

	@Autowired
	private LoteInterfaceRepository loteInterfaceRepository;

	@Autowired
	public PagamentoServiceImpl(PagamentoRepository repository) {
		super(repository);
	}

	@Autowired
	public FeriadoService feriadoService;

	@Override
	public BigDecimal obterValorPagar(Long codigoPagamento, String tipoPagamento) {
		//Valor líquido + multa + juros - desconto
		BigDecimal valorPagar = BigDecimal.ZERO;

		//TR
		if (Pagamento.TipoPagamento.TRIBUTO.toString().equals(tipoPagamento)) {
			JPAQuery<Pagamento> jpaQuery = select();

			NumberExpression<BigDecimal> soma = qPagamento.valor.add(qPagamento.valorMulta).add(qPagamento.valorJuros).subtract(qPagamento.valorDesconto);
			valorPagar = jpaQuery
				.where(qPagamento.codigo.eq(codigoPagamento))
				.select(soma).fetchFirst();
		}

		//TA
		else if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equals(tipoPagamento)) {
			JPAQuery<Pagamento> jpaQuery = select(); //soma o valor de todos os TR associados ao TA

			NumberExpression<BigDecimal> soma = qPagamento.valor.add(qPagamento.valorMulta).add(qPagamento.valorJuros).subtract(qPagamento.valorDesconto.coalesce(BigDecimal.ZERO)).sum();
			valorPagar = jpaQuery
				.where(qPagamento.tributoAcumulado.codigo.eq(codigoPagamento))
				.select(soma).fetchFirst();

			JPAQuery<Pagamento> jpaQueryMultaTA = select(); //recupera o valor de multa, juros e descconto do TA

			NumberExpression<BigDecimal> somaMulta = qPagamento.valorMulta.add(qPagamento.valorJuros).subtract(qPagamento.valorDesconto);
			valorPagar = valorPagar.add(jpaQueryMultaTA
				.where(qPagamento.codigo.eq(codigoPagamento))
				.select(somaMulta).fetchFirst());

		}

		//NF
		else if (Pagamento.TipoPagamento.NOTA_FISCAL.toString().equals(tipoPagamento) || Pagamento.TipoPagamento.NOTA_PAGAMENTO.toString().equals(tipoPagamento)) {
			//valores recuperados diretamente da tabela Pagamento
			NumberExpression<BigDecimal> soma = qPagamento.valor
				.add(qPagamento.valorMulta)
				.add(qPagamento.valorJuros)
				.subtract(qPagamento.valorDesconto)
				.subtract(qPagamento.valorDevolucao.coalesce(BigDecimal.ZERO))
				.subtract(qPagamento.valorAdiantamento.coalesce(BigDecimal.ZERO));

			JPAQuery<Pagamento> jpaQueryNF = select();
			valorPagar = jpaQueryNF
				.where(qPagamento.codigo.eq(codigoPagamento))
				.select(soma).fetchFirst();

			//Soma dos tributos associados a NF - somente se aplica a primeira parcela
			BigDecimal totalTributos = this.calcularTotalAcumulado(codigoPagamento, tipoPagamento);

			//Soma de outros descontos associados a NF - somente se aplica a primeira parcela
			BigDecimal totalOutrosDescontos = this.getTotalOutrosDescontos(codigoPagamento);

			//Valor final
			try {
				valorPagar = valorPagar
					.subtract(totalTributos == null ? BigDecimal.ZERO : totalTributos)
					.subtract(totalOutrosDescontos == null ? BigDecimal.ZERO : totalOutrosDescontos);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return valorPagar == null ? BigDecimal.ZERO : valorPagar.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal obterValorPagar(Long codigoPagamento) {
		BigDecimal valorPagar = BigDecimal.ZERO;
		//valores recuperados diretamente da tabela Pagamento
		NumberExpression<BigDecimal> soma =
			qPagamento.valor
				.add(qPagamento.valorMulta)
				.add(qPagamento.valorJuros)
				.add(qPagamento.valorDesconto)
				.add(qPagamento.valorDevolucao.coalesce(BigDecimal.ZERO))
				.add(qPagamento.valorAdiantamento.coalesce(BigDecimal.ZERO));

		JPAQuery<Pagamento> jpaQueryNF = select();
		valorPagar = jpaQueryNF
			.where(qPagamento.codigo.eq(codigoPagamento))
			.select(soma).fetchFirst();

		return valorPagar;
	}

	@Override
	public BigDecimal obterValorLiquido(Long codigoPagamento, String tipoPagamento) {
		//No cálculo do valor líquido não são utilizados os valores: multa, juros e desconto.
		//TR
		if (Pagamento.TipoPagamento.TRIBUTO.toString().equals(tipoPagamento)) {
			JPAQuery<Pagamento> jpaQuery = select();

			return jpaQuery
				.where(qPagamento.codigo.eq(codigoPagamento))
				.select(qPagamento.valor).fetchFirst(); //realizando a busca deste modo evita que o hibernate dispare novas consultas ao banco para preencher os relacionamentos do tipo EAGER
		}

		//TA
		else if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equals(tipoPagamento)) {
			JPAQuery<Pagamento> jpaQuery = select();

			return jpaQuery
				.where(qPagamento.tributoAcumulado.codigo.eq(codigoPagamento))
				.select(qPagamento.valor.sum()).fetchFirst();
		}

		//NF e NP
		else if (Pagamento.TipoPagamento.NOTA_FISCAL.toString().equals(tipoPagamento) || Pagamento.TipoPagamento.NOTA_PAGAMENTO.toString().equals(tipoPagamento)) {
			//valores recuperados diretamente da tabela Pagamento
			NumberExpression<BigDecimal> soma = qPagamento.valor
				.subtract(qPagamento.valorDevolucao.coalesce(BigDecimal.ZERO))
				.subtract(qPagamento.valorAdiantamento.coalesce(BigDecimal.ZERO));

			JPAQuery<Pagamento> jpaQueryNF = select();
			BigDecimal valor = jpaQueryNF
				.where(qPagamento.codigo.eq(codigoPagamento))
				.select(soma).fetchFirst();

			//Soma dos tributos associados a NF - somente se aplica a primeira parcela
			BigDecimal totalTributos = this.calcularTotalAcumulado(codigoPagamento, tipoPagamento);

			//Soma de outros descontos associados a NF - somente se aplica a primeira parcela
			BigDecimal totalOutrosDescontos = this.getTotalOutrosDescontos(codigoPagamento);

			//Valor final
			return valor
				.subtract(totalTributos == null ? BigDecimal.ZERO : totalTributos)
				.subtract(totalOutrosDescontos == null ? BigDecimal.ZERO : totalOutrosDescontos);
		}

		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal calcularTotalAcumulado(Long codigoPagamento, String tipoPagamento) {

		//TA
		if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equals(tipoPagamento)) {
			return this.obterValorLiquido(codigoPagamento, tipoPagamento);
		}

		//NF
		else if (Pagamento.TipoPagamento.NOTA_FISCAL.toString().equals(tipoPagamento)) {
			QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
			QNota qNota = nota;
			QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;

			//Soma dos tributos associados a NF - somente se aplica a primeira parcela
			JPAQuery<ParcelaNota> jpaQueryTributos = getJPAQueryFactory().selectFrom(qParcelaNota)
				.join(qParcelaNota.nota, qNota)
				.join(qNota.listaNotaTributoAliquota, qNotaTributoAliquota)
				.join(qNotaTributoAliquota.pagamento, qPagamento);

			BigDecimal total = jpaQueryTributos.where(qParcelaNota.codigo.eq(codigoPagamento).and(qParcelaNota.ordem.eq(0)))
				.select(qPagamento.valor.sum()).fetchFirst();

			return total == null ? BigDecimal.ZERO : total;

		} else if (Pagamento.TipoPagamento.NOTA_PAGAMENTO.toString().equals(tipoPagamento)) {
			QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
			QNota qNota = nota;
			QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;

			//Soma dos tributos associados a NF - somente se aplica a primeira parcela
			JPAQuery<ParcelaNota> jpaQueryTributos = getJPAQueryFactory().selectFrom(qParcelaNota)
				.join(qParcelaNota.nota, qNota)
				.join(qNota.listaNotaTributoAliquota, qNotaTributoAliquota);

			BigDecimal total = jpaQueryTributos.where(qParcelaNota.codigo.eq(codigoPagamento).and(qParcelaNota.ordem.eq(0)))
				.select(qNotaTributoAliquota.valor.sum()).fetchFirst();

			if (total == null || total.equals(BigDecimal.ZERO)) {

				QNotaPagamento qNotaPagamento = QNotaPagamento.notaPagamento;
				JPAQuery<NotaPagamento> notaPagamentoJPAQuery = getJPAQueryFactory().selectFrom(qNotaPagamento).from(qParcelaNota)
					.where(
						qNotaPagamento.codigo.eq(qParcelaNota.nota.codigo).
							and(qParcelaNota.codigo.eq(codigoPagamento)).
							and(qParcelaNota.ordem.eq(0))
					);

				total = notaPagamentoJPAQuery.select(qNotaPagamento.valorTributo).fetchFirst();

			}

			return total == null ? BigDecimal.ZERO : total;
		}

		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getTotalOutrosDescontos(Long codigoPagamento) {
		//Somente se aplica a NF e NP
		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
		QNota qNota = nota;
		QNotaDesconto qNotaDesconto = QNotaDesconto.notaDesconto;

		JPAQuery<NotaDesconto> jpaQueryOutrosdescontos = getJPAQueryFactory().selectFrom(qNotaDesconto)
			.join(qNotaDesconto.nota, qNota)
			.join(qNota.parcelas, qParcelaNota);

		BigDecimal totalOutrosDescontos = jpaQueryOutrosdescontos
			.where(qParcelaNota.codigo.eq(codigoPagamento).and(qParcelaNota.ordem.eq(0)))
			.select(qNotaDesconto.valor.sum()).fetchFirst();

		return totalOutrosDescontos == null ? BigDecimal.ZERO : totalOutrosDescontos;
	}

	@Override
	public BigDecimal tributoMaisOutrosDesc(Long codigoPagamento, String tipoPagamento) {
		BigDecimal acum = this.calcularTotalAcumulado(codigoPagamento, tipoPagamento);
		BigDecimal desc = this.getTotalOutrosDescontos(codigoPagamento);
		return acum.add(desc);
	}

	/**
	 * Retornar pagamento
	 *
	 * @param pagamentoTO
	 * @param status
	 * @param atualizar
	 */
	@Transactional
	@Override
	public void retornar(PagamentoTO pagamentoTO, String status, Boolean atualizar) throws ValidationException {

		pagamentoTO.setStatus(status);
		/*FIXME AuditedObject
		pagamento.setAtualizador(autorizador.getUsuario().getLogin());
		pagamento.setDataAtualizacao(new Date());
		*/
		if (pagamentoTO.getNpAdiantamento() != null && pagamentoTO.getNpAdiantamento().getCodigo() != null && atualizar) {
			BigDecimal novoSaldo;
			novoSaldo = pagamentoTO.getNpAdiantamento().getSaldoAdiantamento().add(pagamentoTO.getValorAdiantamento());
			pagamentoTO.getNpAdiantamento().setSaldoAdiantamento(novoSaldo);
			pagamentoTO.getNpAdiantamento().setNotaFiscal(null);
			this.updateNotaPagamento(pagamentoTO.getNpAdiantamento(), null);
			pagamentoTO.setNpAdiantamento(null);
			pagamentoTO.setValorAdiantamento(pagamentoTO.getValorAdiantamento().subtract(pagamentoTO.getValorAdiantamento()));
		}

		Pagamento pagamento = getMapper().map(pagamentoTO, Pagamento.class);

		// FIXME: Só trazendo o objeto do banco é que funcionou a persistência.
		Pagamento pagamentoPersistente = getRepository().findById(pagamentoTO.getCodigo()).orElse(null);
		pagamentoPersistente.setStatus(pagamento.getStatus());
		if (pagamentoTO.getNpAdiantamento() != null && pagamentoTO.getNpAdiantamento().getCodigo() != null && atualizar) {
			BigDecimal novoSaldo;
			novoSaldo = pagamentoPersistente.getNpAdiantamento().getSaldoAdiantamento().add(pagamentoTO.getValorAdiantamento());
			pagamentoPersistente.getNpAdiantamento().setSaldoAdiantamento(novoSaldo);
			pagamentoPersistente.getNpAdiantamento().setNotaFiscal(null);
			pagamentoPersistente.setNpAdiantamento(null);
			pagamentoPersistente.setValorAdiantamento(pagamentoTO.getValorAdiantamento().subtract(pagamentoTO.getValorAdiantamento()));
		}
		getRepository().save(pagamentoPersistente);
	}


	public void updateNotaPagamento(NotaPagamentoTO notaPagamento, List<NotaDesconto> listaNotaDesconto) throws ValidationException {
		if (notaPagamento.isParcelaUnica()) {
			for (ParcelaNota parcela : parcelaNotaRepository.findByNotaCodigo(notaPagamento.getCodigo())) {
				if (parcela.getOrdem() > 0) {
					getRepository().delete(parcela);
				}
			}
		}

		List<NotaTributoAliquota> ntas = notaTributoAliquotaRepository.findByNotaCodigo(notaPagamento.getCodigo());
		if (ntas != null && !ntas.isEmpty()) {
			for (NotaTributoAliquota nta : ntas) {
				if (nta.getPagamento() == null) {
					if (nta.getAliquota().getCategoriaTributo().getTributo().getExibeNota()) {
						Pagamento pagamentoTributo = new Pagamento();
						try {
							pagamentoTributo = this.getPagamentoNotaTributoAliquota(nta, (getMapper().map(notaPagamento.getEmissor(), Colaborador.class)));
						} catch (Exception e) {
							e.printStackTrace();
							throw new ValidationException("ValidationException","Este período de apuração ainda não está sendo tratado pelo sistema!");
						}

						getRepository().save(pagamentoTributo);

						pagamentoTributo.setNumero(pagamentoTributo.getTipo().getDescricao() + "/" + pagamentoTributo.getCodigo());
						nta.setBaseCalculo(notaPagamento.getValor());
						nta.setPagamento(pagamentoTributo);
					}
				} else {
					try {
						if (nta.getAliquota().getCategoriaTributo().getTributo().getExibeNota()) {
							nta.setBaseCalculo(notaPagamento.getValor());

							Pagamento pagamentoTributo = nta.getPagamento();
							pagamentoTributo.setStatus(Pagamento.Status.AJUSTE_FISCAL);
							pagamentoTributo.setValor(this.calcularRetencao(nta));
							pagamentoTributo.setDataVencimento(this.calcularDataVencimento(nta, null));

							pagamentoTributo.setDataPagamento(pagamentoTributo.getDataVencimento());
							pagamentoTributo.setData(new Date(System.currentTimeMillis()));
							pagamentoTributo.setCentroFinanceiro(nta.getNota().getCentroFinanceiro());
							pagamentoTributo.setNaturezaPagamento(new NaturezaPagamento(1L));
							pagamentoTributo.setEmissor(getMapper().map(notaPagamento.getEmissor(), Colaborador.class));
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new ValidationException("ValidationException","Este período de apuração ainda não está sendo tratado pelo sistema!");
					}
				}
			}
		}

		if (listaNotaDesconto != null && !listaNotaDesconto.isEmpty()) {
			for (NotaDesconto nd : listaNotaDesconto) {
				notaDescontoRepository.save(nd);
			}
		}

		notaPagamentoRepository.save(getMapper().map(notaPagamento, NotaPagamento.class));
	}

	public Pagamento getPagamentoNotaTributoAliquota(NotaTributoAliquota nta, Colaborador colaborador) throws ValidationException {
		Pagamento pagamentoTributo = new Pagamento();
		pagamentoTributo.setTipo(Pagamento.TipoPagamento.TRIBUTO);
		pagamentoTributo.setStatus(Pagamento.Status.AJUSTE_FISCAL);
		pagamentoTributo.setTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.GUIA);
		pagamentoTributo.setValor(this.calcularRetencao(nta));
		pagamentoTributo.setDataVencimento(this.calcularDataVencimento(nta, null));

		pagamentoTributo.setDataPagamento(pagamentoTributo.getDataVencimento());
		pagamentoTributo.setData(new Date(System.currentTimeMillis()));
		pagamentoTributo.setCentroFinanceiro(nta.getNota().getCentroFinanceiro());
		pagamentoTributo.setNaturezaPagamento(new NaturezaPagamento(1L));
		pagamentoTributo.setEmissor(colaborador);
		pagamentoTributo.setNumero(pagamentoTributo.getTipo().getDescricao());

		pagamentoTributo.setListaNotaTributoAliquota(new ArrayList<>());
		pagamentoTributo.getListaNotaTributoAliquota().add(nta);

		return pagamentoTributo;
	}

	public BigDecimal calcularRetencao(NotaTributoAliquota notaTributoAliquota) {
		BigDecimal percentual = new BigDecimal("0");
		if (notaTributoAliquota == null || notaTributoAliquota.getBaseCalculo() == null) {
			return new BigDecimal("0");
		}

		if (notaTributoAliquota.getAliquota() != null && notaTributoAliquota.getAliquota().getPercentual() != null) {
			percentual = notaTributoAliquota.getAliquota().getPercentual();
		}

		return notaTributoAliquota.getBaseCalculo().multiply(percentual.divide(new BigDecimal(100.0)));

	}

	public Date calcularDataVencimento(NotaTributoAliquota notaTributoAliquota, RelacaoBancaria relacaoBancaria) throws ValidationException {
		Calendar dataVencimento = Calendar.getInstance();
		Calendar dataBase = Calendar.getInstance();
		if (notaTributoAliquota == null || notaTributoAliquota.getAliquota() == null || notaTributoAliquota.getAliquota().getCategoriaTributo() == null) {
			return null;
		}
		CategoriaTributo categoriaTributo = notaTributoAliquota.getAliquota().getCategoriaTributo();

		// neste momento o sistema somente trata estes dois períodos de apuração
		if (!categoriaTributo.getPeriodoApuracao().equals(CategoriaTributo.Periodo.QUINZENAL.getValorPeriodo())
			&& !categoriaTributo.getPeriodoApuracao().equals(CategoriaTributo.Periodo.MENSAL.getValorPeriodo())) {
			throw new ValidationException("ValidationException","Este período de apuração ainda não está sendo tratado pelo sistema!");
		}

		// Definição da data de base para o cálculo da data de vencimento do
		// tributo (Data de Emissão ou Pagamento)
		if (categoriaTributo.getReferenciaData().equals(CategoriaTributo.DataReferencia.EMISSAO)) {
			if (notaTributoAliquota.getNota() instanceof NotaFiscal) {
				dataBase.setTime(((NotaFiscal) notaTributoAliquota.getNota()).getDataEmissao());
			} else {
				// Nota de Pagamento
				dataBase.setTime(notaTributoAliquota.getNota().getDataCriacao());
			}
		} else {

			if (relacaoBancaria == null) {
				dataBase.setTime(notaTributoAliquota.getNota().getParcelas().get(0).getDataPagamento());
			} else {
				dataBase.setTime(relacaoBancaria.getDataPagamento());
			}

		}

		dataVencimento.setTime(dataBase.getTime());

		// a data de vencimento dos tributos, será sempre baseada no período subsequente ao
		// período de apuração do tributo.
		dataVencimento.add(Calendar.MONTH, 1);

		if (categoriaTributo.getPeriodoApuracao().equals(CategoriaTributo.Periodo.QUINZENAL.getValorPeriodo())) {
			/*
			 * Quando o período de apuração for quinzenal, o provável dia do
			 * vencimento será o último dia útil da quinzena subsequente.
			 */
			if (dataBase.get(Calendar.DAY_OF_MONTH) <= 15) {
				// primeira quinzena
				dataVencimento.set(Calendar.DAY_OF_MONTH, 1); //define-se o primeiro dia do mês subsequente
				dataVencimento.add(Calendar.DAY_OF_MONTH, -1); //retono para o último dia da quinzena subsequente
			} else {
				// quando a data base está na segunda quinzena, a data de vencimento provável será no dia 15
				// do mês subsequente
				dataVencimento.set(Calendar.DAY_OF_MONTH, 15);
			}
		} else if (categoriaTributo.getPeriodoApuracao().equals(CategoriaTributo.Periodo.MENSAL.getValorPeriodo()))
			dataVencimento.set(Calendar.DAY_OF_MONTH, categoriaTributo.getPrimeiroDia());

		// veirificar se a data de vencimento prevista é um feriado, ou fim de
		// semana
		boolean definiuData = false;

		while (!definiuData) {
			boolean isFeriado = feriadoService.verificarFeriado(dataVencimento);
			if (isFeriado) {
				if (categoriaTributo.getAntecipaPosterga().equals(CategoriaTributo.Antecipacao.ANTECIPA)) {
					dataVencimento.add(Calendar.DAY_OF_MONTH, -1);
				} else {
					dataVencimento.add(Calendar.DAY_OF_MONTH, 1);
				}
			} else if (dataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				if (categoriaTributo.getAntecipaPosterga().equals(CategoriaTributo.Antecipacao.ANTECIPA)) {
					dataVencimento.add(Calendar.DAY_OF_MONTH, -1);
				} else {
					dataVencimento.add(Calendar.DAY_OF_MONTH, 1);
				}
			} else {
				definiuData = true;
			}
		}

		return dataVencimento.getTime();
	}

	@Override
	public Date recalcularDataVencimento(Long codigoNotaTributoAliquota, Long codigoRelacaoBancaria) {
		NotaTributoAliquota notaTributoAliquota = notaTributoAliquotaRepository.findById(codigoNotaTributoAliquota).get();
		RelacaoBancaria relacaoBancaria = relacaoBancariaRepository.findById(codigoRelacaoBancaria).get();
		return calcularDataVencimento(notaTributoAliquota, relacaoBancaria);
	}

	@Override
	public Long getCodigoNota(Long codigoPagamento) {
		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
		List<Long> codigos = getJPAQueryFactory().selectFrom(qParcelaNota).where(qParcelaNota.codigo.eq(codigoPagamento)).select(qParcelaNota.nota.codigo).fetch();
		return codigos == null || codigos.isEmpty() ? 0L : codigos.get(0);
	}

	public List<PagamentoTO> executarJpaQuery(JPAQuery jpaQuery, QBean qBean) {
		return map(jpaQuery.fetch());
	}

	@Override
	public String obterNomeTributo(Long codigoPagamento, String tipoPagamento) {
		QTributo qTributo = QTributo.tributo;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QAliquota qAliquota = QAliquota.aliquota;
		QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;

		String nomeTributo;
		if (Pagamento.TipoPagamento.TRIBUTO.toString().equalsIgnoreCase(tipoPagamento)) {
			nomeTributo = getJPAQueryFactory().selectFrom(qNotaTributoAliquota)
				.join(qNotaTributoAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.join(qCategoriaTributo.tributo, qTributo)
				.where(qNotaTributoAliquota.pagamento.codigo.eq(codigoPagamento))
				.select(qTributo.nome).fetchFirst();
		} else {
			nomeTributo = select()
				.join(qPagamento.listaNotaTributoAliquota, qNotaTributoAliquota)
				.join(qNotaTributoAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.join(qCategoriaTributo.tributo, qTributo)
				.where(qPagamento.tributoAcumulado.codigo.eq(codigoPagamento))
				.select(qTributo.nome).fetchFirst();
		}

		return nomeTributo;
	}

	//TODO ESTES METODOS FORAM EXTRAIDOS DA ENTIDADE, USAR CONFORME NECESSIDADE
	public BigDecimal obterValorJurosMulta(Long codigoPagamento) {
		Pagamento pgto = getRepository().findById(codigoPagamento).orElse(null);

		BigDecimal valor = BigDecimal.ZERO;

		if (pgto.getValorJuros() != null) {
			valor = valor.add(pgto.getValorJuros());
		}

		if (pgto.getValorMulta() != null) {
			valor = valor.add(pgto.getValorMulta());
		}

		return valor;
	}

	public BigDecimal obterTotalOutrosDescontos(Long codigoPagamento) {
		Pagamento pgto = getRepository().findById(codigoPagamento).orElse(null);
		if (pgto.getTipo() != null) {
			if (pgto.getTipo().equals(Pagamento.TipoPagamento.NOTA_FISCAL) || pgto.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO)) {
				if (pgto instanceof ParcelaNota) {
					ParcelaNota parcela = ((ParcelaNota) pgto);
					if (parcela.getOrdem() == 0) {
						BigDecimal total = new BigDecimal(0);
						if (pgto instanceof ParcelaNota) {
							Nota nota = ((ParcelaNota) pgto).getNota();
							if (nota.getListaNotaDesconto() != null && !nota.getListaNotaDesconto().isEmpty()) {
								for (NotaDesconto notaDesconto : nota.getListaNotaDesconto()) {
									total = total.add(notaDesconto.getValor());
								}
							}
						}
						return total;
					}
				}
			}
		}
		return BigDecimal.ZERO;
	}

	public boolean isPrimeiraParcela(Long codigoPagamento) {
		Pagamento pgto = getRepository().findById(codigoPagamento).orElse(null);
		boolean isPrimeiraParcela = false;

		if (pgto instanceof ParcelaNota) {
			ParcelaNota parcela = ((ParcelaNota) pgto);
			if (parcela.getOrdem() == 0) {
				isPrimeiraParcela = true;
			}
		} else {
			isPrimeiraParcela = true;
		}

		return isPrimeiraParcela;
	}

	@Override
	public PagamentoTO obterPagamento(Long codigo) {
		QPessoa qPessoa = QPessoa.pessoa;
		JPAQuery<Pagamento> jpaQueryPagamento = select();
		jpaQueryPagamento.from(qPagamento).leftJoin(qPagamento.credor, qPessoa);
		QBean qBean = Projections.bean(
			Pagamento.class,
			qPagamento.codigo,
			qPagamento.numero,
			Projections.bean(
				Pessoa.class,
				qPessoa.codigo,
				qPessoa.tipoPessoa
			).as("credor")
		);
		jpaQueryPagamento.where(qPagamento.codigo.eq(codigo)).select(qBean);
		return map(jpaQueryPagamento.fetchOne());
	}

	@Override
	public PagamentoTO obterPagamentoEDadosBancariosPeloCodigoPagamento(Long codigoPagamento) {
		Pagamento pg = select()
			.from(QPagamento.pagamento)
			.select(Projections.fields(Pagamento.class,
				qPagamento.codigo,
				qPagamento.dataPagamento,
				qPagamento.status,
				qPagamento.tipo,
				qPagamento.tipoDocumentoPagamento,
				Projections.bean(DadosBancarios.class, QDadosBancarios.dadosBancarios.codigo
				).as("dadosBancarios")))
			.where(QPagamento.pagamento.codigo.eq(codigoPagamento))
			.fetchOne();
		return map(pg);
	}

	@Override
	public PagamentoTO obterPagamentoSimples(Long codigo) {
		QPagamento qPagamento = QPagamento.pagamento;
		QPessoa qPessoa = QPessoa.pessoa;
		QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;

		JPAQuery jpaQueryPagamento = select().from(qPagamento);
		jpaQueryPagamento.from(qPagamento)
			.leftJoin(qPagamento.credor, qPessoa)
			.leftJoin(qPagamento.centroFinanceiro, qCentroFinanceiro)
			.leftJoin(qPagamento.dadosBancarios, qDadosBancarios)
			.leftJoin(qDadosBancarios.banco, qBanco);

		QBean qBean = Projections.bean(
			Pagamento.class,
			qPagamento.codigo,
			qPagamento.valorMulta,
			qPagamento.valorJuros,
			qPagamento.valor,
			qPagamento.valorDesconto,
			qPagamento.valorAdiantamento,
			qPagamento.valorDevolucao,
			qPagamento.tipo,
			qPagamento.numero,
			qPagamento.tipoDocumentoPagamento,
			Projections.bean(
				DadosBancarios.class,
				qDadosBancarios.codigo,
				qDadosBancarios.digitoAgencia,
				qDadosBancarios.agencia,
				qDadosBancarios.conta,
				qDadosBancarios.digito,
				qDadosBancarios.tipoConta,
				Projections.bean(Banco.class, qBanco.codigo, qBanco.nome).as("banco")
			).as("dadosBancarios"),
			Projections.bean(
				PessoaJuridica.class,
				qPessoaJuridica.codigo,
				qPessoaJuridica.nome,
				qPessoaJuridica.nomeFantasia,
				qPessoaJuridica.documento,
				qPessoaJuridica.tipoPessoa
			).as("credor"),
			Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome
			).as("centroFinanceiro")
		);

		jpaQueryPagamento.select(qBean).where(new BooleanBuilder().and(qPagamento.codigo.eq(codigo)));
		Pagamento pagamento = (Pagamento) jpaQueryPagamento.fetchOne();

		return map(pagamento);
	}

	@Override
	public JPAQuery<Pagamento> jpaQueryPagamento() {
		return select();
	}

	@Override
	public PagamentoPesquisaTO getPagamentoPopUp(Long id, String tipo, Long codNota) {
		PagamentoPesquisaTO pagamentoPesquisaTO = new PagamentoPesquisaTO();

		QPagamento qPagamento = QPagamento.pagamento;

		JPAQuery jpaQueryPagamento = select().from(qPagamento);
		jpaQueryPagamento.from(qPagamento);

		QBean qBean = Projections.bean(
			Pagamento.class,
			qPagamento.codigo,
			qPagamento.valorMulta,
			qPagamento.valorJuros,
			qPagamento.valor,
			qPagamento.valorDesconto,
			qPagamento.valorAdiantamento,
			qPagamento.valorDevolucao,
			qPagamento.tipo,
			qPagamento.numero
		);

		jpaQueryPagamento.select(qBean).where(new BooleanBuilder().and(qPagamento.codigo.eq(id)));
		Pagamento pagamento = (Pagamento) jpaQueryPagamento.fetchOne();

		pagamentoPesquisaTO = mapSearch(pagamento);
		pagamentoPesquisaTO.setValorPagar(obterValorPagar(id, tipo));
		pagamentoPesquisaTO.setValorTributo(calcularTotalAcumulado(id, tipo));

		if (Pagamento.TipoPagamento.TRIBUTO.getDescricao().equals(tipo)) {
			QTributo qTributo = QTributo.tributo;
			QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
			QAliquota qAliquota = QAliquota.aliquota;
			QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;

			NotaTributoAliquota nta = getJPAQueryFactory().selectFrom(qNotaTributoAliquota)
				.join(qNotaTributoAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.join(qCategoriaTributo.tributo, qTributo)
				.where(qNotaTributoAliquota.pagamento.codigo.eq(id))
				.select(Projections.bean(
					NotaTributoAliquota.class,
					qNotaTributoAliquota.baseCalculo,
					qNotaTributoAliquota.percentual,
					Projections.bean(
						Aliquota.class,
						qAliquota.codigo,
						qAliquota.percentual,
						qAliquota.categoriaTributo
					).as("aliquota"),
					Projections.bean(
						Tributo.class,
						qTributo.codigo,
						qTributo.nome
					).as("tributo")
				)).fetchOne();

			pagamentoPesquisaTO.setValorTributo(nta.getBaseCalculo());

			if (nta.getPercentual() != null) {
				pagamentoPesquisaTO.setNotaValorISS(nta.getPercentual());

			} else {
				pagamentoPesquisaTO.setNotaValorISS(nta.getAliquota().getPercentual());
			}
		}

		if (Pagamento.TipoPagamento.NOTA_PAGAMENTO.getDescricao().equals(tipo)) {
			pagamentoPesquisaTO.setTotalOutrosDescontos(getTotalOutrosDescontos(id));
		}
		if (Pagamento.TipoPagamento.NOTA_FISCAL.getDescricao().equals(tipo)) {
			QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;

			NotaFiscal nfG = getJPAQueryFactory().selectFrom(qNotaFiscal)
				.where(qNotaFiscal.codigo.eq(codNota))
				.select(Projections.bean(
					NotaFiscal.class,
					qNotaFiscal.valorIpi,
					qNotaFiscal.desconto,
					qNotaFiscal.valorOutrasDespesas,
					qNotaFiscal.valorFrete,
					qNotaFiscal.valorSeguro,
					qNotaFiscal.valor,
					qNotaFiscal.tipoFornecimento,
					qNotaFiscal.tipoNotaFiscal

				)).fetchOne();

			pagamentoPesquisaTO.setNotaFiscalTO(mapTO(nfG));

			pagamentoPesquisaTO.setTotalValorItens(obterValorItens(codNota, nfG.getTipoNotaFiscal()));

			pagamentoPesquisaTO.setValorFreteSeguro(pagamentoPesquisaTO.getNotaFiscalTO().getValorFrete().add(pagamentoPesquisaTO.getNotaFiscalTO().getValorSeguro()));

			if (NaturezaFornecimento.TipoFornecimento.SERVICOS.toString().equals(pagamentoPesquisaTO.getNotaFiscalTO().getTipoFornecimento())) {
				pagamentoPesquisaTO.setTotalOutrosDescontos(getTotalOutrosDescontos(id));

				pagamentoPesquisaTO.setTotalValorServicos(obterValorServico(codNota, nfG.getTipoNotaFiscal()));

				pagamentoPesquisaTO.setValor(pagamentoPesquisaTO.getValor().subtract(pagamentoPesquisaTO.getValorTributo()));

				QTributo qTributo = QTributo.tributo;
				QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
				QAliquota qAliquota = QAliquota.aliquota;
				QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;


				List<NotaTributoAliquota> notaTributoAliquota = getJPAQueryFactory().selectFrom(qNotaTributoAliquota)
					.join(qNotaTributoAliquota.aliquota, qAliquota)
					.join(qAliquota.categoriaTributo, qCategoriaTributo)
					.join(qCategoriaTributo.tributo, qTributo)
					.where(qNotaTributoAliquota.nota.codigo.eq(codNota))
					.select(Projections.bean(
						NotaTributoAliquota.class,
						qNotaTributoAliquota.baseCalculo,
						qNotaTributoAliquota.percentual,
						Projections.bean(
							Aliquota.class,
							qAliquota.codigo,
							qAliquota.percentual,
							qAliquota.categoriaTributo
						).as("aliquota"),
						Projections.bean(
							Tributo.class,
							qTributo.codigo,
							qTributo.nome
						).as("tributo")
					)).fetch();

				if (!notaTributoAliquota.isEmpty()) {
					for (NotaTributoAliquota nta : notaTributoAliquota) {

						switch (nta.getTributo().getNome()) {
							case "IRRF":
								if (nta.getPercentual() != null && !nta.getPercentual().toString().equals("0.00")) {
									pagamentoPesquisaTO.setNotaValorIRRF(nta.getBaseCalculo().multiply(nta.getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								} else {
									pagamentoPesquisaTO.setNotaValorIRRF(nta.getBaseCalculo().multiply(nta.getAliquota().getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								}
								break;
							case "ISS":
								if (nta.getPercentual() != null && !nta.getPercentual().toString().equals("0.00")) {
									pagamentoPesquisaTO.setNotaValorISS(nta.getBaseCalculo().multiply(nta.getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								} else {
									pagamentoPesquisaTO.setNotaValorISS(nta.getBaseCalculo().multiply(nta.getAliquota().getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								}
								pagamentoPesquisaTO.setIssUF(nta.getAliquota().getPercentualCidadeUf());
								break;
							case "INSS":
								if (nta.getPercentual() != null && !nta.getPercentual().toString().equals("0.00")) {
									pagamentoPesquisaTO.setNotaValorINSS(nta.getBaseCalculo().multiply(nta.getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								} else {
									pagamentoPesquisaTO.setNotaValorINSS(nta.getBaseCalculo().multiply(nta.getAliquota().getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								}
								break;
							case "PIS/COFINS/CSLL":
								if (nta.getPercentual() != null && !nta.getPercentual().toString().equals("0.00")) {
									pagamentoPesquisaTO.setNotaValorPIS(nta.getBaseCalculo().multiply(nta.getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								} else {
									pagamentoPesquisaTO.setNotaValorPIS(nta.getBaseCalculo().multiply(nta.getAliquota().getPercentual().divide(new BigDecimal(100.0))).setScale(2, BigDecimal.ROUND_HALF_UP));
								}
								break;
							default:
								break;
						}
					}
				}
			}
		}

		return pagamentoPesquisaTO;
	}

	@Override
	public ParcelaNotaTO getParcelaNotaByPagamento(Long codNota) {
		List<ParcelaNota> list = parcelaNotaRepository.findByNotaCodigo(getCodigoNota(codNota));
		if (!list.isEmpty()) {
			return getMapper().map(list.get(0), ParcelaNotaTO.class);
		} else {
			return null;
		}
	}

	private NotaFiscalTO mapTO(NotaFiscal entity) {
		if (entity == null) return null;
		return getMapper().map(entity, NotaFiscalTO.class);
	}

	public BigDecimal obterValorItens(Long codNota, NotaFiscal.TipoNotaFiscal tipoNF) {
		BigDecimal totalValorItens = new BigDecimal("0");
		if (NotaFiscal.TipoNotaFiscal.NORMAL.equals(tipoNF)) {
			QRecebimento qRecebimento = QRecebimento.recebimento;
			QItemRecebimento qItemRecebimento = QItemRecebimento.itemRecebimento;

			JPAQuery<ItemRecebimento> itemRecebimentoJPAQuery = getJPAQueryFactory().selectFrom(qItemRecebimento)
				.join(qItemRecebimento.recebimento, qRecebimento);

			totalValorItens = itemRecebimentoJPAQuery
				.where(qRecebimento.notaFiscal.codigo.eq(codNota))
				.select(qItemRecebimento.preco.multiply(qItemRecebimento.quantidade).sum()).fetchFirst();


		} else {
			QItemNotaFiscal qItemNotaFiscal = QItemNotaFiscal.itemNotaFiscal;

			JPAQuery<ItemNotaFiscal> jpaQueryItens = getJPAQueryFactory().selectFrom(qItemNotaFiscal);

			totalValorItens = jpaQueryItens
				.where(qItemNotaFiscal.notaFiscal.codigo.eq(codNota))
				.select(qItemNotaFiscal.preco.multiply(qItemNotaFiscal.quantidade).sum()).fetchFirst();
		}

		return totalValorItens == null ? BigDecimal.ZERO : totalValorItens;
	}


	public BigDecimal obterValorServico(Long codNota, NotaFiscal.TipoNotaFiscal tipoNF) {
		BigDecimal total = new BigDecimal("0");
		if (NotaFiscal.TipoNotaFiscal.NORMAL.equals(tipoNF)) {
			QRecebimento qRecebimento = QRecebimento.recebimento;
			QItemRecebimento qItemRecebimento = QItemRecebimento.itemRecebimento;
			QItemPedido qItemPedido = QItemPedido.itemPedido;
			QItemProposta qItemProposta = QItemProposta.itemProposta;
			QItemCompra qItemCompra = QItemCompra.itemCompra;

			JPAQuery<ItemRecebimento> itemRecebimentoJPAQuery = getJPAQueryFactory().selectFrom(qItemRecebimento)
				.join(qItemRecebimento.recebimento, qRecebimento)
				.join(qItemRecebimento.itemPedido, qItemPedido)
				.join(qItemPedido.itemProposta, qItemProposta)
				.join(qItemProposta.itemCompra, qItemCompra);

			total = itemRecebimentoJPAQuery
				.where(qRecebimento.notaFiscal.codigo.eq(codNota).and(qItemCompra.tipoItem.servico.isTrue()))
				.select(qItemRecebimento.preco.multiply(qItemRecebimento.quantidade).sum()).fetchFirst();

		} else {
			QItemNotaFiscal qItemNotaFiscal = QItemNotaFiscal.itemNotaFiscal;
			QItemCompra qItemCompra = QItemCompra.itemCompra;

			JPAQuery<ItemNotaFiscal> itemPAQuery = getJPAQueryFactory().selectFrom(qItemNotaFiscal)
				.join(qItemNotaFiscal.itemCompra, qItemCompra);

			total = itemPAQuery
				.where(qItemNotaFiscal.notaFiscal.codigo.eq(codNota).and(qItemCompra.tipoItem.servico.isTrue()))
				.select(qItemNotaFiscal.preco.multiply(qItemNotaFiscal.quantidade).sum()).fetchFirst();
		}

		return total == null ? BigDecimal.ZERO : total;
	}

	@Transactional()
	private long updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento tipo, Long codigo) {
		return selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.tipoDocumentoPagamento, tipo).execute();
	}

	@Override
	public long updateTipoDocumentoPagamentoBoleto(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.BOLETO, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoDeposito(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.DEPOSITO, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoDda(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.DDA, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoGuia(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.GUIA, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoDoc(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.DOC, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoTed(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.TED, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoConcessionaria(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.CONCESSIONARIA, codigo);
	}

	@Override
	public long updateTipoDocumentoPagamentoOutros(Long codigo) {
		return updateTipoDocumentoPagamento(Pagamento.TipoDocumentoPagamento.OUTROS, codigo);
	}

	@Override
	@Transactional()
	public long updateDataPagamento(Date dataPagamento, Long codigo) {
		return selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.dataPagamento, dataPagamento).execute();
	}

	@Override
	@Transactional()
	public long updateDataVencimento(Date dataVencimento, Long codigo) {
		return selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.dataVencimento, dataVencimento).execute();
	}

	@Override
	@Transactional()
	public long updateCodigoBarras(String codigoBarras, Long codigo) {
		return selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.codigoBarras, codigoBarras).execute();
	}

	@Override
	@Transactional()
	public long updateLinhaDigitavel(String linhaDigitavel, Long codigo) {
		return selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.linhaDigitavel, linhaDigitavel).execute();
	}

	@Override
	@Transactional()
	public long updateDadosBancarios(Long codigo, Long codigoDadosBancarios) {
		Optional<DadosBancarios> dadosBancarios = dadosBancariosRepository.findById(codigoDadosBancarios);
		return dadosBancarios.map(bancarios -> selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.dadosBancarios, bancarios).execute()).orElse(0L);
	}

	@Transactional()
	private long updateStatus(Pagamento.Status status, Long codigo) {
		return selectUpdate(qPagamento.codigo.eq(codigo))
			.set(qPagamento.status, status).execute();
	}

	@Override
	public long updateStatusAberto(Long codigo) {
		return updateStatus(Pagamento.Status.ABERTO, codigo);
	}

	@Override
	public long updateStatusAjusteFiscal(Long codigo) {
		return updateStatus(Pagamento.Status.AJUSTE_FISCAL, codigo);
	}

	@Override
	public long updateStatusAnalise(Long codigo) {
		return updateStatus(Pagamento.Status.ANALISE, codigo);
	}

	@Override
	public long updateStatusReaberto(Long codigo) {
		return updateStatus(Pagamento.Status.REABERTO, codigo);
	}

	@Override
	public long updateStatusAutorizacao(Long codigo) {
		return updateStatus(Pagamento.Status.AUTORIZACAO, codigo);
	}

	@Override
	public long updateStatusCancelamento(Long codigo) {
		return updateStatus(Pagamento.Status.CANCELAMENTO, codigo);
	}

	@Override
	public long updateStatusCancelado(Long codigo) {
		return updateStatus(Pagamento.Status.CANCELADO, codigo);
	}

	@Override
	public long updateStatusAutorizado(Long codigo) {
		return updateStatus(Pagamento.Status.AUTORIZADO, codigo);
	}

	@Override
	public long updateStatusPagamento(Long codigo) {
		return updateStatus(Pagamento.Status.PAGAMENTO, codigo);
	}

	@Override
	public long updateStatusRelacionado(Long codigo) {
		return updateStatus(Pagamento.Status.RELACIONADO, codigo);
	}

	@Override
	public long updateStatusFinalizado(Long codigo) {
		return updateStatus(Pagamento.Status.FINALIZADO, codigo);
	}

	@Override
	public long updateStatusAprovacao(Long codigo) {
		return updateStatus(Pagamento.Status.APROVACAO, codigo);
	}

	@Override
	public long updateStatusAguardandoAgrupamento(Long codigo) {
		return updateStatus(Pagamento.Status.AGUARDANDO_AGRUPAMENTO, codigo);
	}

	@Override
	public long updateStatusAguardandoAgrupado(Long codigo) {
		return updateStatus(Pagamento.Status.AGRUPADO, codigo);
	}

	@Override
	public long updateStatusEmHomologacaoFundoFixo(Long codigo) {
		return updateStatus(Pagamento.Status.EM_HOMOLOGACAO_FUNDO_FIXO, codigo);
	}

	@Override
	public long updateStatusAguardandoLiberacaoRn(Long codigo) {
		return updateStatus(Pagamento.Status.AGUARDANDO_LIBERACAO_RN, codigo);
	}

	@Override
	public long updateStatusNaoFinalizado(Long codigo) {
		return updateStatus(Pagamento.Status.NAO_FINALIZADO, codigo);
	}

	@Override
	@Transactional()
	public JPAQuery<?> findByTributoAcumuladoCodigo(Long tributoAcumuladoCodigo) {
		return select().where(qPagamento.tributoAcumulado.codigo.eq(tributoAcumuladoCodigo));
	}

	@Override
	@Transactional()
	public JPAQuery<?> findByItemRelacaoPagamentoCodigo(Long codigo) {
		return select()
			.join(qPagamento.listaItemRelacao, QItemRelacaoPagamento.itemRelacaoPagamento)
			.where(QItemRelacaoPagamento.itemRelacaoPagamento.codigo.eq(codigo));
	}

	@Override
	public ParcelaNotaTO mockLoteCustos() {
		/* Ajustar esse trecho após migrar para o Job*/
		LoteInterface loteInterface = loteInterfaceRepository.findById(23414L).orElse(null);

		Calendar dataIni = Calendar.getInstance();
		dataIni.setTime(loteInterface.getDataEvento());
		dataIni.set(Calendar.DAY_OF_MONTH, dataIni.getActualMinimum(Calendar.DAY_OF_MONTH));
		Calendar dataFim = Calendar.getInstance();
		dataFim.setTime(loteInterface.getDataEvento());
		dataFim.set(Calendar.DAY_OF_MONTH, dataFim.getActualMaximum(Calendar.DAY_OF_MONTH));
		/* ******************************************** */


		//Seleção dos pagamentos de Nota Fiscal
//		this.loteCustosNFOCSemRateio(dataIni.getTime(), dataFim.getTime(), loteInterface.getUnidade().getCodigo());
		this.loteCustosNotasFiscaisAvulsas(dataIni.getTime(), dataFim.getTime(), loteInterface.getUnidade().getCodigo());
//		//Seleção dos pagamentos de Nota Fiscal e Nota Pagamento
//		List<Pagamento> pagamentosNP = getJPAQueryFactory().selectFrom(qParcelaNota)
//			.join(qParcelaNota.nota, qNota)
//			.where(
//				qParcelaNota.dataPagamento.between(dataIni.getTime(), dataFim.getTime())
//					.and(qParcelaNota.status.eq(Pagamento.Status.FINALIZADO))
//					.and(qNota.empresa.codigo.eq(loteInterface.getUnidade().getCodigo()))
//					.and(qParcelaNota.tipo.eq((Pagamento.TipoPagamento.NOTA_PAGAMENTO) ))
//			)
//			.select(Projections.bean(
//				Pagamento.class,
//				qParcelaNota.codigo,
//				qParcelaNota.numero,
//				qParcelaNota.tipo,
//				qParcelaNota.dataPagamento,
//				qParcelaNota.valor
//			)).fetch();
//
//		System.out.println(pagamentosNP.size());
//		for (Pagamento p : pagamentosNP)
//			System.out.println(p);
//
//		//Seleção dos pagamentos de Tributo.
//		QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;
//		List<Pagamento> pagamentosTR = getJPAQueryFactory().selectFrom(qPagamento)
//			.join(qPagamento.listaNotaTributoAliquota, qNotaTributoAliquota)
//			.join(qNotaTributoAliquota.nota, qNota)
//			.where(
//				qPagamento.dataPagamento.between(dataIni.getTime(), dataFim.getTime())
//					.and(qPagamento.status.eq(Pagamento.Status.FINALIZADO))
//					.and(qNota.empresa.codigo.eq(loteInterface.getUnidade().getCodigo()))
//					.and(qPagamento.tipo.eq((Pagamento.TipoPagamento.TRIBUTO) ))
//			)
//			.select(Projections.bean(
//				Pagamento.class,
//				qPagamento.codigo,
//				qPagamento.tipo,
//				qPagamento.dataPagamento,
//				qPagamento.valor
//			)).fetch();
//
//		//Seleção dos pagamentos de Tributo Acumulado(TA).
//		QPagamento qPagamentoTR = new QPagamento("qPagamentoTR");
//		List<Pagamento> pagamentosTA = getJPAQueryFactory().selectFrom(qPagamento)
//			.join(qPagamento.listaPagamento, qPagamentoTR)
//			.join(qPagamentoTR.listaNotaTributoAliquota, qNotaTributoAliquota)
//			.join(qNotaTributoAliquota.nota, qNota)
//			.where(
//				qPagamento.dataPagamento.between(dataIni.getTime(), dataFim.getTime())
//					.and(qPagamento.status.eq(Pagamento.Status.FINALIZADO))
//					.and(qNota.empresa.codigo.eq(loteInterface.getUnidade().getCodigo()))
//					.and(qPagamento.tipo.eq((Pagamento.TipoPagamento.TRIBUTO_ACUMULADO) ))
//			)
//			.select(Projections.bean(
//				Pagamento.class,
//				qPagamento.codigo,
//				qPagamento.tipo,
//				qPagamento.dataPagamento,
//				qPagamento.valor
//			)).distinct().fetch();

		return new ParcelaNotaTO();
//		return map(pagamentosNF.get(0));
	}

	/* LOTE DE CUSTOS */
	//Proccessa todos os pagamentos de Notas Fiscais de OC que não possuem rateio discriminado na tabela rateio_item_recebimento
	//Nestes casos o valor do rateio será calculado por média simples (valor do item / número de centros financeiros)
	private List<ItemLoteContabil> loteCustosNFOCSemRateio(Date dataIni, Date dataFim, Long codigoUnidade) {
		// As NFs de Fundo Fixo não entram no lote de custos pois são pagas pelo Colaborador.
		List<NotaFiscal.TipoNotaFiscal> tipos = new ArrayList<>();
		tipos.add(NotaFiscal.TipoNotaFiscal.OC_PC_ADIANTAMENTO);
		tipos.add(NotaFiscal.TipoNotaFiscal.OC_PC_FUNDO_FIXO);
		tipos.add(NotaFiscal.TipoNotaFiscal.OC_PC_REEMBOLSO);

		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
		QNota qNota = QNota.nota;
		List<Pagamento> pagamentosNF = getJPAQueryFactory().selectFrom(qParcelaNota)
			.join(qParcelaNota.nota, qNota)
			.where(
				qParcelaNota.dataPagamento.between(dataIni, dataFim)
					.and(qParcelaNota.status.eq(Pagamento.Status.FINALIZADO))
					.and(qNota.empresa.codigo.eq(codigoUnidade))
					.and(qParcelaNota.tipo.eq((Pagamento.TipoPagamento.NOTA_FISCAL) ))
			)
			.select(Projections.bean(
				Pagamento.class,
				qParcelaNota.codigo,
				qParcelaNota.numero,
				qParcelaNota.tipo,
				qParcelaNota.dataPagamento,
				qParcelaNota.valor
			)).fetch();

		return new ArrayList<>();
	}

	//Proccessa todos os pagamentos de Notas Fiscais de OC  com rateio discriminado na tabela rateio_item_recebimento
	private List<ItemLoteContabil> loteCustosNFOCComRateio(Date dataIni, Date dataFim, Long codigoUnidade) {
		// As NFs de Fundo Fixo não entram no lote de custos pois são pagas pelo Colaborador.
		List<NotaFiscal.TipoNotaFiscal> tipos = new ArrayList<>();
		tipos.add(NotaFiscal.TipoNotaFiscal.OC_PC_ADIANTAMENTO);
		tipos.add(NotaFiscal.TipoNotaFiscal.OC_PC_FUNDO_FIXO);
		tipos.add(NotaFiscal.TipoNotaFiscal.OC_PC_REEMBOLSO);

		return new ArrayList<>();
	}

	//Proccessa todos os pagamentos de Notas Fiscais avulsas
	private List<ItemLoteContabil> loteCustosNotasFiscaisAvulsas(Date dataIni, Date dataFim, Long codigoUnidade) {
		// As NFs de Fundo Fixo não entram no lote de custos pois são pagas pelo Colaborador.
		List<NotaFiscal.TipoNotaFiscal> tipos = new ArrayList<>();
		tipos.add(NotaFiscal.TipoNotaFiscal.FUNDO_FIXO);
		tipos.add(NotaFiscal.TipoNotaFiscal.REEMBOLSO);
		tipos.add(NotaFiscal.TipoNotaFiscal.ADIANTAMENTO);

		QParcelaNota qParcelaNota = QParcelaNota.parcelaNota;
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
		QItemNotaFiscal qItemNotaFiscal = QItemNotaFiscal.itemNotaFiscal;
		QRateioNotaFiscal qRateioNotaFiscal = QRateioNotaFiscal.rateioNotaFiscal;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QContaContabil qContaContabilCL = new QContaContabil("qContaContabilCL");
		QContaContabil qContaContabilCC = new QContaContabil("qContaContabilCC");
		QItemCompra qItemCompra = QItemCompra.itemCompra;
		QItemCompraContaContabil qItemCompraContaContabil = QItemCompraContaContabil.itemCompraContaContabil;
		QContaContabil qContaContabilItemCompra = new QContaContabil("qContaContabilItemCompra");
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery jpaQuery = getJPAQueryFactory().selectFrom(qParcelaNota)
			.join(qParcelaNota.nota, qNotaFiscal._super)
			.join(qNotaFiscal.items, qItemNotaFiscal)
			.join(qItemNotaFiscal.rateios, qRateioNotaFiscal)
			.join(qRateioNotaFiscal.centroFinanceiro, qCentroFinanceiro)
			.join(qItemNotaFiscal.itemCompra, qItemCompra)
			.leftJoin(qItemCompra.listaContaContabil, qItemCompraContaContabil)
			.leftJoin(qItemCompraContaContabil.contaContabil, qContaContabilItemCompra)
			.leftJoin(qContaContabilItemCompra.empresa, qUnidade)
			.leftJoin(qItemNotaFiscal.contaContabilCentroLucro, qContaContabilCL)
			.leftJoin(qItemNotaFiscal.contaContabilCentroCusto, qContaContabilCC)
			.where(
				qParcelaNota.dataPagamento.between(dataIni, dataFim)
					.and(qParcelaNota.status.eq(Pagamento.Status.FINALIZADO))
					.and(qNotaFiscal.empresa.codigo.eq(codigoUnidade))
					.and(qParcelaNota.tipo.eq(Pagamento.TipoPagamento.NOTA_FISCAL))
					.and(qNotaFiscal.tipoNotaFiscal.notIn(tipos))
					.and(
						qContaContabilItemCompra.codigo.isNull().or(qUnidade.codigo.eq(codigoUnidade))
					)
			)
			.select(
				Projections.fields(
					LoteCustosTO.class,
					qParcelaNota.codigo,
					qParcelaNota.numero,
					qParcelaNota.tipo,
					qParcelaNota.dataPagamento,
					qParcelaNota.valor,
					qParcelaNota.valorJuros,
					qParcelaNota.valorMulta,
					qCentroFinanceiro.codigo.as("cfCodigo"),
					qCentroFinanceiro.identificador.as("cfIdentificador"),
					qCentroFinanceiro.tipo.as("cfTipo"),
					qRateioNotaFiscal.valor.as("valorRateio"),
					qContaContabilCL.codigo.as("ccCLCodigo"),
					qContaContabilCL.numero.as("ccCLNumero"),
					qContaContabilCC.codigo.as("ccCCCodigo"),
					qContaContabilCC.numero.as("ccCCNumero")
				)
			);

		List<LoteCustosTO> resultado = jpaQuery.fetch();
//		for (LoteCustosTO loteCustosTO : resultado) {
//			System.out.println(loteCustosTO);
//			System.out.println("\n");
//		}
		return new ArrayList<>();
	}

	//Proccessa todos os pagamentos de Notas de pagamento
	private List<ItemLoteContabil> loteCustosNP(Date dataIni, Date dataFim, Long codigoUnidade) {

		return new ArrayList<>();
	}

	//Processa todos os pagamentos de tributos que não foram pagos através de TA (tributo acumulado)
	private List<ItemLoteContabil> loteCustosTR(Date dataIni, Date dataFim, Long codigoUnidade) {

		return new ArrayList<>();
	}

	//Processa todos os pagamentos TA (tributo acumulado)
	private List<ItemLoteContabil> loteCustosTA(Date dataIni, Date dataFim, Long codigoUnidade) {

		return new ArrayList<>();
	}
	/* FIM DOS MÉTODOS RELACIONADOS A LOTE DE CUSTOS */
}
