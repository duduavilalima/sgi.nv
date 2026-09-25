package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterfaceNota;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquotaEmissao;
import br.com.araujoabreu.sgi.controleFiscal.domain.QNotaTributoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoRetencao;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.ServicoTipoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.BancoService;
import br.com.araujoabreu.sgi.service.categoriaTributo.AliquotaService;
import br.com.araujoabreu.sgi.service.categoriaTributo.CategoriaTributoService;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.cidade.CidadeService;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.contaCorrente.ContaCorrenteService;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contrato.ContratoService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoAliquotaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoRetencaoService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceFaturamentoService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceTO;
import br.com.araujoabreu.sgi.service.nota.*;
import br.com.araujoabreu.sgi.service.nota.transfer.*;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.tipoCobranca.TipoCobrancaFaturamentoService;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.administracao.TipoRepository;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.compras.NotaFiscalRepository;
import br.com.fasolti.sgi.repository.despesa.DespesaRepository;
import br.com.fasolti.sgi.repository.documentoFiscal.DocumentoFiscalRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoAliquotaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRetencaoRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceFaturamentoRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceNotaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.*;
import br.com.fasolti.sgi.repository.notaSaida.*;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.pessoaContaContabil.PessoaContaContabilRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NotaFiscalFaturamentoServiceImpl extends BaseSearchService<NotaSaida, NotaSaidaTO, NotaSaidaPesquisaTO, NotaSaidaRepository> implements NotaFiscalFaturamentoService {

	protected static final String CHAVE_FATURAMENTO_CANCELAR_NF = "faturamento-canc_nf";

	protected static final String PARAMETRO_FATURAMENTO_CANCELAMENTO_NF_PARCELAS = "faturamento.cancelamento.nf.parcelas";

	private static final QNotaSaida qNotaSaida = QNotaSaida.notaSaida;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private NotaSaidaParcelaMotivoDescDevService notaSaidaParcelaMotivoDescDevService;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private FaturamentoRetencaoService faturamentoRetencaoService;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private FaturamentoAliquotaRepository faturamentoAliquotaRepository;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private ServicoTipoRepository servicoTipoRepository;

	@Autowired
	private DocumentoFiscalRepository documentoFiscalRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private CategoriaTributoRepository categoriaTributoRepository;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private NotaTributoAliquotaRepository notaTributoAliquotaRepository;

	@Autowired
	private NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Autowired
	private LoteInterfaceNotaRepository loteInterfaceNotaRepository;

	@Autowired
	private LoteInterfaceFaturamentoRepository loteInterfaceFaturamentoRepository;

	@Autowired
	private FaturamentoRetencaoRepository faturamentoRetencaoRepository;

	@Autowired
	private NotaSaidaParcelaRetencaoRepository notaSaidaParcelaRetencaoRepository;

	@Autowired
	private NotaSaidaParcelaRetencaoService notaSaidaParcelaRetencaoService;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private DespesaService despesaService;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private MotivoRetornoNotaRepository motivoRetornoNotaRepository;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private AliquotaRepository aliquotaRepository;

	@Autowired
	private TipoRepository tipoRepository;

	@Autowired
	private NotaTributoAliquotaEmissaoRepository notaTributoAliquotaEmissaoRepository;

	@Autowired
	private DespesaRepository despesaRepository;

	@Autowired
	private DozerHelper dozerHelper;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private FaturamentoAliquotaService faturamentoAliquotaService;

	@Autowired
	private CategoriaTributoService categoriaTributoService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private FaturamentoService faturamentoService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private ContaCorrenteService contaCorrenteService;

	@Autowired
	private BancoService bancoService;

	@Autowired
	private TipoCobrancaFaturamentoService tipoCobrancaFaturamentoService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private NotaTributoAliquotaService notaTributoAliquotaService;

	@Autowired
	private NotaTributoAliquotaEmissaoService notaTributoAliquotaEmissaoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private ServicoTipoService servicoTipoService;

	@Autowired
	private LoteInterfaceFaturamentoService loteInterfaceFaturamentoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	public FeriadoService feriadoService;

	@Autowired
	private PessoaContaContabilRepository pessoaContaContabilRepository;

	@Autowired
	public NotaFiscalFaturamentoServiceImpl(NotaSaidaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<NotaSaida> jpaQuery, NotaSaidaPesquisaTO exampleObj) {
		jpaQuery.select(Projections.fields(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.numero,
			qNotaSaida.status,
			qNotaSaida.formaPagamento,
			qNotaSaida.isentoICMS,
			qNotaSaida.inscricaoSuframa,
			qNotaSaida.tipoDocumentoTransportadora,
			qNotaSaida.documentoTransportadora,
			qNotaSaida.nomeTransportadora,
			qNotaSaida.inscricaoEstadualTransportadora,
			qNotaSaida.isentoICMSTransportadora,
			qNotaSaida.enderecoTransportadora,
			qNotaSaida.baseCalculoICMSTransporte,
			qNotaSaida.issRetidoCliente,
			qNotaSaida.issRetidoClienteRecebimento,
			qNotaSaida.aliquotaICMSTransporte,
			qNotaSaida.infoComplementarContribuinte,
			qNotaSaida.servicoTipo,
			qNotaSaida.protocoloEntrega,
			qNotaSaida.dataAtualizacao,
			qNotaSaida.atualizador,
			qNotaSaida.dataCadastroPrefeitura,
			qNotaSaida.dataCancelamento,
			qNotaSaida.dataCancelamentoNumeracao,
			qNotaSaida.dataContrFiscal,
			qNotaSaida.dataCriacao,
			qNotaSaida.criador,
			qNotaSaida.dataEmissao,
			qNotaSaida.dataEmissor,
			qNotaSaida.dataEnvioRfb,
			qNotaSaida.dataNumeracao,
			qNotaSaida.dataRetornoFinanceiro,
			qNotaSaida.dataUltimoArquivo,
			qNotaSaida.serie,
			qNotaSaida.subserie,
			qNotaSaida.limiteEmissao,
			qNotaSaida.saida,
			qNotaSaida.conclusao,
			qNotaSaida.chaveEletronica,
			qNotaSaida.desconto,
			qNotaSaida.valorFrete,
			qNotaSaida.valorSeguro,
			qNotaSaida.valorIpi,
			qNotaSaida.valorOutrasDespesas,
			qNotaSaida.tipoFrete,
			qNotaSaida.tipoSeguro,
			qNotaSaida.tipoFornecimento,
			qNotaSaida.tipoNotaFiscal,
			qNotaSaida.horaSaida,
			Projections.fields(Unidade.class, qNotaSaida.empresa.codigo).as("empresa"))
		);
	}

	@Override
	public NotaFiscalFaturamentoTO createNotaFiscal(NotaFiscalFaturamentoTO exampleObj) {
		NotaFiscalFaturamentoTO notaFiscalFaturamentoTO = new NotaFiscalFaturamentoTO();
		FaturamentoEtapaTO faturamentoEtapa = faturamentoEtapaService.obterFaturamentoEtapaParaNotaFiscalSemFaturamento(exampleObj.getFaturamentoEtapaTO().getCodigo());
		notaFiscalFaturamentoTO.setNumero("NF/" + Util.formataNumero(0, 9));
		faturamentoEtapa.setFaturamento(faturamentoService.obterFaturamentoSimpleTelaNotaFiscal(faturamentoEtapa.getFaturamento().getCodigo()));
		notaFiscalFaturamentoTO.setTipoFornecimento(faturamentoEtapa.getFaturamento().getTipoFornecimento());
		notaFiscalFaturamentoTO.setServicoTipo(faturamentoEtapa.getFaturamento().getServicoTipo());

		if (Check.isAssigned(faturamentoEtapa.getFaturamento().getCentroFinanceiro())
			&& Check.isAssigned(faturamentoEtapa.getFaturamento().getCentroFinanceiro().getUnidade().getCodigo())) {
			notaFiscalFaturamentoTO.setEmpresa(unidadeService.obterUnidade(faturamentoEtapa.getFaturamento().getCentroFinanceiro().getUnidade().getMatriz().getCodigo()));
		}

		notaFiscalFaturamentoTO.setFaturamentoEtapaTO(faturamentoEtapa);
		notaFiscalFaturamentoTO.setStatus(Nota.StatusNota.ABERTA_FATURAMENTO.toString());

		faturamentoService.changeClienteSubcliente(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento());

		notaFiscalFaturamentoTO.setDataCriacao(new Date());
		notaFiscalFaturamentoTO.setCriador(securityService.getUser().getPessoaFisica().getNome());

		notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento().setFaturamentoRetencoes(
			faturamentoRetencaoService.obterListaPorCodigoFaturamento(faturamentoEtapa.getFaturamento().getCodigo())
		);

		notaFiscalFaturamentoTO.setDataCriacao(new Date());
		notaFiscalFaturamentoTO.setCriador(getUser().getUsername());

		notaFiscalFaturamentoTO.setEmpresa(unidadeService.obterUnidade(notaFiscalFaturamentoTO.getEmpresa().getCodigo()));

		return notaFiscalFaturamentoTO;
	}

	@Override
	@Transactional(readOnly = false)
	public NotaFiscalFaturamentoTO findLastNotaFiscalByUnidadeCodigo(NotaFiscalFaturamentoTO exampleObj) {
		int proximoNumero = 0;
		if (Check.isAssigned(exampleObj) && Check.isAssigned(exampleObj.getEmpresa()) && Check.isAssigned(exampleObj.getEmpresa().getCodigo())) {
			UnidadeTO unidade = unidadeService.obterUnidadeParaVerificacaoDeNumeracao(exampleObj.getEmpresa().getCodigo());

			if (unidade != null) {
				if (unidade.getZerarNumeracaoNfAno()) {
					Date dataEmissao = exampleObj.getDataEmissao();
					Calendar cal = Calendar.getInstance();
					cal.setTime(dataEmissao);
					Integer ano = cal.get(Calendar.YEAR);

					if (ano > unidade.getAnoProximaNf()) {
						unidadeService.atualizarNumeracaoProximaNf(unidade.getCodigo(), 1, ano.shortValue());
					} else {
						proximoNumero = exampleObj.getTipoFornecimento().equals(Faturamento.TipoFornecimentoNF.MATERIAL.getDescricao())? unidade.getProximaNfMaterial() : unidade.getProximaNf();
					}
				} else {
					proximoNumero = exampleObj.getTipoFornecimento().equals(Faturamento.TipoFornecimentoNF.MATERIAL.getDescricao())? unidade.getProximaNfMaterial() : unidade.getProximaNf();
				}
				if (exampleObj.getTipoFornecimento().equals(Faturamento.TipoFornecimentoNF.MATERIAL.getDescricao()))
					unidadeService.atualizarNumeroProximaNfMaterial(unidade.getCodigo(), proximoNumero+1);
			}
		}

		exampleObj.setNumero("NF/" + Util.formataNumero(proximoNumero + 1, 9));

		return exampleObj;
	}

	@Override
	public NotaFiscalFaturamentoTO findNotaFiscalByCodigo(NotaFiscalFaturamentoTO exampleObj) throws ValidationException {
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;

		JPAQuery<NotaSaida> query = select(Projections.fields(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.numero,
			qNotaSaida.status,
			qNotaSaida.formaPagamento,
			qNotaSaida.isentoICMS,
			qNotaSaida.inscricaoSuframa,
			qNotaSaida.tipoDocumentoTransportadora,
			qNotaSaida.documentoTransportadora,
			qNotaSaida.nomeTransportadora,
			qNotaSaida.inscricaoEstadualTransportadora,
			qNotaSaida.isentoICMSTransportadora,
			qNotaSaida.issRetidoCliente,
			qNotaSaida.issRetidoClienteRecebimento,
			qNotaSaida.enderecoTransportadora,
			qNotaSaida.baseCalculoICMSTransporte,
			qNotaSaida.aliquotaICMSTransporte,
			qNotaSaida.infoComplementarContribuinte,
			qNotaSaida.protocoloEntrega,
			qNotaSaida.dataAtualizacao,
			qNotaSaida.atualizador,
			qNotaSaida.dataCadastroPrefeitura,
			qNotaSaida.dataCancelamento,
			qNotaSaida.dataCancelamentoNumeracao,
			qNotaSaida.dataReversaoCancelamento,
			qNotaSaida.dataContrFiscal,
			qNotaSaida.dataCriacao,
			qNotaSaida.criador,
			qNotaSaida.dataEmissao,
			qNotaSaida.dataEmissor,
			qNotaSaida.dataEnvioRfb,
			qNotaSaida.dataNumeracao,
			qNotaSaida.dataRetornoFinanceiro,
			qNotaSaida.dataUltimoArquivo,
			qNotaSaida.serie,
			qNotaSaida.subserie,
			qNotaSaida.limiteEmissao,
			qNotaSaida.saida,
			qNotaSaida.conclusao,
			qNotaSaida.chaveEletronica,
			qNotaSaida.desconto,
			qNotaSaida.valorFrete,
			qNotaSaida.valorSeguro,
			qNotaSaida.valorIpi,
			qNotaSaida.valorOutrasDespesas,
			qNotaSaida.tipoFrete,
			qNotaSaida.tipoSeguro,
			qNotaSaida.tipoFornecimento,
			qNotaSaida.tipoNotaFiscal,
			qNotaSaida.horaSaida,
			qNotaSaida.dataRespNotaAssociadaCancelamento,
			Projections.fields(ServicoTipo.class, qServicoTipo.codigo, qServicoTipo.nome, qServicoTipo.sigla, qServicoTipo.situacao).as(qNotaSaida.servicoTipo),
			Projections.fields(NotaSaida.class, qNotaSaida.notaAssociadaCancelamento.codigo, qNotaSaida.notaAssociadaCancelamento.numero, qNotaSaida.notaAssociadaCancelamento.dataEmissao).as("notaAssociadaCancelamento"),
			Projections.fields(Pessoa.class, qNotaSaida.respNotaAssociadaCancelamento.codigo, qNotaSaida.respNotaAssociadaCancelamento.nome).as("respNotaAssociadaCancelamento"),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelCancelarNumeracao.codigo, qNotaSaida.responsavelCancelarNumeracao.nome).as(qNotaSaida.responsavelCancelarNumeracao),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelCancelamento.codigo, qNotaSaida.responsavelCancelamento.nome).as(qNotaSaida.responsavelCancelamento),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelReversaoCancelamento.codigo, qNotaSaida.responsavelReversaoCancelamento.nome).as(qNotaSaida.responsavelReversaoCancelamento),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelCadastroPrefeitura.codigo, qNotaSaida.responsavelCadastroPrefeitura.nome).as(qNotaSaida.responsavelCadastroPrefeitura),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelEnvioRfb.codigo, qNotaSaida.responsavelEnvioRfb.nome).as(qNotaSaida.responsavelEnvioRfb),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelNumeracao.codigo, qNotaSaida.responsavelNumeracao.nome).as(qNotaSaida.responsavelNumeracao),
			Projections.fields(Pessoa.class, qNotaSaida.responsavelUltimoArquivo.codigo, qNotaSaida.responsavelUltimoArquivo.nome).as(qNotaSaida.responsavelUltimoArquivo),
			Projections.fields(Unidade.class, qNotaSaida.empresa.codigo, qNotaSaida.empresa.codnome).as("empresa")
		))
			.leftJoin(qNotaSaida.notaAssociadaCancelamento)
			.leftJoin(qNotaSaida.respNotaAssociadaCancelamento)
			.leftJoin(qNotaSaida.responsavelCancelarNumeracao, QPessoa.pessoa)
			.leftJoin(qNotaSaida.responsavelCancelamento, QPessoa.pessoa)
			.leftJoin(qNotaSaida.responsavelReversaoCancelamento, QPessoa.pessoa)
			.leftJoin(qNotaSaida.responsavelCadastroPrefeitura, QPessoa.pessoa)
			.leftJoin(qNotaSaida.responsavelEnvioRfb, QPessoa.pessoa)
			.leftJoin(qNotaSaida.responsavelNumeracao, QPessoa.pessoa)
			.leftJoin(qNotaSaida.responsavelUltimoArquivo, QPessoa.pessoa)
			.leftJoin(qNotaSaida.empresa, QUnidade.unidade)
			.leftJoin(qNotaSaida.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
			.leftJoin(qNotaSaida.servicoTipo, qServicoTipo)

			.where(qNotaSaida.codigo.eq(exampleObj.getCodigo())
				.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO)));

		// FIXME: Usuário com esse perfil só pode ser notas dos CLs associados a ele.
		if (usuarioService.possuiUmPerfilLista("faturamento-cns-gerencia")) {
			List<CentroFinanceiroTO> list = usuarioService.getCentroFinanceiros();
			List<Long> codigos = new ArrayList<>();
			list.forEach(cf -> codigos.add(cf.getCodigo()));
			query.where(QCentroFinanceiro.centroFinanceiro.codigo.in(codigos).or(QCentroFinanceiro.centroFinanceiro.pai.codigo.in(codigos)));
		}

		NotaSaida resultado = query.fetchOne();

		if (resultado == null) {
			// Mensagem mais amigável ao usuário.
			throw new ValidationException("NotaFiscal", "Você não possui acesso aos dados desta nota fiscal.");
		}

		NotaSaidaTO notaSaidaTO = mapTO(resultado);

		//O ajuste abaixo foi feito para acertar a exibição do campo data de emissão, pois devido ao erro no horário do servidor, é exibido o dia anterior ao gravado
		//TODO: verificar se ocorrer com outros campos e possivelmente transferir a solução para o método get do campo
		notaSaidaTO.setEmpresa(unidadeService.obterUnidade(resultado.getEmpresa().getCodigo()));
		Calendar dataEmissao = Calendar.getInstance();
		dataEmissao.setTimeInMillis(notaSaidaTO.getDataEmissao().getTime());
		dataEmissao.set(Calendar.HOUR, 10);
		notaSaidaTO.setDataEmissao(dataEmissao.getTime());

		NotaFiscalFaturamentoTO notaFaturamento = converteNotaSaidaEmNotaFaturamento(notaSaidaTO);

		notaFaturamento.setNotaSaidaParcelas(notaSaidaParcelaService.getListaParcelaByNotaSaidaId(resultado.getCodigo()));

		notaFaturamento.setInterfacePrefeitura(unidadeService.obterUnidadeInterfacePrefeitura(resultado.getEmpresa().getCodigo()));

		if (Nota.FormaPagamento.PARCELA_UNICA.equals(resultado.getFormaPagamento())) {
			if (!notaFaturamento.getNotaSaidaParcelas().isEmpty()) {
				notaFaturamento.setVencimento(notaFaturamento.getNotaSaidaParcelas().get(0).getDataVencimento());
			}
		}

		FaturamentoEtapaTO etapa = faturamentoEtapaService.obterFaturamentoEtapaTelaNotaFiscalComCodigoFaturamento(resultado.getCodigo());

		/* Métodos para melhora na performance na tela de nota fiscal (exibição) - SGINV-1288 */

		if (etapa == null || etapa.getFaturamento() == null) {
			throw new ValidationException("ValidationException", "Não há etapa para essa nota fiscal");
		}

		FaturamentoTO faturamentoTO = faturamentoService.obterFaturamentoSimpleTelaNotaFiscal(etapa.getFaturamento().getCodigo());
		/* Preenchimento dos campos da aba de faturamento, passando apenas o código obtido, retornando somente o necessário na aba */

		faturamentoTO.setCliente(pessoaService.obterClienteSimpleTelaNotaFiscal(faturamentoTO.getCliente().getCodigo()));

		//Caso possua cliente principal
		if (faturamentoTO.getCliente().getClientePrincipal() != null && faturamentoTO.getCliente().getClientePrincipal().getCodigo() != null) {
			faturamentoTO.getCliente().setClientePrincipal(pessoaService.obterClientePrincipalSimpleTelaNotaFiscal(faturamentoTO.getCliente().getClientePrincipal().getCodigo()));
		}

		faturamentoTO.setCentroFinanceiro(centroFinanceiroService.obterCentroFinanceiroSimpleTelaNotaFiscal(faturamentoTO.getCentroFinanceiro().getCodigo()));

		faturamentoTO.getCentroFinanceiro().setContratoTO(contratoService.obterContratoSimpleNotaFiscal(faturamentoTO.getCentroFinanceiro().getContratoTO().getCodigo()));
		faturamentoTO.getCentroFinanceiro().getContratoTO().setUnidade(unidadeService.obterUnidade(faturamentoTO.getCentroFinanceiro().getContratoTO().getCodigo()));
		faturamentoTO.setContaCorrente(contaCorrenteService.obterContaCorrenteSimpleTelaNotaFiscal(faturamentoTO.getContaCorrente().getCodigo()));

		//Caso possua cidade de prestação de serviços
		if (faturamentoTO.getCidadePrestacaoServico() != null && faturamentoTO.getCidadePrestacaoServico().getCodigo() != null) {
			faturamentoTO.setCidadePrestacaoServico(cidadeService.obterCidadeSimpleTelaNotaFiscal(faturamentoTO.getCidadePrestacaoServico().getCodigo()));
		}
		faturamentoTO.getContaCorrente().setBanco(bancoService.obterBancoSimpleTelaNotaFiscal(faturamentoTO.getContaCorrente().getBanco().getCodigo()));

		faturamentoTO.setTipoCobrancaFaturamento(tipoCobrancaFaturamentoService.obterTipoCobrancaFaturamentoSimpleTelaNotaFiscal(faturamentoTO.getTipoCobrancaFaturamento().getCodigo()));

		etapa.setFaturamento(faturamentoTO);

		notaFaturamento.setFaturamentoEtapaTO(etapa);
		faturamentoService.changeClienteSubcliente(notaFaturamento.getFaturamentoEtapaTO().getFaturamento());

		notaFaturamento.setServicoTipo(mapTO(resultado.getServicoTipo()));

		if (resultado.getTipoFornecimento() != null)
			notaFaturamento.setTipoFornecimento(resultado.getTipoFornecimento().getDescricao());

		List<LoteInterfaceNota> listaLotes = loteInterfaceNotaRepository.findLoteNotaByCodigoNota(resultado.getCodigo());
		List<LoteInterfaceNotaTO> lotes = mapLoteNotaTO(listaLotes);

		if (notaFaturamento != null && notaFaturamento.getCriador() != null) {
			notaFaturamento.setCriador(pessoaFisicaService.nomeUsuarioPorLogin(notaFaturamento.getCriador()));
		}

		if (notaFaturamento != null && notaFaturamento.getAtualizador() != null) {
			notaFaturamento.setAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(notaFaturamento.getAtualizador()));
		}

		for (LoteInterfaceNota loteNota : listaLotes) {
			for (LoteInterfaceNotaTO loteNotaTO : lotes) {
				if (loteNota.getLoteInterface().getCodigo().equals(loteNotaTO.getLoteInterface().getCodigo())) {

					loteNotaTO.getLoteInterface().setDataEvento(loteNota.getLoteInterface().getDataEvento());
					loteNotaTO.getLoteInterface().setSituacao(loteNota.getLoteInterface().getSituacao().getNome());
					loteNotaTO.getLoteInterface().setDataCriacao(loteNota.getLoteInterface().getDataCriacao());

					String nomeCriador = pessoaFisicaService.nomeUsuarioPorLogin(loteNota.getLoteInterface().getCriador());

					if (nomeCriador.isEmpty()) {
						PessoaTO pessoaTO = new PessoaTO();
						pessoaTO.setNome(loteNota.getLoteInterface().getCriador());
						loteNotaTO.getLoteInterface().setResponsavel(pessoaTO);
					} else {
						PessoaTO pessoaTO = new PessoaTO();
						pessoaTO.setNome(nomeCriador);
						loteNotaTO.getLoteInterface().setResponsavel(pessoaTO);
					}

					break;
				}
			}
		}

		notaFaturamento.setLotes(lotes);

		if (etapa != null && !Check.isAssigned(etapa)) {
			notaFaturamento.getFaturamentoEtapaTO().getFaturamento().setFaturamentoRetencoes(
				faturamentoRetencaoService.obterListaPorCodigoFaturamento(etapa.getFaturamento().getCodigo())
			);
		}

		return notaFaturamento;
	}

	@Override
	public List<ServicoTipoPesquisaTO> buscarServicos(ServicoTipoTO exampleObj) {
		List<ServicoTipoTO> listaHabitado = servicoTipoService.findByNomeAndSituacaoAndTipoFaturamento(exampleObj.getNome(), ServicoTipo.NOME_SITUACAO_ATIVO, ServicoTipo.NOME_TF_HABILITADO);
		List<ServicoTipoTO> listaHabitadoCidade = servicoTipoService.findByNomeAndSituacaoAndTipoFaturamento(exampleObj.getNome(), ServicoTipo.NOME_SITUACAO_ATIVO, ServicoTipo.NOME_TF_HABILITADO_CIDADE);

		List<ServicoTipoTO> lista = new ArrayList<>();
		lista.addAll(listaHabitado);
		lista.addAll(listaHabitadoCidade);

		lista.sort((t1, t2) -> t1.getNome().compareToIgnoreCase(t2.getNome()));

		return dozerHelper.map(lista, ServicoTipoPesquisaTO.class);
	}

	@Override
	@Transactional
	public NotaFiscalFaturamentoTO saveNF(NotaFiscalFaturamentoTO transferObj) throws Exception {
		try {
			doBeforeSaveNotaFiscalFaturamento(transferObj);
			NotaSaida notaSaida = salvarNotaFiscalFaturamento(transferObj);
			transferObj.setCodigo(notaSaida.getCodigo());

			if (transferObj.getEditTributos() != null && transferObj.getEditTributos()) {
				doAfterSaveNotaFiscalFaturamento(notaSaida, transferObj);
			}

			return transferObj;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private NotaSaida salvarNotaFiscalFaturamento(NotaFiscalFaturamentoTO transferObj) throws Exception {
		NotaSaida notaSaida = createOrUpdateNotaSaida(transferObj);

		faturamentoEtapaService.updateSituacaoFaturada(transferObj.getFaturamentoEtapaTO().getCodigo());
		// FIXME: UPDATE "Direto" não funciona quando NF ainda não existe. Refatorar.
		//faturamentoEtapaService.updateNotaSaida(transferObj.getFaturamentoEtapaTO().getCodigo(), notaSaida.getCodigo());
		FaturamentoEtapa faturamentoEtapa = faturamentoEtapaRepository.findByCodigo(transferObj.getFaturamentoEtapaTO().getCodigo());
		faturamentoEtapa.setNotaSaida(notaSaida);
		faturamentoEtapaRepository.save(faturamentoEtapa);
		faturamentoService.updateSituacaoEmFaturamento(transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo());

		return notaSaida;
	}

	private void verificarUltimoNumero(NotaFiscalFaturamentoTO notaFiscalFaturamento) throws Exception {
		if (isNumeroDisponivel(notaFiscalFaturamento)) {
			UnidadeTO unidade = unidadeService.obterUnidadeParaVerificacaoDeNumeracao(notaFiscalFaturamento.getEmpresa().getCodigo());
			Integer numeroGravadoNaUnidadeProximaNf = unidade.getProximaNf() + 1;

			String ultimoNumeroGravadoString = "NF/" + Util.formataNumero(numeroGravadoNaUnidadeProximaNf, 9);

			Integer numeroEscolhidoPeloUsuarioInteiro = Integer.parseInt(notaFiscalFaturamento.getNumero().replace("NF/", ""));

			// Usou o número gerado pelo sistema
			if (numeroEscolhidoPeloUsuarioInteiro.equals(ultimoNumeroGravadoString)) {
				notaFiscalFaturamento.setNumero(ultimoNumeroGravadoString);
			} else if (numeroEscolhidoPeloUsuarioInteiro > unidade.getProximaNf()) {
				unidadeService.atualizarNumeroProximaNf(unidade.getCodigo(), numeroEscolhidoPeloUsuarioInteiro);
			} else if (numeroEscolhidoPeloUsuarioInteiro > numeroGravadoNaUnidadeProximaNf) {
				unidadeService.atualizarNumeroProximaNf(unidade.getCodigo(), numeroEscolhidoPeloUsuarioInteiro);
			}
		} else {
			throw new Exception("A " + notaFiscalFaturamento.getNumero() + " já existe.");
		}
	}

	/**
	 * Verifica se alguma NF já tem o número passado no parametro
	 *
	 * @param notaFiscalFaturamentoTO
	 * @return
	 */
	private boolean isNumeroDisponivel(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO) {
		Long numeros = 0L;

		if (unidadeService.zerarNumeracaoNfAno(notaFiscalFaturamentoTO.getEmpresa().getCodigo())) {
			Date dataEmissao = notaFiscalFaturamentoTO.getDataEmissao();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataEmissao);
			int ano = cal.get(Calendar.YEAR);

			numeros = notaSaidaService.obterNumerosFaturamento(
				notaFiscalFaturamentoTO.getNumero(),
				notaFiscalFaturamentoTO.getEmpresa().getCodigo(),
				ano);
		} else {
			numeros = notaSaidaService.obterNumerosFaturamento(
				notaFiscalFaturamentoTO.getNumero(),
				notaFiscalFaturamentoTO.getEmpresa().getCodigo());
		}

		if (numeros != null && numeros > 0) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	public MenssagemPadraoTO possuiContaContabilAssociada(Long codigoNotaFiscal) {
		NotaSaida notaFiscalFaturamento = notaSaidaRepository.findById(codigoNotaFiscal).orElse(null);


		MenssagemPadraoTO menssagemPadraoTO = new MenssagemPadraoTO();

		Long codigoCliente = notaFiscalFaturamento.getFaturamentoEtapas().get(0).getFaturamento().getCliente() != null ?
			notaFiscalFaturamento.getFaturamentoEtapas().get(0).getFaturamento().getCliente().getCodigo() : 0l;
		Long codigoUnidade = notaFiscalFaturamento.getCentroFinanceiro().getUnidade().getMatriz().getCodigo();
		List<PessoaContaContabil> contasContabeis = pessoaContaContabilRepository.findByPessoaCodigo(codigoCliente);

		if(!contasContabeis.isEmpty()){
			for (PessoaContaContabil pessoa: contasContabeis) {
				if (pessoa.getTipoEspecial().equals(PessoaContaContabil.TipoRelacionamento.CLIENTE) && pessoa.getUnidade().getCodigo().equals(codigoUnidade)){
					return menssagemPadraoTO;
				}
			}
		}
		menssagemPadraoTO.addWarnings(
			String.format("Este cliente não possui conta contábil associada para a empresa '%s'. É necessário informar a Contabilidade sobre o cadastro para evitar problemas nos lotes contábeis.", notaFiscalFaturamento.getCentroFinanceiro().getUnidade().getCodnome()));
		return menssagemPadraoTO;
	}

	@Transactional()
	@Override
	public NotaFiscalFaturamentoTO numerarNF(NotaFiscalFaturamentoTO transferObj) throws Exception {
		try {
			prepareValidateToSave(transferObj);

			unidadeService.getSelect().setLockMode(LockModeType.PESSIMISTIC_WRITE);

			verificarUltimoNumero(transferObj);

			doBeforeSaveNotaFiscalFaturamento(transferObj);

			getRepository().updateStatus(transferObj.getCodigo(), Nota.StatusNota.A_PROCESSAR_FATURAMENTO);
			transferObj.setStatus(Nota.StatusNota.A_PROCESSAR_FATURAMENTO.getNomeStatus());

			getRepository().updateResponsavelNumeracao(transferObj.getCodigo(), securityService.getUser().getPessoaFisica());
			getRepository().updateDataNumeracao(transferObj.getCodigo(), new Date());

			transferObj.setResponsavelNumeracao(getMapper().map(securityService.getUser().getPessoaFisica(), PessoaFisicaTO.class));

			Date data = new Date();
			getRepository().updateDataNumeracao(transferObj.getCodigo(), data);
			transferObj.setDataNumeracao(data);

			getRepository().updateDataEmissao(transferObj.getCodigo(), transferObj.getDataEmissao());
			getRepository().updateNumero(transferObj.getCodigo(), transferObj.getNumero());

			getRepository().updateDataAtualizacao(transferObj.getCodigo(), new Date());
			getRepository().updateAtualizador(transferObj.getCodigo(), getUser().getUsername());

			// SGINV-1877
			gravarTributosAssociadosANota(transferObj.getCodigo());

			unidadeService.getSelect().setLockMode(LockModeType.NONE);

			return transferObj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	private void gravarTributosAssociadosANota(Long codigoNota) {

		List<NotaTributoAliquota> lista = notaTributoAliquotaRepository.findByNotaCodigo(codigoNota);

		for (NotaTributoAliquota notaTributoAliquota : lista) {
			NotaTributoAliquotaEmissao nEmissao = new NotaTributoAliquotaEmissao();
			nEmissao.setAliquota(notaTributoAliquota.getAliquota());
			nEmissao.setBaseCalculo(notaTributoAliquota.getBaseCalculo());
			nEmissao.setNota(notaTributoAliquota.getNota());
			nEmissao.setPercentual(notaTributoAliquota.getPercentual());
			nEmissao.setValor(notaTributoAliquota.getValor());
			nEmissao.setPercentual(nEmissao.getPercentual() == null ? nEmissao.getAliquota().getPercentual() : nEmissao.getPercentual());
			notaTributoAliquotaEmissaoRepository.save(nEmissao);
		}
	}

	private void removerTributosAssociadosANota(Long codigoNota) {
		List<NotaTributoAliquotaEmissao> lista = notaTributoAliquotaEmissaoRepository.findByNotaCodigo(codigoNota);

		for (NotaTributoAliquotaEmissao nEmissao : lista) {
			notaTributoAliquotaEmissaoRepository.delete(nEmissao);
		}
	}

	@Override
	@Transactional
	public NotaFiscalFaturamentoTO cancelarNumeracaoNF(NotaFiscalFaturamentoTO transferObj) throws Exception {
		try {
			doBeforeSaveNotaFiscalFaturamento(transferObj);

			getRepository().updateStatus(transferObj.getCodigo(), Nota.StatusNota.ABERTA_FATURAMENTO);
			getRepository().updateNumero(transferObj.getCodigo(), "NF/" + Util.formataNumero(0, 9));
			getRepository().updateResponsavelCancelarNumeracao(transferObj.getCodigo(), securityService.getUser().getPessoaFisica());
			getRepository().updateDataCancelamentoNumeracao(transferObj.getCodigo(), new Date());

			getRepository().updateDataAtualizacao(transferObj.getCodigo(), new Date());
			getRepository().updateAtualizador(transferObj.getCodigo(), getUser().getUsername());

			removerTributosAssociadosANota(transferObj.getCodigo());

			return transferObj;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public NotaSaidaTO cancelarNf(NotaSaidaTO to) throws Exception {
		return salvarMotivoRetornoDocumentoNota(to);
	}

	public NotaSaidaTO salvarMotivoRetornoDocumentoNota(NotaSaidaTO to) throws Exception {
		try {
			NotaSaida notaSaida = getRepository().findById(to.getCodigo()).orElse(null);
			notaSaida.setStatus(Nota.StatusNota.CANCELADA_FATURAMENTO);
			notaSaida.setDataCancelamento(new Date());
			notaSaida.setResponsavelCancelamento(securityService.getUser().getPessoaFisica());
			notaSaida.setDataAtualizacao(new Date());
			notaSaida.setAtualizador(securityService.getUsername());

			//PERSISTIR OS MOTIVOS RETORNO DOCUMENTO NOTA
			MotivoRetornoNota motivoRetornoNota = new MotivoRetornoNota();
			motivoRetornoNota.setNota(notaSaida);
			motivoRetornoNota.setMotivoRetornoDocumento(new MotivoRetornoDocumento());
			motivoRetornoNota.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(CHAVE_FATURAMENTO_CANCELAR_NF));

			motivoRetornoNota.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
			motivoRetornoNota.getMotivoRetornoDocumento().setCriador(securityService.getUsername());
			motivoRetornoNota.getMotivoRetornoDocumento().setDataCriacao(new Date());

			motivoRetornoNota.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

			motivoRetornoNota.getMotivoRetornoDocumento().setObservacao(parametroRepository.findByChave(PARAMETRO_FATURAMENTO_CANCELAMENTO_NF_PARCELAS).getValor());

			getRepository().save(notaSaida);

			MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(motivoRetornoNota.getMotivoRetornoDocumento());

			motivoRetornoNota.setMotivoRetornoDocumento(motivoRetornoDocumento);

			motivoRetornoNotaRepository.save(motivoRetornoNota);

			return getMapper().map(notaSaida, NotaSaidaTO.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	@Transactional()
	public NotaFiscalFaturamentoTO confirmaCadastroNF(@RequestBody NotaFiscalFaturamentoTO transferObj) throws Exception {
		try {
			for (NotaSaidaParcelaTO parcela : notaSaidaParcelaService.getListaParcelaByNotaSaidaId(transferObj.getCodigo())) {
				notaSaidaParcelaRepository.updateSituacao(NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO, parcela.getCodigo());
			}

			getRepository().updateNumero(transferObj.getCodigo(), transferObj.getNumero());
			getRepository().updateDataEmissao(transferObj.getCodigo(), transferObj.getDataEmissao());
			getRepository().updateResponsavelCadastroPrefeitura(securityService.getUser().getPessoaFisica(), transferObj.getCodigo());
			getRepository().updateDataCadastroPrefeitura(new Date(), transferObj.getCodigo());
			getRepository().updateStatus(transferObj.getCodigo(), NotaSaida.StatusNota.EM_RECEBIMENTO_FATURAMENTO);
			getRepository().updateDataAtualizacao(transferObj.getCodigo(), new Date());
			getRepository().updateAtualizador(transferObj.getCodigo(), getUser().getUsername());

			transferObj.setCodigo(transferObj.getCodigo());

			return transferObj;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	@Transactional
	public void removerNF(NotaFiscalFaturamentoTO transferObj) throws Exception {
		try {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;

			JPAQuery<FaturamentoEtapa> jpaQueryFaturamentoEtapa = faturamentoEtapaService.jpaQueryFaturamentoEtapa();
			jpaQueryFaturamentoEtapa.select(Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo)
			).where(qFaturamentoEtapa.notaSaida.codigo.eq(transferObj.getCodigo()));

			List<FaturamentoEtapa> etapas = jpaQueryFaturamentoEtapa.fetch();

			for (FaturamentoEtapa etapa : etapas) {
				faturamentoEtapaService.updateNotaSaida(etapa.getCodigo(), null);
				faturamentoEtapaService.updateSituacaoAFaturar(transferObj.getFaturamentoEtapaTO().getCodigo());
				faturamentoEtapaService.updateDataAtualizacao(etapa.getCodigo(), new Date());
				faturamentoEtapaService.updateAtualizador(etapa.getCodigo(), getUser().getUsername());
			}
			/*
			 * Se não houver nenhuma etapa "a faturar", tornar o faturamento "aberto".
			 */
			JPAQuery<FaturamentoEtapa> jpaQueryEtapaDeUmFaturamento = faturamentoEtapaService.jpaQueryFaturamentoEtapa();
			jpaQueryEtapaDeUmFaturamento.select(Projections.fields(
				FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.situacao)
			).where(qFaturamentoEtapa.faturamento.codigo.eq(transferObj.getFaturamentoEtapaTO().getFaturamento().getId()));

			List<FaturamentoEtapa> etapasDoFaturamento = jpaQueryEtapaDeUmFaturamento.fetch();
			boolean peloMenosUmAFaturar = false;
			for (FaturamentoEtapa etapa : etapasDoFaturamento) {
				if (FaturamentoEtapa.Status.FATURADA.equals(etapa.getSituacao())) {
					peloMenosUmAFaturar = true;
					break;
				}
			}
			if (!peloMenosUmAFaturar) {
				faturamentoService.updateSituacaoAberto(transferObj.getFaturamentoEtapaTO().getFaturamento().getId());
				faturamentoService.updateDataAtualizacao(transferObj.getFaturamentoEtapaTO().getFaturamento().getId(), new Date());
				faturamentoService.updateAtualizador(transferObj.getFaturamentoEtapaTO().getFaturamento().getId(), getUser().getUsername());
			}

			List<NotaSaidaParcela> listaParcelas = notaSaidaParcelaRepository.findByNotaSaidaCodigo(transferObj.getCodigo());
			listaParcelas.forEach(notaSaidaParcela -> notaSaidaParcelaRepository.deleteById(notaSaidaParcela.getCodigo()));
			notaSaidaRepository.deleteById(transferObj.getCodigo());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	protected void doAfterSaveNotaFiscalFaturamento(NotaFiscal entity, NotaFiscalFaturamentoTO to) {
		if (to.getListaNotaTributoAliquota() != null && !Check.isEmpty(to.getListaNotaTributoAliquota())) {
			entity.setListaNotaTributoAliquota(new ArrayList<>());
			for (NotaTributoAliquotaTO notaTributoAliquotaTO : to.getListaNotaTributoAliquota()) {
				NotaTributoAliquota notaTributoAliquota = getMapper().map(notaTributoAliquotaTO, NotaTributoAliquota.class);
				notaTributoAliquotaTO.setCodigo(null);
				notaTributoAliquota.setNota(entity);
				notaTributoAliquota.setIndividual(notaTributoAliquota.getIndividual() == null ? 0 : notaTributoAliquota.getIndividual());
				notaTributoAliquotaRepository.save(notaTributoAliquota);
			}
		}
	}

	private void doBeforeSaveNotaFiscalFaturamento(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO) {
		prepareValidateToSave(notaFiscalFaturamentoTO);

		if (notaFiscalFaturamentoTO != null && notaFiscalFaturamentoTO.getVencimento() != null) {
			Calendar dataVencimento = Calendar.getInstance();
			dataVencimento.setTimeInMillis(notaFiscalFaturamentoTO.getVencimento().getTime());
			dataVencimento.set(Calendar.AM_PM, Calendar.AM);
			dataVencimento.set(Calendar.HOUR, 12);
			notaFiscalFaturamentoTO.setVencimento(dataVencimento.getTime());
		}

		if (notaFiscalFaturamentoTO.getEditTributos() != null && notaFiscalFaturamentoTO.getEditTributos()) {
			// FIX: Refatorar adicionando o o serviço no notaTributoAliquotaService.
			JPAQuery jpaQuery = notaTributoAliquotaService.getJPAQuery();
			jpaQuery.where(QNotaTributoAliquota.notaTributoAliquota.nota.codigo.eq(notaFiscalFaturamentoTO.getCodigo()));
			jpaQuery.select(Projections.fields(NotaTributoAliquota.class, QNotaTributoAliquota.notaTributoAliquota.codigo));
			List<NotaTributoAliquota> notaTributoAliquotas = jpaQuery.fetch();

			for (NotaTributoAliquota notaTributoAliquota : notaTributoAliquotas) {
				notaTributoAliquotaService.deletarNotaTributoAliquota(notaTributoAliquota.getCodigo());
			}
		}
	}

	public void prepareValidateToSave(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO) throws ValidationException {
		if (NaturezaFornecimento.TipoFornecimento.MATERIAIS.getDescricao().equals(notaFiscalFaturamentoTO.getTipoFornecimento()))
			notaFiscalFaturamentoTO.setServicoTipo(null);

		if (!Check.isAssigned(notaFiscalFaturamentoTO.getEmpresa()) || !Check.isAssigned(notaFiscalFaturamentoTO.getEmpresa().getCodigo())) {
			throw new ValidationException("ValidationException", "Empresa é um campo de preenchimento obrigatório.");
		}

		if (Check.isEmpty(notaFiscalFaturamentoTO.getNumero())) {
			throw new ValidationException("ValidationException", "Número é um campo de preenchimento obrigatório.");
		} else {
			String num = notaFiscalFaturamentoTO.getNumero().replace("NF/", "");
			if (Check.isNumeric(num)) {
				int numero = Integer.parseInt(num);
				String numeroFinal = Util.formataNumero(numero, 9);
				notaFiscalFaturamentoTO.setNumero("NF/" + numeroFinal);
			} else {
				throw new ValidationException("ValidationException", "Número da NF inválido.");
			}
		}

		if (Check.isEmpty(notaFiscalFaturamentoTO.getStatus())) {
			throw new ValidationException("ValidationException", "Situação é um campo de preenchimento obrigatório.");
		}

		if (Check.isNotEmpty(notaFiscalFaturamentoTO.getStatus()) && notaFiscalFaturamentoTO.getStatus().equals("A processar")) {

			List<NotaSaida> lista = findNotasByNumeroAndEmpresa(notaFiscalFaturamentoTO.getNumero(), notaFiscalFaturamentoTO.getEmpresa().getCodigo(), NotaFiscal.TipoNotaFiscal.FATURAMENTO, Nota.StatusNota.ABERTA_FATURAMENTO);

			if (Check.isNotEmpty(lista)) {
				for (NotaSaida notaSaida : lista) {
					if (!notaSaida.getCodigo().equals(notaFiscalFaturamentoTO.getCodigo())) {
						Integer anoNotaSaida = Integer.parseInt(new SimpleDateFormat("yyyy").format(notaSaida.getDataEmissao()));
						Integer anoFiscalFaturamentoTO = Integer.parseInt(new SimpleDateFormat("yyyy").format(notaFiscalFaturamentoTO.getDataEmissao()));

						UnidadeTO unidade = unidadeService.obterUnidadeParaVerificacaoDeNumeracao(notaFiscalFaturamentoTO.getEmpresa().getCodigo());
						Boolean zerarNumeracao = unidade.getZerarNumeracaoNfAno();

						if (anoNotaSaida.equals(anoFiscalFaturamentoTO) || !zerarNumeracao) {
							throw new ValidationException("ValidationException", "A " + notaFiscalFaturamentoTO.getNumero() + " já está sendo utilizada na empresa \"" + notaFiscalFaturamentoTO.getEmpresa().getCodnome() + "\". Tente numerar a NF novamente.");
						}
					}
				}
			}
		}

		if (notaFiscalFaturamentoTO.getDataEmissao() == null)
			throw new ValidationException("ValidationException", "Emissão é um campo de preenchimento obrigatório.");

		if (Check.isEmpty(notaFiscalFaturamentoTO.getFormaPagamento()))
			throw new ValidationException("ValidationException", "Forma de pagamento é um campo de preenchimento obrigatório.");

		if (notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcela única") && notaFiscalFaturamentoTO.getVencimento() == null)
			throw new ValidationException("ValidationException", "Vencimento é um campo de preenchimento obrigatório.");

		if (notaFiscalFaturamentoTO.getFormaPagamento().equalsIgnoreCase("Parcela única")
			&& Check.isAssigned(notaFiscalFaturamentoTO.getVencimento())
			&& Util.comparaDatas(notaFiscalFaturamentoTO.getVencimento(), notaFiscalFaturamentoTO.getDataEmissao()) < 0) {
			throw new ValidationException("ValidationException", "A data de vencimento deve ser maior ou igual a data de emissão.");
		}

		if (Check.isEmpty(notaFiscalFaturamentoTO.getInfoComplementarContribuinte()))
			throw new ValidationException("ValidationException", "Discriminação dos serviços é um campo de preenchimento obrigatório.");

		if (notaFiscalFaturamentoTO.getFaturamentoEtapaTO() == null)
			throw new ValidationException("ValidationException", "O objeto do etapa não pode ser nulo nem vazio.");

		if (notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento() == null)
			throw new ValidationException("ValidationException", "O objeto do faturamento não pode ser nulo nem vazio.");

		//Verifica se a forma de pagamento é "Parcelada" e se não há nenhuma parcela associada
		if (Check.isEmpty(notaFiscalFaturamentoTO.getNotaSaidaParcelas()) && notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcelada"))
			throw new ValidationException("ValidationException", "O valor total das parcelas deve ser igual ao valor da etapa.");

		if (notaFiscalFaturamentoTO.getServicoTipo() != null && notaFiscalFaturamentoTO.getServicoTipo().getCodigo() != null) {
			ServicoTipo servicoTipo = findServicoTipoById(notaFiscalFaturamentoTO.getServicoTipo().getCodigo());
			if (servicoTipo == null)
				throw new ValidationException("ValidationException", "O Código do serviço informado é inválido.");
		}

		//Verifica se a forma de pagamento é "Parcela única" e se há mais de uma parcela associada
		if (!Check.isEmpty(notaFiscalFaturamentoTO.getNotaSaidaParcelas()) && notaFiscalFaturamentoTO.getNotaSaidaParcelas().size() > 1
			&& notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcela única"))
			throw new ValidationException("ValidationException", "A forma de pagamento \"Parcela única\" não aceita parcelamento.");

		if (notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcelada")) {
			BigDecimal valorTotalParcelas = new BigDecimal("0.00");
			BigDecimal valorTotalPermutas = new BigDecimal("0.00");
			BigDecimal valorTotalDescontos = new BigDecimal("0.00");
			BigDecimal valorTotalMultas = new BigDecimal("0.00");
			BigDecimal valorTotalJuros = new BigDecimal("0.00");

			//Percorre todas as parcelas somando os seus valores "Brutos"
			for (NotaSaidaParcelaTO parcela : notaFiscalFaturamentoTO.getNotaSaidaParcelas()) {
				if (parcela.getValorBruto() != null)
					valorTotalParcelas = valorTotalParcelas.add(parcela.getValorBruto());

				if (parcela.getValorPermuta() != null)
					valorTotalPermutas = valorTotalPermutas.add(parcela.getValorPermuta());

				if (parcela.getValorDesconto() != null)
					valorTotalDescontos = valorTotalDescontos.add(parcela.getValorDesconto());

				if (parcela.getValorMulta() != null)
					valorTotalMultas = valorTotalMultas.add(parcela.getValorMulta());

				if (parcela.getValorJuros() != null)
					valorTotalJuros = valorTotalJuros.add(parcela.getValorJuros());

				if (parcela.getValorBruto() == null)
					throw new ValidationException("ValidationException", "O valor da parcela é um campo de preenchimento obrigatório.");

				if (parcela.getValorPermuta() == null)
					throw new ValidationException("ValidationException", "O valor da permuta é um campo de preenchimento obrigatório.");

				if (parcela.getValorDesconto() == null)
					throw new ValidationException("ValidationException", "O valor do desconto é um campo de preenchimento obrigatório.");

				if (parcela.getValorMulta() == null)
					throw new ValidationException("ValidationException", "O valor da multa é um campo de preenchimento obrigatório.");

				if (parcela.getValorJuros() == null)
					throw new ValidationException("ValidationException", "O valor do juros é um campo de preenchimento obrigatório.");

				if (parcela.getDataVencimento() == null)
					throw new ValidationException("ValidationException", "A data de vencimento da parcela é um campo de preenchimento obrigatório.");
			}

			valorTotalParcelas.setScale(2, RoundingMode.HALF_EVEN);
			valorTotalPermutas.setScale(2, RoundingMode.HALF_EVEN);
			valorTotalDescontos.setScale(2, RoundingMode.HALF_EVEN);
			valorTotalMultas.setScale(2, RoundingMode.HALF_EVEN);
			valorTotalJuros.setScale(2, RoundingMode.HALF_EVEN);

			//Verifica se o total das parcelas é diferente do total da etapa
			if (valorTotalParcelas.setScale(2, RoundingMode.HALF_UP).doubleValue() != notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValor().setScale(2, RoundingMode.HALF_UP).doubleValue())
				throw new ValidationException("ValidationException", "O valor total das parcelas deve ser igual ao valor da etapa.");

			//Guarda o número da parcela com erro na data de vencimento
			int numeroParcela = 0;

			/*Guarda a data de vencimento da parcela anterior para que possa ser comparada com a data de vencimento da parcela corrente e
			 ser exibida na mensagem de validação caso haja divergencias nas datas*/
			Date vencimentoParcelaAnterior = null;

			for (NotaSaidaParcelaTO parcela : notaFiscalFaturamentoTO.getNotaSaidaParcelas()) {
				//Se for primeira parcela a data de vencimento da parcela anterior recebe a data de emissão da nota
				if (parcela.getOrdem() == 1)
					vencimentoParcelaAnterior = notaFiscalFaturamentoTO.getDataEmissao();

				//Verifica se a data de vencimento da primeira parcela é menor que a data de emissão da nota
				if (parcela.getOrdem() == 1 && Util.comparaDatas(parcela.getDataVencimento(), vencimentoParcelaAnterior) < 0) {
					numeroParcela = parcela.getOrdem();
					break;
				}

				//Verifica se a data de vencimento da parcela atual é menor ou igual que a data de vencimento da parcela anterior
				if (vencimentoParcelaAnterior != null && parcela.getOrdem() > 1 && Util.comparaDatas(parcela.getDataVencimento(), vencimentoParcelaAnterior) <= 0) {
					numeroParcela = parcela.getOrdem();
					break;
				}

				//atualiza a váriavel vencimentoParcelaAnterior com a data de vencimento da parcela corrente para que possa ser feita a próxima comparação
				vencimentoParcelaAnterior = parcela.getDataVencimento();
			}

			if (numeroParcela > 0) {
				if (numeroParcela == 1) {
					throw new ValidationException("ValidationException", "O vencimento da primeira parcela deve ser maior ou igual a data de emissão.");
				} else {
					///TODO CONFERIR, NAO FAZ SENTIDO
//					vencimentoParcelaAnterior = notaFiscalFaturamentoTO.getNotaSaidaParcelas().get(numeroParcela-1).getDataVencimento();
					throw new ValidationException("ValidationException", "O vencimento da parcela " + numeroParcela + " tem que ser posterior ao vencimento da parcela anterior.");
//					throw new ValidationException("ValidationException","O vencimento da parcela "+numeroParcela+" tem que ser posterior a "+Util.converteDateEmString(vencimentoParcelaAnterior, Constantes.DATE_FORMAT_DATE)+".");
				}
			}
		}
	}

	@Transactional
	private NotaSaida createOrUpdateNotaSaida(NotaFiscalFaturamentoTO transferObj) throws Exception {
		if (transferObj.getCodigo() == null) {
			transferObj.setDataCriacao(new Date());
			transferObj.setCriador(getUser().getUsername());
		} else {
			transferObj.setDataAtualizacao(new Date());
			transferObj.setAtualizador(getUser().getUsername());
		}
		if (transferObj.getCodigo() == null) {
			return getRepository().save(createNotaSaida(transferObj));
		} else {
			return updateNotaSaida(transferObj);
		}
	}

	private void atualizarNotaSaidaParcelaRetencao(Long codigoFaturamento, Long CodigoNotaFiscalFaturamento, NotaSaidaParcela notaSaidaParcela) {
		List<FaturamentoRetencaoTO> listaFaturamentoRetencaoTO = faturamentoService.findFaturamentoRetencaoByCodFaturamento(codigoFaturamento);
		Set<FaturamentoRetencao> listaFaturamentoRetencao = new TreeSet<>();
		for (FaturamentoRetencaoTO faturamentoRetencaoTO : listaFaturamentoRetencaoTO) {
			listaFaturamentoRetencao.add(getMapper().map(faturamentoRetencaoTO, FaturamentoRetencao.class));
		}
		if (Check.isNotEmpty(listaFaturamentoRetencao)) {
			notaSaidaParcelaRetencaoService.deleteNotaSaidaParcelaRetencaoDaParcela(CodigoNotaFiscalFaturamento);
			notaSaidaParcelaRetencaoRepository.saveAll(createListaParcelaRetencao(notaSaidaParcela, listaFaturamentoRetencao));
		}
	}

	@Transactional
	private NotaSaida updateNotaSaida(NotaFiscalFaturamentoTO transferObj) {
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;

		NotaSaida notaSaida = select(
			Projections.fields(
				NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.status,
				qNotaSaida.formaPagamento,
				Projections.fields(
					ServicoTipo.class,
					qServicoTipo.codigo,
					qServicoTipo.sigla,
					qServicoTipo.situacao
				).as(qNotaSaida.servicoTipo)
			)
		)
			.leftJoin(qNotaSaida.servicoTipo, qServicoTipo)
			.where(
				qNotaSaida.codigo.eq(transferObj.getCodigo())
					.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
			).fetchFirst();

		notaSaidaRepository.updateEmpresa(transferObj.getCodigo(), getMapper().map(transferObj.getEmpresa(), Unidade.class));

		//Edição da aba dados principais
		if (!transferObj.getNumero().equals(notaSaida.getNumero())) {
			notaSaidaRepository.updateNumero(transferObj.getCodigo(), transferObj.getNumero());
		}

		notaSaidaRepository.updateProtocoloEntrega(transferObj.getCodigo(), transferObj.getProtocoloEntrega());

		if (!Util.converteDateEmString(transferObj.getDataEmissao(), Constants.DATE_FORMAT_DATE).equals(Util.converteDateEmString(notaSaida.getDataEmissao(), Constants.DATE_FORMAT_DATE))) {
			notaSaidaRepository.updateDataEmissao(transferObj.getCodigo(), Util.converteDateEmString(transferObj.getDataEmissao(), Constants.DATE_FORMAT_FULL));
//			notaSaidaRepository.updateDataEmissao(transferObj.getCodigo(), transferObj.getDataEmissao());
		}

		if (notaSaida.getServicoTipo() != null && notaSaida.getServicoTipo().getCodigo() != null
				&& !Objects.equals(transferObj.getServicoTipo().getCodigo(), notaSaida.getServicoTipo().getCodigo())) {
			ServicoTipo servicoTipo = new ServicoTipo();
			servicoTipo.setCodigo(transferObj.getServicoTipo().getCodigo());
			notaSaidaRepository.updateServicoTipo(transferObj.getCodigo(), servicoTipo);
		}

		if (!transferObj.getNatureza().equals(notaSaida.getNatureza().toString())) {
			if (transferObj.getNatureza().equals("Venda à vista")) {
				notaSaidaRepository.updateNatureza(transferObj.getCodigo(), NotaFiscal.NaturezaNotaFiscal.VENDA_AVISTA);
			} else {
				notaSaidaRepository.updateNatureza(transferObj.getCodigo(), NotaFiscal.NaturezaNotaFiscal.VENDA_APRAZO);
			}
		}

		notaSaida.setNotaSaidaParcelas(notaSaidaParcelaRepository.findByNotaSaidaCodigo(notaSaida.getCodigo()));

		notaSaida.setListaNotaTributoAliquota(notaTributoAliquotaRepository.findByNotaCodigo(notaSaida.getCodigo()));

		if (transferObj.getFormaPagamento().equals(Nota.FormaPagamento.PARCELA_UNICA.toString())) {

			notaSaidaRepository.updateFormaPagamento(transferObj.getCodigo(), Nota.FormaPagamento.PARCELA_UNICA);
			// É uma nova parcela, criar uma nova e apagar a antiga.
			if (transferObj.getNotaSaidaParcelas().get(0).getCodigo() == null) {
				List<NotaSaidaParcela> lista = createParcelaUnica(transferObj, notaSaida);
				for (NotaSaidaParcela parcelaUnica : lista) {
					for (NotaSaidaParcela notaSaidaParcela : notaSaida.getNotaSaidaParcelas()) {
						if (notaSaidaParcela.getOrdem() != 1) {
							notaSaidaParcelaRepository.deleteById(notaSaidaParcela.getCodigo());
						} else {
							parcelaUnica = notaSaidaParcela;
						}
					}
					parcelaUnica.setOrdem(1);
					parcelaUnica.setValorLiquido(transferObj.getNotaSaidaParcelas().get(0).getValorLiquido());
					parcelaUnica.setValorBruto(transferObj.getNotaSaidaParcelas().get(0).getValorBruto());
					parcelaUnica.setValorDesconto(transferObj.getNotaSaidaParcelas().get(0).getValorDesconto());
					parcelaUnica.setValorDevolucao(transferObj.getNotaSaidaParcelas().get(0).getValorDevolucao());
					parcelaUnica.setValorJuros(transferObj.getNotaSaidaParcelas().get(0).getValorJuros());
					parcelaUnica.setValorPermuta(transferObj.getNotaSaidaParcelas().get(0).getValorPermuta());
					parcelaUnica.setDataVencimento(transferObj.getNotaSaidaParcelas().get(0).getDataVencimento());
					parcelaUnica.setDataPagamento(transferObj.getNotaSaidaParcelas().get(0).getDataPagamento());
					parcelaUnica = notaSaidaParcelaRepository.save(parcelaUnica);
					atualizarNotaSaidaParcelaRetencao(transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo(), transferObj.getCodigo(), parcelaUnica);
				}
			} else {
				// Se a parcela não é nova, atualizar a parcela que veio da tela e apagar as outras, caso nota tenha passado de parcelada para parcela única.
				for (NotaSaidaParcela notaSaidaParcela : notaSaida.getNotaSaidaParcelas()) {
					if (!NotaSaidaParcela.SituacaoParcela.FINALIZADA.equals(notaSaidaParcela.getSituacao()) && !NotaSaidaParcela.SituacaoParcela.CANCELADA.equals(notaSaidaParcela.getSituacao())) {
						if (notaSaidaParcela.getCodigo().equals(transferObj.getNotaSaidaParcelas().get(0).getCodigo())) {
							notaSaidaParcela = getMapper().map(transferObj.getNotaSaidaParcelas().get(0), NotaSaidaParcela.class);

							notaSaidaParcelaRepository.updateValorOrdem(notaSaidaParcela.getCodigo(), 1);
							notaSaidaParcelaRepository.updateValorLiquido(notaSaidaParcela.getCodigo(), transferObj.getNotaSaidaParcelas().get(0).getValorLiquido());
							notaSaidaParcelaRepository.updateValorBruto(notaSaidaParcela.getCodigo(), transferObj.getNotaSaidaParcelas().get(0).getValorBruto());

							// FIXME: Remover depois, Marcel criará o issue
							notaSaidaParcelaRepository.updateValorTributo(notaSaidaParcela.getCodigo(), BigDecimal.ZERO);

							notaSaidaParcelaRepository.updateValorPermuta(transferObj.getNotaSaidaParcelas().get(0).getValorPermuta(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateValorDesconto(transferObj.getNotaSaidaParcelas().get(0).getValorDesconto(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateValorDevolucao(notaSaidaParcela.getCodigo(), transferObj.getNotaSaidaParcelas().get(0).getValorDevolucao());
							notaSaidaParcelaRepository.updateValorJuros(transferObj.getNotaSaidaParcelas().get(0).getValorJuros(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateDataVencimento(transferObj.getNotaSaidaParcelas().get(0).getDataVencimento(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateDataPagamento(transferObj.getNotaSaidaParcelas().get(0).getDataPagamento(), notaSaidaParcela.getCodigo());

							notaSaidaParcelaRepository.updateDataAtualizacao(notaSaidaParcela.getCodigo(), new Date());
							notaSaidaParcelaRepository.updateAtualizador(notaSaidaParcela.getCodigo(), securityService.getUsername());

							notaSaidaParcela.setValorBruto(transferObj.getNotaSaidaParcelas().get(0).getValorBruto());
							atualizarNotaSaidaParcelaRetencao(transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo(), transferObj.getCodigo(), notaSaidaParcela);
						} else {
							notaSaidaParcelaRepository.deleteById(notaSaidaParcela.getCodigo());
						}
					}
				}
			}
			//Edição da aba parcelas
		} else if (transferObj.getFormaPagamento().equals(Nota.FormaPagamento.PARCELADA.toString())) {

			notaSaidaRepository.updateFormaPagamento(transferObj.getCodigo(), Nota.FormaPagamento.PARCELADA);

			List<Long> listaCodigos = new ArrayList<>();
			List<Long> listaCodigosBancoDados = new ArrayList<>();

			boolean todasCodigoNulas = true;

			List<NotaSaidaParcelaTO> parcelasNoBanco = notaSaidaParcelaService.getListaParcelaByNotaSaidaId(notaSaida.getCodigo());

			// Trocou de parcela única para parcelada
			if (Nota.FormaPagamento.PARCELA_UNICA.equals(notaSaida.getFormaPagamento())) {
				// A primeira parcela receberá o código apenas para manter o fluxo da tela, mesmo tendo todos os valores trocados
				transferObj.getNotaSaidaParcelas().get(0).setCodigo(parcelasNoBanco.get(0).getCodigo());
			}

			for (NotaSaidaParcelaTO notaSaidaParcelaTO : parcelasNoBanco) {
				NotaSaidaParcela notaSaidaParcela = (NotaSaidaParcela) notaSaidaParcelaService.obterJpaQueryNotaSaidaParcelaBasicaCodigoSituacao(notaSaidaParcelaTO.getCodigo()).fetchOne();

				listaCodigosBancoDados.add(notaSaidaParcela.getCodigo());

				if (!NotaSaidaParcela.SituacaoParcela.FINALIZADA.equals(notaSaidaParcela.getSituacao()) && !NotaSaidaParcela.SituacaoParcela.CANCELADA.equals(notaSaidaParcela.getSituacao())) {
					for (NotaSaidaParcelaTO to : transferObj.getNotaSaidaParcelas()) {
						if (to.getCodigo() == null) {
							continue;
						}
						todasCodigoNulas = false;
						if (to.getSituacao().equalsIgnoreCase("Finalizada (recebida)")) {
							listaCodigos.add(to.getCodigo());
							continue;
						}
						if (notaSaidaParcela.getCodigo().equals(to.getCodigo())) {
							listaCodigos.add(to.getCodigo());

							notaSaidaParcela = getMapper().map(to, NotaSaidaParcela.class);
							notaSaidaParcelaRepository.updateValorOrdem(notaSaidaParcela.getCodigo(), to.getOrdem());
							notaSaidaParcelaRepository.updateValorLiquido(notaSaidaParcela.getCodigo(), to.getValorLiquido());
							notaSaidaParcelaRepository.updateValorBruto(notaSaidaParcela.getCodigo(), to.getValorBruto());

							// FIXME: Remover depois, Marcel criará o issue
							notaSaidaParcelaRepository.updateValorTributo(notaSaidaParcela.getCodigo(), BigDecimal.ZERO);

							notaSaidaParcelaRepository.updateValorPermuta(to.getValorPermuta(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateValorDesconto(to.getValorDesconto(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateValorDevolucao(notaSaidaParcela.getCodigo(), to.getValorDevolucao());
							notaSaidaParcelaRepository.updateValorJuros(to.getValorJuros(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateDataVencimento(to.getDataVencimento(), notaSaidaParcela.getCodigo());
							notaSaidaParcelaRepository.updateDataPagamento(to.getDataPagamento(), notaSaidaParcela.getCodigo());

							notaSaidaParcelaRepository.updateDataAtualizacao(notaSaidaParcela.getCodigo(), new Date());
							notaSaidaParcelaRepository.updateAtualizador(notaSaidaParcela.getCodigo(), securityService.getUsername());

							if (to.getOrdem() == 1) {
								notaSaidaParcela.setValorBruto(transferObj.getNotaSaidaParcelas().get(0).getValorBruto());
								atualizarNotaSaidaParcelaRetencao(transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo(), transferObj.getCodigo(), notaSaidaParcela);
							}
						}
					}
				}
			}

			for (Long codigoBanco : listaCodigosBancoDados) {
				if (!listaCodigos.contains(codigoBanco)) {
					notaSaidaParcelaRepository.deleteById(codigoBanco);
				}
			}

			List<NotaSaidaParcelaTO> listaParcelasNovas = new ArrayList<>();

			/*
			 * É verificado se todas as parcelas são nulas. Se todas nulas e mesmo assim existir
			 * pelo menos 1 parcela no banco, isso significa que a NF passou de parcela única
			 * para parcelada. Assim, a primeira parcela vinda da tela deve ser essa parcelas
			 * única.
			 */
			if (todasCodigoNulas) {
				if (parcelasNoBanco.size() == 1) {
					// Havendo apenas 1 parcela, apenas colocar o código e o sistema irá atualizar essa parcela.
					transferObj.getNotaSaidaParcelas().forEach(to -> {
						if (to.getOrdem() == 1) {
							to.setCodigo(parcelasNoBanco.get(0).getCodigo());
							// Considerar como uma nova parcela
							listaParcelasNovas.add(to);
						}
					});
				}
			}

			// Aqui vamos criar as novas parcelas... código == null
			for (NotaSaidaParcelaTO to : transferObj.getNotaSaidaParcelas()) {
				if (to.getCodigo() == null) {
					listaParcelasNovas.add(to);
				}
			}

			List<NotaSaidaParcela> parcelas = createParcelas(listaParcelasNovas, notaSaida, transferObj, Boolean.TRUE);

			parcelas.forEach(notaSaidaParcela -> notaSaidaParcelaRepository.save(notaSaidaParcela));
		}

		//Edição da aba discriminação dos serviços
		if (!transferObj.getInfoComplementarContribuinte().equals(notaSaida.getInfoComplementarContribuinte())) {
			notaSaidaService.updateInfoComplementarContribuinte(notaSaida.getCodigo(), transferObj.getInfoComplementarContribuinte());
		}

		notaSaidaRepository.updateDataAtualizacao(transferObj.getCodigo(), new Date());
		notaSaidaRepository.updateAtualizador(transferObj.getCodigo(), securityService.getUsername());

		notaSaidaRepository.updateIssRetidoCliente(transferObj.getCodigo(), transferObj.getIssRetidoCliente());
		notaSaidaRepository.updateIssRetidoClienteRecebimento(transferObj.getCodigo(), transferObj.getIssRetidoCliente());

		return notaSaida;
	}

	private BigDecimal calcularValorTotalTributo(List<NotaTributoAliquotaTO> listaTributos, Boolean issRetidoCliente) {
		BigDecimal resultado = BigDecimal.ZERO;

		if (listaTributos != null && !listaTributos.isEmpty()) {
			for (NotaTributoAliquotaTO to : listaTributos) {
				if (to != null && to.getAliquota() != null && to.getAliquota().getCodigoCategoriaTributo() != null) {
					String codStrCatTributo = Long.toString(to.getAliquota().getCodigoCategoriaTributo());
					CategoriaTributoTO categoriaTributoTO = categoriaTributoService.buscarCategoriaTributoById(codStrCatTributo);
					if (categoriaTributoTO.getTributoTO().getCodigo() == 4) {
						if (issRetidoCliente) {
							resultado = resultado.add(to.getValor());
						}
					} else {
						resultado = resultado.add(to.getValor());
					}
				}
			}
		}

		return resultado;
	}

	private BigDecimal calcularValorLiquidoParcela(NotaSaidaParcelaTO to) {
		BigDecimal resultado = BigDecimal.ZERO;

		resultado = resultado.add(to.getValorBruto());
		resultado = resultado.subtract(to.getValorTributo());

		if (to.getValorRetencaoContratual() != null) {
			resultado = resultado.subtract(to.getValorRetencaoContratual());
		}

		resultado = resultado.subtract(to.getValorPermuta());
		resultado = resultado.subtract(to.getValorDesconto());
		resultado = resultado.add(to.getValorMulta());
		resultado = resultado.add(to.getValorJuros());

		return resultado;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private NotaSaida createNotaSaida(NotaFiscalFaturamentoTO transferObj) throws Exception {
		Pessoa credor = new Pessoa();
		credor.setCodigo(transferObj.getEmpresa().getCodigo());

		NotaSaida notaSaida = new NotaSaida();
		notaSaida.setCredor(credor);
		notaSaida.setNumero(transferObj.getNumero());
		notaSaida.setStatus(Nota.StatusNota.ABERTA_FATURAMENTO);
		notaSaida.setTipo(Pagamento.TipoPagamento.NOTA_FISCAL);
		notaSaida.setTipoNotaFiscal(NotaFiscal.TipoNotaFiscal.FATURAMENTO);
		notaSaida.setTipoFornecimento((NaturezaFornecimento.TipoFornecimento) Util.getEnumByString(NaturezaFornecimento.TipoFornecimento.class, transferObj.getTipoFornecimento()));

		if (transferObj.getServicoTipo() != null)
			notaSaida.setServicoTipo(getMapper().map(transferObj.getServicoTipo(), ServicoTipo.class));

		CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
		centroFinanceiro.setCodigo(transferObj.getFaturamentoEtapaTO().getFaturamento().getCentroFinanceiro().getCodigo());
		notaSaida.setCentroFinanceiro(centroFinanceiro);

		notaSaida.setEmpresa(new Unidade(transferObj.getEmpresa().getCodigo()));
		notaSaida.setProtocoloEntrega(transferObj.getProtocoloEntrega());

		BigDecimal valorServico = transferObj.getFaturamentoEtapaTO().getValor();
		BigDecimal valorMaterial = BigDecimal.ZERO;
		BigDecimal valorNota = valorServico.add(valorMaterial);
		notaSaida.setValor(valorNota);

		List<NotaTributoAliquota> listaNotaTributoAliquotas = Collections.emptyList();

		if (transferObj.getListaNotaTributoAliquota() != null) {
			listaNotaTributoAliquotas = createTributos(transferObj.getListaNotaTributoAliquota(), notaSaida);
		}

		if (Check.isNotEmpty(listaNotaTributoAliquotas)) {
			notaSaida.setListaNotaTributoAliquota(listaNotaTributoAliquotas);
		}

		if (transferObj.getNatureza().equals("Venda à vista")) {
			notaSaida.setNatureza(NotaFiscal.NaturezaNotaFiscal.VENDA_AVISTA);
		} else {
			notaSaida.setNatureza(NotaFiscal.NaturezaNotaFiscal.VENDA_APRAZO);
		}

		if (transferObj.getFormaPagamento().equals("Parcela única")) {
			notaSaida.setFormaPagamento(Nota.FormaPagamento.PARCELA_UNICA);
			notaSaida.setNotaSaidaParcelas(createParcelaUnica(transferObj, notaSaida));
		} else {
			notaSaida.setFormaPagamento(Nota.FormaPagamento.PARCELADA);
			notaSaida.setNotaSaidaParcelas(createParcelas(transferObj.getNotaSaidaParcelas(), notaSaida, transferObj, Boolean.FALSE));
		}

		notaSaida.setDataEmissao(transferObj.getDataEmissao());
		notaSaida.setEmitente(credor);

		notaSaida.setInfoComplementarContribuinte(transferObj.getInfoComplementarContribuinte());

		if (transferObj.getServicoTipo() != null && transferObj.getServicoTipo().getCodigo() != null) {
			ServicoTipo servicoTipo = new ServicoTipo();
			servicoTipo.setCodigo(transferObj.getServicoTipo().getCodigo());
			notaSaida.setServicoTipo(servicoTipo);
		}

		notaSaida.setIssRetidoCliente(transferObj.getIssRetidoCliente());
		notaSaida.setIssRetidoClienteRecebimento(transferObj.getIssRetidoCliente());

		notaSaida.setDocumentoFiscal(obterDocumentoFiscal());
		notaSaida.setIsentoICMS(obterIsentoIcms());
		notaSaida.setCriador(securityService.getUsername());
		notaSaida.setDataCriacao(new Date());

		return notaSaida;
	}

	private void deletaParcelas(List<NotaSaidaParcela> parcelas) {
		if (Check.isNotEmpty(parcelas)) {
			for (NotaSaidaParcela entity : parcelas) {
				notaSaidaParcelaRepository.deleteById(entity.getCodigo());
			}
			parcelas.clear();
			notaSaidaParcelaRepository.flush();
		}
	}

	private void deletaParcelasDaNota(NotaSaida notaSaida) {

		if (notaSaida.getNotaSaidaParcelas() == null) {
			notaSaida.setNotaSaidaParcelas(new ArrayList<>());
		} else {
			deletaParcelas(notaSaida.getNotaSaidaParcelas());
		}
	}

	private boolean obterIsentoIcms() {
		String parametroIsentoIcms = "faturamento.notafiscal.isento_icms";
		Parametro resultadoIsentoIcms = parametroRepository.findById(parametroIsentoIcms).orElse(null);

		if (resultadoIsentoIcms != null)
			return resultadoIsentoIcms.getValor().equals("1") ? true : false;
		else
			return false;
	}

	private DocumentoFiscal obterDocumentoFiscal() {
		String codigoReceita = "55";
		String parametroDocFiscal = "faturamento.notafiscal.documentoFiscal.codigo";
		DocumentoFiscal documentoFiscal = null;

		Parametro resultadoDocFiscal = parametroRepository.findById(parametroDocFiscal).orElse(null);
		if (resultadoDocFiscal != null) {
			documentoFiscal = documentoFiscalRepository.findByCodigo(Long.parseLong(resultadoDocFiscal.getValor()));
		} else {
			documentoFiscal = documentoFiscalRepository.findByCodigoReceita(codigoReceita);
		}
		return documentoFiscal;
	}

	private List<NotaTributoAliquota> createTributos(List<NotaTributoAliquotaTO> listaNotaTributoAliquota, Nota nota) throws Exception {
		Objects.requireNonNull(listaNotaTributoAliquota);
		Objects.requireNonNull(nota);

		List<NotaTributoAliquota> tributos = new ArrayList<>();

		for (NotaTributoAliquotaTO notaTributoAliquotaTO : listaNotaTributoAliquota) {

			NotaTributoAliquota notaTributoAliquota = new NotaTributoAliquota();
			notaTributoAliquota.setNota(nota);
			notaTributoAliquota.setPercentual(notaTributoAliquotaTO.getPercentual());

			Aliquota aliquota = aliquotaRepository.findById(notaTributoAliquotaTO.getAliquota().getCodigo()).get();

			// FIXME: String????
			CategoriaTributoTO categoriaTributo = categoriaTributoService.getCategoriaTributoByCod(aliquota.getCategoriaTributo().getCodigo().toString());

			notaTributoAliquota.setAliquota(aliquota);
			notaTributoAliquota.setBaseCalculo(notaTributoAliquotaTO.getBaseCalculo());

			// O valor foi zerado no dialog retenção
			if (notaTributoAliquotaTO.getValor().equals(BigDecimal.ZERO)) {
				notaTributoAliquota.setValor(BigDecimal.ZERO);
			} else {
				if (categoriaTributo.getTributoTO().getCodigo().equals(4L)) {
					if (categoriaTributo.getCidade() != null && categoriaTributo.getCidade().getCodigo() != null && categoriaTributo.getCidade().getCodigo().equals(3304557L)) {
						notaTributoAliquota.setValor(notaTributoAliquotaTO.getBaseCalculo().multiply(notaTributoAliquotaTO.getPercentual()).divide(new BigDecimal(100), 2, RoundingMode.DOWN));
					} else {
						notaTributoAliquota.setValor(notaTributoAliquotaTO.getBaseCalculo().multiply(notaTributoAliquotaTO.getPercentual()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN));
					}
				} else {
					notaTributoAliquota.setValor(notaTributoAliquotaTO.getBaseCalculo().multiply(notaTributoAliquotaTO.getPercentual()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN));
				}
			}

			tributos.add(notaTributoAliquota);
		}

		return tributos;
	}

	private List<NotaSaidaParcela> createParcelas(List<NotaSaidaParcelaTO> listaParcelas, NotaSaida nota, NotaFiscalFaturamentoTO notaFiscalFaturamento, Boolean isUpdate) {
		List<NotaSaidaParcela> parcelas = new ArrayList<>();
		for (NotaSaidaParcelaTO parcela : listaParcelas) {
			if ("Finalizada (recebida)".equals(parcela.getSituacao())) {
				continue;
			}
			parcelas.add(criarParcela(parcela, nota, notaFiscalFaturamento, isUpdate));
		}
		return parcelas;
	}

	private NotaSaidaParcela criarParcela(NotaSaidaParcelaTO parcela, NotaSaida nota, NotaFiscalFaturamentoTO notaFiscalFaturamento, Boolean isUpdate) {
		NotaSaidaParcela notaSaidaParcela = new NotaSaidaParcela();
		if (parcela.getCodigo() != null) {
			notaSaidaParcela = notaSaidaParcelaRepository.findById(parcela.getCodigo()).orElse(null);
		}
		notaSaidaParcela.setNotaSaida(nota);
		notaSaidaParcela.setOrdem(parcela.getOrdem());

		if (parcela.getSituacao() == null) {
			notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.ABERTA);
		} else if (NotaSaidaParcela.SituacaoParcela.ABERTA.getDescricao().equals(parcela.getSituacao()) && Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO.equals(nota.getStatus())) {
			notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO);
		} else {
			for (NotaSaidaParcela.SituacaoParcela situacaoParcela : NotaSaidaParcela.SituacaoParcela.values()) {
				if (situacaoParcela.getDescricao().equals(parcela.getSituacao()))
					notaSaidaParcela.setSituacao(situacaoParcela);
			}
		}

		notaSaidaParcela.setValorBruto(parcela.getValorBruto());
		notaSaidaParcela.setValorLiquido(parcela.getValorLiquido());
		notaSaidaParcela.setValorDesconto(parcela.getValorDesconto());
		notaSaidaParcela.setValorJuros(parcela.getValorJuros());
		notaSaidaParcela.setValorMulta(parcela.getValorMulta());
		notaSaidaParcela.setValorPermuta(parcela.getValorPermuta());
		notaSaidaParcela.setDataVencimento(parcela.getDataVencimento());
		notaSaidaParcela.setDataPagamento(parcela.getDataVencimento());

		notaSaidaParcela.setDataPagamento(feriadoService.devolveDataUtil(parcela.getDataVencimento()));
		notaSaidaParcela.setDataAgendamento(feriadoService.devolveDataUtil(notaSaidaParcela.getDataPagamento(), 1));

		if (isUpdate != null && !isUpdate) {
			notaSaidaParcela.setDataCriacao(new Date());
			notaSaidaParcela.setCriador(securityService.getUsername());
		} else if (isUpdate != null) {
			notaSaidaParcela.setDataCriacao(new Date());
			notaSaidaParcela.setCriador(securityService.getUsername());

			notaSaidaParcela.setAtualizador(securityService.getUsername());
			notaSaidaParcela.setDataAtualizacao(new Date());
		}

		notaSaidaParcela.setContaCorrentePrevista(mergeTO(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getContaCorrente(), new ContaCorrente()));
		notaSaidaParcela.setContaCorrenteRecebimento(mergeTO(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getContaCorrente(), new ContaCorrente()));
		notaSaidaParcela.setTipoDocumentoPrevisto(getTipoDocumentoPagamentoByDesc(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento()));
		notaSaidaParcela.setTipoDocumentoRecebimento(getTipoDocumentoPagamentoByDesc(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento()));

		List<FaturamentoRetencaoTO> listaFaturamentoRetencaoTO = faturamentoService.findFaturamentoRetencaoByCodFaturamento(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getCodigo());
		List<FaturamentoRetencao> listaFaturamentoRetencao = new ArrayList<>();
		for (FaturamentoRetencaoTO faturamentoRetencaoTO : listaFaturamentoRetencaoTO) {
			listaFaturamentoRetencao.add(getMapper().map(faturamentoRetencaoTO, FaturamentoRetencao.class));
		}
		if (Check.isNotEmpty(listaFaturamentoRetencao))
			notaSaidaParcela.setParcelaRetencoes(createListaParcelaRetencao(notaSaidaParcela, new TreeSet<>(listaFaturamentoRetencao)));

		if (parcela.getContaCorrentePrevista() != null) {
			notaSaidaParcela.setContaCorrentePrevista(getMapper().map(parcela.getContaCorrentePrevista(), ContaCorrente.class));
		} else {
			if (notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getContaCorrente() != null) {
				notaSaidaParcela.setContaCorrentePrevista(getMapper().map(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getContaCorrente(), ContaCorrente.class));
			}
		}

		if (parcela.getContaCorrenteRecebimento() != null) {
			notaSaidaParcela.setContaCorrenteRecebimento(getMapper().map(parcela.getContaCorrenteRecebimento(), ContaCorrente.class));
		} else {
			if (notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getContaCorrente() != null) {
				notaSaidaParcela.setContaCorrentePrevista(getMapper().map(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getContaCorrente(), ContaCorrente.class));
			}
		}

		if (parcela.getTipoDocumentoPrevisto() != null) {
			notaSaidaParcela.setTipoDocumentoPrevisto(getTipoDocumentoPagamentoByDesc(parcela.getTipoDocumentoPrevisto()));
		} else {
			if (notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento() != null)
				notaSaidaParcela.setTipoDocumentoPrevisto(getTipoDocumentoPagamentoByDesc(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento()));
		}

		if (parcela.getTipoDocumentoRecebimento() != null) {
			notaSaidaParcela.setTipoDocumentoRecebimento(getTipoDocumentoPagamentoByDesc(parcela.getTipoDocumentoRecebimento()));
		} else {
			if (notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento() != null)
				notaSaidaParcela.setTipoDocumentoRecebimento(getTipoDocumentoPagamentoByDesc(notaFiscalFaturamento.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento()));
		}

		return notaSaidaParcela;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private List<NotaSaidaParcela> createParcelaUnica(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO, NotaSaida nota) {
		List<NotaSaidaParcela> parcelas = new ArrayList<>();

		NotaSaidaParcela notaSaidaParcela = new NotaSaidaParcela();
		notaSaidaParcela.setNotaSaida(nota);
		notaSaidaParcela.setOrdem(1);

		if (Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO.equals(nota.getStatus())) {
			notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO);
		} else {
			notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.ABERTA);
		}

		AtomicReference<BigDecimal> totalValorTributos = new AtomicReference<>(BigDecimal.ZERO);

		if(!NaturezaFornecimento.TipoFornecimento.MATERIAIS.getDescricao().equals(notaFiscalFaturamentoTO.getTipoFornecimento())) {
			if (nota.getListaNotaTributoAliquota() != null && !nota.getListaNotaTributoAliquota().isEmpty()) {
				nota.getListaNotaTributoAliquota().forEach(notaTributoAliquota -> {
					// tributo Iss
					if (notaTributoAliquota.getAliquota().getCategoriaTributo().getTributo().getCodigo() == 4) {
						if (notaFiscalFaturamentoTO.getIssRetidoCliente()) {
							totalValorTributos.set(totalValorTributos.get().add(notaTributoAliquota.getValor()));
						}
					} else {
						totalValorTributos.set(totalValorTributos.get().add(notaTributoAliquota.getValor()));
					}
				});
			}
		}

		notaSaidaParcela.setValorBruto(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValor());
		notaSaidaParcela.setValorLiquido(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValor().subtract(totalValorTributos.get()));

		notaSaidaParcela.setValorDesconto(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValorDesconto());
		notaSaidaParcela.setValorJuros(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValorJuros());
		notaSaidaParcela.setValorMulta(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValorMulta());
		notaSaidaParcela.setValorPermuta(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValorPermuta());
		notaSaidaParcela.setDataVencimento(notaFiscalFaturamentoTO.getVencimento());
//		notaSaidaParcela.setDataPagamento(notaFiscalFaturamentoTO.getVencimento());

		notaSaidaParcela.setDataCriacao(new Date());
		notaSaidaParcela.setCriador(securityService.getUsername());

		notaSaidaParcela.setContaCorrentePrevista(mergeTO(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento().getContaCorrente(), new ContaCorrente()));
		notaSaidaParcela.setContaCorrenteRecebimento(mergeTO(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento().getContaCorrente(), new ContaCorrente()));
		notaSaidaParcela.setTipoDocumentoPrevisto(getTipoDocumentoPagamentoByDesc(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento()));
		notaSaidaParcela.setTipoDocumentoRecebimento(getTipoDocumentoPagamentoByDesc(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento()));

		List<FaturamentoRetencaoTO> listaFaturamentoRetencao = faturamentoRetencaoService.obterListaPorCodigoFaturamento(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento().getCodigo());

		if (Check.isNotEmpty(listaFaturamentoRetencao)) {
			List<FaturamentoRetencao> list = new ArrayList<>();
			for (FaturamentoRetencaoTO faturamentoRetencaoTO : listaFaturamentoRetencao) {
				list.add(getMapper().map(faturamentoRetencaoTO, FaturamentoRetencao.class));
			}
			notaSaidaParcela.setParcelaRetencoes(createListaParcelaRetencao(notaSaidaParcela, new TreeSet<>(list)));
		}
		notaSaidaParcela.setDataPagamento(feriadoService.devolveDataUtil(notaSaidaParcela.getDataVencimento()));
		notaSaidaParcela.setDataAgendamento(feriadoService.devolveDataUtil(notaSaidaParcela.getDataPagamento(), 1));
		parcelas.add(notaSaidaParcela);

		return parcelas;
	}

	private List<NotaSaidaParcelaRetencao> createListaParcelaRetencao(NotaSaidaParcela notaSaidaParcela, Set<FaturamentoRetencao> listaFaturamentoRetencao) {
		List<NotaSaidaParcelaRetencao> listaParcelaRetencao = new ArrayList<>();

		for (FaturamentoRetencao faturamentoRetencao : listaFaturamentoRetencao) {

			NotaSaidaParcelaRetencao parcelaRetencao = null;

			if (notaSaidaParcela.getCodigo() != null) {
				parcelaRetencao = notaSaidaParcelaRetencaoRepository.findNotaSaidaParcelaRetencaoByCodigoNotaSaidaParcelaAndTipo(notaSaidaParcela.getCodigo(), getTipoRetencaoByTipo(faturamentoRetencao.getTipo()));
			}

			if (parcelaRetencao == null) {
				parcelaRetencao = new NotaSaidaParcelaRetencao();
				parcelaRetencao.setValorRetido(BigDecimal.ZERO);
			}

			parcelaRetencao.setParcela(notaSaidaParcela);
			Tipo tipo = tipoRepository.findById(faturamentoRetencao.getTipo().getCodigo()).get();
			parcelaRetencao.setTipo(getTipoRetencaoByTipo(tipo));

			BigDecimal valorRetencao;
			valorRetencao = notaSaidaParcela.getValorBruto().multiply(faturamentoRetencao.getPercentual()).divide(new BigDecimal("100"));
			parcelaRetencao.setValorAReter(valorRetencao);

			listaParcelaRetencao.add(parcelaRetencao);
		}

		return listaParcelaRetencao;
	}

	//FIXME Remover essa batelada de métodos de mapeamento e usar os métodos padrão

	//Mapas para converte algumas entidades e to's
	private FaturamentoEtapaTO mapTO(FaturamentoEtapa entity) {
		if (entity == null) return null;
		return getMapper().map(entity, FaturamentoEtapaTO.class);
	}

	private NotaSaidaTO mapTO(NotaSaida entity) {
		if (entity == null) return null;
		return getMapper().map(entity, NotaSaidaTO.class);
	}

	private NotaSaidaParcelaRetencaoTO mapTO(NotaSaidaParcelaRetencao entity) {
		if (entity == null) return null;
		return getMapper().map(entity, NotaSaidaParcelaRetencaoTO.class);
	}

	private ServicoTipoTO mapTO(ServicoTipo entity) {
		if (entity == null) return null;
		return getMapper().map(entity, ServicoTipoTO.class);
	}

	private UnidadeTO mapTO(Unidade entity) {
		if (entity == null) return null;
		return getMapper().map(entity, UnidadeTO.class);
	}

	private PessoaTO mapTO(Pessoa entity) {
		if (entity == null) return null;
		return getMapper().map(entity, PessoaTO.class);
	}

	private List<ServicoTipoPesquisaTO> mapSearchServicoTO(Iterable<ServicoTipo> iterable) {
		return dozerHelper.map(iterable, ServicoTipoPesquisaTO.class);
	}

	private List<NotaSaidaParcelaTO> mapSearchParcelaTO(Iterable<NotaSaidaParcela> iterable) {
		return dozerHelper.map(iterable, NotaSaidaParcelaTO.class);
	}

	private List<NotaSaidaParcelaRetencaoTO> mapSearchParcelaRetencaoTO(Iterable<NotaSaidaParcelaRetencao> iterable) {
		return dozerHelper.map(iterable, NotaSaidaParcelaRetencaoTO.class);
	}

	private List<LoteInterfaceNotaTO> mapLoteNotaTO(Iterable<LoteInterfaceNota> iterable) {
		return dozerHelper.map(iterable, LoteInterfaceNotaTO.class);
	}

	private List<LoteInterfaceTO> mapLoteTO(Iterable<LoteInterface> iterable) {
		return dozerHelper.map(iterable, LoteInterfaceTO.class);
	}

	private List<FaturamentoRetencaoTO> mapFaturamentoRetencaoTO(Iterable<FaturamentoRetencao> iterable) {
		return dozerHelper.map(iterable, FaturamentoRetencaoTO.class);
	}

	private NotaSaida mergeTO(NotaSaidaTO object, NotaSaida entity) {
		if (object == null) return entity;
		entity = getMapper().map(object, entity.getClass());
		return entity;
	}

	private NotaSaidaParcela mergeTO(NotaSaidaParcelaTO object, NotaSaidaParcela entity) {
		if (object == null) return entity;
		entity = getMapper().map(object, entity.getClass());
		return entity;
	}

	private Faturamento mergeTO(FaturamentoTO object, Faturamento entity) {
		if (object == null) return entity;
		entity = getMapper().map(object, entity.getClass());
		return entity;
	}

	private ContaCorrente mergeTO(ContaCorrenteTO object, ContaCorrente entity) {
		if (object == null) return entity;
		entity = getMapper().map(object, entity.getClass());
		return entity;
	}

	//converte manualmente um nota de saida em nota faturamento
	private NotaFiscalFaturamentoTO converteNotaSaidaEmNotaFaturamento(NotaSaidaTO notaSaidaTO) {
		NotaFiscalFaturamentoTO notaFaturamento = new NotaFiscalFaturamentoTO();

		notaFaturamento.setCodigo(notaSaidaTO.getCodigo());
		notaFaturamento.setNumero(notaSaidaTO.getNumero());
		notaFaturamento.setStatus(notaSaidaTO.getStatus());
		notaFaturamento.setIssRetidoCliente(notaSaidaTO.getIssRetidoCliente());
		notaFaturamento.setIssRetidoClienteRecebimento(notaSaidaTO.getIssRetidoClienteRecebimento());
		notaFaturamento.setDataEmissao(notaSaidaTO.getDataEmissao());
		notaFaturamento.setServicoTipo(notaSaidaTO.getServicoTipo());
		notaFaturamento.setFormaPagamento(notaSaidaTO.getFormaPagamento());
		notaFaturamento.setNatureza(notaSaidaTO.getNatureza());
		notaFaturamento.setEmpresa(notaSaidaTO.getEmpresa());
		notaFaturamento.setInfoComplementarContribuinte(notaSaidaTO.getInfoComplementarContribuinte());
		notaFaturamento.setCriador(notaSaidaTO.getCriador());
		notaFaturamento.setDataCriacao(notaSaidaTO.getDataCriacao());
		notaFaturamento.setAtualizador(notaSaidaTO.getAtualizador());
		notaFaturamento.setDataAtualizacao(notaSaidaTO.getDataAtualizacao());

		notaFaturamento.setNotaAssociadaCancelamento(notaSaidaTO.getNotaAssociadaCancelamento());
		notaFaturamento.setRespNotaAssociadaCancelamento(notaSaidaTO.getRespNotaAssociadaCancelamento());
		notaFaturamento.setDataRespNotaAssociadaCancelamento(notaSaidaTO.getDataRespNotaAssociadaCancelamento());

		if (notaSaidaTO.getResponsavelNumeracao() != null) {
			notaFaturamento.setResponsavelNumeracao(notaSaidaTO.getResponsavelNumeracao());
			notaFaturamento.setDataNumeracao(notaSaidaTO.getDataNumeracao());
		}
		if (notaSaidaTO.getResponsavelCancelarNumeracao() != null) {
			notaFaturamento.setResponsavelCancelarNumeracao(notaSaidaTO.getResponsavelCancelarNumeracao());
			notaFaturamento.setDataCancelamentoNumeracao(notaSaidaTO.getDataCancelamentoNumeracao());
		}
		if (notaSaidaTO.getResponsavelCadastroPrefeitura() != null) {
			notaFaturamento.setResponsavelCadastroPrefeitura(notaSaidaTO.getResponsavelCadastroPrefeitura());
			notaFaturamento.setDataCadastroPrefeitura(notaSaidaTO.getDataCadastroPrefeitura());
		}
		if (notaSaidaTO.getResponsavelCadastroPrefeitura() != null) {
			notaFaturamento.setResponsavelCadastroPrefeitura(notaSaidaTO.getResponsavelCadastroPrefeitura());
			notaFaturamento.setDataCadastroPrefeitura(notaSaidaTO.getDataCadastroPrefeitura());
		}
		if (notaSaidaTO.getResponsavelCancelamento() != null) {
			notaFaturamento.setResponsavelCancelamento(notaSaidaTO.getResponsavelCancelamento());
			notaFaturamento.setDataCancelamento(notaSaidaTO.getDataCancelamento());
		}
		if (notaSaidaTO.getResponsavelReversaoCancelamento() != null) {
			notaFaturamento.setResponsavelReversaoCancelamento(notaSaidaTO.getResponsavelReversaoCancelamento());
			notaFaturamento.setDataReversaoCancelamento(notaSaidaTO.getDataReversaoCancelamento());
		}
		if (notaSaidaTO.getProtocoloEntrega() != null) {
			notaFaturamento.setProtocoloEntrega(notaSaidaTO.getProtocoloEntrega());
		}
		if (notaSaidaTO.getValor() != null) {
			notaFaturamento.setValor(notaSaidaTO.getValor());
		}


		return notaFaturamento;
	}

	/**
	 * Busca o TipoDocumentoPagamento pela sua descrição.
	 *
	 * @param descricao Descrição do TipoDocumentoPagamento.
	 * @return TipoDocumentoPagamento
	 */
	private Pagamento.TipoDocumentoPagamento getTipoDocumentoPagamentoByDesc(String descricao) {
		for (Pagamento.TipoDocumentoPagamento tipo : Pagamento.TipoDocumentoPagamento.values()) {
			if (descricao != null && descricao.equals(tipo.toString())) {
				return tipo;
			}
		}
		return null;
	}

	@Override
	protected void doAfterLoad(NotaSaidaTO transferObj) {
		transferObj.getNotaSaidaParcelas().sort((o1, o2) -> o1.getOrdem().compareTo(o2.getOrdem()));
	}

	/**
	 * Cria um NotaFiscalFaturamentoTO pelo ID da nota de saida,
	 * para ser exibido na tela de impressão.
	 * FIXME: Verificar se pode refatorar método.
	 *
	 * @param id
	 * @return
	 */
	@Override
	public NotaFiscalFaturamentoTO findNotaFiscalParaImpressaoByCodigo(String id) {
		NotaSaida notaSaida = getNotaSaidaById(Long.parseLong(id));
		notaSaida.setDataEmissao(ajustarHorario(notaSaida.getDataEmissao()));
		List<NotaSaidaParcelaTO> listaParcelasTO = notaSaidaParcelaService.getListaParcelaByNotaSaidaId(notaSaida.getCodigo());
		List<NotaSaidaParcela> listaParcelas = new ArrayList<>();

		for (NotaSaidaParcelaTO nspTO : listaParcelasTO) {
			nspTO.setDataVencimento(ajustarHorario(nspTO.getDataVencimento()));
			nspTO.setDataAgendamento(ajustarHorario(nspTO.getDataAgendamento()));
			nspTO.setDataPagamento(ajustarHorario(nspTO.getDataPagamento()));
			nspTO.setDataContabilizacao(ajustarHorario(nspTO.getDataContabilizacao()));

			listaParcelas.add(getMapper().map(nspTO, NotaSaidaParcela.class));
		}

		notaSaida.setNotaSaidaParcelas(listaParcelas);
		NotaSaidaTO notaSaidaTO = mapTO(notaSaida);
		NotaFiscalFaturamentoTO notaFaturamento = converteNotaSaidaEmNotaFaturamento(notaSaidaTO);

		List<NotaSaidaParcelaTO> parcelas = mapSearchParcelaTO(notaSaida.getNotaSaidaParcelas());
		notaFaturamento.setNotaSaidaParcelas(parcelas);

		FaturamentoEtapaTO faturamentoEtapaTO = faturamentoEtapaService.getFaturamentoEtapaByNotaSaidaId(notaSaida.getCodigo());
		notaFaturamento.setFaturamentoEtapaTO(faturamentoEtapaTO);
		notaFaturamento.setServicoTipo(mapTO(notaSaida.getServicoTipo()));

		if (notaFaturamento != null && notaFaturamento.getCriador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(notaFaturamento.getCriador());
			if (list != null && !list.isEmpty()) {
				notaFaturamento.setCriador(list.get(0).getNome());
			}
		}

		if (notaFaturamento != null && notaFaturamento.getAtualizador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(notaFaturamento.getAtualizador());
			if (list != null && !list.isEmpty()) {
				notaFaturamento.setAtualizador(list.get(0).getNome());
			}
		}

		notaFaturamento.setValorLiquido(calcularValorLiquido(notaSaidaTO));

		return notaFaturamento;
	}

	/**
	 * O ajuste abaixo foi feito para acertar a exibição do campo data de várias datas,
	 * pois devido ao erro no horário do servidor, é exibido o dia anterior ao gravado	 *
	 * @param data
	 * @return
	 */
	private Date ajustarHorario(Date data) {
		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(data.getTime());
		calendar.set(Calendar.HOUR, 12);
		return calendar.getTime();
	}

	/**
	 * Busca uma nota de saida pelo id.
	 *
	 * @param id
	 * @return
	 */
	private NotaSaida getNotaSaidaById(Long id) {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QUnidade qUnidade = QUnidade.unidade;
		QEndereco qEndereco = QEndereco.endereco1;
		QPessoa qPessoa = QPessoa.pessoa;
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;


		JPAQuery<NotaSaida> jpaQueryNotaSaida = select();

		QBean qBean = Projections.bean(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.status,
			qNotaSaida.numero,
			qNotaSaida.dataEmissao,
			qNotaSaida.criador,
			qNotaSaida.atualizador,
			qNotaSaida.issRetidoCliente,
			qNotaSaida.issRetidoClienteRecebimento,
			qNotaSaida.servicoTipo,
			qNotaSaida.infoComplementarContribuinte,

			qNotaSaida.dataCriacao,
			qNotaSaida.dataAtualizacao,

			qNotaSaida.dataNumeracao,
			qNotaSaida.dataCancelamentoNumeracao,
			qNotaSaida.dataCadastroPrefeitura,
			qNotaSaida.dataCancelamento,
			qNotaSaida.valor,
			Projections.bean(Pessoa.class, qNotaSaida.responsavelNumeracao.codigo, qNotaSaida.responsavelNumeracao.nome).as("responsavelNumeracao"),
			Projections.bean(Pessoa.class, qNotaSaida.responsavelCancelarNumeracao.codigo, qNotaSaida.responsavelCancelarNumeracao.nome).as("responsavelCancelarNumeracao"),
			Projections.bean(Pessoa.class, qNotaSaida.responsavelCadastroPrefeitura.codigo, qNotaSaida.responsavelCadastroPrefeitura.nome).as("responsavelCadastroPrefeitura"),
			Projections.bean(Pessoa.class, qNotaSaida.responsavelCancelamento.codigo, qNotaSaida.responsavelCancelamento.nome).as("responsavelCancelamento"),

			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.nome,
				qUnidade.codnome,
				qUnidade.documento,
				qUnidade.inscricaoEstadual,
				qUnidade.inscricaoMunicipal,
				Projections.bean(Endereco.class, qEndereco.codigo, qEndereco.bairro, qEndereco.cep).as("endereco")
			).as("empresa")
		);

		jpaQueryNotaSaida
			.select(qBean)
			.from(qNotaSaida)
			.leftJoin(qNotaSaida.empresa, qUnidade)
			.leftJoin(qUnidade.endereco, qEndereco)
			.leftJoin(qNotaSaida.responsavelNumeracao, qPessoa)
			.leftJoin(qNotaSaida.responsavelCancelarNumeracao, qPessoa)
			.leftJoin(qNotaSaida.responsavelCadastroPrefeitura, qPessoa)
			.leftJoin(qNotaSaida.responsavelCancelamento, qPessoa)
			.leftJoin(qNotaSaida.servicoTipo, qServicoTipo)
			.where(qNotaSaida.codigo.eq(id));

		return jpaQueryNotaSaida.fetchOne();
	}

	private NotaSaida getMiniNotaSaidaById(Long id) {
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<NotaSaida> jpaQueryNotaSaida = select();

		QBean qBean = Projections.bean(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.dataEmissao,

			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.codnome,

				Projections.bean(
					Unidade.class,
					qUnidade.matriz.codigo,
					qUnidade.matriz.codnome
				).as("matriz")

			).as("empresa")
		);

		jpaQueryNotaSaida
			.select(qBean)
			.from(qNotaSaida)
			.leftJoin(qNotaSaida.empresa, qUnidade)
			.where(qNotaSaida.codigo.eq(id));

		return jpaQueryNotaSaida.fetchOne();
	}

	private List<NotaSaida> findLatestNotaByUnidadeCodigo(Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo, Nota.StatusNota status) {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<NotaSaida> jpaQueryNotaSaida = select();

		QBean qBean = Projections.bean(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.numero
		);

		jpaQueryNotaSaida
			.select(qBean)
			.from(qNotaSaida)
			.leftJoin(qNotaSaida.empresa, qUnidade)
			.where(
				qUnidade.codigo.eq(codigoUnidade)
					.and(qNotaSaida.tipoNotaFiscal.eq(tipo)
						.and(qNotaSaida.status.ne(status)))
			)
			.orderBy(qNotaSaida.numero.desc());

		return jpaQueryNotaSaida.fetch();
	}

	/**
	 * Lista das últimas notas por unidade e ano
	 *
	 * @param codigoUnidade
	 * @param tipo
	 * @param status
	 * @param ano
	 * @return
	 */
	private List<NotaSaida> findLatestNotaByUnidadeCodigoAndAno(Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo, Nota.StatusNota status, int ano) {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<NotaSaida> jpaQueryNotaSaida = select();

		QBean qBean = Projections.bean(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.numero
		);

		jpaQueryNotaSaida
			.select(qBean)
			.from(qNotaSaida)
			.leftJoin(qNotaSaida.empresa, qUnidade)
			.where(
				qUnidade.codigo.eq(codigoUnidade)
					.and(qNotaSaida.tipoNotaFiscal.eq(tipo)
						.and(qNotaSaida.status.ne(status)))
					.and(qNotaSaida.dataEmissao.year().eq(ano))
			)
			.orderBy(qNotaSaida.numero.desc());

		return jpaQueryNotaSaida.fetch();
	}

	/**
	 * Busca uma lista de NotaSaidas usando parametros como criterio da busca.
	 *
	 * @param numero
	 * @param codigoUnidade
	 * @param tipo
	 * @param status
	 * @return
	 */
	private List<NotaSaida> findNotasByNumeroAndEmpresa(String numero, Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo, Nota.StatusNota status) {

		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<NotaSaida> jpaQueryNotaSaida = select();

		QBean qBean = Projections.bean(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.dataEmissao
		);

		jpaQueryNotaSaida
			.select(qBean)
			.from(qNotaSaida)
			.leftJoin(qNotaSaida.empresa, qUnidade)
			.where(
				qNotaSaida.numero.eq(numero)
					.and(qUnidade.codigo.eq(codigoUnidade)
						.and(qNotaSaida.tipoNotaFiscal.eq(tipo)
							.and(qNotaSaida.status.ne(status))))
			)
			.orderBy(qNotaSaida.numero.desc());

		return jpaQueryNotaSaida.fetch();
	}

	/**
	 * Busca o ServicoTipo pelo seu ID.
	 *
	 * @param id
	 * @return
	 */
	private ServicoTipo findServicoTipoById(Long id) {
		ServicoTipoTO servicoTipoTO = servicoTipoService.findByCodigo(id);

		if (servicoTipoTO != null) {
			return getMapper().map(servicoTipoTO, ServicoTipo.class);
		}

		return null;
	}

	/**
	 * FIXME: Este método é provisório, para auxiliar nas mudanças da classe NotaSaidaParcelaRetencao.
	 * Que não tem mais o atributo "tipoRetencao" (classe Tipo) , tendo o atributo "tipo" (Enum) no lugar
	 *
	 * @param tipo
	 * @return
	 */
	private NotaSaidaParcelaRetencao.TipoRetencao getTipoRetencaoByTipo(Tipo tipo) {
		NotaSaidaParcelaRetencao.TipoRetencao tipoRetencao = null;

		if (tipo != null && tipo.getCodigo() != null) {
			if ("contratual".equals(tipo.getChave())) {
				tipoRetencao = NotaSaidaParcelaRetencao.TipoRetencao.CONTRATUAL;
			} else if ("acessoria".equals(tipo.getChave())) {
				tipoRetencao = NotaSaidaParcelaRetencao.TipoRetencao.ACESSORIA;
			}
		}

		return tipoRetencao;
	}

	@Override
	@Transactional
	public void alterarValorBruto(NotaFiscalFaturamentoTO transferObj) {
		BigDecimal valor = transferObj.getFaturamentoEtapaTO().getValor();
		Long codNota = transferObj.getCodigo();
		Long codFatEtapa = transferObj.getFaturamentoEtapaTO().getCodigo();
		Long codFaturamento = transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo();

		//1 - atualizar despesa
		despesaService.updateValor(codNota, valor);
		despesaService.updateAtualizador(codNota, securityService.getUsername());
		despesaService.updateDataAtualizacao(codNota, new Date());

		//2 - atualizar faturamento etapa
		BigDecimal valorAnterior = faturamentoEtapaRepository.findValorEtapa(codFatEtapa);

		faturamentoEtapaService.updateUltimoValor(codFatEtapa, valorAnterior);
		faturamentoEtapaService.updateDataUltAlteracaoValor(codFatEtapa, new Date());
		faturamentoEtapaService.updateCodigoAlteradorValor(codFatEtapa, securityService.getUser().getCodigo());
		faturamentoEtapaService.updateValor(codFatEtapa, valor);

		//3 - atualizar Lista de notaTributoAliquota
		BigDecimal percentualServicoFaturamento = transferObj.getFaturamentoEtapaTO().getFaturamento().getPercentualServico();
		List<NotaTributoAliquotaTO> listaTributos = notaTributoAliquotaService.listarPorCodigoNota(codNota);

		BigDecimal valorTotalTributos = BigDecimal.ZERO;

		for (NotaTributoAliquotaTO nta : listaTributos) {

			BigDecimal baseDeCalculo = BigDecimal.ZERO;

			// tributo INSS
			if (nta.getAliquota().getCodigoTributo() == 6) {
				baseDeCalculo = percentualServicoFaturamento.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_EVEN).multiply(valor);
			} else {
				baseDeCalculo = valor;
			}

			BigDecimal percentualAliquota = nta.getAliquota().getPercentual();
			BigDecimal valorTributo = baseDeCalculo.multiply(percentualAliquota).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_EVEN);

			// update
			notaTributoAliquotaService.updateBaseCalculo(nta.getCodigo(), baseDeCalculo);
			notaTributoAliquotaService.updateValor(nta.getCodigo(), valorTributo);

			// tributo Iss
			if (nta.getAliquota().getCodigoTributo() == 4) {
				if (transferObj.getIssRetidoCliente()) {
					valorTotalTributos = valorTotalTributos.add(valorTributo);
				}

			} else {
				valorTotalTributos = valorTotalTributos.add(valorTributo);
			}
		}

		//4 - atualizar nota_saida_parcela
		List<FaturamentoRetencaoTO> listaRetencao = faturamentoRetencaoService.obterListaPorCodigoFaturamento(codFaturamento);
		BigDecimal valorTotalRetencoes = BigDecimal.ZERO;

		for (FaturamentoRetencaoTO faturamentoRetencaoTO : listaRetencao) {
			BigDecimal valorRetencao = (valor.multiply(faturamentoRetencaoTO.getPercentual())).divide(new BigDecimal("100"));
			valorTotalRetencoes = valorTotalRetencoes.add(valorRetencao);
		}

		JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaida(codNota);

		List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

		NotaSaidaParcela nsParcela = parcelas.get(0);

		BigDecimal novoValorLiquido = BigDecimal.ZERO;

		//FIXME: Confirmar se este é o calculo do valor liquido
		novoValorLiquido = novoValorLiquido.add(valor)
			.subtract(valorTotalTributos)
			.subtract(valorTotalRetencoes)
			.subtract(nsParcela.getValorPermuta())
			.subtract(nsParcela.getValorDesconto())
			.add(nsParcela.getValorMulta())
			.add(nsParcela.getValorJuros());

		notaSaidaParcelaRepository.updateValorBruto(nsParcela.getCodigo(), valor);
		notaSaidaParcelaRepository.updateValorLiquido(nsParcela.getCodigo(), novoValorLiquido);

	}

	@Override
	@Transactional
	public NotaFiscalFaturamentoTO salvarProtocolo(NotaFiscalFaturamentoTO transferObj) {
		notaSaidaRepository.updateProtocoloEntrega(transferObj.getCodigo(), transferObj.getProtocoloEntrega());
		notaSaidaRepository.updateDataAtualizacao(transferObj.getCodigo(), new Date());
		notaSaidaRepository.updateAtualizador(transferObj.getCodigo(), getUser().getUsername());
		transferObj.setDataAtualizacao(new Date());
		List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(getUser().getUsername());
		if (list != null && !list.isEmpty()) {
			transferObj.setAtualizador(list.get(0).getNome());
		}
		return transferObj;
	}

	@Override
	public List<NotaSaidaTO> pesquisaPorNumeroAutoComplete(NotaFiscalFaturamentoTO to) {
		try {
			Long numero = Long.parseLong(to.getNumero());
			List<NotaSaida> resultado = select(Projections.fields(
				NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.dataEmissao
			))
				.innerJoin(qNotaSaida.faturamentoEtapas, QFaturamentoEtapa.faturamentoEtapa)
				.where(
					qNotaSaida.numero.contains(String.format("%09d", numero))
						.and(qNotaSaida.status.eq(Nota.StatusNota.CANCELADA_FATURAMENTO).not()
//					.and(QFaturamentoEtapa.faturamentoEtapa.faturamento.codigo.eq(to.getCodigoFaturamento())) // Removido no issue SGINV-2285
						)
				).fetch();
			return map(resultado);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void atualizarAssociacaoNfSubstituta(NotaFiscalFaturamentoTO to) {
		notaSaidaService.updateCodigoAssociarNFSubstituta(to.getCodigo(), to.getCodigoNotaAssociadaCancelamento());
	}

	@Override
	public Boolean nfDisponivelParaLote(Long id) {
		NotaSaida notaSaida = this.getMiniNotaSaidaById(id);
		return loteInterfaceFaturamentoService.notaFiscalDisponivelParaLoteFATN(notaSaida.getDataEmissao(), notaSaida.getEmpresa().getMatriz().getCodigo());
	}

	@Override
	public BigDecimal calcularValorLiquido(NotaSaidaTO notaSaidaTO) {
		BigDecimal total = BigDecimal.ZERO;
		if (notaSaidaTO.getStatus().equals("Aberta")) {
			List<NotaTributoAliquotaTO> lista = notaTributoAliquotaService.listarPorCodigoNota(notaSaidaTO.getCodigo());

			for (NotaTributoAliquotaTO notaTributoAliquotaTO : lista) {
				if (notaTributoAliquotaTO.getAliquota().getCodigoTributo() == 4) { // ISS = 4
					if (notaSaidaTO.getIssRetidoCliente()) {
						total = total.add(notaTributoAliquotaTO.getValor());
					}
				} else {
					total = total.add(notaTributoAliquotaTO.getValor());
				}
			}
		} else {
			List<NotaTributoAliquotaEmissaoTO> lista = notaTributoAliquotaEmissaoService.listarPorCodigoNota(notaSaidaTO.getCodigo());

			for (NotaTributoAliquotaEmissaoTO notaTributoAliquotaEmissaoTO : lista) {
				if (notaTributoAliquotaEmissaoTO.getAliquota().getCodigoTributo() == 4) { // ISS = 4
					if (notaSaidaTO.getIssRetidoCliente()) {
						total = total.add(notaTributoAliquotaEmissaoTO.getValor());
					}
				} else {
					total = total.add(notaTributoAliquotaEmissaoTO.getValor());
				}
			}
		}

		return notaSaidaTO.getValor().subtract(total);
	}
}
