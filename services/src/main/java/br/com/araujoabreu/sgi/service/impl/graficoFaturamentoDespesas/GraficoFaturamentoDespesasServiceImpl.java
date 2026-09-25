package br.com.araujoabreu.sgi.service.impl.graficoFaturamentoDespesas;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.custos.PrevisaoCustos;
import br.com.araujoabreu.sgi.custos.QPrevisaoCustos;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.GraficoFaturamentoDespesasService;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.FiltroGraficoCLTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.GraficoChartTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.GraficoFaturamentoDespesasTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.ViewCLTO;
import br.com.araujoabreu.sgi.service.relatorios.RelatoriosService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.views.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraficoFaturamentoDespesasServiceImpl implements GraficoFaturamentoDespesasService {

	@Autowired
	RelatoriosService relatoriosService;

	@Autowired
	CentroFinanceiroService centroFinanceiroService;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ViewCLTO> buscarCLsPorGerenciaUnidade(FiltroGraficoCLTO filtroGraficoCLTO) {

		CentroFinanceiroTO centroFinanceiroTO = new CentroFinanceiroTO();
		centroFinanceiroTO.setCodigo(filtroGraficoCLTO.getCodigoCentroFinanceiro());

		List<CentroFinanceiroTO> resultado;

		if (filtroGraficoCLTO != null && filtroGraficoCLTO.getListaCodEmpresa() != null && !filtroGraficoCLTO.getListaCodEmpresa().isEmpty()) {
			resultado = centroFinanceiroService.getCFsByUnidadeGerencia(filtroGraficoCLTO);
		} else {
			resultado = centroFinanceiroService.buscaCcClPorGerencia(centroFinanceiroTO);
		}

		List<ViewCLTO> listaView = new ArrayList<>();

		for (CentroFinanceiroTO to : resultado) {
			ViewCLTO view = new ViewCLTO();
			view.setCodigo(to.getCodigo());
			view.setIdentificadorFinal(to.getIdentificadorFinal());
			view.setNome(to.getNome());
			view.setCor(gerarCorCl(to, filtroGraficoCLTO.getAno()));
			listaView.add(view);
		}

		return listaView;
	}

	private List<CentroFinanceiroTO> filtrarCLsPorUnidade(List<CentroFinanceiroTO> listaCLs, List<Long> listaCodEmpresa) {

		List<CentroFinanceiroTO> listaCLsPorUnidade = new ArrayList<>();

		for (Long codEmpresa : listaCodEmpresa) {
			CentroFinanceiroTO cfTO = new CentroFinanceiroTO();
			cfTO.setUnidade(new UnidadeTO());
			cfTO.getUnidade().setCodigo(codEmpresa);

			List<CentroFinanceiroTO> temp = centroFinanceiroService.buscarCFs(cfTO);

			listaCLsPorUnidade.addAll(temp);
		}

		List<CentroFinanceiroTO> resultado = new ArrayList<>();

		Set<Long> conjCodCl = new HashSet<>();

		for (CentroFinanceiroTO to : listaCLsPorUnidade) {
			conjCodCl.add(to.getCodigo());
		}

		for (CentroFinanceiroTO to : listaCLs) {
			if (conjCodCl.contains(to.getCodigo())) {
				resultado.add(to);
			}
		}

		return resultado;
	}

	private JPAQuery<VwCustosFaturamentoPrevisto> selectFaturamentoPrevisto() {
		return new JPAQueryFactory(entityManager).selectFrom(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto);
	}

	private JPAQuery<PrevisaoCustos> selectPrevisaoCusto(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		QCentroFinanceiro qCentroFinanceiroPai = new QCentroFinanceiro("cfPai");
		QGerencia qGerencia = QGerencia.gerencia;
//		QRegional qRegional = QRegional.regional;
		QRegionalGerencia qRegionalGerencia = QRegionalGerencia.regionalGerencia;

		JPAQuery<PrevisaoCustos> jpaQueryPrevisaoCusto = new JPAQueryFactory(entityManager).selectFrom(QPrevisaoCustos.previsaoCustos);

		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			jpaQueryPrevisaoCusto
				.join(QPrevisaoCustos.previsaoCustos.centroFinanceiro.pai, qCentroFinanceiroPai);
		}

//		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
//			if (graficoFaturamentoDespesasTO.getCodigoGerencia() == null) {
//				jpaQueryPrevisaoCusto
//					.join(QPrevisaoCustos.previsaoCustos.centroFinanceiro.pai, qCentroFinanceiroPai);
//			}
//
//			jpaQueryPrevisaoCusto
//				.join(qCentroFinanceiroPai).on(qCentroFinanceiroPai.codigo.eq(qGerencia.codigo))
//				.join(qGerencia).on(qRegionalGerencia.gerencia.codigo.eq(qGerencia.codigo));
//		}
//			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
//				.select(Projections.fields(
//					RegionalGerencia.class,
//					QRegionalGerencia.regionalGerencia.codigo,
//					Projections.fields(Gerencia.class,
//						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
//				))
//				.fetch();
//			List<Long> listCodigos = new ArrayList<>();
//			for (RegionalGerencia rg : listRegionalGerencia) {
//				listCodigos.add(rg.getGerencia().getCodigo());
//			}
//			booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoGerencia.in(listCodigos));
//		}
//		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
//			booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
//		}

		return jpaQueryPrevisaoCusto;
	}

	private JPAQuery<VwCustosFaturamentoRealizado> selectFaturamentoRealizado() {
		return new JPAQueryFactory(entityManager).selectFrom(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado);
	}

	private JPAQuery<RegionalGerencia> selectRegionalGerencia() {
		return new JPAQueryFactory(entityManager).selectFrom(QRegionalGerencia.regionalGerencia);
	}

	private Map<Integer, BigDecimal> totalPrevistoFaturamento(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> previsto = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal totalPrevisto = selectFaturamentoPrevisto()
				.select(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.valorMensal.sum())
				.where(booleanBuilder.getValue())
				.where(
					(	(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.mesPrevisaoPrimeiraParcela.loe(i)
						.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoPrevisaoPrimeiraParcela.eq(graficoFaturamentoDespesasTO.getAno()))
					)
						.or(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoPrevisaoPrimeiraParcela.lt(graficoFaturamentoDespesasTO.getAno()))
					)
						.and(
							QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoEncerramento.isNull()
								.or(
									(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.mesEncerramento.gt(i)
										.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoEncerramento.goe(graficoFaturamentoDespesasTO.getAno()))
									)
								)
						)
				)
				.fetch().get(0);
			previsto.put(i, totalPrevisto);
		}
		return previsto;
	}

	private Map<Integer, BigDecimal> totalRealizadoFaturamento(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO, int tipoFaturamento) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(
			QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.anoEmissao.eq(graficoFaturamentoDespesasTO.getAno())
				.and(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.tipoFaturamento.eq(tipoFaturamento))
		);

		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> realizador = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal totalRealizado = selectFaturamentoRealizado()
				.select(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.valorBruto.sum())
				.where(booleanBuilder.getValue())
				.where(QVwCustosFaturamentoRealizado.vwCustosFaturamentoRealizado.mesEmissao.eq(i))
				.fetch().get(0);
			realizador.put(i, totalRealizado);
		}

		return realizador;
	}

	@Override
	public GraficoChartTO gerarGraficoFaturamento(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		Map<Integer, BigDecimal> totalPrevisto = totalPrevistoFaturamento(graficoFaturamentoDespesasTO);
		Map<Integer, BigDecimal> realizador = totalRealizadoFaturamento(graficoFaturamentoDespesasTO, 0);
		Map<Integer, BigDecimal> realizadorExtra = totalRealizadoFaturamento(graficoFaturamentoDespesasTO, 1);

		GraficoChartTO graficoChartTO = new GraficoChartTO();

		List<Double> listaPrevisto = new ArrayList<>();
		List<Double> listaRealizado = new ArrayList<>();
		List<Double> listaExtra = new ArrayList<>();


		for (int i = 0; i < 12; i++) {
			listaPrevisto.add(totalPrevisto.get(i + 1) == null ? 0d : totalPrevisto.get(i + 1).doubleValue());
			listaRealizado.add(realizador.get(i + 1) == null ? 0d : realizador.get(i + 1).doubleValue());
			listaExtra.add(realizadorExtra.get(i + 1) == null ? 0d : realizadorExtra.get(i + 1).doubleValue());
		}

		graficoChartTO.setPrevisto(listaPrevisto);
		graficoChartTO.setRealizado(listaRealizado);
		graficoChartTO.setExtra(listaExtra);

		return graficoChartTO;
	}

	private JPAQuery<VwCustosReceitaPrevista> selectReceitaPrevista() {
		return new JPAQueryFactory(entityManager).selectFrom(QVwCustosReceitaPrevista.vwCustosReceitaPrevista);
	}

	private JPAQuery<VwCustosReceitaRealizada> selectReceitaRealizada() {
		return new JPAQueryFactory(entityManager).selectFrom(QVwCustosReceitaRealizada.vwCustosReceitaRealizada);
	}

	private JPAQuery<VwCustosGastosPrevistos> selectCustos() {
		return new JPAQueryFactory(entityManager).selectFrom(QVwCustosGastosPrevistos.vwCustosGastosPrevistos);
	}

	private Map<Integer, BigDecimal> totalPrevistoLiquido(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.anoVencimento.eq(graficoFaturamentoDespesasTO.getAno()));
		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> previsto = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal totalPrevisto = selectReceitaPrevista()
				.select(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.valorLiquido.sum())
				.where(booleanBuilder.getValue())
				.where(QVwCustosReceitaPrevista.vwCustosReceitaPrevista.mesVenciemento.eq(i))
				.fetch().get(0);
			previsto.put(i, totalPrevisto);
		}

		return previsto;
	}

	private Map<Integer, BigDecimal> totalRealizadoLiquido(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO, int tipoFaturamento) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		booleanBuilder.and(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.anoPagamento.eq(graficoFaturamentoDespesasTO.getAno()));
		booleanBuilder.and(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.tipoFaturamento.eq(tipoFaturamento));
		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> realizador = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal totalRealizado = selectReceitaRealizada()
				.select(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.valorLiquido.sum())
				.where(booleanBuilder.getValue())
				.where(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.mesVPagamento.eq(i))
				.fetch().get(0);
			realizador.put(i, totalRealizado);
		}
		return realizador;
	}

	@Override
	public GraficoChartTO gerarGraficoReceitaLiquida(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		Map<Integer, BigDecimal> previsto = totalPrevistoLiquido(graficoFaturamentoDespesasTO);
		Map<Integer, BigDecimal> realizador = totalRealizadoLiquido(graficoFaturamentoDespesasTO, 0);
		Map<Integer, BigDecimal> realizadorExtra = totalRealizadoLiquido(graficoFaturamentoDespesasTO, 1);

		GraficoChartTO graficoChartTO = new GraficoChartTO();

		List<Double> listaPrevisto = new ArrayList<>();
		List<Double> listaRealizado = new ArrayList<>();
		List<Double> listaExtra = new ArrayList<>();

		for (int i = 0; i < 12; i++) {
			listaPrevisto.add(previsto.get(i + 1) == null ? 0d : previsto.get(i + 1).doubleValue());
			listaRealizado.add(realizador.get(i + 1) == null ? 0d : realizador.get(i + 1).doubleValue());
			listaExtra.add(realizadorExtra.get(i + 1) == null ? 0d : realizadorExtra.get(i + 1).doubleValue());
		}

		graficoChartTO.setPrevisto(listaPrevisto);
		graficoChartTO.setRealizado(listaRealizado);
		graficoChartTO.setExtra(listaExtra);

		return graficoChartTO;
	}

	private Map<Integer, BigDecimal> totalPrevistoFaturamentoCustos(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> previsto = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal totalPrevisto;
			totalPrevisto = selectFaturamentoPrevisto()
				.select(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.custoMensal.sum())
				.where(booleanBuilder.getValue())
				.where(
					(	(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.mesPrevisaoPrimeiraParcela.loe(i)
						.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoPrevisaoPrimeiraParcela.eq(graficoFaturamentoDespesasTO.getAno()))
					)
						.or(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoPrevisaoPrimeiraParcela.lt(graficoFaturamentoDespesasTO.getAno()))
					)
						.and(
							QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoEncerramento.isNull()
								.or(
									(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.mesEncerramento.gt(i)
										.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoEncerramento.goe(graficoFaturamentoDespesasTO.getAno()))
									)
								)
						)
				)
				.fetch().get(0);
			previsto.put(i, totalPrevisto);
		}
		return previsto;
	}

	private Map<Integer, BigDecimal> totalRealizadoFaturamentoCustos(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO, int tipoFaturamento) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.anoPagamento.eq(graficoFaturamentoDespesasTO.getAno()));
		booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.tipoFaturamento.eq(tipoFaturamento));

		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> realizador = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal result = selectCustos()
				.select(
					(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.valorRecebimento
						.multiply(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.valorLiquidoPagamento))
						.divide(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.totalItens).sum()
				)
				.where(booleanBuilder.getValue())
				.where(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.mesPagamento.eq(i))
				.fetch().get(0);

			realizador.put(i, result);
		}

		return realizador;
	}

	@Override
	public GraficoChartTO gerarGraficoCustos(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		//BigDecimal totalPrevisto = totalPrevistoFaturamentoCustos(graficoFaturamentoDespesasTO);
		Map<Integer, BigDecimal> previsto = totalPrevistoFaturamentoCustos(graficoFaturamentoDespesasTO);
		Map<Integer, BigDecimal> custoContratual = totalRealizadoFaturamentoCustos(graficoFaturamentoDespesasTO, 0);
		Map<Integer, BigDecimal> custoExtra = totalRealizadoFaturamentoCustos(graficoFaturamentoDespesasTO, 1);
		Map<Integer, BigDecimal> previsaoMO = totalPrevisaoMO(graficoFaturamentoDespesasTO);

		GraficoChartTO graficoChartTO = new GraficoChartTO();

		List<Double> listaPrevisto = new ArrayList<>();
		List<Double> listaRealizado = new ArrayList<>();
		List<Double> listaExtra = new ArrayList<>();
		List<Double> listaPrevisaoMO = new ArrayList<>();

		LocalDate localDate = LocalDate.now();

		double valorMes = 0;

		for (int i = 1; i <= 12; i++) {
			valorMes = (previsaoMO.get(i) != null) ? previsaoMO.get(i).doubleValue() : valorMes;
			//listaPrevisto.add(totalPrevisto == null ? 0d : totalPrevisto.doubleValue());
			listaPrevisto.add(previsto.get(i) == null ? 0d : previsto.get(i).doubleValue());
			listaRealizado.add(custoContratual.get(i) == null ? 0d : custoContratual.get(i).doubleValue());
			listaExtra.add(custoExtra.get(i) == null ? 0d : custoExtra.get(i).doubleValue());
			listaPrevisaoMO.add((localDate.getMonthValue() != i  ? 0d : valorMes));
		}

		graficoChartTO.setPrevisto(listaPrevisto);
		graficoChartTO.setRealizado(listaRealizado);
		graficoChartTO.setExtra(listaExtra);
		graficoChartTO.setPrevisaoMO((listaPrevisaoMO));

		return graficoChartTO;
	}

	private Map<Integer, BigDecimal> totalPrevistoTotalBdi(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoCentroFinanceiro.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoGerencia.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()));
		}
		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoGerencia.in(listCodigos));
		}
		if (graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.codigoUnidade.in(graficoFaturamentoDespesasTO.getListaCodEmpresa()));
		}

		Map<Integer, BigDecimal> previsto = new TreeMap<>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal totalPrevisto = selectFaturamentoPrevisto()
				.select(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.valorBdi.sum())
				.where(booleanBuilder.getValue())
				.where(
					(	(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.mesPrevisaoPrimeiraParcela.loe(i)
						.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoPrevisaoPrimeiraParcela.eq(graficoFaturamentoDespesasTO.getAno()))
					)
						.or(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoPrevisaoPrimeiraParcela.lt(graficoFaturamentoDespesasTO.getAno()))
					)
						.and(
							QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoEncerramento.isNull()
								.or(
									(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.mesEncerramento.gt(i)
										.and(QVwCustosFaturamentoPrevisto.vwCustosFaturamentoPrevisto.anoEncerramento.goe(graficoFaturamentoDespesasTO.getAno()))
									)
								)
						)
				)
				.fetch().get(0);
			previsto.put(i, totalPrevisto);
		}
		return previsto;
	}

	private Map<Integer, BigDecimal> totalPrevisaoMO(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		QCentroFinanceiro qCentroFinanceiroPai = new QCentroFinanceiro("cfPai");
		QGerencia qGerencia = QGerencia.gerencia;
		QRegionalGerencia qRegionalGerencia = QRegionalGerencia.regionalGerencia;

		if (graficoFaturamentoDespesasTO.getCodigoCl() != null) {
			booleanBuilder.and(QPrevisaoCustos.previsaoCustos.centroFinanceiro.codigo.eq(graficoFaturamentoDespesasTO.getCodigoCl()));
		}

		if (graficoFaturamentoDespesasTO.getCodigoGerencia() != null) {
			booleanBuilder.and(
				qCentroFinanceiroPai.codigo.eq(graficoFaturamentoDespesasTO.getCodigoGerencia())
					.or(QPrevisaoCustos.previsaoCustos.gerencia.codigo.eq(graficoFaturamentoDespesasTO.getCodigoGerencia()))
			);
		}

		if(graficoFaturamentoDespesasTO.getListaCodEmpresa() != null && !graficoFaturamentoDespesasTO.getListaCodEmpresa().isEmpty()) {
			booleanBuilder.and(
				QPrevisaoCustos.previsaoCustos.centroFinanceiro.unidade.codigo.in(graficoFaturamentoDespesasTO.getListaCodEmpresa())
			);
		}

		if (graficoFaturamentoDespesasTO.getCodigoRegional() != null) {
			List<RegionalGerencia> listRegionalGerencia = selectRegionalGerencia()
				.select(Projections.fields(
					RegionalGerencia.class,
					QRegionalGerencia.regionalGerencia.codigo,
					Projections.fields(Gerencia.class,
						QRegionalGerencia.regionalGerencia.gerencia.codigo).as("gerencia")
				))
				.where(QRegionalGerencia.regionalGerencia.regional.codigo.eq(graficoFaturamentoDespesasTO.getCodigoRegional()))
				.fetch();
			List<Long> listCodigos = new ArrayList<>();
			for (RegionalGerencia rg : listRegionalGerencia) {
				listCodigos.add(rg.getGerencia().getCodigo());
			}
			booleanBuilder.and(QPrevisaoCustos.previsaoCustos.gerencia.codigo.in(listCodigos));
		}


		Map<Integer, BigDecimal> previsao = new TreeMap<>();

		for (int i = 1; i <= 12; i++) {
			BigDecimal totalPrevisao = selectPrevisaoCusto(graficoFaturamentoDespesasTO)
				.select(QPrevisaoCustos.previsaoCustos.valor.sum())
				.where(booleanBuilder.getValue())
				.where(
					QPrevisaoCustos.previsaoCustos.referenciaAno.eq(graficoFaturamentoDespesasTO.getAno())
						.and(QPrevisaoCustos.previsaoCustos.referenciaMes.eq(i)))
				.fetch().get(0);
			previsao.put(i, totalPrevisao);
		}
		return previsao;
	}

	@Override
	public GraficoChartTO gerarGraficoMargem(GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO) {
		Map<Integer, BigDecimal> realizadorLiquidoContratual = totalRealizadoLiquido(graficoFaturamentoDespesasTO, 0);
		Map<Integer, BigDecimal> realizadorLiquidoExtra = totalRealizadoLiquido(graficoFaturamentoDespesasTO, 1);

		Map<Integer, BigDecimal> custoContratual = totalRealizadoFaturamentoCustos(graficoFaturamentoDespesasTO, 0);
		Map<Integer, BigDecimal> custoExtra = totalRealizadoFaturamentoCustos(graficoFaturamentoDespesasTO, 1);

		Map<Integer, BigDecimal> previsto = totalPrevistoTotalBdi(graficoFaturamentoDespesasTO);
		Map<Integer, BigDecimal> previsaoMO = totalPrevisaoMO(graficoFaturamentoDespesasTO);

		GraficoChartTO graficoChartTO = new GraficoChartTO();

		List<Double> listaPrevisto = new ArrayList<>();
		List<Double> listaRealizado = new ArrayList<>();
		List<Double> listaExtra = new ArrayList<>();
		List<Double> listaPrevisaoMO = new ArrayList<>();

		LocalDate localDate = LocalDate.now();
		double valorMes = 0;

		for (int i = 0; i < 12; i++) {
			valorMes = (previsaoMO.get(i+1) != null) ? previsaoMO.get(i+1).doubleValue() : valorMes;
			double valorMesAux = localDate.getMonthValue() != (i+1)  ? 0d : valorMes;
			listaPrevisto.add(previsto.get(i + 1) == null ? 0d : previsto.get(i + 1).doubleValue());
			listaRealizado.add((realizadorLiquidoContratual.get(i + 1) == null ? 0d : realizadorLiquidoContratual.get(i + 1).doubleValue()) - (custoContratual.get(i + 1) == null ? 0d : custoContratual.get(i + 1).doubleValue()) - valorMesAux);
			listaExtra.add((realizadorLiquidoExtra.get(i + 1) == null ? 0d : realizadorLiquidoExtra.get(i + 1).doubleValue()) - (custoExtra.get(i + 1) == null ? 0d : custoExtra.get(i + 1).doubleValue()));
			listaPrevisaoMO.add(localDate.getMonthValue() != (i+1)  ? 0d : valorMes*(-1));
		}

		graficoChartTO.setPrevisto(listaPrevisto);
		graficoChartTO.setRealizado(listaRealizado);
		graficoChartTO.setExtra(listaExtra);
		graficoChartTO.setPrevisaoMO((listaPrevisaoMO));

		return graficoChartTO;
	}

	private String gerarCorCl(CentroFinanceiroTO centroFinanceiroTO, Integer ano) {
		GraficoFaturamentoDespesasTO graficoFaturamentoDespesasTO = new GraficoFaturamentoDespesasTO();
		graficoFaturamentoDespesasTO.setCodigoCl(centroFinanceiroTO.getCodigo());
		graficoFaturamentoDespesasTO.setAno(ano);

		Map<Integer, BigDecimal> listaTotalPrevistoBdi = totalPrevistoTotalBdi(graficoFaturamentoDespesasTO);
		double totalPrevistoBdi = 0d;
		for (int i = 0; i < 12; i++) {
			totalPrevistoBdi += listaTotalPrevistoBdi.get(i + 1) == null ? 0d : listaTotalPrevistoBdi.get(i + 1).doubleValue();
		}

		BigDecimal totalLiquidoRealizado = selectReceitaRealizada()
			.select(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.valorLiquido.sum())
			.where(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.anoPagamento.eq(ano))
			.where(QVwCustosReceitaRealizada.vwCustosReceitaRealizada.codigoCentroFinanceiro.eq(centroFinanceiroTO.getCodigo()))
			.fetch().get(0);

		BigDecimal resultCusto = selectCustos()
			.select(
				(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.valorRecebimento
					.multiply(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.valorLiquidoPagamento))
					.divide(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.totalItens).sum()
			)
			.where(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.anoPagamento.eq(ano))
			.where(QVwCustosGastosPrevistos.vwCustosGastosPrevistos.codigoCentroFinanceiro.eq(centroFinanceiroTO.getCodigo()))
			.fetch().get(0);

		double totalLiquido = totalLiquidoRealizado == null ? 0d : totalLiquidoRealizado.doubleValue();

		double result = totalLiquido - (resultCusto == null ? 0d : resultCusto.doubleValue());

		if (result < 0) {
			return "red";
		} else if (result < (totalPrevistoBdi == 0 ? 0d : totalPrevistoBdi)) {
			return "orange";
		}

		return "black";
	}
}
