package br.com.araujoabreu.sgi.service.impl.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroPrevisao;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiroPrevisao;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroAliquotaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroPrevisaoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPrevisaoPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPrevisaoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.fecho.FechoService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.service.relatorios.RelatoriosService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroPrevisaoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CentroFinanceiroPrevisaoServiceImpl extends BaseSearchService<CentroFinanceiroPrevisao, CentroFinanceiroPrevisaoTO, CentroFinanceiroPrevisaoPesquisaTO, CentroFinanceiroPrevisaoRepository> implements CentroFinanceiroPrevisaoService {

	private static final QCentroFinanceiroPrevisao qCentroFinanceiroPrevisao = QCentroFinanceiroPrevisao.centroFinanceiroPrevisao;
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(CentroFinanceiroPrevisaoServiceImpl.class);
	@Autowired
	private CentroFinanceiroService centroFinanceiroService;
	@Autowired
	private RelatoriosService relatoriosService;
	@Autowired
	private FechoService fechoService;
	@Autowired
	private AliquotaRepository aliquotaRepository;
	@Autowired
	private CentroFinanceiroAliquotaService centroFinanceiroAliquotaService;

	@Autowired
	public CentroFinanceiroPrevisaoServiceImpl(CentroFinanceiroPrevisaoRepository repository) {
		super(repository);
	}

	/**
	 * @param centroFinanceiroPrevisaoPesquisaTO
	 * @return
	 */
	@Override
	@Transactional
	public List<CentroFinanceiroPrevisaoTO> buscarPrevisoes(CentroFinanceiroPrevisaoPesquisaTO centroFinanceiroPrevisaoPesquisaTO) {

		Long codigoGerencia = centroFinanceiroPrevisaoPesquisaTO.getCodigoGerencia();
		Long codigoCL = centroFinanceiroPrevisaoPesquisaTO.getCodigoCL();
		String competencia = centroFinanceiroPrevisaoPesquisaTO.getCompetencia();

		List<CentroFinanceiroPrevisaoTO> resultado = new ArrayList<>();
		List<CentroFinanceiroTO> listaCentroFinanceiro = new ArrayList<>();

		if (codigoCL != null) {
			CentroFinanceiroTO centroFinanceiroTO = centroFinanceiroService.buscarCentroFinanceiroById(String.valueOf(codigoCL));

			if (centroFinanceiroTO != null && centroFinanceiroTO.getPai().getCodigo().equals(codigoGerencia)) {
				listaCentroFinanceiro.add(centroFinanceiroTO);
			}

		} else {
			CentroFinanceiroTO filtro = new CentroFinanceiroTO();
			filtro.setCodigo(codigoGerencia);

			listaCentroFinanceiro.addAll(relatoriosService.buscaCcClPorGerencia(filtro));
		}

		if (listaCentroFinanceiro != null && !listaCentroFinanceiro.isEmpty()) {
			resultado = procurarPrevisoes(listaCentroFinanceiro, competencia);
		}

		return resultado;
	}

	/**
	 * Busca as previsões, de acordo com a competencia e centros financeiros passados por parametro.
	 *
	 * @param listaCentrofinanceiroTO
	 * @param competencia
	 * @return
	 */
	@Transactional
	private List<CentroFinanceiroPrevisaoTO> procurarPrevisoes(List<CentroFinanceiroTO> listaCentrofinanceiroTO, String competencia) {
		List<CentroFinanceiroPrevisaoTO> resultado = new ArrayList<>();

		for (CentroFinanceiroTO to : listaCentrofinanceiroTO) {
			CentroFinanceiroPrevisaoTO centroFinanceiroPrevisaoTO = findByCodCentroFinanceiroAndCompetencia(to.getCodigo(), competencia);

			if (centroFinanceiroPrevisaoTO == null) {
				centroFinanceiroPrevisaoTO = criarCentroFinanceiroPrevisao(to, competencia);
			}

			resultado.add(centroFinanceiroPrevisaoTO);
		}

		// Preencher OS Inicial
		if (resultado != null && !resultado.isEmpty()) {
			for (CentroFinanceiroPrevisaoTO to : resultado) {
				BigDecimal osInicial = calcularOSInicial(to.getCentroFinanceiro().getCodigo());
				to.setValorOSInicial(osInicial);
			}
		}

		return resultado;
	}

	@Transactional
	private CentroFinanceiroPrevisaoTO criarCentroFinanceiroPrevisao(CentroFinanceiroTO centroFinanceiroTO, String competencia) {
		CentroFinanceiroPrevisao cfp = new CentroFinanceiroPrevisao();
		cfp.setCompetencia(competencia);
		cfp.setValorContratual(BigDecimal.ZERO);
		cfp.setValorExtra(BigDecimal.ZERO);

		CentroFinanceiro centroFinanceiro = getMapper().map(centroFinanceiroTO, CentroFinanceiro.class);

		cfp.setCentroFinanceiro(centroFinanceiro);
		cfp.setCriador(getUser().getUsername());
		cfp.setDataCriacao(new Date());

		cfp = getRepository().save(cfp);
		return getMapper().map(cfp, CentroFinanceiroPrevisaoTO.class);
	}

	@Override
	public CentroFinanceiroPrevisaoTO findByCodCentroFinanceiroAndCompetencia(Long codigoCentroFinanceiro, String competencia) {
		JPAQuery<CentroFinanceiroPrevisao> jpaQuery = select();

		QBean qBean = Projections.bean(
			CentroFinanceiroPrevisao.class,
			qCentroFinanceiroPrevisao.codigo,
			qCentroFinanceiroPrevisao.competencia,
			qCentroFinanceiroPrevisao.valorContratual,
			qCentroFinanceiroPrevisao.valorExtra,
			Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiroPrevisao.centroFinanceiro.codigo,
				qCentroFinanceiroPrevisao.centroFinanceiro.identificadorFinal
			).as(qCentroFinanceiroPrevisao.centroFinanceiro)
		);

		jpaQuery
			.select(qBean)
			.from(qCentroFinanceiroPrevisao)
			.where(qCentroFinanceiroPrevisao.centroFinanceiro.codigo.eq(codigoCentroFinanceiro).and(qCentroFinanceiroPrevisao.competencia.eq(competencia)));

		CentroFinanceiroPrevisao centroFinanceiroPrevisao = jpaQuery.fetchOne();

		CentroFinanceiroPrevisaoTO resultado = null;

		if (centroFinanceiroPrevisao != null) {
			resultado = getMapper().map(centroFinanceiroPrevisao, CentroFinanceiroPrevisaoTO.class);
		}

		return resultado;
	}

	@Override
	public BigDecimal calcularOSInicial(Long codigoCentroFinanceiro) {
		BigDecimal resultado = BigDecimal.ZERO;
		FechoTO ultimoFecho = fechoService.getUltimoFechoByCodCentroFinanceiro(codigoCentroFinanceiro);

		if (ultimoFecho != null && ultimoFecho.getCodigo() != null) {
			BigDecimal fMaoObra = ultimoFecho.getValorMaoObra();
			BigDecimal fGastosGerais = ultimoFecho.getValorGastosGerais();
			BigDecimal fMaterial = ultimoFecho.getValorMaterial();
			BigDecimal fServicoTerceiro = ultimoFecho.getValorServicoTerceiro();
			BigDecimal fBdiBeneficios = ultimoFecho.getPercentualBdiBeneficios();
			BigDecimal fBdiDespesas = ultimoFecho.getPercentualBdiDespesas();

			final BigDecimal CEM = new BigDecimal(100);

			BigDecimal num1 = fMaoObra.add(fGastosGerais).add(fMaterial).add(fServicoTerceiro);

			BigDecimal num2 = (fMaoObra.add(fGastosGerais).add(fMaterial).add(fServicoTerceiro))
				.multiply(fBdiBeneficios.add(fBdiDespesas).divide(CEM))
				.divide(BigDecimal.ONE.subtract(fBdiBeneficios.add(fBdiDespesas).divide(CEM)), 2, RoundingMode.HALF_EVEN);

			BigDecimal num3_1 = num1.add(num2);

			List<CentroFinanceiroAliquotaTO> listaCfaTO = null;

			try {
				listaCfaTO = centroFinanceiroAliquotaService.getListByCodCentroFinanceiro(String.valueOf(codigoCentroFinanceiro));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			BigDecimal num3_2 = BigDecimal.ZERO;

			if (listaCfaTO != null && !listaCfaTO.isEmpty()) {
				for (CentroFinanceiroAliquotaTO cfaTO : listaCfaTO) {
					num3_2 = num3_2.add(cfaTO.getAliquota().getPercentual());
				}
			}

			num3_2 = num3_2.divide(CEM);
			BigDecimal num3_3 = BigDecimal.ONE.subtract(num3_2);
			BigDecimal num3 = num3_1.multiply(num3_2).divide(num3_3, 2, RoundingMode.HALF_EVEN);

			resultado = num1.add(num2).add(num3);
		}

		return resultado;
	}

	@Override
	@Transactional
	public void updatePrevisoes(List<CentroFinanceiroPrevisaoTO> lista) {
		if (lista != null && !lista.isEmpty()) {
			for (CentroFinanceiroPrevisaoTO to : lista) {
				CentroFinanceiroPrevisao original = getRepository().findById(to.getCodigo()).get(); //fixme: usar projeção

				Boolean update = Boolean.FALSE;

				if (to.getValorContratual() == null) {
					to.setValorContratual(BigDecimal.ZERO);
				}
				if (to.getValorExtra() == null) {
					to.setValorExtra(BigDecimal.ZERO);
				}

				if (original.getValorContratual().compareTo(to.getValorContratual()) != 0) {
					update = Boolean.TRUE;
					this.updateValorContratual(to.getCodigo(), to.getValorContratual());
				}

				if (original.getValorExtra().compareTo(to.getValorExtra()) != 0) {
					update = Boolean.TRUE;
					this.updateValorExtra(to.getCodigo(), to.getValorExtra());
				}

				if (update) {
					this.updateDataAtualizacao(to.getCodigo());
				}
			}
		}
	}

	@Transactional
	public void update(CentroFinanceiroPrevisaoTO centroFinanceiroPrevisaoTO) {
		updateCodigoCentroFinanceiro(centroFinanceiroPrevisaoTO.getCodigo(), centroFinanceiroPrevisaoTO.getCentroFinanceiro().getCodigo());
		updateDataAtualizacao(centroFinanceiroPrevisaoTO.getCodigo());
		updateCompetencia(centroFinanceiroPrevisaoTO.getCodigo(), centroFinanceiroPrevisaoTO.getCompetencia());
		updateValorContratual(centroFinanceiroPrevisaoTO.getCodigo(), centroFinanceiroPrevisaoTO.getValorContratual());
		updateValorExtra(centroFinanceiroPrevisaoTO.getCodigo(), centroFinanceiroPrevisaoTO.getValorExtra());
	}

	@Override
	@Transactional
	public void updateDataAtualizacao(Long codigoCentroFinanceiroPrevisao) {
		selectUpdate()
			.set(qCentroFinanceiroPrevisao.dataAtualizacao, new Date())
			.set(qCentroFinanceiroPrevisao.atualizador, getUser().getUsername())
			.where(qCentroFinanceiroPrevisao.codigo.eq(codigoCentroFinanceiroPrevisao)).execute();
	}

	@Override
	@Transactional
	public void updateCompetencia(Long codigoCentroFinanceiroPrevisao, String competencia) {
		selectUpdate()
			.set(qCentroFinanceiroPrevisao.competencia, competencia)
			.where(qCentroFinanceiroPrevisao.codigo.eq(codigoCentroFinanceiroPrevisao)).execute();
	}

	@Override
	@Transactional
	public void updateValorContratual(Long codigoCentroFinanceiroPrevisao, BigDecimal valorContratual) {
		selectUpdate()
			.set(qCentroFinanceiroPrevisao.valorContratual, valorContratual)
			.where(qCentroFinanceiroPrevisao.codigo.eq(codigoCentroFinanceiroPrevisao)).execute();
	}

	@Override
	@Transactional
	public void updateValorExtra(Long codigoCentroFinanceiroPrevisao, BigDecimal valorExtra) {
		selectUpdate()
			.set(qCentroFinanceiroPrevisao.valorExtra, valorExtra)
			.where(qCentroFinanceiroPrevisao.codigo.eq(codigoCentroFinanceiroPrevisao)).execute();
	}

	@Override
	@Transactional
	public void updateCodigoCentroFinanceiro(Long codigoCentroFinanceiroPrevisao, Long CodigoCentroFinanceiro) {
		CentroFinanceiro centroFinanceiro = (CentroFinanceiro) centroFinanceiroService.jPAQueryObterCentroFinanceiroSimple(CodigoCentroFinanceiro).fetchOne();
		selectUpdate()
			.set(qCentroFinanceiroPrevisao.centroFinanceiro, centroFinanceiro)
			.where(qCentroFinanceiroPrevisao.codigo.eq(codigoCentroFinanceiroPrevisao)).execute();
	}

	@Override
	@Transactional
	public void updateCentroFinanceiro(Long codigoCentroFinanceiroPrevisao, CentroFinanceiroTO centroFinanceiro) {
		updateCodigoCentroFinanceiro(codigoCentroFinanceiroPrevisao, centroFinanceiro.getCodigo());
	}
}
