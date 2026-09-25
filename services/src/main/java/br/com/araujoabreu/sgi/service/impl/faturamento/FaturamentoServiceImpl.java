package br.com.araujoabreu.sgi.service.impl.faturamento;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoContato;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoRetencao;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoRetencao;
import br.com.araujoabreu.sgi.faturamento.domain.TipoCobrancaFaturamento;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.SituacaoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.contaCorrente.ContaCorrenteService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoAliquotaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoRetencaoService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.gerencia.GerenciaService;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaEmissaoService;
import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.*;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaVencimentoService;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaVencimentoTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.administracao.SituacaoRepository;
import br.com.fasolti.sgi.repository.administracao.TipoRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.compras.NotaFiscalRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoAliquotaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoContatoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRetencaoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import br.com.fasolti.sgi.repository.notaSaida.ServicoTipoRepository;
import br.com.fasolti.sgi.repository.pessoaContaContabil.PessoaContaContabilRepository;
import br.com.fasolti.sgi.repository.tipoCobranca.TipoCobrancaFaturamentoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FaturamentoServiceImpl extends BaseSearchService<Faturamento, FaturamentoTO, FaturamentoPesquisaTO, FaturamentoRepository> implements FaturamentoService {

	private static final QFaturamento qFaturamento = QFaturamento.faturamento;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private FaturamentoAliquotaRepository faturamentoAliquotaRepository;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private FaturamentoRetencaoService faturamentoRetencaoService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private ContaCorrenteService contaCorrenteService;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private CategoriaTributoRepository categoriaTributoRepository;

	@Autowired
	private NotaTributoAliquotaEmissaoService notaTributoAliquotaEmissaoService;

	@Autowired
	private NotaTributoAliquotaService notaTributoAliquotaService;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Autowired
	private FaturamentoRetencaoRepository faturamentoRetencaoRepository;

	@Autowired
	private TipoRepository tipoRepository;

	@Autowired
	private SituacaoRepository situacaoRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private PessoaContaContabilRepository pessoaContaContabilRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private GerenciaService gerenciaService;

	@Autowired
	private ServicoTipoRepository servicoTipoRepository;

	@Autowired
	private TipoCobrancaFaturamentoRepository tipoCobrancaFaturamentoRepository;

	@Autowired
	private TipoCobrancaVencimentoService tipoCobrancaVencimentoService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private FaturamentoAliquotaService faturamentoAliquotaService;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private FaturamentoContatoRepository faturamentoContatoRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	public FaturamentoServiceImpl(FaturamentoRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<Faturamento> getServiceFilter() {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		return ServiceFilter.create(qFaturamento, booleanBuilder);
	}

	@Override
	public NotaDebitoTO findNotaDebitoFaturamento(FaturamentoEtapaTO etapa) throws Exception {
		return null;
	}

	@Override
	public NotaFiscalExibicaoTO findNotaFiscalFaturamento(FaturamentoEtapaTO etapa) throws Exception {
		if (etapa == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "Etapa de faturamento não pode ser nula.");
		}
		if (etapa.getCodigo() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O código da etapa de faturamento não pode ser nulo.");
		}
		if (etapa.getNotaSaida() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "Nota de saída é um campo de preenchimento obrigatório.");
		}
		if (etapa.getNotaSaida().getCodigo() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O código da nota de saída não pode ser nulo.");
		}
		if (etapa.getNotaSaida().getNumero() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O número da nota de saída é um campo de preenchimento obrigatório.");
		}
		if (etapa.getFaturamento() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O faturamento da etapa não pode ser nulo.");
		}
		if (etapa.getFaturamento().getCodigo() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O código do faturamento da etapa não pode ser nulo.");
		}
		if (etapa.getValor() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O valor da etapa de faturamento é um campo de preenchimento obrigatório.");
		}
		if (etapa.getNumero() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "O número da etapa de faturamento é um campo de preenchimento obrigatório.");
		}
		if (etapa.getDescricao() == null) {
			throw new ValidationException("find-nota-fiscal-faturamento", "A descrição da etapa de faturamento é um campo de preenchimento obrigatório.");
		}
		try {
			return montarNotaFiscalFaturamento(etapa);

		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Iterable<NotaFiscalExibicaoTO> montarNotaFiscalFaturamento(FaturamentoTO exampleObj, PageRequest page) {
		JPAQuery<NotaSaida> jpaQuery = (JPAQuery<NotaSaida>) faturamentoEtapaService.montarNotaFiscalFaturamento(exampleObj, page);

		long count = jpaQuery.fetchCount();

		List<NotaSaida> notas = jpaQuery.fetch();

		List<NotaFiscalExibicaoTO> result = new ArrayList<>();

		for (NotaSaida notaSaida : notas) {
			result.add(montarNotaFiscalFaturamento(notaSaida, exampleObj.getCodigo()));
		}

		return new PageImpl(result, page, count);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Iterable<NotaDebitoTO> montarNotaDebitoFaturamento(FaturamentoTO exampleObj, PageRequest page) {
		JPAQuery<NotaDebito> jpaQuery = (JPAQuery<NotaDebito>) faturamentoEtapaService.montarNotaDebitoFaturamento(exampleObj, page);

		long count = jpaQuery.fetchCount();

		List<NotaDebito> notasdebito = jpaQuery.fetch();

		return new PageImpl(getMapper().map(notasdebito, NotaDebitoTO.class), page, count);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	private NotaFiscalExibicaoTO montarNotaFiscalFaturamento(NotaSaida notaSaida, Long codigoFaturamento) {
		NotaFiscalExibicaoTO notaExibicao = new NotaFiscalExibicaoTO();

		JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaida(notaSaida.getCodigo());
		Boolean recebParcial = false;

		List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

		BigDecimal somaMultaJuros = BigDecimal.ZERO;
		BigDecimal somaMulta = BigDecimal.ZERO;
		BigDecimal somaJuros = BigDecimal.ZERO;
		BigDecimal somaValorPagar = BigDecimal.ZERO;
		BigDecimal somaValorPago = BigDecimal.ZERO;
		BigDecimal somaDescontos = BigDecimal.ZERO;
		BigDecimal somaValorPermutas = BigDecimal.ZERO;
		BigDecimal somaValorRetencoes = BigDecimal.ZERO;
		BigDecimal valorTributos = BigDecimal.ZERO;
		BigDecimal valorNota = BigDecimal.ZERO;

		for (NotaSaidaParcela parcela : parcelas) {
			somaMultaJuros = somaMultaJuros.add(parcela.getValorMulta()).add(parcela.getValorJuros());
			somaMulta = somaMulta.add(parcela.getValorMulta());
			somaJuros = somaJuros.add(parcela.getValorJuros());
			somaValorPagar = somaValorPagar.add(parcela.getValorBruto());
			somaDescontos = somaDescontos.add(parcela.getValorDesconto());
			somaValorPermutas = somaValorPermutas.add(parcela.getValorPermuta());
			valorNota = valorNota.add(parcela.getValorBruto());

			if (parcela.getSituacao() == NotaSaidaParcela.SituacaoParcela.FINALIZADA) {
				somaValorPago = somaValorPago.add(parcela.getValorLiquido());
				recebParcial = true;
			}

			//notaExibicao.setSituacao(parcela.getSituacao().toString());
		}

		if(recebParcial && parcelas.size() > 1){
			notaExibicao.setSituacao("Receb. parcial");
		}else{
			if(notaSaida.getStatus() == Nota.StatusNota.CANCELADA_FATURAMENTO){
				notaExibicao.setSituacao("Cancelada");
			}else {
				notaExibicao.setSituacao(notaSaida.getStatus().toString());
			}
		}

		List<FaturamentoRetencaoTO> listaRetencoes =
			faturamentoRetencaoService.obterPercentualListaPorCodigoFaturamento(codigoFaturamento);

		BigDecimal percentualTotal = new BigDecimal("100");
		for (FaturamentoRetencaoTO retencao : listaRetencoes) {
			BigDecimal valorRetencao = (valorNota.multiply(retencao.getPercentual())).divide(percentualTotal);
			somaValorRetencoes = somaValorRetencoes.add(valorRetencao);
		}

		FaturamentoEtapa faturamentoEtapa =
			(FaturamentoEtapa) faturamentoEtapaService.obterFaturamentoEtapaMinimoListaCodigoNotaSaida(notaSaida.getCodigo()).fetchOne();

		//valorTributos = (valorServicoDaEtapa * percentualTributos) / 100
		valorTributos = getTotalValorTributoNota(codigoFaturamento, notaSaida.getCodigo());

		// valorAPagar = somaDoValorDasParcelas - valorTributos
		notaExibicao.setValorPagar(somaValorPagar.subtract(valorTributos));
		notaExibicao.setValorPermuta(somaValorPermutas);
		notaExibicao.setValorRetencoes(somaValorRetencoes);
		notaExibicao.setMultaJuros(somaMultaJuros);
		notaExibicao.setDescontos(somaDescontos);
		notaExibicao.setValorPago(somaValorPago);
		notaExibicao.setTributos(valorTributos);
		notaExibicao.setMulta(somaMulta);
		notaExibicao.setJuros(somaJuros);
		notaExibicao.setValorBruto(valorNota);

		notaExibicao.setCodigoNota(notaSaida.getCodigo());
		notaExibicao.setCodigoEtapa(faturamentoEtapa.getCodigo());
		notaExibicao.setEtapa(String.valueOf(faturamentoEtapa.getNumero()));
		notaExibicao.setDescricaoItem(faturamentoEtapa.getDescricao());
		notaExibicao.setNumero(notaSaida.getNumero());
		notaExibicao.setNumParcelas(parcelas.size());
		notaExibicao.setDataEmissao(notaSaida.getDataEmissao());

		return notaExibicao;
	}

	/**
	 * Cria um objeto NotaFiscalExibicaoTO para ser exibido na tabela da aba de notas fiscais.
	 *
	 * @param etapa FaturamentoEtapaTO
	 * @return NotaFiscalExibicaoTO
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	protected NotaFiscalExibicaoTO montarNotaFiscalFaturamento(FaturamentoEtapaTO etapa) {
		NotaFiscalExibicaoTO notaExibicao = new NotaFiscalExibicaoTO();

		JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaida(etapa.getNotaSaida().getCodigo());

		List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

		notaExibicao.setCodigoNota(etapa.getNotaSaida().getCodigo());
		notaExibicao.setCodigoEtapa(etapa.getCodigo());
		notaExibicao.setEtapa(etapa.getNumero());
		notaExibicao.setDescricaoItem(etapa.getDescricao());
		notaExibicao.setNumero(etapa.getNotaSaida().getNumero());
		notaExibicao.setNumParcelas(parcelas.size());

		BigDecimal somaMultaJuros = BigDecimal.ZERO;
		BigDecimal somaMulta = BigDecimal.ZERO;
		BigDecimal somaJuros = BigDecimal.ZERO;
		BigDecimal somaValorPagar = BigDecimal.ZERO;
		BigDecimal somaValorPago = BigDecimal.ZERO;
		BigDecimal somaDescontos = BigDecimal.ZERO;
		BigDecimal somaValorPermutas = BigDecimal.ZERO;
		BigDecimal somaValorRetencoes = BigDecimal.ZERO;
		BigDecimal valorTributos = BigDecimal.ZERO;
		BigDecimal valorNota = BigDecimal.ZERO;

		for (NotaSaidaParcela parcela : parcelas) {
			somaMultaJuros = somaMultaJuros.add(parcela.getValorMulta()).add(parcela.getValorJuros());
			somaMulta = somaMulta.add(parcela.getValorMulta());
			somaJuros = somaJuros.add(parcela.getValorJuros());
			somaValorPagar = somaValorPagar.add(parcela.getValorBruto());
			somaDescontos = somaDescontos.add(parcela.getValorDesconto());
			somaValorPermutas = somaValorPermutas.add(parcela.getValorPermuta());
			valorNota = valorNota.add(parcela.getValorBruto());

			if (parcela.getSituacao() == NotaSaidaParcela.SituacaoParcela.FINALIZADA) {
				somaValorPago = somaValorPago.add(parcela.getValorBruto());
			}

			notaExibicao.setSituacao(parcela.getSituacao().toString());
		}

		Faturamento faturamento = getMapper().map(etapa.getFaturamento(), Faturamento.class);
		List<FaturamentoRetencao> listaRetencoes = faturamentoRetencaoRepository.findByFaturamento(faturamento);

		BigDecimal percentualTotal = new BigDecimal("100");
		for (FaturamentoRetencao retencao : listaRetencoes) {
			BigDecimal valorRetencao = (valorNota.multiply(retencao.getPercentual())).divide(percentualTotal);
			somaValorRetencoes = somaValorRetencoes.add(valorRetencao);
		}

		//valorTributos = (valorServicoDaEtapa * percentualTributos) / 100
		valorTributos = getTotalValorTributoNota(etapa.getFaturamento().getCodigo(), etapa.getNotaSaida().getCodigo());

		// valorAPagar = somaDoValorDasParcelas - valorTributos
		notaExibicao.setValorPagar(somaValorPagar.subtract(valorTributos));
		notaExibicao.setValorPermuta(somaValorPermutas);
		notaExibicao.setValorRetencoes(somaValorRetencoes);
		notaExibicao.setMultaJuros(somaMultaJuros);
		notaExibicao.setDescontos(somaDescontos);
		notaExibicao.setValorPago(somaValorPago);
		notaExibicao.setTributos(valorTributos);
		notaExibicao.setMulta(somaMulta);
		notaExibicao.setJuros(somaJuros);
		notaExibicao.setValorBruto(valorNota);

		return notaExibicao;
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Faturamento> query, FaturamentoPesquisaTO exampleObj) {
		QPessoa qPessoa = QPessoa.pessoa;
		query.join(qFaturamento.cliente, qPessoa).orderBy(qFaturamento.codigo.desc());

		query.select(Projections.fields(
			Faturamento.class,
			qFaturamento.codigo,
			qFaturamento.situacao,
			qFaturamento.tipo,
			qFaturamento.identificacao,
			qFaturamento.periodo,
			qFaturamento.tipoFornecimento,
			qFaturamento.valor,
			qFaturamento.tipoDocumentoFaturamento,
			Projections.bean(PessoaJuridica.class, qFaturamento.cliente.codigo, qFaturamento.cliente.nome, qFaturamento.cliente.documento).as("cliente"),
			Projections.bean(CentroFinanceiro.class, qFaturamento.centroFinanceiro.codigo, qFaturamento.centroFinanceiro.identificador, qFaturamento.centroFinanceiro.identificadorFinal, qFaturamento.centroFinanceiro.nome,
				Projections.bean(Contrato.class, qFaturamento.centroFinanceiro.contrato.codigo).as("contrato")).as("centroFinanceiro")
		));

		if (Check.isAssigned(exampleObj.getDescricaoItem())) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
			query.join(qFaturamento.faturamentoEtapas, qFaturamentoEtapa);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FaturamentoPesquisaTO exampleObj) {
		Faturamento f = getMapper().map(exampleObj, Faturamento.class);

		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(qFaturamento.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qFaturamento.situacao.eq(f.getSituacao()));
		}

		if (Check.isAssigned(exampleObj.getTipo())) {
			builder.and(qFaturamento.tipo.eq(f.getTipo()));
		}

		if (Check.isAssigned(exampleObj.getIdentificacao())) {
			builder.and(qFaturamento.identificacao.upper().contains(exampleObj.getIdentificacao().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getNomeCliente())) {
			QPessoaJuridica qPessoaJuridica = qFaturamento.cliente.as(QPessoaJuridica.class);
			builder.and(qFaturamento.cliente.nome.upper().contains(exampleObj.getNomeCliente().toUpperCase().trim())
				.or(qPessoaJuridica.nomeFantasia.upper().contains(exampleObj.getNomeCliente().toUpperCase().trim()))
				.or(qFaturamento.cliente.documento.upper().contains(exampleObj.getNomeCliente().toUpperCase().trim())));
		}

		if (Check.isAssigned(exampleObj.getNumCentroFinanceiro())) {
			builder.and(qFaturamento.centroFinanceiro.identificador.contains(exampleObj.getNumCentroFinanceiro()));
		}

		if (Check.isAssigned(exampleObj.getTotalDe())) {
			builder.and(qFaturamento.valor.goe(exampleObj.getTotalDe()));
		}

		if (Check.isAssigned(exampleObj.getTotalAte())) {
			builder.and(qFaturamento.valor.loe(exampleObj.getTotalAte()));
		}

		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			builder.and(qFaturamento.centroFinanceiro.contrato.codigo.eq(exampleObj.getCodigoContrato()));
		}

		if (exampleObj.getDescricaoItem() != null && !exampleObj.getDescricaoItem().isEmpty()) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
			builder.and(qFaturamentoEtapa.descricao.upper().trim().contains(exampleObj.getDescricaoItem().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getCliente())) {
			builder.and(qFaturamento.cliente.codigo.eq(exampleObj.getCliente().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getTipoFornecimento())) {
			builder.and(qFaturamento.tipoFornecimento.eq(f.getTipoFornecimento()));
		}

		if (Check.isAssigned(exampleObj.getCodigoCidadePrestacaoServico())) {
			builder.and(qFaturamento.cidadePrestacaoServico.codigo.eq(exampleObj.getCodigoCidadePrestacaoServico()));
		}
	}

	public MenssagemPadraoTO possuiContaContabilAssociada(Long codigoFaturamento) {
		Faturamento faturamento = faturamentoRepository.findById(codigoFaturamento).orElse(null);
		MenssagemPadraoTO menssagemPadraoTO = new MenssagemPadraoTO();

		Long codigoCliente = faturamento.getCliente() != null ?
			faturamento.getCliente().getCodigo() : 0l;
		Long codigoUnidade = faturamento.getCentroFinanceiro().getUnidade().getMatriz().getCodigo();
		List<PessoaContaContabil> contasContabeis = pessoaContaContabilRepository.findByPessoaCodigo(codigoCliente);

		if(!contasContabeis.isEmpty()){
			for (PessoaContaContabil pessoa: contasContabeis) {
				if (pessoa.getTipoEspecial().equals(PessoaContaContabil.TipoRelacionamento.CLIENTE) && pessoa.getUnidade().getCodigo().equals(codigoUnidade)){
					return menssagemPadraoTO;
				}
			}
		}
		menssagemPadraoTO.addWarnings(
			String.format("Este cliente não possui conta contábil associada para a empresa '%s'. É necessário informar a Contabilidade sobre o cadastro para evitar problemas nos lotes contábeis.", faturamento.getCentroFinanceiro().getUnidade().getCodnome()));
		return menssagemPadraoTO;
	}

	@Override
	protected void doAfterSearch(List<Faturamento> entities, List<FaturamentoPesquisaTO> result) throws ServiceException {
		result.forEach(faturamentoPesquisaTO -> {
			JPAQuery jpaQuery = pessoaJuridicaService.jpaQueryPessoaJuridica();
			QPessoaJuridica qPessoaJuridica = QPessoaJuridica.pessoaJuridica;
			jpaQuery
				.from(qPessoaJuridica)
				.select(Projections.fields(PessoaJuridica.class, qPessoaJuridica.codigo, qPessoaJuridica.nomeFantasia))
				.where(qPessoaJuridica.codigo.eq(faturamentoPesquisaTO.getCliente().getCodigo()));
			PessoaJuridica pessoaJuridica = (PessoaJuridica) jpaQuery.fetchOne();
			if (pessoaJuridica != null) {
				faturamentoPesquisaTO.setNomeCliente(pessoaJuridica.getNomeFantasia());
			}
		});
	}

	@Transactional()
	@Override
	protected void doBeforeDelete(FaturamentoTO object) throws ServiceException {
		if (object.getId() != null) {
			JPAQuery jpaQuery = select();
			jpaQuery.from(QFaturamento.faturamento);
			jpaQuery.select(Projections.fields(Faturamento.class, QFaturamento.faturamento.codigo, QFaturamento.faturamento.situacao));
			jpaQuery.where(QFaturamento.faturamento.codigo.eq(object.getId()));
			Faturamento faturamento = (Faturamento) jpaQuery.fetchOne();
			if (faturamento != null) {
				if (!Faturamento.Status.ABERTO.equals(faturamento.getSituacao())) {
					String msg = String.format("Faturamento %s não pode ser removido. Status: %s.", faturamento.getNumero(), faturamento.getSituacao().getDescricao());
					throw new ValidationException("do-before-delete-faturamento", msg);
				}
				faturamentoAliquotaService.deletarFaturamentoAliquota(faturamento.getCodigo());
				List<FaturamentoRetencao> faturamentoRetencoes = faturamentoRetencaoRepository.findByFaturamento(faturamento);
				for (FaturamentoRetencao faturamentoRetencao : faturamentoRetencoes) {
					faturamentoRetencaoRepository.deleteById(faturamentoRetencao.getCodigo());
				}
				List<FaturamentoContato> faturamentoContatos = faturamentoContatoRepository.findByFaturamento(faturamento);
				for (FaturamentoContato faturamentoContato : faturamentoContatos) {
					faturamentoContatoRepository.deleteById(faturamentoContato.getCodigo());
				}
			}
		}
	}

	@Override
	protected FaturamentoTO doAfterCreate(FaturamentoTO to) {
		to.setSituacao(Faturamento.Status.ABERTO.toString());
		to.setDataCriacao(new Date());
		to.setCriador(securityService.getUsername());
		criarRetencoes(to);
		return to;
	}

	@Override
	protected void doAfterLoad(FaturamentoTO to) {
		changeClienteSubcliente(to);
		Long cod_gerencia;

		if (to != null && to.getCriador() != null) {
			to.setNomeCriador(pessoaFisicaService.nomeUsuarioPorLogin(to.getCriador()));
		}

		if (to != null && to.getAtualizador() != null) {
			to.setNomeAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(to.getAtualizador()));
		}

		if (to != null) {
			List<FaturamentoRetencaoTO> listTOs = faturamentoRetencaoService.obterListaPorCodigoFaturamento(to.getCodigo());

			to.setFaturamentoRetencoes(listTOs);

			if (to.getFaturamentoRetencoes().isEmpty()) {
				criarRetencoes(to);
			}
		}

		if (to != null && to.getCentroFinanceiro() != null) {
			if (to.getCentroFinanceiro().getPai() != null) {
				cod_gerencia = to.getCentroFinanceiro().getPai().getCodigo();
			} else {
				cod_gerencia = to.getCentroFinanceiro().getCodigo();
			}

			GerenciaTO gerencia = gerenciaService.obterGerencia(cod_gerencia);
			to.setGerencia(gerencia.getNome());
			to.setGerente(gerencia.getGerente().getNome());
		}
	}

	@Override
	@Transactional
	protected void doAfterUpdate(Faturamento entity, FaturamentoTO to) throws ServiceException {
		atualizarRetencoes(entity, to);
		List<FaturamentoRetencaoTO> listFaturamentoRetencao = faturamentoRetencaoService.obterListaPorCodigoFaturamento(to.getCodigo());
		to.setFaturamentoRetencoes(getMapper().map(listFaturamentoRetencao, FaturamentoRetencaoTO.class));
	}

	@Override
	@Transactional
	protected void doAfterSave(Faturamento entity, FaturamentoTO to) {
		if (to.getFaturamentoAliquotas() != null && !Check.isEmpty(to.getFaturamentoAliquotas())) {
			entity.setFaturamentoAliquotas(new ArrayList<>());
			for (FaturamentoAliquotaTO faturamentoAliquotaTO : to.getFaturamentoAliquotas()) {
				FaturamentoAliquota faturamentoAliquota = getMapper().map(faturamentoAliquotaTO, FaturamentoAliquota.class);
				faturamentoAliquotaTO.setCodigo(null);
				faturamentoAliquota.setFaturamento(entity);
				faturamentoAliquotaRepository.save(faturamentoAliquota);
			}
		}
		atualizarRetencoes(entity, to);
		List<FaturamentoRetencaoTO> listFaturamentoRetencao = faturamentoRetencaoService.obterListaPorCodigoFaturamento(to.getCodigo());
		entity.setFaturamentoRetencoes(getMapper().map(listFaturamentoRetencao, FaturamentoRetencao.class));
	}

	@Override
	@Transactional
	protected void doBeforeUpdate(Faturamento entity, FaturamentoTO to) throws ServiceException {
		// Verificar status de permissão de atualização.
		checkStatusToUpdate(to);
		prepareValidateToSave(to);
		to.setDataAtualizacao(new Date());
		to.setAtualizador(securityService.getUsername());

		if (to.getSubcliente() != null && to.getSubcliente().getCodigo() != null) {
			to.setSubcliente(pessoaService.obterPessoa(to.getSubcliente().getCodigo()));
		}

		if (to.getCliente() != null) {
			to.setCliente(pessoaService.obterPessoa(to.getCliente().getCodigo()));
		}

		faturamentoAliquotaService.deletarFaturamentoAliquota(to.getCodigo());
		faturamentoRetencaoService.deleteByFaturamentoCodigo(to.getCodigo());

		if (to.getCliente() != null && to.getSubcliente() != null) {
			if (to.getSubcliente().getCodigo() != null) {
				to.setCliente(to.getSubcliente());
			}
		}

		populateFaturamento(entity, to);

		if (to.getServicoTipo() != null) {
			entity.setServicoTipo(servicoTipoRepository.findById(to.getServicoTipo().getCodigo()).orElse(null));
		}
		else
			entity.setServicoTipo(null);

		if (entity.getFaturamentoAliquotas() != null && !Check.isEmpty(entity.getFaturamentoAliquotas())) {
			for (FaturamentoAliquota entityAliq : entity.getFaturamentoAliquotas()) {
				entityAliq.setCodigo(null);
				faturamentoAliquotaRepository.save(entityAliq);
			}
		}
	}

	@Override
	@Transactional
	protected void doBeforeSave(Faturamento entity, FaturamentoTO to) throws ServiceException {
//		possuiContaContabilAssociadaAhMatrizDoCL(entity.getCentroFinanceiro());

		//quando cria está setando os dados do cancelamento, que são do faturamento anterior. Nao é o caso, portanto está sendo feito isso
		to.setCancelador(null);
		to.setDataCancelamento(null);
		to.setAtualizador(null);
		to.setDataAtualizacao(null);
		prepareValidateToSave(to);

		populateFaturamento(entity, to);
	}

	@Transactional
	private void atualizarRetencoes(Faturamento entity, FaturamentoTO to) {
		if (to.getFaturamentoRetencoes() != null && !to.getFaturamentoRetencoes().isEmpty()) {
			entity.setFaturamentoRetencoes(new ArrayList<>());
			for (FaturamentoRetencaoTO faturamentoRetencaoTO : to.getFaturamentoRetencoes()) {
				FaturamentoRetencao faturamentoRetencao = getMapper().map(faturamentoRetencaoTO, FaturamentoRetencao.class);
				faturamentoRetencao.setCodigo(null);
				faturamentoRetencao.setFaturamento(entity);
				if (faturamentoRetencao.getPercentual() == null) {
					faturamentoRetencao.setPercentual(new BigDecimal(0.0d));
				}
				entity.getFaturamentoRetencoes().add(faturamentoRetencaoRepository.save(faturamentoRetencao));
			}
		}
	}

	/**
	 * Verificar os status que podem ser permitido atualizar o faturamento.
	 *
	 * @param faturamentoTO O objeto a ser verificado o status.
	 * @throws ServiceException Caso seja um status diferente do permitido para atualização.
	 */
	public void checkStatusToUpdate(FaturamentoTO faturamentoTO) throws ServiceException {
		if (!(Faturamento.Status.ABERTO.equals(Util.getEnumByString(Faturamento.Status.class, faturamentoTO.getSituacao()))
			|| Faturamento.Status.EM_FATURAMENTO.equals(Util.getEnumByString(Faturamento.Status.class, faturamentoTO.getSituacao())))) {
			throw new ValidationException("check-status-to-update - faturamento", String.format("O status atual do faturamento (%s), não permite atualização.", faturamentoTO.getSituacao()));
		}
	}

	public void prepareValidateToSave(FaturamentoTO faturamentoTO) throws ServiceException {
		// Validações antes de persistir (edição)
		if (faturamentoTO.getCentroFinanceiro() == null || faturamentoTO.getCentroFinanceiro().getCodigo() == null) {
			throw new ValidationException("check-status-to-update - faturamento", "Centro Financeiro é um campo obrigatório");
		}

		Long codigoPessoaCliente = null;
		String nomeTipoCliente = null;
		if (faturamentoTO.getSubcliente() != null && faturamentoTO.getSubcliente().getCodigo() != null) {
			nomeTipoCliente = "Subcliente";
			codigoPessoaCliente = faturamentoTO.getSubcliente().getCodigo();

		} else if (faturamentoTO.getCliente() == null || faturamentoTO.getCliente().getCodigo() == null) {
			throw new ValidationException("check-status-to-update - faturamento", "Cliente é um campo obrigatório");

		} else {
			nomeTipoCliente = "Cliente";
			codigoPessoaCliente = faturamentoTO.getCliente().getCodigo();
		}

		PessoaTO clienteTO = pessoaService.obterPessoa(codigoPessoaCliente);
		if (Pessoa.Situacao.DISABLED.getNome().equals(clienteTO.getSituacao())) {
			throw new ValidationException("check-status-to-update - faturamento", nomeTipoCliente + " não pode ser inativo");
		}

		if (faturamentoTO.getTipo() == null)
			throw new ValidationException("check-status-to-update - faturamento", "Tipo é um campo obrigatório");

		//FIXME: SGIEF-84
//        if (faturamentoTO.getIdentificacao() == null || faturamentoTO.getIdentificacao().isEmpty())
//            throw new ValidationException("ValidationException","Identificação do Centro Financeiro é um campo obrigatório");

		if ((faturamentoTO.getServicoTipo() == null || faturamentoTO.getServicoTipo().getCodigo() == null)
			&& !faturamentoTO.getTipoFornecimento().equalsIgnoreCase(NaturezaFornecimento.TipoFornecimento.MATERIAIS.toString()))
			throw new ValidationException("check-status-to-update - faturamento", "O código do tipo de serviço não pode ser nulo");

		if (faturamentoTO.getValor() == null || faturamentoTO.getValor().isEmpty())
			throw new ValidationException("check-status-to-update - faturamento", "Valor do Faturamento é um campo obrigatório");

		if (faturamentoTO.getPercentualServico() == null)
			throw new ValidationException("check-status-to-update - faturamento", "Percentual de Serviço é um campo obrigatório");

		// Devido a um bug de tela, o valor não vem na edição. "Estranhamente" ao seleciona a aba com o campo "tipo cobrança", o valor vem.
		if (faturamentoTO.getTipoCobrancaFaturamento() == null)
			throw new ValidationException("check-status-to-update - faturamento", "Tipo Cobrança é um campo obrigatório");

		if (Check.isEmpty(faturamentoTO.getResumo()))
			throw new ValidationException("check-status-to-update - faturamento", "Objeto (Faturamento) é um campo obrigatório");

		if (faturamentoTO.getPercentualServico() != null) {
			BigDecimal percentualServico = faturamentoTO.getPercentualServico();
			BigDecimal cem = new BigDecimal(100);
			if (cem.compareTo(percentualServico) < 0)
				throw new ValidationException("check-status-to-update - faturamento", "Percentual de Serviço não pode ser superior a 100%");
		}

		if (faturamentoTO.getContaCorrente() == null || faturamentoTO.getContaCorrente().getCodigo() == null)
			throw new ValidationException("check-status-to-update - faturamento", "Conta Corrente é um campo obrigatório");

		if (faturamentoTO.getDiscriminacaoInicial() == null || faturamentoTO.getDiscriminacaoInicial().isEmpty())
			throw new ValidationException("check-status-to-update - faturamento", "Descriminação inicial é um campo obrigatório");

		if (faturamentoTO.getDiscriminacaoFinal() == null || faturamentoTO.getDiscriminacaoFinal().isEmpty())
			throw new ValidationException("check-status-to-update - faturamento", "Descriminação final é um campo obrigatório");

		if (faturamentoTO.getDiscriminacaoInicial() != null && !faturamentoTO.getDiscriminacaoInicial().isEmpty() &&
			faturamentoTO.getDiscriminacaoInicial().length() > 2000)
			throw new ValidationException("check-status-to-update - faturamento", "Descriminação inicial não pode conter mais de 2000 caracteres");

		if (faturamentoTO.getDiscriminacaoCentral() != null && !faturamentoTO.getDiscriminacaoCentral().isEmpty() &&
			faturamentoTO.getDiscriminacaoCentral().length() > 2000)
			throw new ValidationException("check-status-to-update - faturamento", "Descriminação central não pode conter mais de 2000 caracteres");

		if (faturamentoTO.getDiscriminacaoFinal() != null && !faturamentoTO.getDiscriminacaoFinal().isEmpty() &&
			faturamentoTO.getDiscriminacaoFinal().length() > 2000)
			throw new ValidationException("check-status-to-update - faturamento", "Descriminação final não pode conter mais de 2000 caracteres");

		if (faturamentoTO.getIssRetidoCliente() == null)
			throw new ValidationException("check-status-to-update - faturamento", "ISS será retido pelo cliente é um campo de preenchimento obrigatório.");

		if (faturamentoTO.getTipoDocumentoPagamento() == null)
			throw new ValidationException("check-status-to-update - faturamento", "Tipo de documento de pagamento é um campo de preenchimento obrigatório.");

		if (!Check.isEmpty(faturamentoTO.getFaturamentoAliquotas())) {
			List<Tributo> listaTributos = new ArrayList<>();

			for (FaturamentoAliquotaTO faturamentoAliquotaTO : faturamentoTO.getFaturamentoAliquotas()) {
				if (faturamentoAliquotaTO.getCodigo() != null) {
					Tributo tributo = getTributo(faturamentoAliquotaTO);
					listaTributos.add(tributo);
				}
			}

			for (FaturamentoAliquotaTO faturamentoAliquotaTO : faturamentoTO.getFaturamentoAliquotas()) {
				if (faturamentoAliquotaTO.getBaseCalculo().doubleValue() <= 0L) {
					throw new ValidationException("ValidationException","O valor da base de cálculo do tributo não pode ser zero.");
				}
                /*if (faturamentoAliquotaTO.getBaseCalculo() > Double.parseDouble(faturamentoTO.getValor())) {
                    throw new ValidationException("ValidationException","O valor da base de cálculo do tributo não pode ser maior que o valor de serviço/mão-de-obra do faturamento.");
                }*/
				if (faturamentoAliquotaTO.getValor() == null) {
					throw new ValidationException("check-status-to-update - faturamento", "O valor do tributo não pode ser nulo.");
				}

				Tributo tributoFaturamentoAliquota = getTributo(faturamentoAliquotaTO);

				int qtdTributo = 0;
				for (Tributo tributo : listaTributos) {
					if (Objects.equals(tributo.getCodigo(), tributoFaturamentoAliquota.getCodigo())) {
						qtdTributo++;
					}
				}

				if (qtdTributo >= 2) {
					throw new ValidationException("check-status-to-update - faturamento", "Não pode haver tributos repetidos no faturamento.");
				}
			}
		}

	}

	@Transactional(readOnly = true)
	private void populateFaturamento(Faturamento faturamento, FaturamentoTO faturamentoTO) throws ValidationException {
		List<FaturamentoAliquota> lista = new ArrayList<>();
		if (faturamentoTO.getCidadePrestacaoServico() != null) {
			faturamento.setCidadePrestacaoServico(getMapper().map(faturamentoTO.getCidadePrestacaoServico(), Cidade.class));
		}
		if (!Check.isEmpty(faturamentoTO.getFaturamentoAliquotas())
			&& !faturamentoTO.getTipoFornecimento().equalsIgnoreCase(NaturezaFornecimento.TipoFornecimento.MATERIAIS.toString())) {
			for (FaturamentoAliquotaTO faturamentoAliquotaTO : faturamentoTO.getFaturamentoAliquotas()) {
				FaturamentoAliquota faturamentoAliquota = getMapper().map(faturamentoAliquotaTO, FaturamentoAliquota.class);
				faturamentoAliquota.setFaturamento(faturamento);
				lista.add(faturamentoAliquota);
			}
		}

		faturamento.setFaturamentoAliquotas(lista);
		faturamento.setCentroFinanceiro(centroFinanceiroRepository.findById(faturamentoTO.getCentroFinanceiro().getCodigo()).orElse(null));

		if (faturamentoTO.getSubcliente() != null && faturamentoTO.getSubcliente().getCodigo() != null) {
			faturamento.setCliente(pessoaRepository.findById(faturamentoTO.getSubcliente().getCodigo()).orElse(null));
		} else {
			faturamento.setCliente(pessoaRepository.findById(faturamentoTO.getCliente().getCodigo()).orElse(null));
		}
	}

	@Override
	public void changeClienteSubcliente(FaturamentoTO obj) {
		if (obj != null && obj.getCliente() != null && obj.getCliente().getCodigo() != null) {
			CentroFinanceiro cf = centroFinanceiroRepository.findById(obj.getCentroFinanceiro().getId()).orElse(null);
			if (obj.getCliente().getClientePrincipal() != null && obj.getCliente().getClientePrincipal().getCodigo() != null && !cf.getPermiteClienteLivre()) {
				obj.setSubcliente(obj.getCliente());
				obj.setCliente(obj.getCliente().getClientePrincipal());
			}
		}
	}

	private Tributo getTributo(FaturamentoAliquotaTO faturamentoAliquotaTO) {
		//FIXME: Melhorar código abaixo e verificar frontend.
		if (faturamentoAliquotaTO.getAliquota() != null && faturamentoAliquotaTO.getAliquota().getCodigoCategoriaTributo() != null) {
			CategoriaTributo categoriaTributo = categoriaTributoRepository.findById(faturamentoAliquotaTO.getAliquota().getCodigoCategoriaTributo()).orElse(null);
			return categoriaTributo.getTributo();
		} else if (faturamentoAliquotaTO.getCodigo() != null) {
			FaturamentoAliquota faturamentoAliquota = faturamentoAliquotaRepository.findById(faturamentoAliquotaTO.getCodigo()).orElse(null);
			if (faturamentoAliquota != null) {
				CategoriaTributo categoriaTributo = categoriaTributoRepository.findById(faturamentoAliquota.getAliquota().getCategoriaTributo().getCodigo()).orElse(null);
				return categoriaTributo.getTributo();
			}
		}
		return null;
	}

	/**
	 * Calcula o total do percentual dos tributos de um faturamento.
	 *
	 * @param codigoFaturamento Código do faturamento
	 * @return BigDecimal com o valor do total do percentual dos tributos
	 */
	private BigDecimal getTotalValorTributoNota(long codigoFaturamento, long codigoNota) {
		BigDecimal total = BigDecimal.ZERO;
		List<NotaTributoAliquotaEmissaoTO> listNotaTributoAliquotaEmissaoTO = notaTributoAliquotaEmissaoService.listarPorCodigoNota(codigoNota);
		boolean issRetido = isIssRetidoCliente(codigoFaturamento);
		if (listNotaTributoAliquotaEmissaoTO != null && !listNotaTributoAliquotaEmissaoTO.isEmpty()) {
			for (NotaTributoAliquotaEmissaoTO fa : listNotaTributoAliquotaEmissaoTO) {
				if (issRetido) {
					total = total.add(fa.getValor());
				} else {
					if (!"ISS".equals(fa.getAliquota().getNomeTributo())) {
						total = total.add(fa.getValor());
					}
				}
			}
		} else {
			List<NotaTributoAliquotaTO> listNotaTributoAliquotaTO = notaTributoAliquotaService.listarPorCodigoNota(codigoNota);
			for (NotaTributoAliquotaTO fa : listNotaTributoAliquotaTO) {
				if (issRetido) {
					total = total.add(fa.getValor());
				} else {
					if (!"ISS".equals(fa.getAliquota().getNomeTributo())) {
						total = total.add(fa.getValor());
					}
				}
			}
		}
		return total;
	}

	private void criarRetencoes(FaturamentoTO to) {
		to.setFaturamentoRetencoes(new ArrayList<>());

		FaturamentoRetencaoTO faturamentoRetencaoContratural = new FaturamentoRetencaoTO();
		faturamentoRetencaoContratural.setPercentual(new BigDecimal(0.0d));
		faturamentoRetencaoContratural.setSituacao(getMapper().map(situacaoRepository.findByNomeAndSubsituacaoAndChave("Receber", "faturamento_retencao", "a_receber"), SituacaoTO.class));
		faturamentoRetencaoContratural.setTipo(getMapper().map(tipoRepository.findByNomeAndTabelaAndSubtipo("Contratual", "faturamento_retencao", "faturamento_retencao"), TipoTO.class));
		to.getFaturamentoRetencoes().add(faturamentoRetencaoContratural);

		FaturamentoRetencaoTO faturamentoRetencaoAcessoria = new FaturamentoRetencaoTO();
		faturamentoRetencaoAcessoria.setPercentual(new BigDecimal(0.0d));
		faturamentoRetencaoAcessoria.setSituacao(getMapper().map(situacaoRepository.findByNomeAndSubsituacaoAndChave("Receber", "faturamento_retencao", "a_receber"), SituacaoTO.class));
		faturamentoRetencaoAcessoria.setTipo(getMapper().map(tipoRepository.findByNomeAndTabelaAndSubtipo("Acessoria", "faturamento_retencao", "faturamento_retencao"), TipoTO.class));
		to.getFaturamentoRetencoes().add(faturamentoRetencaoAcessoria);
	}

	/**
	 * Busca a lista de FaturamentoRetencao de um faturamento.
	 *
	 * @param codFaturamento
	 * @return
	 */
	@Override
	public List<FaturamentoRetencaoTO> findFaturamentoRetencaoByCodFaturamento(Long codFaturamento) {
		QFaturamentoRetencao qFaturamentoRetencao = QFaturamentoRetencao.faturamentoRetencao;
		QTipo qTipo = QTipo.tipo;

		QBean qBean = Projections.bean(
			FaturamentoRetencao.class,
			qFaturamentoRetencao.codigo,
			qFaturamentoRetencao.percentual,
			Projections.bean(Tipo.class,
				qTipo.codigo,
				qTipo.chave,
				qTipo.descricao,
				qTipo.ordem,
				qTipo.subtipo,
				qTipo.tabela).as("tipo")
		);

		JPAQuery jpaQuery = faturamentoRetencaoService.getJpaQueryFaturamentoRetencao();

		jpaQuery
			.select(qBean)
			.join(qFaturamentoRetencao.faturamento, qFaturamento)
			.join(qFaturamentoRetencao.tipo, qTipo)
			.where(qFaturamento.codigo.eq(codFaturamento));

		List<FaturamentoRetencao> lista = jpaQuery.fetch();

		List<FaturamentoRetencaoTO> faturamentoRetencaoTOS = new ArrayList<>();

		for (FaturamentoRetencao faturamentoRetencao : lista) {
			faturamentoRetencaoTOS.add(getMapper().map(faturamentoRetencao, FaturamentoRetencaoTO.class));
		}

		return faturamentoRetencaoTOS;
	}

	/**
	 * Busca uma lista de faturamento, usando como criterio de pesquisa o
	 * os atributos do faturamento do parametro.
	 *
	 * @param faturamento
	 * @return
	 */
	@Override
	public List<FaturamentoTO> pesquisarFaturamento(FaturamentoTO faturamento) {

		QBean qBean = Projections.bean(
			Faturamento.class,
			qFaturamento.codigo,
			qFaturamento.percentualServico);

		JPAQuery<Faturamento> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.where(filtraPesquisaFaturamento(faturamento))
			.orderBy(qFaturamento.codigo.asc());

		List<Faturamento> lista = jpaQuery.fetch();

		return map(lista);
	}

	/**
	 * Filtra a busca pelos atributos preenchidos que estão no objeto do parametro.
	 *
	 * @param faturamento
	 * @return
	 */
	private Predicate filtraPesquisaFaturamento(FaturamentoTO faturamento) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(faturamento.getCodigo())) {
			builder.and(qFaturamento.codigo.eq(faturamento.getCodigo()));
		}

		return builder;
	}

	@Override
	public FaturamentoTO faturamentoAnterior(Long id) {
		JPAQuery<Faturamento> jpaQuery = select();
		jpaQuery.where(qFaturamento.codigo.eq(id));
		return map(jpaQuery.fetchOne());
	}

	@Override
	public FaturamentoTO obterNomeClientePorNotaSaida(Long codigoNotaSaida) {
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;

		JPAQuery jpaQuery = select();

		jpaQuery.join(qFaturamento.faturamentoEtapas, qFaturamentoEtapa);
		jpaQuery.join(qFaturamentoEtapa.notaSaida, qNotaSaida);
		jpaQuery.where(qNotaSaida.codigo.eq(codigoNotaSaida));

		jpaQuery.select(
			Projections.fields(
				Faturamento.class, qFaturamento.codigo,
				Projections.fields(Pessoa.class, qFaturamento.cliente.nome).as("cliente")
			)
		);

		Faturamento faturamento = (Faturamento) jpaQuery.fetchOne();

		return map(faturamento);

	}

	@Override
	public FaturamentoTO obterFaturamentoSimpleTelaNotaFiscal(Long codigoFaturamento) {
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QContrato qContrato = QContrato.contrato;
		QUnidade qUnidadeContrato = QUnidade.unidade;

		JPAQuery<Faturamento> jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Faturamento.class,
				qFaturamento.codigo,
				qFaturamento.situacao,
				qFaturamento.tipo,
				qFaturamento.periodo,
				qFaturamento.identificacao,
				qFaturamento.dataVencimento1,
				qFaturamento.dataVencimento2,
				qFaturamento.tipoDocumentoPagamento,
				qFaturamento.issRetidoCliente,
				qFaturamento.percentualServico,
				qFaturamento.valor,
				qFaturamento.tipoFornecimento,

				Projections.bean(ServicoTipo.class,
					qServicoTipo.codigo,
					qServicoTipo.sigla,
					qServicoTipo.nome
				).as(qFaturamento.servicoTipo),

				Projections.bean(Pessoa.class,
					qFaturamento.cliente.codigo,
					qFaturamento.cliente.nome,
					qFaturamento.cliente.documento,
					qFaturamento.cliente.tipoPessoa
				).as(qFaturamento.cliente),

				Projections.bean(CentroFinanceiro.class,
					qCentroFinanceiro.codigo,
					qCentroFinanceiro.nome,
					qCentroFinanceiro.identificadorFinal,
					qCentroFinanceiro.identificador,
					//FIXME Utilizar projeção para unidade do Centro Financeiro
					qFaturamento.centroFinanceiro.unidade,
					Projections.fields(Contrato.class,
						qContrato.codigo,
						Projections.fields(Unidade.class,
							qUnidadeContrato.codigo,
							qUnidadeContrato.codnome,
							qUnidadeContrato.tipoUnidade
						).as(qContrato.unidade)
					).as(qCentroFinanceiro.contrato)
				).as(qFaturamento.centroFinanceiro),

				//Campo tipo de cobrança faturamento
				Projections.bean(TipoCobrancaFaturamento.class,
					qFaturamento.tipoCobrancaFaturamento.codigo,
					qFaturamento.tipoCobrancaFaturamento.descricao
				).as(qFaturamento.tipoCobrancaFaturamento),


				//Campo banco e agencia/conta (conta corrente)
				Projections.bean(ContaCorrente.class,
					qFaturamento.contaCorrente.codigo,
					qFaturamento.contaCorrente.agencia,
					qFaturamento.contaCorrente.digitoAgencia,
					qFaturamento.contaCorrente.conta,
					qFaturamento.contaCorrente.digito,
					Projections.bean(Banco.class,
						qFaturamento.contaCorrente.banco.codigo,
						qFaturamento.contaCorrente.banco.nome
					).as(qFaturamento.contaCorrente.banco)
				).as(qFaturamento.contaCorrente),

				//Campo cidade do ISS
				Projections.bean(Cidade.class,
					qFaturamento.cidadePrestacaoServico.codigo,
					qFaturamento.cidadePrestacaoServico.nome,
					qFaturamento.cidadePrestacaoServico.uf
				).as(qFaturamento.cidadePrestacaoServico)
			)
		)
		.leftJoin(qFaturamento.centroFinanceiro, qCentroFinanceiro)
		.leftJoin(qCentroFinanceiro.contrato, qContrato.contrato)
		.leftJoin(qContrato.unidade, qUnidadeContrato)
		.leftJoin(qFaturamento.servicoTipo, qServicoTipo)
		.where(qFaturamento.codigo.eq(codigoFaturamento));

		Faturamento faturamento = jpaQuery.fetchOne();

		FaturamentoTO faturamentoTO = getMapper().map(faturamento, FaturamentoTO.class);
		PessoaTO clientePrincipal = pessoaService.buscarClientePrincipalPorCodCliente(faturamentoTO.getCliente().getCodigo());
		faturamentoTO.getCliente().setClientePrincipal(clientePrincipal);

		List<TipoCobrancaVencimentoTO> listaTipoVencimentoTO = tipoCobrancaVencimentoService.findListaByCodCobranca(faturamentoTO.getTipoCobrancaFaturamento().getId());
		faturamentoTO.getTipoCobrancaFaturamento().setTipoCobrancaVencimentos(listaTipoVencimentoTO);

		return faturamentoTO;
	}

	@Override
	public PessoaTO obterClienteDeUmFaturamento(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Faturamento.class,
				qFaturamento.codigo,
				qFaturamento.situacao,
				qFaturamento.tipo,
				qFaturamento.periodo,
				qFaturamento.identificacao,
				qFaturamento.dataVencimento1,
				qFaturamento.dataVencimento2,
				qFaturamento.tipoDocumentoPagamento,
				qFaturamento.issRetidoCliente,
				Projections.bean(Pessoa.class,
					qFaturamento.cliente.codigo,
					qFaturamento.cliente.nome,
					qFaturamento.cliente.documento
				).as(qFaturamento.cliente)
			)
		)
			.innerJoin(qFaturamento.cliente, QPessoa.pessoa);
		jpaQuery.where(qFaturamento.codigo.eq(codigo));
		return getMapper().map(((Faturamento) jpaQuery.fetchOne()).getCliente(), PessoaTO.class);
	}

	@Override
	public void updateSituacaoAberto(Long codigo) {
		updateSituacao(codigo, Faturamento.Status.ABERTO);
	}

	@Override
	public void updateSituacaoEmFaturamento(Long codigo) {
		updateSituacao(codigo, Faturamento.Status.EM_FATURAMENTO);
	}

	@Override
	public void updateSituacaoEmRecebimento(Long codigo) {
		updateSituacao(codigo, Faturamento.Status.EM_RECEBIMENTO);
	}

	@Override
	public void updateSituacaoRecebido(Long codigo) {
		updateSituacao(codigo, Faturamento.Status.RECEBIDO);
	}

	@Override
	public void updateSituacaoCancelado(Long codigo) {
		updateSituacao(codigo, Faturamento.Status.CANCELADO);
	}

	private void updateSituacao(Long codigo, Faturamento.Status status) {
		selectUpdate(qFaturamento.codigo.eq(codigo)).set(qFaturamento.situacao, status).execute();
	}

	@Override
	public void updateDataAtualizacao(Long codigo, Date date) {
		selectUpdate(qFaturamento.codigo.eq(codigo)).set(qFaturamento.dataAtualizacao, date).execute();
	}

	@Override
	public void updateAtualizador(Long codigo, String username) {
		selectUpdate(qFaturamento.codigo.eq(codigo)).set(qFaturamento.atualizador, username).execute();
	}

	@Override
	public boolean isIssRetidoCliente(Long codigoFaturamento) {
		Faturamento faturamento = select(
			Projections.fields(Faturamento.class,
				qFaturamento.codigo,
				qFaturamento.issRetidoCliente
			)).where(qFaturamento.codigo.eq(codigoFaturamento)).fetchOne();
		return faturamento != null ? faturamento.getIssRetidoCliente() : false;
	}

	@Override
	public boolean isIssRetidoClienteByCodNotaSaida(Long codigoNotaSaida) {
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;

		Faturamento faturamento = select(
			Projections.fields(Faturamento.class,
				qFaturamento.codigo,
				qFaturamento.issRetidoCliente
			)).where(
			qFaturamento.codigo.eq(
				JPAExpressions
					.selectFrom(qFaturamentoEtapa)
					.where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida))
					.select(qFaturamentoEtapa.faturamento.codigo)
			)
		).fetchOne();

		return faturamento != null ? faturamento.getIssRetidoCliente() : false;
	}

	@Override
	public ValueTO notaFiscalAbertaCheck(Long codigo) {
		return new ValueTO(notaSaidaService.temNotaAbertaDoFaturamento(codigo));
	}

}
