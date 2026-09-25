package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.administracao.enums.TipoInformacaoNumeroBoleto;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.ParcelaNota;
import br.com.araujoabreu.sgi.compras.domain.QNota;
import br.com.araujoabreu.sgi.compras.domain.QParcelaNota;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.PagamentoContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamentoContaContabil;
import br.com.araujoabreu.sgi.controleFiscal.domain.*;
import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.compras.NotaPagamentoService;
import br.com.araujoabreu.sgi.service.compras.ParcelaNotaService;
import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.contaCorrente.ContaCorrenteService;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteSimpleTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoContaContabilService;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.DadosBancariosService;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.nota.NotaFiscalService;
import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaTO;
import br.com.araujoabreu.sgi.service.tesouraria.*;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPagamentosVO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.*;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.*;
import br.com.araujoabreu.sgi.tesouraria.lang.CodigoDeBarrasException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelValidateException;
import br.com.araujoabreu.sgi.tesouraria.lang.RelacaoBancariaValidateException;
import br.com.araujoabreu.sgi.tesouraria.util.ConcessionariaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.GuiaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.BoletoUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.commons.lang.DownloadException;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.pessoa.domain.*;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.ChequeContaCorrenteDocumentosImpressaoRepository;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.banco.BancoRepository;
import br.com.fasolti.sgi.repository.contaContabil.ContaContabilRepository;
import br.com.fasolti.sgi.repository.contaCorrente.ContaCorrenteRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoContaContabilRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.contasApagar.ParcelaNotaRepository;
import br.com.fasolti.sgi.repository.feriado.FeriadoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.tesouraria.*;
import br.com.fasolti.sgi.repository.tesouraria.enumerations.TiposImpressao;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class RelacaoBancariaServiceImpl extends BaseSearchService<RelacaoBancaria, RelacaoBancariaTO, RelacaoBancariaPesquisaTO, RelacaoBancariaRepository> implements RelacaoBancariaService {

	private static final QRelacaoBancaria qRelacaoBancaria = QRelacaoBancaria.relacaoBancaria;

	private final String MSG_LINHA_DIGITAVEL_INVALIDA = "O número ou a linha digitável do(s) boleto(s) indicado(s) em %s é(são) inválido(s). %s";
	private final String MSG_LINHA_DIGITAVEL_INVALIDA_ISS_GUIA = "O pagamento de tributo \"ISS\", indicado em %s, só pode ser feito se o código de barras for informado. %s";
	private final String MSG_LINHA_DIGITAVEL_INVALIDA_CONCESSIONARIA = "O pagamento de concessionária, indicado em %s, só pode ser feito se o código de barras for informado. %s";

	@Autowired
	private ParcelaNotaRepository parcelaNotaRepository;

	@Autowired
	private ItemRelacaoRepository itemRelacaoRepository;

	@Autowired
	private ItemRelacaoPagamentoRepository itemRelacaoPagamentoRepository;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private FeriadoService feriadoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;

	@Autowired
	private BancoRepository bancoRepository;

	@Autowired
	private DadosBancariosRepository dadosBancariosRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private ContaContabilRepository contaContabilRepository;

	@Autowired
	private PagamentoContaContabilRepository pagamentoContaContabilRepository;

	@Autowired
	private RetornoBancarioRepository retornoBancarioRepository;

	@Autowired
	private NotaTributoAliquotaRepository notaTributoAliquotaRepository;

	@Autowired
	private ChequeContaCorrenteRepository chequeContaCorrenteRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private ChequeContaCorrenteItemRelacaoRepository chequeContaCorrenteItemRelacaoRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private ChequeContaCorrenteDocumentosImpressaoRepository chequeContaCorrenteDocumentosImpressaoRepository;

	@Autowired
	private FeriadoRepository feriadoRepository;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ContaCorrenteService contaCorrenteService;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private ChequesService chequesService;

	@Autowired
	private DadosBancariosService dadosBancariosService;

	@Autowired
	private ItemRelacaoService itemRelacaoService;

	@Autowired
	private ItemRelacaoPagamentoService itemRelacaoPagamentoService;

	@Autowired
	private ChequeContaCorrenteItemRelacaoService chequeContaCorrenteItemRelacaoService;

	@Autowired
	private ParcelaNotaService parcelaNotaService;

	@Autowired
	private PagamentoContaContabilService pagamentoContaContabilService;

	@Autowired
	private NotaPagamentoService notaPagamentoService;

	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private NotaTributoAliquotaService notaTributoAliquotaService;

	@Autowired
	private DozerHelper dozerHelper;

	@Autowired
	public RelacaoBancariaServiceImpl(RelacaoBancariaRepository repository) {
		super(repository);
	}

	//TODO: Adicionar em um Util da vida...
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<>(c);
		Collections.sort(list);
		return list;
	}

	@Override
	protected void buildSearchQuery(JPAQuery<RelacaoBancaria> jpaQuery, RelacaoBancariaPesquisaTO exampleObj) {
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;
		QPagamento qPagamento = QPagamento.pagamento;
		QBanco qBanco = QBanco.banco;
		QPessoa qPessoa = QPessoa.pessoa;

		// Numéro pagamento, Credor e Tributo
		if (Check.isNotEmpty(exampleObj.getNumeroPagamento()) || Check.isAssigned(exampleObj.getNomeCredor()) || Check.isNotEmpty(exampleObj.getTributos())) {

			jpaQuery.leftJoin(qRelacaoBancaria.itens, qItemRelacao)
				.leftJoin(qItemRelacao.listaPagamentos, qItemRelacaoPagamento)
				.leftJoin(qItemRelacaoPagamento.pagamento, qPagamento)
				.leftJoin(qPagamento.credor, qPessoa).distinct();
		}

		// Tributo
		if (Check.isNotEmpty(exampleObj.getTributos())) {

			jpaQuery.leftJoin(qPagamento.listaNotaTributoAliquota, qNotaTributoAliquota)
				.leftJoin(qNotaTributoAliquota.aliquota, qAliquota)
				.leftJoin(qAliquota.categoriaTributo, qCategoriaTributo)
				.leftJoin(qCategoriaTributo.tributo, qTributo).distinct();
		}

		if (Check.isAssigned(exampleObj.getBancoPesquisa())) {
			QContaCorrente qContaContacorrente = QContaCorrente.contaCorrente;

			jpaQuery.join(qRelacaoBancaria.contaCorrente, qContaContacorrente)
				.join(qContaContacorrente.banco, qBanco);
		}

		jpaQuery.orderBy(qRelacaoBancaria.numero.asc());
	}

	@Override
	protected void setDefaultValues(RelacaoBancariaTO object) throws ServiceException {
		object.setStatus(RelacaoBancaria.StatusRelacaoBancaria.ABERTA.toString());
		object.setPagamentoIndividual(Boolean.TRUE);
		object.setStatus(RelacaoBancaria.StatusRelacaoBancaria.ABERTA.toString());
		object.setPagamentoIndividual(Boolean.TRUE);
		object.setQtdPagamentos(0);
		object.setTotal(BigDecimal.ZERO);
		//TODO: COMO SABER SE SERÁ TRUE OU FALSE?
		object.setImpressao2via(Boolean.FALSE);
		object.setImpressaoCheque(Boolean.FALSE);
		object.setImpressaoEndosso(Boolean.FALSE);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, RelacaoBancariaPesquisaTO exampleObj) {
		RelacaoBancaria relacaoBancaria = getMapper().map(exampleObj, RelacaoBancaria.class);

		if (Check.isAssigned(exampleObj.getNumero())) {
			builder.and(qRelacaoBancaria.numero.upper().contains(exampleObj.getNumero().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getStatus())) {
			builder.and(qRelacaoBancaria.status.eq(relacaoBancaria.getStatus()));
		}

		if (Check.isAssigned(exampleObj.getPagamentoDe())) {
			builder.and(qRelacaoBancaria.dataPagamento.goe(exampleObj.getPagamentoDe()));
		}

		if (Check.isAssigned(exampleObj.getPagamentoAte())) {
			builder.and(qRelacaoBancaria.dataPagamento.loe(exampleObj.getPagamentoAte()));
		}

		if (Check.isAssigned(exampleObj.getUnidade())) {
			builder.and(qRelacaoBancaria.unidade.codigo.eq(exampleObj.getUnidade().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getBancoPesquisa())) {
			QBanco qBanco = QBanco.banco;
			builder.and(qBanco.codigo.eq(exampleObj.getBancoPesquisa().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getContaCorrente()) && Check.isAssigned(exampleObj.getContaCorrente().getCodigo())) {
			builder.and(qRelacaoBancaria.contaCorrente.codigo.eq(exampleObj.getContaCorrente().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getMeioPagamento())) {
			builder.and(qRelacaoBancaria.meioPagamento.eq(relacaoBancaria.getMeioPagamento()));
		}

		if (Check.isNotEmpty(exampleObj.getTributos())) {
			QTributo qTributo = QTributo.tributo;

			List<Tributo> tributos = new ArrayList<>();
			for (TributoTO tributoTO : exampleObj.getTributos()) {
				tributos.add(getMapper().map(tributoTO, Tributo.class));
			}
			builder.and(qTributo.in(tributos));
		}

		if (Check.isNotEmpty(exampleObj.getNomeCredor())) {
			QPessoaJuridica qPessoaJuridica = QPessoa.pessoa.as(QPessoaJuridica.class);
			builder.and(qPessoaJuridica.nome.containsIgnoreCase(exampleObj.getNomeCredor()))
				.or(qPessoaJuridica.documento.containsIgnoreCase(exampleObj.getNomeCredor()))
				.or(qPessoaJuridica.nomeFantasia.containsIgnoreCase(exampleObj.getNomeCredor()));
		}

		if (Check.isNotEmpty(exampleObj.getNumeroPagamento())) {
			QPagamento qPagamento = QPagamento.pagamento;
			builder.and(qPagamento.numero.containsIgnoreCase(exampleObj.getNumeroPagamento()));
		}
	}

	@Override
	protected void doAfterSave(RelacaoBancaria entity, RelacaoBancariaTO object) {
		Long numeroCheque = null;

		if (object.getChequeAnterior() == null) {
			numeroCheque = contaCorrenteService.obterNumeroChequeAnterior(entity.getContaCorrente().getCodigo());
		} else {
			numeroCheque = object.getChequeAnterior();
		}

		List<Banco> bancos = bancoRepository.findAllByOrderByNome();
		Pagamento.TipoDocumentoPagamento[] tipos = Pagamento.TipoDocumentoPagamento.values();

		for (RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO : object.getRelacaoBancariaPagamentosVO()) {
			relacaoBancariaPagamentosVO.getItemRelacao().setRelacaoBancaria(object);

			if (entity.getItens() == null) {
				entity.setItens(new ArrayList<>());
			}

			ItemRelacao itemRelacao = getMapper().map(relacaoBancariaPagamentosVO.getItemRelacao(), ItemRelacao.class);
			itemRelacao.setRelacaoBancaria(entity);
			itemRelacao.setDataPagamento(relacaoBancariaPagamentosVO.getDataPagamento());
			itemRelacao.setTotal(relacaoBancariaPagamentosVO.getValorAPagar());

			for (Banco banco : bancos) {
				if (banco.getNome().equals(relacaoBancariaPagamentosVO.getBancoSelecionado().getNome())) {
					itemRelacao.setBanco(banco);
					break;
				}
			}

			for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : tipos) {
				if (tipoDocumentoPagamento.toString().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())) {
					itemRelacao.setTipoDocumentoPagamento(tipoDocumentoPagamento);
					break;
				}
			}

			if (RelacaoBancaria.MeioPagamento.CHEQUE.toString().equals(object.getMeioPagamento())) {
				if (object.getPagamentoIndividual() == true) {
					numeroCheque++;
					itemRelacao.setCheque(numeroCheque);
				} else {
					itemRelacao.setCheque(numeroCheque);
				}
			}

			for (ItemRelacaoPagamentoTO itemRelacaoPagamentoTO : relacaoBancariaPagamentosVO.getItemRelacao().getListaPagamentos()) {
				if (itemRelacao.getListaPagamentos() == null)
					itemRelacao.setListaPagamentos(new ArrayList<>());

				ItemRelacaoPagamento itemRelacaoPagamento = new ItemRelacaoPagamento();
				itemRelacaoPagamento.setItemRelacao(itemRelacao);

				pagamentoService.updateStatusRelacionado(itemRelacaoPagamentoTO.getPagamento().getCodigo());

				itemRelacaoPagamento.setPagamento(getMapper().map(itemRelacaoPagamentoTO.getPagamento(), Pagamento.class));
				itemRelacao.getListaPagamentos().add(itemRelacaoPagamento);
			}

			entity.getItens().add(itemRelacao);


		}

		// Salvando os relacionamentos.
		for (ItemRelacao itemRelacao : entity.getItens()) {
			itemRelacao.setRelacaoBancaria(entity);
			// FIXME: A lista está sendo deletada após salvar. Por isso o procedimento abaixo.
			List<ItemRelacaoPagamentoTO> listaItemPagamento = new ArrayList<>();
			for (ItemRelacaoPagamento itemRelacaoPagamento : itemRelacao.getListaPagamentos()) {
				listaItemPagamento.add(getMapper().map(itemRelacaoPagamento, ItemRelacaoPagamentoTO.class));
			}
			itemRelacaoRepository.save(itemRelacao);
			for (ItemRelacaoPagamentoTO itemRelacaoPagamentoTO : listaItemPagamento) {
				itemRelacaoPagamentoTO.setItemRelacao(getMapper().map(itemRelacao, ItemRelacaoTO.class));
				itemRelacaoPagamentoTO.setPagamento(itemRelacaoPagamentoTO.getPagamento());
				itemRelacaoPagamentoRepository.save(getMapper().map(itemRelacaoPagamentoTO, ItemRelacaoPagamento.class));
			}
		}

		contaCorrenteRepository.updateChequeAnterior(numeroCheque, entity.getContaCorrente().getCodigo());

		if (RelacaoBancaria.MeioPagamento.CHEQUE.toString().equals(object.getMeioPagamento())) {
			List<ChequeContaCorrenteItemRelacao> listaItensCheque = new ArrayList<>();

			for (ItemRelacao itemRelacao : entity.getItens()) {

				if (object.getPagamentoIndividual() == true) {

					ChequeContaCorrente cheque = new ChequeContaCorrente();
					cheque.setContaCorrente(entity.getContaCorrente());
					cheque.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
					cheque.setValor(itemRelacao.getTotal());
					cheque.setDataCompensacao(itemRelacao.getDataPagamento());
					cheque.setCheque(itemRelacao.getCheque());
					cheque.setCriador(securityService.getUser().getLogin());
					cheque.setDataCriacao(new Date());

					chequeContaCorrenteRepository.save(cheque);

					ChequeContaCorrenteItemRelacao chequeItemRelacao = new ChequeContaCorrenteItemRelacao();
					chequeItemRelacao.setCodigoChequeContaCorrente(cheque.getCodigo());
					chequeItemRelacao.setCodigoItemRelacao(itemRelacao.getCodigo());

					chequeContaCorrenteItemRelacaoRepository.save(chequeItemRelacao);
				} else {
					itemRelacaoRepository.updateCheque(numeroCheque, itemRelacao.getCodigo());

					ChequeContaCorrenteItemRelacao chequeItemRelacao = new ChequeContaCorrenteItemRelacao();
					chequeItemRelacao.setCodigoItemRelacao(itemRelacao.getCodigo());
					listaItensCheque.add(chequeItemRelacao);
				}
			}


			if (!listaItensCheque.isEmpty()) {
				ChequeContaCorrente cheque = new ChequeContaCorrente();
				cheque.setContaCorrente(entity.getContaCorrente());
				cheque.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
				cheque.setValor(object.getTotal());
				cheque.setDataCompensacao(entity.getDataPagamento());
				cheque.setCheque(numeroCheque);
				cheque.setCriador(securityService.getUser().getLogin());
				cheque.setDataCriacao(new Date());

				chequeContaCorrenteRepository.save(cheque);

				for (ChequeContaCorrenteItemRelacao chequeItemRelacao : listaItensCheque) {
					chequeItemRelacao.setCodigoChequeContaCorrente(cheque.getCodigo());
					chequeContaCorrenteItemRelacaoRepository.save(chequeItemRelacao);
				}
			}
		}

		updateNumero(entity.getCodigo());
	}

	public void prepareValidateToSave(RelacaoBancariaTO relacaoBancariaTO) throws ServiceException {

		// Validações antes de persistir (edição/novo)
		if (relacaoBancariaTO.getContaCorrente().getBanco() == null || relacaoBancariaTO.getContaCorrente().getBanco().getCodigo() == null) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Banco é um campo de preenchimento obrigatório");
		}

		if (relacaoBancariaTO.getContaCorrente().getAgencia() == null) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Agência é um campo de preenchimento obrigatório");
		}

		if (relacaoBancariaTO.getMeioPagamento() == null) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Meio Pagamento é um campo de preenchimento obrigatório");
		}

		if (relacaoBancariaTO.getUnidade() == null || relacaoBancariaTO.getUnidade().getCodigo() == null) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Empresa é um campo de preenchimento obrigatório");
		}

		if (relacaoBancariaTO.getRelacaoBancariaPagamentosVO() == null || relacaoBancariaTO.getRelacaoBancariaPagamentosVO().isEmpty()) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Deve haver, pelo menos, um pagamento selecionado.");
		}

	}

	private void prepareValidateToUpdate(RelacaoBancaria entity, RelacaoBancariaTO relacaoBancariaTO) {
		if (RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO.getDescricao().equals(relacaoBancariaTO.getStatus()) ||
			RelacaoBancaria.StatusRelacaoBancaria.PROCESSANDO_RETORNO.getDescricao().equals(relacaoBancariaTO.getStatus()) ||
			RelacaoBancaria.StatusRelacaoBancaria.FINALIZADA.getDescricao().equals(relacaoBancariaTO.getStatus()) ||
			RelacaoBancaria.StatusRelacaoBancaria.CANCELADA.getDescricao().equals(relacaoBancariaTO.getStatus())) {

			throw new ValidationException("ValidationException","Não é permitido edição par ao status atual do registro.");
		}
	}

	@Override
	@Transactional
	protected void doBeforeSave(RelacaoBancaria entity, RelacaoBancariaTO relacaoBancariaTO) throws ServiceException {
		if (entity.getColaboradorEstornoBaixaManual() != null && (entity.getColaboradorEstornoBaixaManual().getCodigo() == null))
			entity.setColaboradorEstornoBaixaManual(null);
		if (RelacaoBancaria.MeioPagamento.CHEQUE.equals(entity.getMeioPagamento()) && entity.getChequeAnterior() != null) {

			List<ChequesEmitidosExibirTO> list = contaCorrenteService.buscarChequesContaCorrenteNumero(
				entity.getContaCorrente().getCodigo(),
				relacaoBancariaTO.getChequeAnterior(),
				relacaoBancariaTO.getChequeAnterior() + relacaoBancariaTO.getRelacaoBancariaPagamentosVO().size());

			Long cheque = relacaoBancariaTO.getChequeAnterior();

			for (RelacaoBancariaPagamentosVO to : relacaoBancariaTO.getRelacaoBancariaPagamentosVO()) {
				cheque = cheque + 1L;
				for (ChequesEmitidosExibirTO chequesEmitidosExibirTO : list) {
					if (chequesEmitidosExibirTO.getChequeItem().equals(cheque)) {
						if (chequesEmitidosExibirTO.getCodigoItemRelacao() == null) {
							continue;
						}
						List<ChequeContaCorrenteItemRelacaoTO> listCCIR = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoItemRelacao(chequesEmitidosExibirTO.getCodigoItemRelacao());
						if (!listCCIR.isEmpty()) {
							Stream<ChequeContaCorrenteItemRelacaoTO> stream = listCCIR.stream();
							ChequeContaCorrenteItemRelacaoTO last = stream.reduce((first, second) -> second).orElse(null);
							if (last != null) {
								ChequeContaCorrente chequeContaCorrente = chequeContaCorrenteRepository.findById(last.getCodigoChequeContaCorrente()).orElse(null);
								if (chequeContaCorrente != null) {
									if (ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO.equals(chequeContaCorrente.getSituacao())) {
										throw new ValidationException("RelacaoBancariaServiceImpl", String.format("O cheque %s foi emitido na %s e está aguardando retorno do pagamento. Este número não poderá ser utilizado.", cheque, chequesEmitidosExibirTO.getNumeroRelacaoBancaria()));
									} else if (ChequeContaCorrente.StatusChequeContaCorrente.PAGO.equals(chequeContaCorrente.getSituacao())) {
										throw new ValidationException("RelacaoBancariaServiceImpl", String.format("O cheque %s foi pago na %s. Este número não poderá ser utilizado.", cheque, chequesEmitidosExibirTO.getNumeroRelacaoBancaria()));
									} else if (ChequeContaCorrente.StatusChequeContaCorrente.ESTORNADO.equals(chequeContaCorrente.getSituacao())) {
										// permitir o uso do número E atualizar a situação deste cheque anterior localizado para "CANCELADO_REIMPRESSAO"
										// chequeContaCorrente.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.CANCELADO_REIMPRESSAO);
										// chequeContaCorrenteRepository.save(chequeContaCorrente);
									} else if (ChequeContaCorrente.StatusChequeContaCorrente.CANCELADO_USUARIO.equals(chequeContaCorrente.getSituacao())) {
										throw new ValidationException("RelacaoBancariaServiceImpl", String.format("O cheque %s foi cancelado por %s. Este número não poderá ser utilizado.", cheque, chequeContaCorrente.getResponsavelCancelamento().getNome()));
									}
								}
							}
						}
					}
				}

				to.setCheque(cheque.toString());
			}
		}
		prepareValidateToSave(relacaoBancariaTO);

		// Obter cheque inicial.
		validarChequeAnterior(relacaoBancariaTO);

		entity.setNumero("RB/");
	}

	private void validarChequeAnterior(RelacaoBancariaTO relacaoBancariaTO) throws ServiceException {
		// Obter cheque inicial.
		ContaCorrenteSimpleTO contaCorrente = obterUltimoCheque(relacaoBancariaTO.getContaCorrente().getCodigo());
		if (relacaoBancariaTO.getChequeAnterior() != null && relacaoBancariaTO.getChequeAnterior() < contaCorrente.getChequeInicial()) {
			throw new ValidationException("RelacaoBancariaServiceImpl", String.format("O valor do cheque anterior deve ser maior ou igual ao valor do cheque inicial %d", contaCorrente.getChequeInicial()));
		}
	}

	/**
	 * Obter o cheque anterior de uma conta corrente.
	 *
	 * @param codigoContaCorrente O código da conta corrente.
	 * @return O número do cheque anterior de uma conta corrente.
	 */
	@Override
	public Long obterChequeAnteriorDeContaCorrente(long codigoContaCorrente) {
		Long chequeAnterior = contaCorrenteService.obterChequeAnteriorPeloCodigo(codigoContaCorrente);

		if (chequeAnterior != null) {
			return chequeAnterior;
		} else {
			return null;
		}
	}

	/**
	 * Obter o número do cheque anterior de uma relação bancária.
	 *
	 * @param codigoRelacaobancaria A código de uma relação bancária.
	 * @return A cheque anterior de uma relação bancária.
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Long obterChequeAnteriorDeUmaRelacaoBancaria(long codigoRelacaobancaria) {
		QRelacaoBancaria qRelacaoBancaria = QRelacaoBancaria.relacaoBancaria;
		JPAQuery jpaQuery = select();
		jpaQuery.select(Projections.bean(RelacaoBancaria.class, qRelacaoBancaria.codigo, qRelacaoBancaria.chequeAnterior))
			.where(qRelacaoBancaria.codigo.eq(codigoRelacaobancaria));
		RelacaoBancaria relacaoBancaria = (RelacaoBancaria) jpaQuery.fetchOne();

		if (relacaoBancaria != null) {
			return relacaoBancaria.getChequeAnterior();
		}

		return null;
	}

	@Override
	protected void doBeforeUpdate(RelacaoBancaria entity, RelacaoBancariaTO relacaoBancariaTO) {
		if (entity.getColaboradorEstornoBaixaManual() != null && (entity.getColaboradorEstornoBaixaManual().getCodigo() == null))
			entity.setColaboradorEstornoBaixaManual(null);

		if (relacaoBancariaTO.getColaboradorEstornoBaixaManual() != null && (relacaoBancariaTO.getColaboradorEstornoBaixaManual().getCodigo() == null))
			relacaoBancariaTO.setColaboradorEstornoBaixaManual(null);

		prepareValidateToUpdate(entity, relacaoBancariaTO);
		prepareValidateToSave(relacaoBancariaTO);

		// Obter cheque inicial.
		validarChequeAnterior(relacaoBancariaTO);

		if (RelacaoBancaria.MeioPagamento.CHEQUE.equals(entity.getMeioPagamento())) {
			if (entity.getStatus().equals(RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO)
				&& entity.getPagamentoIndividual()) {
				Long maiorCheque = 0L;

				List<ItemRelacaoTO> itensRb = itemRelacaoService.obterItemCodigoEChequeCodigoRelacaoBancaria(entity.getCodigo());

				for (ItemRelacaoTO itemRelacao : itensRb) {
					if (maiorCheque < itemRelacao.getCheque())
						maiorCheque = itemRelacao.getCheque();
				}

				Long chequeAnterior = obterChequeAnteriorDeContaCorrente(entity.getContaCorrente().getCodigo());

				if (chequeAnterior < maiorCheque) {
					contaCorrenteRepository.updateChequeAnterior(maiorCheque, entity.getContaCorrente().getCodigo());
				}
			} else if (entity.getStatus().equals(RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO)
				&& !entity.getPagamentoIndividual()) {
				ContaCorrente contaCorrente = contaCorrenteRepository.findById(entity.getContaCorrente().getCodigo()).orElse(null);
				if (contaCorrente.getChequeAnterior() < ((entity.getChequeAnterior() == null ? 0 : entity.getChequeAnterior()) + 1)) {
					contaCorrenteRepository.updateChequeAnterior(((entity.getChequeAnterior() == null ? 0 : entity.getChequeAnterior()) + 1),
						entity.getContaCorrente().getCodigo());
				}
			}
		}

		Long chequeAnteriorRBOriginal = obterChequeAnteriorDeUmaRelacaoBancaria(entity.getCodigo());

		if (!Objects.equals(chequeAnteriorRBOriginal, entity.getChequeAnterior())) {
			relacaoBancariaTO.setHouverMudancaNoUltimoCheque(true);
		} else {
			relacaoBancariaTO.setHouverMudancaNoUltimoCheque(false);
		}

		Long numeroCheque = null;

		// Caso não tenha informado o cheque-anterior na RB, usar o do banco.
		if (entity.getChequeAnterior() == null) {
			numeroCheque = obterChequeAnteriorDeContaCorrente(entity.getContaCorrente().getCodigo());
		} else {
			numeroCheque = entity.getChequeAnterior();
		}

		// Neste momento, remover todos o ChequeContaCorrenteItemRelacao para garantir daddos consistente.
		List<ItemRelacaoTO> listaItens = itemRelacaoService.obterItemCodigoEChequeCodigoRelacaoBancaria(entity.getCodigo());

		if (listaItens != null && !listaItens.isEmpty()) {
			for (ItemRelacaoTO itemRelacao : listaItens) {
				for (ChequeContaCorrenteItemRelacao cheque : chequeContaCorrenteItemRelacaoRepository.findByCodigoItemRelacao(itemRelacao.getCodigo())) {
					chequeContaCorrenteItemRelacaoRepository.delete(cheque);
				}
			}
		}

		/*
		 * Neste momento, iremos remover os itens que foram adicionados na lista de "itensRemovidos".
		 * Isso porque foi pedido no issue que o itens só serão removidos se o usuário confirmar o
		 * update da RB. Aqui, pegamos os itens de pagamento para exclusão e depois removemos os itens.
		 */
		if (relacaoBancariaTO.getItensRemovidos() != null) {
			for (Long codigoItem : relacaoBancariaTO.getItensRemovidos()) {
				for (ItemRelacaoPagamento itemRelacaoPagamento : obterItemRelacaoPagamentoEPagamentoSoCodigo(codigoItem)) {
					pagamentoService.updateStatusPagamento(itemRelacaoPagamento.getPagamento().getCodigo());
					itemRelacaoPagamentoRepository.deleteById(itemRelacaoPagamento.getCodigo());
				}
				itemRelacaoRepository.deleteById(codigoItem);
			}
		}

		List<Banco> bancos = bancoRepository.findAllByOrderByNome();
		Pagamento.TipoDocumentoPagamento[] tiposDocPagamentos = Pagamento.TipoDocumentoPagamento.values();

		List<RelacaoBancariaPagamentosVO> listaVO = new ArrayList<>(relacaoBancariaTO.getRelacaoBancariaPagamentosVO());

		for (RelacaoBancariaPagamentosVO to : listaVO) {
			to.getItemRelacao().setRelacaoBancaria(relacaoBancariaTO);

			ItemRelacao itemRelacao = getMapper().map(to.getItemRelacao(), ItemRelacao.class);
			itemRelacao.setRelacaoBancaria(entity);
			itemRelacao.setDataPagamento(to.getDataPagamento());
			itemRelacao.setTotal(to.getValorAPagar());

			for (Banco banco : bancos) {
				if (banco.getNome().equals(to.getBancoSelecionado().getNome())) {
					itemRelacao.setBanco(banco);
					break;
				}
			}

			for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : tiposDocPagamentos) {
				if (tipoDocumentoPagamento.toString().equals(to.getTipoDocumentoPagamento())) {
					itemRelacao.setTipoDocumentoPagamento(tipoDocumentoPagamento);
					break;
				}
			}

			if (RelacaoBancaria.MeioPagamento.CHEQUE.equals(entity.getMeioPagamento())) {
				if (entity.getPagamentoIndividual() == true) {
//					if (to.getItemRelacao().getCheque() == null || relacaoBancariaTO.getHouverMudancaNoUltimoCheque()) {
//						++numeroCheque;
//						itemRelacao.setCheque(numeroCheque);
//					} else {
//						++numeroCheque;
//						itemRelacao.setCheque(to.getItemRelacao().getCheque());
//					}
					// FIXME: Refazer toda a numeração... Pois pode ser excluído os pagamentos anteriores.
					++numeroCheque;
					itemRelacao.setCheque(numeroCheque);
				} else {
					if (to.getItemRelacao().getCheque() == null || relacaoBancariaTO.getHouverMudancaNoUltimoCheque()) {
						++numeroCheque;
						itemRelacao.setCheque(numeroCheque);
					} else {
						itemRelacao.setCheque(to.getItemRelacao().getCheque());
					}
				}
			} else {
				itemRelacao.setCheque(null);
			}

			// Apagando os relacionamentos item x pagamento.
			if (to.getItemRelacao().getCodigo() != null) {
				for (ItemRelacaoPagamento pg : obterItemRelacaoPagamentoEPagamentoSoCodigo(to.getItemRelacao().getCodigo())) {
					itemRelacaoPagamentoRepository.deleteItem(pg.getCodigo());
				}
			}

			for (ItemRelacaoPagamentoTO itemRelacaoPagamentoTO : to.getItemRelacao().getListaPagamentos()) {
				if (itemRelacao.getListaPagamentos() == null) {
					itemRelacao.setListaPagamentos(new ArrayList<>());
				}

				ItemRelacaoPagamento itemRelacaoPagamento = new ItemRelacaoPagamento();
				itemRelacaoPagamento.setItemRelacao(itemRelacao);
				pagamentoService.updateStatusRelacionado(itemRelacaoPagamentoTO.getPagamento().getCodigo());
				itemRelacaoPagamento.setPagamento(getMapper().map(itemRelacaoPagamentoTO.getPagamento(), Pagamento.class));
				itemRelacao.getListaPagamentos().add(itemRelacaoPagamento);
			}

			itemRelacao.setQtdPagamentos(itemRelacao.getListaPagamentos().size());
			itemRelacaoRepository.save(itemRelacao);

			for (ItemRelacaoPagamento itemRelacaoPagamento : itemRelacao.getListaPagamentos()) {
				itemRelacaoPagamento.setItemRelacao(itemRelacao);
				itemRelacaoPagamentoRepository.save(itemRelacaoPagamento);
			}
		}

		BigDecimal total = new BigDecimal(0L);

		for (RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO : relacaoBancariaTO.getRelacaoBancariaPagamentosVO()) {
			total = total.add(relacaoBancariaPagamentosVO.getValorAPagar());
		}

		entity.setTotal(total);

		chequeItemRelacao(entity, numeroCheque);
	}

	/**
	 * Obter lista de itens relacao pagamento e seus pagamentos.
	 * Só serão retornados os código da entidades.
	 *
	 * @param codigoItem O Código do item relação.
	 * @return A lista de itens relacao pagamento e seus pagamentos.
	 */
	private List<ItemRelacaoPagamento> obterItemRelacaoPagamentoEPagamentoSoCodigo(Long codigoItem) {
		List<ItemRelacaoPagamentoTO> itemRelacaoPagamentoTOS = itemRelacaoPagamentoService.obterCodigoECodigoPagamentoPeloCodigoItemRelacao(codigoItem);

		List<ItemRelacaoPagamento> listaItemRelacaoPagamento = new ArrayList<>();

		for (ItemRelacaoPagamentoTO itemRelacaoPagamentoTO : itemRelacaoPagamentoTOS) {
			listaItemRelacaoPagamento.add(getMapper().map(itemRelacaoPagamentoTO, ItemRelacaoPagamento.class));
		}

		return listaItemRelacaoPagamento;
	}

	/**
	 * Cria o relacionamento {@link ChequeContaCorrente} x {@link ChequeContaCorrenteItemRelacao}.
	 *
	 * @param entity       A entidade {@link RelacaoBancaria}.
	 * @param numeroCheque O numero atual do cheque.
	 */
	private void chequeItemRelacao(RelacaoBancaria entity, Long numeroCheque) {
		if (RelacaoBancaria.MeioPagamento.CHEQUE.equals(entity.getMeioPagamento()) &&
			!entity.getStatus().equals(RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO.toString())) {

			List<ChequeContaCorrenteItemRelacao> listaItensCheque = new ArrayList<>();
			List<ItemRelacao> listaItensRelacao = itemRelacaoRepository.findByRelacaoBancariaCodigo(entity.getCodigo());

			for (ItemRelacao itemRelacao : listaItensRelacao) {
				if (entity.getPagamentoIndividual() == true) {

					//FIXME: Refatorar. Tem o mesmo código abaixo.
					List<ChequeContaCorrente> chequeList = chequeContaCorrenteRepository.findByContaCorrenteCodigoAndChequeAndSituacao(entity.getContaCorrente().getCodigo(), itemRelacao.getCheque(), ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
					ChequeContaCorrente cheque = new ChequeContaCorrente();

					if (chequeList.isEmpty()) {
						cheque.setContaCorrente(entity.getContaCorrente());
						cheque.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
						cheque.setValor(itemRelacao.getTotal());
						cheque.setDataCompensacao(itemRelacao.getDataPagamento());
						cheque.setCheque(itemRelacao.getCheque());
						cheque.setCriador(securityService.getUser().getLogin());
						cheque.setDataCriacao(new Date());

						chequeContaCorrenteRepository.save(cheque);
					} else {
						cheque = chequeList.get(chequeList.size() -1);
					}

					ChequeContaCorrenteItemRelacao chequeItemRelacao = new ChequeContaCorrenteItemRelacao();
					chequeItemRelacao.setCodigoChequeContaCorrente(cheque.getCodigo());
					chequeItemRelacao.setCodigoItemRelacao(itemRelacao.getCodigo());

					chequeContaCorrenteItemRelacaoRepository.save(chequeItemRelacao);
				} else {
					ChequeContaCorrenteItemRelacao chequeItemRelacao = new ChequeContaCorrenteItemRelacao();
					chequeItemRelacao.setCodigoItemRelacao(itemRelacao.getCodigo());
					listaItensCheque.add(chequeItemRelacao);
				}
			}

			ContaCorrente contaCorrente = contaCorrenteRepository.findById(entity.getContaCorrente().getCodigo()).orElse(null);
			contaCorrente.setChequeAnterior(numeroCheque);
			contaCorrenteRepository.save(contaCorrente);

			if (!listaItensCheque.isEmpty()) {
				//FIXME: Refatorar. Tem o mesmo código acima.
				List<ChequeContaCorrente> chequeList = chequeContaCorrenteRepository.findByContaCorrenteCodigoAndChequeAndSituacao(entity.getContaCorrente().getCodigo(), listaItensRelacao.get(0).getCheque(), ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
				ChequeContaCorrente cheque = new ChequeContaCorrente();

				if (chequeList.isEmpty()) {
					cheque.setContaCorrente(entity.getContaCorrente());
					cheque.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
					cheque.setValor(entity.getTotal());
					cheque.setDataCompensacao(entity.getDataPagamento());
					cheque.setCheque(listaItensRelacao.get(0).getCheque());
					cheque.setCriador(securityService.getUser().getLogin());
					cheque.setDataCriacao(new Date());
					chequeContaCorrenteRepository.save(cheque);
				} else {
					cheque = chequeList.get(chequeList.size() -1);
				}

				for (ChequeContaCorrenteItemRelacao chequeItemRelacao : listaItensCheque) {
					chequeItemRelacao.setCodigoChequeContaCorrente(cheque.getCodigo());
					chequeContaCorrenteItemRelacaoRepository.save(chequeItemRelacao);
				}
			}
		}
	}

	@Override
	protected void doAfterSearch(List<RelacaoBancaria> entities, List<RelacaoBancariaPesquisaTO> result) throws ServiceException {
		for (RelacaoBancariaPesquisaTO relacaoBancariaPesquisaTO : result) {
			calcularTotaisRB(relacaoBancariaPesquisaTO);
		}
	}

	private RelacaoBancariaPesquisaTO calcularTotaisRB(RelacaoBancariaPesquisaTO relacaoBancariaPesquisaTO) {
		BigDecimal totalValorPagamentosRestantes = new BigDecimal(BigInteger.ZERO);
		BigDecimal totalValorPagamentosPagos = new BigDecimal(BigInteger.ZERO);
		BigDecimal totalValorPagamentosEstornados = new BigDecimal(BigInteger.ZERO);

		int qtdPagamentosRestantes = 0;
		int qtdPagamentosPagos = 0;
		int qtdPagamentosEstornados = 0;

		if (relacaoBancariaPesquisaTO.getCodigo() == null) {
			return relacaoBancariaPesquisaTO;
		}

		List<ItemRelacao> listaItensRelacao = obterListaItemRelacao(relacaoBancariaPesquisaTO.getCodigo());
		// FIXME
		relacaoBancariaPesquisaTO.setItemRelacao(getMapper().map(listaItensRelacao.get(0), ItemRelacaoTO.class));

		if (listaItensRelacao != null && !listaItensRelacao.isEmpty()) {
			for (ItemRelacao item : listaItensRelacao) {
				relacaoBancariaPesquisaTO.getItemRelacao().setStatus(item.getStatus().getNome());

				if (ItemRelacao.StatusItemRelacao.ESTORNADO.getNome().equalsIgnoreCase(item.getStatus().getNome())) {
					qtdPagamentosEstornados += item.getQtdPagamentos();
					totalValorPagamentosEstornados = totalValorPagamentosEstornados.add(item.getTotal());

				} else if (ItemRelacao.StatusItemRelacao.CONFIRMADO.getNome().equalsIgnoreCase(item.getStatus().getNome())) {
					qtdPagamentosPagos += item.getQtdPagamentos();
					totalValorPagamentosPagos = totalValorPagamentosPagos.add(item.getTotal());

				} else {
					qtdPagamentosRestantes += item.getQtdPagamentos();
					totalValorPagamentosRestantes = totalValorPagamentosRestantes.add(item.getTotal());
				}
			}


			// Why??
			relacaoBancariaPesquisaTO.getItemRelacao().setTotalPgtos(qtdPagamentosPagos + qtdPagamentosRestantes);
			relacaoBancariaPesquisaTO.getItemRelacao().setTotalPago(totalValorPagamentosPagos);

			// Why? [2] (Apenas mantendo a lógica anterior)
			relacaoBancariaPesquisaTO.setTotalPgtos(qtdPagamentosPagos + qtdPagamentosRestantes);

			relacaoBancariaPesquisaTO.setTotalPgtosPagos(qtdPagamentosPagos);
			relacaoBancariaPesquisaTO.setTotalPago(totalValorPagamentosPagos);

			////// Itens estornados/invalidos da RB (somatorio dos itens estornados/invalidos)
			relacaoBancariaPesquisaTO.setTotalPgtosEstornados(qtdPagamentosEstornados);
			relacaoBancariaPesquisaTO.setTotalEstornados(totalValorPagamentosEstornados);

			relacaoBancariaPesquisaTO.setTotalPgtosRestantes(qtdPagamentosRestantes);
			relacaoBancariaPesquisaTO.setTotalRestantes(totalValorPagamentosRestantes);

			relacaoBancariaPesquisaTO.setTotalPgtosItens(qtdPagamentosEstornados + qtdPagamentosPagos + qtdPagamentosRestantes);
			relacaoBancariaPesquisaTO.setTotalItens(totalValorPagamentosEstornados
				.add(totalValorPagamentosPagos)
				.add(totalValorPagamentosRestantes));
		}

		return relacaoBancariaPesquisaTO;
	}

	@Override
	protected void doAfterLoad(RelacaoBancariaTO relacaoBancariaTO) {
		if (relacaoBancariaTO != null && relacaoBancariaTO.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(relacaoBancariaTO.getCriador());
			if (list != null && !list.isEmpty()) {
				relacaoBancariaTO.setCriador(list.get(0).getNome());
			}
		}

		if (relacaoBancariaTO != null && relacaoBancariaTO.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(relacaoBancariaTO.getAtualizador());
			if (list != null && !list.isEmpty()) {
				relacaoBancariaTO.setAtualizador(list.get(0).getNome());
			}
		}

		habilitarConciliacaoManual(relacaoBancariaTO);
	}

	public boolean habilitarConciliacaoManual(RelacaoBancariaTO relacaoBancariaTO) {
		if (relacaoBancariaTO.getContaCorrente() == null) {
			return Boolean.FALSE;
		}
		ContaCorrente contaCorrente = contaCorrenteRepository.findById(relacaoBancariaTO.getContaCorrente().getCodigo()).orElse(null);
		if (relacaoBancariaTO != null && RelacaoBancaria.MeioPagamento.INTERNET_BANKING.toString().equalsIgnoreCase(relacaoBancariaTO.getMeioPagamento()) &&
			RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO.getDescricao().equalsIgnoreCase(relacaoBancariaTO.getStatus()) && relacaoBancariaTO.getDataEnvioIb() != null &&
			contaCorrente != null && contaCorrente.getPrazoProcessamentoIb() != null) {

			int prazo = contaCorrente.getPrazoProcessamentoIb();
			Date now = GregorianCalendar.getInstance().getTime();
			Date envio = relacaoBancariaTO.getDataEnvioIb();

			long millis = now.getTime() - envio.getTime();
			long segundos = millis / 1000;
			long minutos = segundos / 60;
			long horas = minutos / 60;
			if (horas > prazo) {
				relacaoBancariaTO.setBaixaManual(Boolean.TRUE);
				return relacaoBancariaTO.getBaixaManual();
			}
		}
		relacaoBancariaTO.setBaixaManual(Boolean.FALSE);
		return relacaoBancariaTO.getBaixaManual();
	}

	public List<Pagamento> obterPagamentos(Long codigoItemRelacao) {
		ItemRelacao itemRelacao = new ItemRelacao();
		itemRelacao.setCodigo(codigoItemRelacao);
		return obterPagamentos(itemRelacao);
	}

	public List<Pagamento> obterPagamentos(ItemRelacao itemRelacao) {
		//return itemRelacaoPagamentoRepository.findPagamentosByItemRelacaoCodigo(itemRelacao.getCodigo());
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;

		List<ItemRelacaoPagamento> list = obterItemRelacaoPagamentoEPagamentoSoCodigo(itemRelacao.getCodigo());

		List<Pagamento> listaPagamentos = new ArrayList<>();

		for (ItemRelacaoPagamento itemRelacaoPagamento : list) {
			PagamentoTO pg = pagamentoService.obterPagamentoEDadosBancariosPeloCodigoPagamento(itemRelacaoPagamento.getPagamento().getCodigo());

			if (pg.getDadosBancarios() != null && pg.getDadosBancarios().getCodigo() != null) {
				pg.setDadosBancarios(dadosBancariosService.obterDadosBancarioCodigoComBanco(pg.getDadosBancarios().getCodigo()));
			}

			listaPagamentos.add(getMapper().map(pg, Pagamento.class));
		}

		return listaPagamentos;
	}

	private String obterNumero(List<Pagamento> pagamentosRB, List<Pagamento> listaPagamentos) {
		if (listaPagamentos.size() == 1) {
			return pagamentosRB.get(0).getNumero();
		} else {
			return "—";
		}
	}

	/**
	 * Verificamos se há apenas 1 pagamento. Se houver mais de
	 * 1 pagamento, significa que não há TR ou TA. Para TR ou TA,
	 * só haverá um pagamento.
	 *
	 * @param listaPagamentos A lista de pagamentos.
	 * @return <code>TRUE</code> se for TA ou TR. <code>FALSE</code> caso contrário.
	 */
	private boolean isTRorTA(List<Pagamento> listaPagamentos) {
		if (listaPagamentos != null && !listaPagamentos.isEmpty()) {
			if (Pagamento.TipoPagamento.TRIBUTO.equals(listaPagamentos.get(0).getTipo())
				|| Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(listaPagamentos.get(0).getTipo())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String obterNomeTributo(List<PagamentoTO> pagamentos) {
		if (pagamentos != null && !pagamentos.isEmpty()) {
			return obterNomeTributo(pagamentos.get(0));
		}
		return null;
	}

	private List<DadosBancariosTO> obterDadosBancarios(Long codigoCredor, List<Pagamento> listaPagamentos) {
		List<DadosBancarios> list = obterDadosbancariosDeUmCredor(codigoCredor);

		// Usado para informa se existe algum banco selecionado.
		boolean selecionado = false;
		for (DadosBancarios bancarios : list) {
			/*
			 * Todos os pagamentos são apenas um banco, tipo de documento, fornecedor e data de pagamento.
			 * Neste caso, o primeiro banco igual ao banco usado nas pagamentos será o selecionado.
			 */
			if (listaPagamentos != null && !listaPagamentos.isEmpty() && listaPagamentos.get(0).getDadosBancarios() != null && listaPagamentos.get(0).getDadosBancarios().getCodigo() != null && listaPagamentos.get(0).getDadosBancarios().getCodigo().equals(bancarios.getCodigo())) {
				bancarios.setSelecao(Boolean.TRUE);
				selecionado = true;
				break;
			}
		}

		/*
		 * Neste momento, caso não tenha nenhum banco selecionado,
		 * iremos marcar o banco principal como selecionado. Isso
		 * para apresentar o "X" se seleção na tabela de bancos.
		 * Apenas para indicar ao usuário que há um banco selecionado.
		 */
		if (!selecionado) {
			for (DadosBancarios bancarios : list) {
				if (bancarios.getPrincipal()) {
					bancarios.setSelecao(true);
				}
			}
		}

		List<DadosBancariosTO> listTO = new ArrayList<>();

		for (DadosBancarios dadosBancarios : list) {
			listTO.add(getMapper().map(dadosBancarios, DadosBancariosTO.class));
		}

		return listTO;
	}

	@Override
	public String obterNomeTributo(PagamentoTO pagamento) {
		if (pagamento != null) {
			NotaTributoAliquota nta = notaTributoAliquotaRepository.findByPagamentoCodigo(pagamento.getCodigo());
			if (Pagamento.TipoPagamento.TRIBUTO.getDescricao().equalsIgnoreCase(pagamento.getTipo())) {
				return nta.getAliquota().getCategoriaTributo().getTributo().getNome();
			} else if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.getDescricao().equalsIgnoreCase(pagamento.getTipo())) {
				List<Pagamento> listaTAs = (List<Pagamento>) pagamentoService.findByTributoAcumuladoCodigo(pagamento.getCodigo()).fetch();
				return listaTAs.get(0).getNotaTributoAliquota().getAliquota().getCategoriaTributo().getTributo().getNome();
			}
		}
		return null;
	}

	@Override
	public BancoTO obterBancoCredorSelecionado(ItemRelacaoTO itemRelacao, List<PagamentoTO> pagamentos) {
		if (pagamentos != null && !pagamentos.isEmpty() && pagamentos.get(0).getBancoBoleto() != null) {
			Banco banco = bancoRepository.findById(pagamentos.get(0).getBancoBoleto().getCodigo()).orElse(null);
			return getMapper().map(banco, BancoTO.class);

		} else if (pagamentos != null && !pagamentos.isEmpty() && pagamentos.get(0).getCodigoBarras() != null && !pagamentos.get(0).getCodigoBarras().isEmpty()) {
			String codigoBanco = pagamentos.get(0).getCodigoBarras().substring(0, 3);

			return getMapper().map(bancoRepository.findById(new Long(codigoBanco)).orElse(null), BancoTO.class);

			//FIXME conferir o getCredor!!
		} else if (pagamentos.get(0).getCredor() == null || dadosBancariosRepository.findByPessoaCodigo(pagamentos.get(0).getCredor().getCodigo()).isEmpty()) {
			return new BancoTO();
		} else if (pagamentos != null && !pagamentos.isEmpty() && pagamentos.get(0).getDadosBancarios() != null) {
			return getMapper().map(pagamentos.get(0).getDadosBancarios().getBanco(), BancoTO.class);
		} else if (pagamentos.get(0).getCredor() != null) {
			// Não tendo banco em pagamento, retornar o principal.
			for (DadosBancarios dadosBancarios : obterDadosbancariosDeUmCredor(pagamentos.get(0).getCredor().getCodigo())) {
				if (dadosBancarios.getPrincipal()) {
					return getMapper().map(dadosBancarios.getBanco(), BancoTO.class);
				}
			}
		}
		return new BancoTO();
	}

	private String obterNomeBancoDoCodigoDeBarrasELinhaDigitavel(List<Pagamento> pagamentos) {
		Banco banco = pagamentos.get(0).getBancoBoleto();
		String codigoDeBarras = pagamentos.get(0).getCodigoBarras();
		String linhaDigitavel = pagamentos.get(0).getLinhaDigitavel();

		if (banco == null) {

			if (codigoDeBarras != null && !StringUtils.isEmpty(codigoDeBarras)) {
				String codigoDoBancoDoCodigoDeBarras = codigoDeBarras.replace(" ", "").substring(0, 3);
				Banco bancoCodigoBarra = bancoRepository.findById(Long.parseLong(codigoDoBancoDoCodigoDeBarras)).orElse(null);
				if (bancoCodigoBarra != null)
					return bancoCodigoBarra.getNome();
				else
					return null;
			}

			if (linhaDigitavel != null && !StringUtils.isEmpty(linhaDigitavel)) {
				String codigoDoBancoDaLinhaDigitavel = linhaDigitavel.replace(" ", "").substring(0, 3);
				Banco bancoLinhaDigitavel = bancoRepository.findById(Long.parseLong(codigoDoBancoDaLinhaDigitavel)).orElse(null);
				return bancoLinhaDigitavel.getNome();
			}

		}

		return null;
	}

	private boolean exibirDadosCongelados(ItemRelacao itemRelacao, String status) {
		if (Check.isNotEmpty(status)) {
			switch (status) {
				case "A processar (internet banking)":
				case "Em processamento (internet banking)":
				case "Aguardando retorno":
				case "Processando retorno (internet banking)":
				case "Finalizada": {
					if (itemRelacao.existemDadosBancariosRB()) {
						return true;
					}
				}
				default:
					break;
			}
		}
		return false;
	}

	// FIXME: Melhorar esse código.
	private void atualizarPagamentosDaRelacaoBancariaPagamento(List<ItemRelacao> listaItensRelacao) {
		for (ItemRelacao itemRelacao : listaItensRelacao) {
			List<Pagamento> listaPgtos = new ArrayList<>();
			for (ItemRelacaoPagamento irp : obterItemRelacaoPagamentoEPagamentoSoCodigo(itemRelacao.getCodigo())) {
				listaPgtos.add(new Pagamento(irp.getPagamento().getCodigo()));
			}
			for (Pagamento pagamento : listaPgtos) {
				try {
					pagamentoService.updateStatusPagamento(pagamento.getCodigo());
				} catch (Exception e) {
					e.printStackTrace();
					throw new ValidationException("RelacaoBancariaServiceImpl", e.getMessage());
				}
			}
		}
	}

	@Transactional
	@Override
	protected void doBeforeDelete(RelacaoBancariaTO object) throws ServiceException {
		RelacaoBancaria relacaoBancaria = select(Projections.fields(RelacaoBancaria.class, qRelacaoBancaria.codigo, qRelacaoBancaria.status)).where(qRelacaoBancaria.codigo.eq(object.getCodigo())).fetchOne();
		if (RelacaoBancaria.StatusRelacaoBancaria.ABERTA.equals(relacaoBancaria.getStatus())) {
			List<ItemRelacao> listaItensRelacao = itemRelacaoRepository.findByRelacaoBancariaCodigo(object.getCodigo());
			for (ItemRelacao itemRelacao : listaItensRelacao) {
				for (ItemRelacaoPagamento itemRelacaoPagamento : obterItemRelacaoPagamentoEPagamentoSoCodigo(itemRelacao.getCodigo())) {
					pagamentoService.updateStatusPagamento(itemRelacaoPagamento.getPagamento().getCodigo());
					itemRelacaoPagamentoRepository.delete(itemRelacaoPagamentoRepository.findById(itemRelacaoPagamento.getId()).get());
				}

				List<ChequeContaCorrenteItemRelacaoTO> listaChequeContaCorrenteItemRelacaos = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoItemRelacao(itemRelacao.getCodigo());

				for (ChequeContaCorrenteItemRelacaoTO chequeContaCorrenteItemRelacao : listaChequeContaCorrenteItemRelacaos) {
					chequeContaCorrenteItemRelacaoRepository.deleteById(chequeContaCorrenteItemRelacao.getCodigoChequeContaCorrente());
				}

				itemRelacaoRepository.deleteById(itemRelacao.getCodigo());
			}
		} else {
			throw new ValidationException("RelacaoBancariaServiceImpl", "A Relação Bancária não pode ser removida com as situações diferente de 'Aberta'.");
		}
	}

	/**
	 * Atualiza os items da relação bancária.
	 *
	 * @param listaItensRelacao Os itens da RB obter os items a serem atualizados.
	 * @param status            O novo status do ItemRelacao.
	 */
	private void atualizarItemRelacaoDaRelacaoBancaria(List<ItemRelacao> listaItensRelacao, ItemRelacao.StatusItemRelacao status) {
		for (ItemRelacao itemRelacao : listaItensRelacao) {
			itemRelacao.setStatus(status);
			itemRelacaoRepository.save(itemRelacao);
		}
	}

	@Override
	public RelacaoBancariaTO cancelarRelacaoBancaria(Long id) throws Exception {
		try {
			return cancelarRB(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public RelacaoBancariaTO cancelarRB(Long relacaoBancariaCod) throws ValidationException {
		RelacaoBancaria relacaoBancaria = getRepository().findByCodigo(relacaoBancariaCod);
		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);
		List<ItemRelacao> listarItemRelacao = itemRelacaoRepository.findByRelacaoBancariaCodigo(relacaoBancaria.getCodigo());

		relacaoBancaria.setStatus(RelacaoBancaria.StatusRelacaoBancaria.CANCELADA);
		relacaoBancaria.setDataCancelamento(new Date());
		relacaoBancaria.setColaboradorCancelamento(colaborador);

		atualizarPagamentosDaRelacaoBancariaPagamento(listarItemRelacao);

		atualizarItemRelacaoDaRelacaoBancaria(listarItemRelacao, ItemRelacao.StatusItemRelacao.ESTORNADO);

		if (relacaoBancaria.getMeioPagamento().equals(RelacaoBancaria.MeioPagamento.CHEQUE)) {
			listarItemRelacao.forEach(itemRelacao -> {
				List<ChequeContaCorrenteItemRelacaoTO> list = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoItemRelacao(itemRelacao.getCodigo());
				list.forEach(chequeContaCorrenteItemRelacaoTO -> {
					Optional<ChequeContaCorrente> chequeContaCorrente = chequeContaCorrenteRepository.findById(chequeContaCorrenteItemRelacaoTO.getCodigoChequeContaCorrente());
					if (chequeContaCorrente.isPresent()) {
						chequeContaCorrente.get().setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.ESTORNADO);
						chequeContaCorrenteRepository.save(chequeContaCorrente.get());
					}
				});
			});
		}

		getRepository().save(relacaoBancaria);

		return map(relacaoBancaria);
	}

	@Override
	public RelacaoBancariaTO enviarRelacaoBancaria(Long id) throws Exception {
		try {
			return enviarRB(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public boolean getCorTipoDocumento(Pagamento.TipoDocumentoPagamento tipo, Pessoa credor, RelacaoBancaria relacaoBancaria) {
		if (tipo != null
			&& credor != null
			&& credor.getCodigo() != null
			&& (tipo.equals(Pagamento.TipoDocumentoPagamento.DEPOSITO)
			|| tipo.equals(Pagamento.TipoDocumentoPagamento.DDA)
			|| tipo.equals(Pagamento.TipoDocumentoPagamento.DOC)
			|| tipo.equals(Pagamento.TipoDocumentoPagamento.TED))
			&& relacaoBancaria != null
			&& RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())
			&& (credor.getListaDadosBancarios() == null || credor.getListaDadosBancarios().isEmpty())) {
			return true;
		}
		return false;

	}

	private boolean validarLinhaDigitavelEFavorecido(String acao, RelacaoBancaria relacaoBancaria) throws NumberFormatException, ValidationException {
		boolean msgValor = false;
		boolean msgVencimento = false;
		boolean msgLinhaInvalida = false;
		int count = 0;
		int posicao = 0;
		String strMsgEnvio = RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento()) ? "O envio da relação bancária não será permitido." : "";

		if (relacaoBancaria.getItens() != null && !relacaoBancaria.getItens().isEmpty()) {
			for (ItemRelacao item : relacaoBancaria.getItens()) {
				if (item != null && item.getPagamentos() != null && !item.getPagamentos().isEmpty()) {
					Pagamento pgto = item.getPagamentos().get(0);

					if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pgto.getTipoDocumentoPagamento())) {
						try {
							verificarLinhaDigitavelBoleto(item);

							if ("enviar".equals(acao)) {
								verificarLinhaDigitavelGuia(item);
								verificarLinhaDigitavelConcessionaria(item);
							}
						} catch (RelacaoBancariaValidateException e) {
							if (item.isPagamentoSemNumeroBoleto() && !msgLinhaInvalida) {
								msgLinhaInvalida = true;
								throw new ValidationException("RelacaoBancariaServiceImpl", String.format(e.getMessage(), "marrom", strMsgEnvio));
							}

							if (item.isErroValorLinhaDigitavel() && !msgValor) {
								msgValor = true;
								throw new ValidationException("RelacaoBancariaServiceImpl", String.format(e.getMessage(), "laranja", strMsgEnvio));
							}

							if (item.isVencimentoPosterior() && !msgVencimento) {
								msgVencimento = true;
								throw new ValidationException("RelacaoBancariaServiceImpl", String.format(e.getMessage(), "vermelho", strMsgEnvio));
							}
						}
					} else if (getCorTipoDocumento(pgto.getTipoDocumentoPagamento(), pgto.getCredor(), relacaoBancaria)) {
						count++;
						posicao = relacaoBancaria.getItens().indexOf(item) + 1;
					}
				}
			}

			if (count == 1) {
				throw new ValidationException("RelacaoBancariaServiceImpl", "O favorecido do pagamento destacado em vermelho na " + posicao + "ª linha não possui dado(s) bancário(s) cadastrado(s).");
			}

			if (count > 1) {
				throw new ValidationException("RelacaoBancariaServiceImpl", "Os favorecidos dos pagamentos destacados em vermelho não possuem dados bancários cadastrados.");
			}
		}

		return !msgValor && !msgVencimento && !msgLinhaInvalida;
	}

	@Override
	@Transactional()
	public RelacaoBancariaTO enviarRB(Long relacaoBancariaCod) throws Exception {
		String acao = "enviar";

		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;

		RelacaoBancaria relacaoBancaria = select()
			.select(Projections.fields(RelacaoBancaria.class, qRelacaoBancaria.codigo, qRelacaoBancaria.meioPagamento, qRelacaoBancaria.dataPagamento,
				Projections.fields(ContaCorrente.class, qContaCorrente.codigo).as("contaCorrente")))
			.where(qRelacaoBancaria.codigo.eq(relacaoBancariaCod))
			.fetchOne();

		if (!validarLinhaDigitavelEFavorecido("enviar", relacaoBancaria) && RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			return null;
		}

		List<ItemRelacao> listaItensRelacao = itemRelacaoRepository.findByRelacaoBancariaCodigo(relacaoBancaria.getCodigo());

		for (ItemRelacao itemRelacao : listaItensRelacao) {
			itemRelacao.setPagamentos(obterPagamentos(itemRelacao));
		}

		if (listaItensRelacao == null || listaItensRelacao.isEmpty()) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Não existem pagamentos associados à RB. Envio não permitido.");
		}

		if (this.vencimentoPosteriorCodigoBarrasLinhaDigitavelRB(relacaoBancaria, listaItensRelacao)) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "O vencimento do(s) boleto(s) indicado(s) em vermelho é anterior à data de pagamento da relação bancária. Envio não permitido.");
		}

		try {
			this.validarNumerosBoletosPagamentoRelacaoBancaria(relacaoBancaria, listaItensRelacao);
			this.validarLinhaDigitavelPagamentosRelacaoBancaria(relacaoBancaria, listaItensRelacao, acao);
		} catch (RelacaoBancariaValidateException e) {
			e.printStackTrace();
			throw new ValidationException("RelacaoBancariaServiceImpl", e.getMessage());
		}
		try {
			this.validarRelacaoBancariaComInternetBanking(relacaoBancaria, listaItensRelacao);
			this.validarDadosBancariosSelecionadosNaoHomologados(relacaoBancaria, listaItensRelacao);
			this.validarLimiteTed(listaItensRelacao);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new ValidationException("RelacaoBancariaServiceImpl", e.getMessage());
		}
		if (relacaoBancaria.getMeioPagamento() != null && !RelacaoBancaria.MeioPagamento.CHEQUE.equals(relacaoBancaria.getMeioPagamento())) {
			try {
				this.validarItemRelacaoSemDadosBancarios(relacaoBancaria, listaItensRelacao);
			} catch (ValidationException e) {
				throw new ValidationException("RelacaoBancariaServiceImpl", e.getMessage());
			}
		}

		updateAtualizadorAndDataAtualizacao(relacaoBancaria.getCodigo());
		updateDataEnvio(relacaoBancaria.getCodigo());
		updateColaboradorEnvio(relacaoBancaria.getCodigo());

		for (ItemRelacao itemRelacao : listaItensRelacao) {
			for (Pagamento pg : itemRelacao.getPagamentos()) {
				pagamentoService.updateDataPagamento(relacaoBancaria.getDataPagamento(), pg.getCodigo());

				if (pg.getDadosBancarios().getCodigo() != null) {
					DadosBancarios dadosBancarios = getMapper().map(dadosBancariosService.obterDadosBancarioSimples(pg.getDadosBancarios().getCodigo()), DadosBancarios.class);

					if (dadosBancarios != null) {
						if(dadosBancarios.getSituacao().equals(Situacao.INATIVO) &&
						   !(pg.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.BOLETO) ||
							pg.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.GUIA) ||
							pg.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.CONCESSIONARIA))) {
							throw new ValidationException ("RelacaoBancariaServiceImpl", "Há pagamentos que possuem dados bancários inválidos. O envio da relação bancária não será permitido");
						} else {
							itemRelacao.setBanco(dadosBancarios.getBanco());
							itemRelacao.setAgencia(dadosBancarios.getAgencia());
							itemRelacao.setDigitoAgencia(dadosBancarios.getDigitoAgencia());
							itemRelacao.setConta(dadosBancarios.getConta());
							itemRelacao.setDigito(dadosBancarios.getDigito());
							itemRelacao.setTipoConta(dadosBancarios.isPoupanca() ? ItemRelacao.TipoContaBancaria.CONTA_POUPANCA : ItemRelacao.TipoContaBancaria.CONTA_CORRENTE);
							itemRelacao.setTipoDocumentoPagamento(pg.getTipoDocumentoPagamento());
							itemRelacao.setDataPagamento(relacaoBancaria.getDataPagamento());

							if (RelacaoBancaria.MeioPagamento.CHEQUE.equals(relacaoBancaria.getMeioPagamento())) {
								List<ChequeContaCorrente> chequeList = chequeContaCorrenteRepository.findByContaCorrenteCodigoAndChequeAndSituacao(relacaoBancaria.getContaCorrente().getCodigo(), itemRelacao.getCheque(), ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
								if (!chequeList.isEmpty()) {
									ChequeContaCorrente cheque = chequeList.get(chequeList.size() -1);
									chequeContaCorrenteRepository.updateDataCompensacao(relacaoBancaria.getDataPagamento(), cheque.getCodigo());
									chequeContaCorrenteRepository.updateAtualizador(securityService.getUser().getLogin(), cheque.getCodigo());
									chequeContaCorrenteRepository.updateDataAtualizacao(new Date(), cheque.getCodigo());
								}
							}
						}

					}
				}

				recalcularDataVencimentoTributo(pg, relacaoBancariaCod);
			}

			itemRelacao.setStatus(ItemRelacao.StatusItemRelacao.ENVIADO);
			itemRelacaoRepository.save(itemRelacao);
		}

		if (RelacaoBancaria.MeioPagamento.CHEQUE.equals(relacaoBancaria.getMeioPagamento()) ||
			RelacaoBancaria.MeioPagamento.BANKLINE_MANUAL.equals(relacaoBancaria.getMeioPagamento())) {
			updateStatusAGUARDANDORETORNO(relacaoBancaria.getCodigo());
		} else if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			updateStatusAPROCESSAR(relacaoBancaria.getCodigo());
		}

		return map(relacaoBancaria);
	}

	/**
	 * SGINV-1876
	 *
	 * @param pagamento
	 */
	private void recalcularDataVencimentoTributo(Pagamento pagamento, Long codigoRelacaoBancaria) {
		ParcelaNotaTO parcelaNotaTO = parcelaNotaService.obterParcelaCodigoEHomologador(pagamento.getCodigo());

		if (parcelaNotaTO != null && parcelaNotaTO.getNota() != null && parcelaNotaTO.getNota().getCodigo() != null) {

			NotaFiscalTO notaFiscalTO = notaFiscalService.buscarNotaFiscalById(parcelaNotaTO.getNota().getCodigo());

			if (notaFiscalTO != null) {

				String servicos = "SERVICOS";

				if (servicos.equals(notaFiscalTO.getTipoFornecimento())) {
					List<NotaTributoAliquotaTO> listaNtaTO = notaTributoAliquotaService.listarPorCodigoNota(parcelaNotaTO.getNota().getCodigo());

					if (listaNtaTO != null && !listaNtaTO.isEmpty()) {

						for (NotaTributoAliquotaTO ntaTO : listaNtaTO) {

							// codigo 1 do tributo "PIS/COFINS/CSLL"
							if (ntaTO.getAliquota().getCodigoTributo() == 1) {

								PagamentoTO pagTR = notaTributoAliquotaService.buscarPagamentoById(ntaTO.getCodigo());

								if (pagTR != null && pagTR.getCodigo() != null) {

									Set<String> conjStatus = new HashSet<>();
									conjStatus.add("Aberta");
									conjStatus.add("Aguardando Agrupamento");
									conjStatus.add("Em Autorização");

									if (conjStatus.contains(pagTR.getStatus())) {
										Date novaDataVencimento = pagamentoService.recalcularDataVencimento(ntaTO.getCodigo(), codigoRelacaoBancaria);
										pagamentoService.updateDataVencimento(novaDataVencimento, pagTR.getCodigo());
										pagamentoService.updateDataPagamento(novaDataVencimento, pagTR.getCodigo());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void removerPagamentoRB(@PathVariable Long id) throws Exception {
		try {
			removerPagamentoRb(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void removerPagamentoRb(Long itemRelacaoBancariaCod) {
		List<ItemRelacao> itensList = new ArrayList<>();
		itensList.add(itemRelacaoRepository.findById(itemRelacaoBancariaCod).orElse(null));

		// Obter os pagamentos que serão estornados para serem removidos da quantidade da RB.
		List<Pagamento> quantidadePagamento =
			(List<Pagamento>) pagamentoService.findByItemRelacaoPagamentoCodigo(itemRelacaoBancariaCod).fetch();

		atualizarPagamentosDaRelacaoBancariaPagamento(itensList);

		atualizarRelacionamentosRB(itensList.get(0));

		List<ChequeContaCorrenteItemRelacao> chequesItemRelacao = new ArrayList<>();

		if (itensList != null && !itensList.isEmpty()) {
			for (ItemRelacao itemRelacao : itensList) {
				List<ChequeContaCorrenteItemRelacao> itensCheque = chequeContaCorrenteItemRelacaoRepository.findByCodigoItemRelacao(itemRelacao.getCodigo());
				chequesItemRelacao.addAll(itensCheque);
			}

			if (chequesItemRelacao != null && !chequesItemRelacao.isEmpty()) {
				for (ChequeContaCorrenteItemRelacao cheque : chequesItemRelacao) {
					chequeContaCorrenteItemRelacaoRepository.delete(cheque);
				}
			}
		}

		itemRelacaoRepository.delete(itensList.get(0));

		RelacaoBancaria relacaoBancaria = getRepository().findById(itensList.get(0).getRelacaoBancaria().getCodigo()).orElse(null);

		relacaoBancaria.setQtdPagamentos(relacaoBancaria.getQtdPagamentos() - quantidadePagamento.size());

		getRepository().save(relacaoBancaria);
	}

	private void validarDadosPagamento(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) throws Exception {
		if (relacaoBancariaPagamentosVO.getLinhaDigitavel() != null && !relacaoBancariaPagamentosVO.getLinhaDigitavel().trim().equals(Constants.BLANK)) {
			relacaoBancariaPagamentosVO.setCodigoBarras(BoletoUtil.linhaDigitavelNumericaEmCodigoDeBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel()));
		} else if (relacaoBancariaPagamentosVO.getCodigoBarras() != null && !relacaoBancariaPagamentosVO.getCodigoBarras().trim().equals(Constants.BLANK)) {
			relacaoBancariaPagamentosVO.setLinhaDigitavel(BoletoUtil.codigoDeBarrasEmLinhaDigitavelNumerica(relacaoBancariaPagamentosVO.getCodigoBarras()));
		}

		if (Pagamento.TipoDocumentoPagamento.DOC.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())) {
			String aux = parametroRepository.findByChave("tesouraria.limite.superior.doc").getValor();
			BigDecimal valorDoc = new BigDecimal(aux);

			if (relacaoBancariaPagamentosVO.getValorAPagar().doubleValue() >= valorDoc.doubleValue()) {
				throw new ValidationException("RelacaoBancariaServiceImpl", String.format("Este pagamento possui valor maior ou igual ao valor permitido para DOC (%s).", Util.formatarValor(valorDoc)));
			}
		}

		if (Pagamento.TipoPagamento.TRIBUTO.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoPagamento()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoPagamento())) {
			if (relacaoBancariaPagamentosVO.getDataPagamento().after(relacaoBancariaPagamentosVO.getDataVencimento())) {
				if (relacaoBancariaPagamentosVO.getValorMulta() == null || relacaoBancariaPagamentosVO.getValorMulta().equals(BigDecimal.ZERO)) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Multa é um campo obrigatório");
				}
				if (relacaoBancariaPagamentosVO.getValorJuros() == null || relacaoBancariaPagamentosVO.getValorJuros().equals(BigDecimal.ZERO)) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Juros é um campo obrigatório");
				}
			}
		}

		if (relacaoBancariaPagamentosVO.getDadosBancarios() == null && relacaoBancariaPagamentosVO.getListDadosBancarios() != null && !relacaoBancariaPagamentosVO.getListDadosBancarios().isEmpty()) {
			for (DadosBancariosTO dbTO : relacaoBancariaPagamentosVO.getListDadosBancarios()) {
				if (dbTO.getSelecao() == true)
					relacaoBancariaPagamentosVO.setDadosBancarios(dbTO);
			}
		}

		if ((Pagamento.TipoDocumentoPagamento.DEPOSITO.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.DDA.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())
			|| Pagamento.TipoDocumentoPagamento.DOC.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.TED.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())) &&
			(relacaoBancariaPagamentosVO.getDadosBancarios() == null || relacaoBancariaPagamentosVO.getDadosBancarios().getCodigo() == null)) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Não é permitido selecionar \"" + relacaoBancariaPagamentosVO.getTipoDocumentoPagamento() + "\" para um credor/favorecido que não possui dados bancários.");
		}

		if (relacaoBancariaPagamentosVO.getDadosBancarios() != null && relacaoBancariaPagamentosVO.getDadosBancarios().getCodigo() != null) {
			DadosBancarios dadosBancarios = dadosBancariosRepository.findById(relacaoBancariaPagamentosVO.getDadosBancarios().getCodigo()).orElse(null);
			if (Pagamento.TipoDocumentoPagamento.DEPOSITO.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento()) && !dadosBancarios.getHomologado()) {
				throw new ValidationException("RelacaoBancariaServiceImpl", "Os dados bancários selecionados não estão homologados e, por isso, não é permitido selecionar 'Depósito em conta/transferência'.");
			}
		}

		if (Pagamento.TipoDocumentoPagamento.BOLETO.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(relacaoBancariaPagamentosVO.getInfoBoleto())) {
				if (relacaoBancariaPagamentosVO.getLinhaDigitavel() == null || Constants.BLANK.equals(relacaoBancariaPagamentosVO.getLinhaDigitavel())) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Linha digitável é obrigatória.");

				} else if (boletoService.validarLinhaAndCodigoBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(relacaoBancariaPagamentosVO, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					relacaoBancariaPagamentosVO.setCodigoBarras(BoletoUtil.linhaDigitavelNumericaEmCodigoDeBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel().replace(".", "")));
					relacaoBancariaPagamentosVO.setLinhaDigitavel(relacaoBancariaPagamentosVO.getLinhaDigitavel().replace(".", ""));
				}

				BigDecimal valorLinhaDigitavel = null;

				if (relacaoBancariaPagamentosVO.getLinhaDigitavel().contains(".")) {
					valorLinhaDigitavel = BoletoUtil.getValorBigDecimalDoTituloDaLinhaDigitavelFormatada(relacaoBancariaPagamentosVO.getLinhaDigitavel());
				} else {
					valorLinhaDigitavel = BoletoUtil.getValorBigDecimalDoTituloDoCodigoDeBarras(relacaoBancariaPagamentosVO.getCodigoBarras());
				}

				if (valorLinhaDigitavel.compareTo(BigDecimal.ZERO) != 0) {
					if (valorLinhaDigitavel.compareTo(relacaoBancariaPagamentosVO.getValorNota()) != 0) {
						if (valorLinhaDigitavel.compareTo(relacaoBancariaPagamentosVO.getValorAPagar()) != 0) {
							throw new ValidationException("RelacaoBancariaServiceImpl", "O valor do campo \"Valor da nota\" está diferente do valor informado na linha digitável do pagamento.");
						}
					}
				}

			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(relacaoBancariaPagamentosVO.getInfoBoleto())) {
				if (relacaoBancariaPagamentosVO.getCodigoBarras() == null || Constants.BLANK.equals(relacaoBancariaPagamentosVO.getCodigoBarras())) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Código de barras é obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(relacaoBancariaPagamentosVO.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(relacaoBancariaPagamentosVO, PagamentoTO.class))) {
					relacaoBancariaPagamentosVO.setLinhaDigitavel(BoletoUtil.codigoDeBarrasEmLinhaDigitavelNumerica(relacaoBancariaPagamentosVO.getCodigoBarras().replace(".", "")));
				}

				BigDecimal valorCodigoBarras = BoletoUtil.getValorBigDecimalDoTituloDoCodigoDeBarras(relacaoBancariaPagamentosVO.getCodigoBarras());

				if (valorCodigoBarras.compareTo(BigDecimal.ZERO) != 0 && !relacaoBancariaPagamentosVO.istAorTR()) {
					if (valorCodigoBarras.compareTo(relacaoBancariaPagamentosVO.getValorNota()) != 0) {
						if (valorCodigoBarras.compareTo(relacaoBancariaPagamentosVO.getValorAPagar()) != 0) {
							throw new ValidationException("RelacaoBancariaServiceImpl", "O valor do campo \"Valor da nota\" está diferente do valor informado na linha digitável do pagamento.");
						}
					}
				}
			}

			// Obter o código do banco de um código de barras.
			if (boletoService.validarBancoDoCodigoDeBarras(relacaoBancariaPagamentosVO.getCodigoBarras())) {

				BancoTO banco = boletoService.getCodDoBancoDoCodigoDeBarras(relacaoBancariaPagamentosVO.getCodigoBarras());

				relacaoBancariaPagamentosVO.setBancoBoleto(getMapper().map(banco, BancoTO.class));

			} else {
				throw new ValidationException("RelacaoBancariaServiceImpl", String.format("Banco com código [%s] não existe.", boletoService.getCodDoBancoDoCodigoDeBarras(relacaoBancariaPagamentosVO.getCodigoBarras())));

			}
		}

		if (Pagamento.TipoDocumentoPagamento.GUIA.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento()) &&
			((relacaoBancariaPagamentosVO.getLinhaDigitavel() != null || relacaoBancariaPagamentosVO.getCodigoBarras() != null))
		) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(relacaoBancariaPagamentosVO.getInfoBoleto())) {
				if (boletoService.validarLinhaAndCodigoBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(relacaoBancariaPagamentosVO, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					relacaoBancariaPagamentosVO.setCodigoBarras(GuiaUtil.linhaDigitavelNumericaEmCodigoDeBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel()));
					relacaoBancariaPagamentosVO.setLinhaDigitavel(GuiaUtil.linhaDigitavelFormatadaEmNumerica(relacaoBancariaPagamentosVO.getLinhaDigitavel()));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(relacaoBancariaPagamentosVO.getInfoBoleto())) {
				if (boletoService.validarLinhaAndCodigoBarras(relacaoBancariaPagamentosVO.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(relacaoBancariaPagamentosVO, PagamentoTO.class))) {
					relacaoBancariaPagamentosVO.setLinhaDigitavel(GuiaUtil.codigoDeBarrasEmLinhaDigitavelNumerica(relacaoBancariaPagamentosVO.getCodigoBarras().replace(".", "")));

				}
			}
		}

		if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(relacaoBancariaPagamentosVO.getInfoBoleto())) {
				if (relacaoBancariaPagamentosVO.getLinhaDigitavel() == null || Constants.BLANK.equals(relacaoBancariaPagamentosVO.getLinhaDigitavel())) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Linha digitável é obrigatória.");

				} else if (boletoService.validarLinhaAndCodigoBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(relacaoBancariaPagamentosVO, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					relacaoBancariaPagamentosVO.setCodigoBarras(ConcessionariaUtil.linhaDigitavelNumericaEmCodigoDeBarras(relacaoBancariaPagamentosVO.getLinhaDigitavel()));
					relacaoBancariaPagamentosVO.setLinhaDigitavel(ConcessionariaUtil.linhaDigitavelFormatadaEmNumerica(relacaoBancariaPagamentosVO.getLinhaDigitavel()));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(relacaoBancariaPagamentosVO.getInfoBoleto())) {
				if (relacaoBancariaPagamentosVO.getCodigoBarras() == null || Constants.BLANK.equals(relacaoBancariaPagamentosVO.getCodigoBarras())) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Código de barras é obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(relacaoBancariaPagamentosVO.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(relacaoBancariaPagamentosVO, PagamentoTO.class))) {
					relacaoBancariaPagamentosVO.setLinhaDigitavel(ConcessionariaUtil.codigoDeBarrasEmLinhaDigitavelNumerica(relacaoBancariaPagamentosVO.getCodigoBarras().replace(".", "")));

				}
			}
		}
	}


	@Override
	public RelacaoBancariaPagamentosVO editarPagamentoRB(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) throws Exception {
		try {
			return editarPagamentoRb(relacaoBancariaPagamentosVO);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public RelacaoBancariaPagamentosVO editarPagamentoRb(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) throws Exception {

		Pagamento pagamento;

		if (relacaoBancariaPagamentosVO.getCodigo() != null) {
			ItemRelacao item = itemRelacaoRepository.findById(new Long(relacaoBancariaPagamentosVO.getCodigo())).orElse(null);
			ItemRelacaoPagamento irp = itemRelacaoPagamentoRepository.findByItemRelacaoCodigo(item.getCodigo()).get(0);
			pagamento = (Pagamento) pagamentoService.findByItemRelacaoPagamentoCodigo(irp.getCodigo()).fetch().get(0);
		} else {
			pagamento = pagamentoRepository.findById(relacaoBancariaPagamentosVO.getCodigoPagamento()).orElse(null);
		}

		if (relacaoBancariaPagamentosVO.getDadosBancarios() != null) {
			DadosBancarios dbVO = getMapper().map(relacaoBancariaPagamentosVO.getDadosBancarios(), DadosBancarios.class);
			pagamento.setDadosBancarios(dbVO);
		}

		pagamento.setDataAtualizacao(new Date());
		pagamento.setAtualizador(securityService.getUser().getLogin());

		validarDadosPagamento(relacaoBancariaPagamentosVO);

		pagamento.setLinhaDigitavel(relacaoBancariaPagamentosVO.getLinhaDigitavel());

		pagamento.setCodigoBarras(relacaoBancariaPagamentosVO.getCodigoBarras());

		pagamento.setValorJuros(relacaoBancariaPagamentosVO.getValorJuros());

		pagamento.setValorMulta(relacaoBancariaPagamentosVO.getValorMulta());

		pagamento.setValorDesconto(relacaoBancariaPagamentosVO.getValorDesconto());

		for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : Pagamento.TipoDocumentoPagamento.values()) {
			if (tipoDocumentoPagamento.getDescricao().equals(relacaoBancariaPagamentosVO.getTipoDocumentoPagamento())) {
				pagamento.setTipoDocumentoPagamento(tipoDocumentoPagamento);
				break;
			}
		}

		pagamento.setDataPagamento(relacaoBancariaPagamentosVO.getDataPagamento());

		pagamentoRepository.save(pagamento);

		if (relacaoBancariaPagamentosVO.getListaPgtoCC() != null && !relacaoBancariaPagamentosVO.getListaPgtoCC().isEmpty()) {
			for (PagamentoContaContabilTO pgTo : relacaoBancariaPagamentosVO.getListaPgtoCC()) {
				PagamentoContaContabil pagamentoCc = getMapper().map(pgTo, PagamentoContaContabil.class);
				pagamentoCc.setPagamento(pagamento);
				pagamentoCc.setTipo(getTipoPagamentoContaContabil(pgTo.getTipo()));
				pagamentoContaContabilRepository.save(pagamentoCc);
			}
		}

		if (relacaoBancariaPagamentosVO.getCodigo() != null) {
			ItemRelacao item = itemRelacaoRepository.findById(new Long(relacaoBancariaPagamentosVO.getCodigo())).get();

			if (item != null) {
				item.setTotal(pagamento.getValor().add(pagamento.getValorJuros()).add(pagamento.getValorMulta()).subtract(pagamento.getValorDesconto()));
				itemRelacaoRepository.save(item);
			}
		}

		relacaoBancariaPagamentosVO.setTotal(relacaoBancariaPagamentosVO.getValorAPagar());

		return relacaoBancariaPagamentosVO;

	}

	private void atualizarRelacionamentosRB(ItemRelacao itemRelacao) {
		for (ItemRelacaoPagamento irp : itemRelacaoPagamentoRepository.findByItemRelacaoCodigo(itemRelacao.getCodigo())) {
			itemRelacaoPagamentoRepository.delete(irp);
		}
	}

	@Override
	public void realizarBaixaBancaria(RelacaoBancariaTO relacaoBancariaTO) throws Exception {
		try {
			realizarBaixaBancariaRB(relacaoBancariaTO);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional
	public RelacaoBancariaTO realizarBaixaBancariaRB(RelacaoBancariaTO to) throws Exception {
		// Alterar o status para finalizado.
		try {
			RelacaoBancaria relacaoBancaria = getRepository().findByCodigo(to.getCodigo());

			relacaoBancaria.setStatus(RelacaoBancaria.StatusRelacaoBancaria.FINALIZADA);

			Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);

			relacaoBancaria.setColaboradorBaixaManual(colaborador);
			relacaoBancaria.setDataBaixaManual(new Date());
			relacaoBancaria.setUnidade(getMapper().map(to.getUnidade(), Unidade.class));

			int qtd = 0;

			for (RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO : to.getRelacaoBancariaPagamentosVO()) {
				ItemRelacao itemRelacao = itemRelacaoRepository.findById(new Long(relacaoBancariaPagamentosVO.getCodigo())).orElse(null);

				List<ChequeContaCorrenteItemRelacaoTO> listRelacao = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoItemRelacao(itemRelacao.getCodigo());

				ChequeContaCorrente chequeContaCorrente = null;

				if (!listRelacao.isEmpty()) {
					Stream<ChequeContaCorrenteItemRelacaoTO> stream = listRelacao.stream();
					ChequeContaCorrenteItemRelacaoTO last = stream.reduce((first, second) -> second).orElse(new ChequeContaCorrenteItemRelacaoTO());

					chequeContaCorrente = chequeContaCorrenteRepository.findById(last.getCodigoChequeContaCorrente()).orElse(null);
				}

				if (relacaoBancariaPagamentosVO.getStatusConciliacaoBancaria().equals("Pago")) {
					itemRelacao.setStatus(ItemRelacao.StatusItemRelacao.CONFIRMADO);

					if (chequeContaCorrente != null) {
						chequeContaCorrente.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.PAGO);
						chequeContaCorrenteRepository.save(chequeContaCorrente);
					}

					List<Pagamento> pagamentos = pagamentoRepository.findByItemRelacoesCodigo(itemRelacao.getCodigo());
					for (Pagamento pg : pagamentos) {
						pg.setStatus(Pagamento.Status.FINALIZADO);
						qtd++;
						pg.setDataPagamento(relacaoBancaria.getDataPagamento());
						pagamentoRepository.save(pg);
					}
				} else if (relacaoBancariaPagamentosVO.getStatusConciliacaoBancaria().equals("Estornado")) {
					itemRelacao.setStatus(ItemRelacao.StatusItemRelacao.ESTORNADO);
					if (chequeContaCorrente != null) {
						chequeContaCorrente.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.ESTORNADO);
						chequeContaCorrenteRepository.save(chequeContaCorrente);
					}
					RetornoBancario retornoBancario = retornoBancarioRepository.findById(relacaoBancariaPagamentosVO.getRetornoBancario().getCodigo()).orElse(null);
					itemRelacao.setRetornoBancario(retornoBancario);
					List<Pagamento> pagamentos = pagamentoRepository.findByItemRelacoesCodigo(itemRelacao.getCodigo());
					for (Pagamento pg : pagamentos) {
						pg.setStatus(Pagamento.Status.PAGAMENTO);
						pagamentoRepository.save(pg);
					}
				}
				itemRelacaoRepository.save(itemRelacao);
			}

			relacaoBancaria.setQtdPagamentos(relacaoBancaria.getQtdPagamentos() - qtd);

			getRepository().save(relacaoBancaria);
			return map(relacaoBancaria);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public RelacaoBancariaTO realizarBaixaBancariaManual(RelacaoBancariaTO to) throws Exception {
		try {
			return realizarBaixaBancariaManualRB(to, false);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public RelacaoBancariaTO estornarBaixaBancariaManual(RelacaoBancariaTO to) throws Exception {
		try {
			return realizarBaixaBancariaManualRB(to, true);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public RelacaoBancariaTO realizarBaixaBancariaManualRB(RelacaoBancariaTO to, Boolean estorno) throws ValidationException {
		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);
		RelacaoBancaria relacaoBancaria = getRepository().findByCodigo(to.getCodigo());

		if (!estorno && (to.getMotivoBaixaManual() == null || to.getMotivoBaixaManual().isEmpty())) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Motivo da baixa manual é um campo de preenchimento obrigatório.");
		}

		if (estorno) {
			relacaoBancaria.setColaboradorEstornoBaixaManual(colaborador);
			relacaoBancaria.setDataEstornoBaixaManual(new Date(System.currentTimeMillis()));
		}
		else {
			relacaoBancaria.setStatus(RelacaoBancaria.StatusRelacaoBancaria.FINALIZADA);
			relacaoBancaria.setColaboradorBaixaManual(colaborador);
			relacaoBancaria.setMotivoBaixaManual(to.getMotivoBaixaManual());
		}

		int qtdPgs = 0;

		for (RelacaoBancariaPagamentosVO vo : to.getRelacaoBancariaPagamentosVO()) {
			ItemRelacao itemRelacao = itemRelacaoRepository.findById(new Long(vo.getCodigo())).orElse(null);

			if (vo.getSelecionado() != null && vo.getSelecionado()) {

				if (estorno || (!itemRelacao.getStatus().equals(ItemRelacao.StatusItemRelacao.ESTORNADO) &&
					!itemRelacao.getStatus().equals(ItemRelacao.StatusItemRelacao.CONFIRMADO))) {

					itemRelacao.setStatus(ItemRelacao.StatusItemRelacao.CONFIRMADO);
					itemRelacaoRepository.save(itemRelacao);

					for (Pagamento pg : pagamentoRepository.findByItemRelacoesCodigo(new Long(vo.getCodigo()))) {
						pg.setStatus(Pagamento.Status.FINALIZADO);
						pg.setDataPagamento(relacaoBancaria.getDataPagamento());
						DadosBancarios dadosBancarios = dadosBancariosRepository.findByPagamentoCodigo(pg.getCodigo());
						if (dadosBancarios.getHomologado() == null || !dadosBancarios.getHomologado()) {
							if (pg.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.TED) || pg.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.DOC)) {
								pg.getDadosBancarios().setDataHomologacao(new Date());
								pg.getDadosBancarios().setHomologado(true);
							}
						}
						qtdPgs++;
						pagamentoRepository.save(pg);
					}
				}

			} else {
				if (!estorno || (estorno && vo.getCaminhoComprovante() == null && vo.getStatusPagamento().equals("Pago"))) {
					itemRelacao.setStatus(ItemRelacao.StatusItemRelacao.ESTORNADO);
					itemRelacao.setRetornoBancario(null);
					itemRelacaoRepository.save(itemRelacao);
					for (Pagamento pg : pagamentoRepository.findByItemRelacoesCodigo(new Long(vo.getCodigo()))) {
						pg.setStatus(Pagamento.Status.PAGAMENTO);
						pg.setDataAutorizador(new Date());
						pg.setAtualizador(securityService.getUser().getLogin());
						pagamentoRepository.save(pg);
					}
				}
			}
		}
		relacaoBancaria.setAtualizador(securityService.getUser().getLogin());
		relacaoBancaria.setQtdPagamentos(qtdPgs);

		getRepository().save(relacaoBancaria);

		return map(relacaoBancaria);
	}


	/**
	 * Validações diversas antes de enviar a RB.
	 *
	 * @param relacaoBancaria   a relacao bancaria a ser validada.
	 * @param listaItensRelacao
	 */
	public void validarItemRelacaoSemDadosBancarios(RelacaoBancaria relacaoBancaria, List<ItemRelacao> listaItensRelacao) throws ValidationException {
		for (ItemRelacao itemRelacao : listaItensRelacao) {
			List<ItemRelacaoPagamento> listaItensRelacaoPagamento = itemRelacaoPagamentoRepository.findByItemRelacaoCodigo(itemRelacao.getCodigo());
//			for (ItemRelacaoPagamento irp : listaItensRelacaoPagamento) {
			for (Pagamento pagamento : itemRelacao.getPagamentos()) {
				if (Pagamento.TipoDocumentoPagamento.DEPOSITO.equals(pagamento.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.DDA.equals(pagamento.getTipoDocumentoPagamento()) ||
					Pagamento.TipoDocumentoPagamento.DOC.equals(pagamento.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.TED.equals(pagamento.getTipoDocumentoPagamento())) {
					if (pagamento.getDadosBancarios() == null) {
						throw new ValidationException("RelacaoBancariaServiceImpl", "O(s) pagamento(s) indicado(s) não possui(em) dados bancários para o credor/favorecido. Relação bancária não pode ser enviada para pagamento.");
					}
				}
			}
//			}
		}
	}


	public void validarRelacaoBancariaComInternetBanking(RelacaoBancaria relacaoBancaria, List<ItemRelacao> listaItensRelacao) throws ValidationException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			if (!(dateFormat.format(new Date()).equals(dateFormat.format(relacaoBancaria.getDataPagamento())))) {
				if (relacaoBancaria.getDataPagamento().before(new Date())) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "A data de pagamento para Internet banking não pode ser anterior a data de hoje.");
				}
			}

			boolean npOuNfNaoConsumo = false;
			boolean trTaOuNpConsumo = false;

			String TESOURARIA_IB_TED_HORARIO = "tesouraria.ib.ted.horario";
			String horaLimiteTed = parametroRepository.findByChave(TESOURARIA_IB_TED_HORARIO).getValor();

			for (ItemRelacao itemRelacao : listaItensRelacao) {
				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					if (Pagamento.TipoDocumentoPagamento.TED.equals(pagamento.getTipoDocumentoPagamento())) {
						// Mesmo dia.
						if ((dateFormat.format(new Date()).equals(dateFormat.format(relacaoBancaria.getDataPagamento())))) {


							if (horaLimiteTed == null || horaLimiteTed.isEmpty()) {
								throw new ValidationException("RelacaoBancariaServiceImpl", String.format("A propriedade \"%s\" não foi definida.", TESOURARIA_IB_TED_HORARIO));
							}

							dateFormat = new SimpleDateFormat("HH:mm");

							if (Integer.parseInt(dateFormat.format(new Date()).substring(0, 2)) > Integer.parseInt(horaLimiteTed.substring(0, 2))) {
								throw new ValidationException("RelacaoBancariaServiceImpl", String.format("O horário para cadastramento/envio de Internet banking no mesmo dia não pode ser posterior a %s.", horaLimiteTed));
							} else if (Integer.parseInt(dateFormat.format(new Date()).substring(0, 2)) == Integer.parseInt(horaLimiteTed.substring(0, 2))) {
								if (Integer.parseInt(dateFormat.format(new Date()).substring(3, 5)) > Integer.parseInt(horaLimiteTed.substring(3, 5))) {
									throw new ValidationException("RelacaoBancariaServiceImpl", String.format("O horário para cadastramento/envio de Internet banking no mesmo dia não pode ser posterior a %s.", horaLimiteTed));
								}
							}
						}
					}

					if (Pagamento.TipoDocumentoPagamento.DDA.equals(pagamento.getTipoDocumentoPagamento())
						|| Pagamento.TipoDocumentoPagamento.OUTROS.equals(pagamento.getTipoDocumentoPagamento())) {
						throw new ValidationException("RelacaoBancariaServiceImpl", String.format("Não é possível, para o Internet banking, realizar operações com o tipo de documento para pagamento \"%s\".", pagamento.getTipoDocumentoPagamento().getDescricao()));
					}
				}

				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					npOuNfNaoConsumo = validarNfOuNpNaoConsumo(pagamento);
					if (npOuNfNaoConsumo) {
						break;
					}
				}

				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					trTaOuNpConsumo = validarTrTaOuNpConsumo(pagamento);
					if (trTaOuNpConsumo) {
						break;
					}
				}

				if (npOuNfNaoConsumo && trTaOuNpConsumo) {
					throw new ValidationException("RelacaoBancariaServiceImpl", "Por restrições para o Internet banking, não é permitido incluir, na mesma RB, NF ou NP (não consumo) e também tributos ou NP (de consumo).");
				}

				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					if (Pagamento.TipoDocumentoPagamento.DEPOSITO.equals(pagamento.getTipoDocumentoPagamento())) {
						ContaCorrenteTO contaCorrente = contaCorrenteService.obterContaCorrenteApenasCodigoEBanco(relacaoBancaria.getContaCorrente().getCodigo());
						if (!pagamento.getDadosBancarios().getBanco().getCodigo().equals(contaCorrente.getBanco().getCodigo())) {
							throw new ValidationException("RelacaoBancariaServiceImpl", "Não é possível para o Internet banking, realizar transferência para banco diferente do banco da Relação Bancária. Neste caso, use DOC ou TED.");
						}
					}
				}
			}
		}
	}

	private boolean validarNfOuNpNaoConsumo(Pagamento pagamento) {
		if (pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_FISCAL) || pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO)) {
			if (pagamento instanceof ParcelaNota) {
				ParcelaNota parcelaNota = (ParcelaNota) pagamento;
				Nota nota = parcelaNota.getNota();
				if (nota.getCredor() instanceof PessoaJuridica) {
					return !((PessoaJuridica) nota.getCredor()).getFornecimentoConsumo();
				}
			}
		}
		return false;
	}

	private boolean validarTrTaOuNpConsumo(Pagamento pagamento) {
		if (pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO)) {
			if (pagamento instanceof ParcelaNota) {
				ParcelaNota parcelaNota = (ParcelaNota) pagamento;
				Nota nota = parcelaNota.getNota();
				if (nota.getCredor() instanceof PessoaJuridica) {
					return ((PessoaJuridica) nota.getCredor()).getFornecimentoConsumo();
				}
			}
		} else if (pagamento.getTipo().equals(Pagamento.TipoPagamento.TRIBUTO_ACUMULADO) || pagamento.getTipo().equals(Pagamento.TipoPagamento.TRIBUTO)) {
			return true;
		}
		return false;
	}

	public void validarLimiteTed(List<ItemRelacao> itensSelecionados) throws ValidationException {
		if (itensSelecionados != null && !itensSelecionados.isEmpty()) {
			BigDecimal valorTed = new BigDecimal(parametroRepository.findByChave("tesouraria.limite.inferior.ted").getValor());
			for (ItemRelacao item : itensSelecionados) {
				BigDecimal totalPgtosTED = this.getTotalPagamentosPorTipoDoc(Pagamento.TipoDocumentoPagamento.TED, item);
				if (totalPgtosTED != null) {

					List<ItemRelacaoPagamento> itemRelacaoPagamentos = obterListaItemRelacaoPagamento(item.getRelacaoBancaria().getCodigo());

					Pessoa credor = null;

					if (itemRelacaoPagamentos.isEmpty()) {
						credor = item.getCredor();
					} else {
						credor = itemRelacaoPagamentos.get(0).getPagamento().getCredor();
					}

					if (valorTed.doubleValue() > 0 && totalPgtosTED.doubleValue() < valorTed.doubleValue()) {
						String nome = credor.getNome();
						if (credor.getTipoPessoa().equals(Pessoa.TipoPessoa.PESSOA_JURIDICA)) {
							PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.findById(credor.getCodigo()).orElse(null);
							if (pessoaJuridica != null && pessoaJuridica.getCodigo() != null)
								nome = pessoaJuridica.getNomeFantasia();
						}
						StringBuilder mensagem = new StringBuilder();
						mensagem.append("A linha do credor/favorecido \"%s\" e \"TED\"");
						mensagem.append(" possui valor menor do que o mínimo permitido (%s)");
						throw new ValidationException("RelacaoBancariaServiceImpl", String.format(mensagem.toString(), nome, valorTed));
					}
				}

			}
		}
	}

	public void validarDadosBancariosSelecionadosNaoHomologados(RelacaoBancaria relacaoBancaria, List<ItemRelacao> listaItensRelacao) throws ValidationException {
		if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			for (ItemRelacao itemRelacao : listaItensRelacao) {
//				QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;
//				QRelacaoBancaria qRelacaoBancaria = QRelacaoBancaria.relacaoBancaria;

//				JPAQuery<ItemRelacao> itemRelacaoJPAQuery = new JPAQuery<ItemRelacao>(getEntityManager());

//				itemRelacao = itemRelacaoJPAQuery
//					.from(qItemRelacao)
//					.join(qItemRelacao.relacaoBancaria, qRelacaoBancaria)
//					.select(Projections.fields(ItemRelacao.class, qItemRelacao.codigo, Projections.fields(RelacaoBancaria.class, qRelacaoBancaria.codigo, qRelacaoBancaria.meioPagamento).as("relacaoBancaria")))
//					.where(new BooleanBuilder().and(qItemRelacao.codigo.eq(itemRelacao.getCodigo()))).fetchOne();

				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					if (pagamento.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.DEPOSITO)
						&& RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
						if (pagamento.getCredor() != null) {
							List<DadosBancarios> lista = dadosBancariosRepository.findByPessoaCodigo(pagamento.getCredor().getCodigo());
							if (lista != null && !lista.isEmpty()) {
								for (DadosBancarios dadosBancarios : lista) {
									if (dadosBancarios != null && dadosBancarios.getCodigo() != null) {
										if (dadosBancarios.getSelecao() != null && dadosBancarios.getSelecao() && !dadosBancarios.getHomologado()) {
											throw new ValidationException("RelacaoBancariaServiceImpl", "Existe(m) pagamento(s) para \"Depósito/Transf.\" com dados bancários selecionados não homologados (indicados em vermelho). O envio da relação bancária não será permitido.");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public BigDecimal getTotalPagamentosPorTipoDoc(Pagamento.TipoDocumentoPagamento tipoDoc, ItemRelacao itemRelacao) {
		BigDecimal totalPg = new BigDecimal(0);
		boolean existeTipoDoc = false;
		for (ItemRelacaoPagamento irp : obterListaItemRelacaoPagamento(itemRelacao.getRelacaoBancaria().getCodigo())) {
			if (irp.getPagamento().getTipoDocumentoPagamento() != null && irp.getPagamento().getTipoDocumentoPagamento().equals(tipoDoc)) {
				existeTipoDoc = true;
				totalPg = totalPg.add(pagamentoService.obterValorPagar(irp.getPagamento().getCodigo(), irp.getPagamento().getTipo().toString()));
			}
		}
		return existeTipoDoc ? totalPg : null;
	}

	public void validarNumerosBoletosPagamentoRelacaoBancaria(RelacaoBancaria relacaoBancaria, List<ItemRelacao> listaItensRelacao) throws Exception {
		boolean haPagamentosSemNumeroBoleto = false;
		if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			for (ItemRelacao itemRelacao : listaItensRelacao) {
				ContaCorrenteTO contaCorrente = contaCorrenteService.obterContaCorrenteApenasCodigoEBanco(relacaoBancaria.getContaCorrente().getCodigo());

				if (Pagamento.TipoDocumentoPagamento.DEPOSITO.equals(itemRelacao.getTipoDocumentoPagamento())) {
					if (itemRelacao.getBanco() == null) {
						throw new ValidationException("RelacaoBancariaServiceImpl", "Não é possível para o Internet banking, realizar transferência para banco (em branco) diferente do banco da Relação Bancária. Neste caso, use DOC ou TED.");
					} else if (!contaCorrente.getBanco().getNome().equals(itemRelacao.getBanco().getNome())) {
						if (!itemRelacao.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.TED) && !itemRelacao.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.DOC)) {
							throw new ValidationException("RelacaoBancariaServiceImpl", "Não é possível para o Internet banking, realizar transferência para banco diferente do banco da Relação Bancária. Neste caso, use DOC ou TED.");
						}
					}
				}

				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pagamento.getTipoDocumentoPagamento())) {
						try {
							boletoService.validarBoletoLinhaDigitavel(pagamento.getLinhaDigitavel());
							boletoService.validarBoletoCodigoBarras(pagamento.getCodigoBarras());
							continue;
						} catch (NullPointerException | IllegalArgumentException e) {
							haPagamentosSemNumeroBoleto = true;
						}
						itemRelacao.setPagamentoSemNumeroBoleto(true);
					}
				}
			}
		}

		if (haPagamentosSemNumeroBoleto) {
			String MENSAGEM_VALIDACAO_BOLETO_PAGAMENTOS = "É necessário informar o número ou a linha digitável do(s) boleto(s) indicado(s) em vermelho. O envio da relação bancária não será permitido.";
			throw new RelacaoBancariaValidateException(MENSAGEM_VALIDACAO_BOLETO_PAGAMENTOS);
		}
	}


	public void validarLinhaDigitavelPagamentosRelacaoBancaria(RelacaoBancaria relacaoBancaria, List<ItemRelacao> listaItensRelacao, String acao) throws Exception {
		boolean haPagamentosLinhaDigitavelInvalidaBoleto = false;
		boolean haPagamentosLinhaDigitavelInvalidaGuia = false;
		boolean haPagamentosLinhaDigitavelInvalidaConcessionaria = false;

		if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			for (ItemRelacao itemRelacao : listaItensRelacao) {
				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					if (verificarLinhaDigitavelBoleto(pagamento)) {
						haPagamentosLinhaDigitavelInvalidaBoleto = true;
						itemRelacao.setPagamentoSemNumeroBoleto(true);
					}

					if (("enviar").equalsIgnoreCase(acao)) {
						if (verificarLinhaDigitavelGuia(converterListaPagamentoParaPagamentoTO(itemRelacao.getPagamentos()))) {
							haPagamentosLinhaDigitavelInvalidaGuia = true;
						}

						if (verificarLinhaDigitavelConcessionaria(pagamento)) {
							haPagamentosLinhaDigitavelInvalidaConcessionaria = true;
						}
					}
				}
			}
		}

		if (haPagamentosLinhaDigitavelInvalidaBoleto) {
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_INVALIDA);
		} else if (haPagamentosLinhaDigitavelInvalidaGuia) {
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_INVALIDA_ISS_GUIA);
		} else if (haPagamentosLinhaDigitavelInvalidaConcessionaria) {
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_INVALIDA_CONCESSIONARIA);
		}
	}

	public boolean verificarLinhaDigitavelBoleto(Pagamento pagamento) {
		boolean pagamentoInvalido = false;

		if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pagamento.getTipoDocumentoPagamento())) {
			try {
				boletoService.validarBoletoLinhaDigitavel(pagamento.getLinhaDigitavel());
			} catch (IllegalArgumentException e) {
				pagamentoInvalido = true;
			} catch (Exception e) {
				pagamentoInvalido = true;
			}
		}

		return pagamentoInvalido;
	}

	public boolean verificarLinhaDigitavelBoleto(ItemRelacao itemRelacao) throws RelacaoBancariaValidateException {
		boolean linhaInvalida = false;
		boolean valorInvalido = false;
		boolean vencimentoInvalido = false;

		itemRelacao.setPagamentoSemNumeroBoleto(false);
		itemRelacao.setVencimentoPosterior(false);
		itemRelacao.setErroValorLinhaDigitavel(false);

		Pagamento pagamento = itemRelacao.getListaPagamentos().get(0).getPagamento();

		if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pagamento.getTipoDocumentoPagamento())) {
			try {//Verificar linha digitável/codigo de barras é valida
				BoletoUtil.checkFormatoLinhaDigitavelNumerica(pagamento.getLinhaDigitavel());
				BoletoUtil.checkFormatoCodigoDeBarras(pagamento.getCodigoBarras());
				BoletoUtil.validarLinhaDigitavelNumerica(pagamento.getLinhaDigitavel());
			} catch (NullPointerException | CodigoDeBarrasException | LinhaDigitavelValidateException | LinhaDigitavelException | IllegalArgumentException e) {
				linhaInvalida = true;
			}

			if (!linhaInvalida && this.vencimentoPosteriorCodigoBarrasLinhaDigitavelRB(itemRelacao)) {
				vencimentoInvalido = true;
			}

			if (!linhaInvalida && !vencimentoInvalido) {
				String linhaDigitavel = pagamento.getLinhaDigitavel().replace(".", "");
				String valor = linhaDigitavel.substring(37, 47);
				valor = BoletoUtil.formatValorTitulo(valor);
				BigDecimal valorPgto = new BigDecimal(valor);
				BigDecimal totalPgto = BigDecimal.ZERO;


				for (Pagamento pgto : obterPagamentos(itemRelacao.getCodigo())) {
					if (pgto.getValorPagar() != null && BigDecimal.ZERO.compareTo(pgto.getValorPagar()) < 0) {
						totalPgto = totalPgto.add(pgto.getValorPagar());
					}
				}

				if (totalPgto.compareTo(valorPgto) != 0) {
					if (valorPgto.compareTo(BigDecimal.ZERO) != 0) {
						valorInvalido = true;
					}
				}
			}


		}

		if (linhaInvalida) {
			itemRelacao.setPagamentoSemNumeroBoleto(true);
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_INVALIDA);
		} else if (valorInvalido) {
			itemRelacao.setErroValorLinhaDigitavel(true);
			String MSG_LINHA_DIGITAVEL_VALOR_INVALIDO = "O valor final do(s) boleto(s) indicado(s) em %s deve ser igual ao valor a pagar. %s";
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_VALOR_INVALIDO);
		} else if (vencimentoInvalido) {
			itemRelacao.setVencimentoPosterior(true);
			String MSG_LINHA_DIGITAVEL_DATA_INVALIDA = "O vencimento do(s) boleto(s) indicado(s) em %s é anterior à data de pagamento da relação bancária. %s";
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_DATA_INVALIDA);
		}

		return true;
	}

	public boolean vencimentoPosteriorCodigoBarrasLinhaDigitavelRB(ItemRelacao itemrelacao) {
		boolean vencimentoPosterior = false;

		for (Pagamento pagamento : itemrelacao.getPagamentos()) {
			if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pagamento.getTipoDocumentoPagamento())) {
				if ((pagamento.getLinhaDigitavel() != null && !pagamento.getLinhaDigitavel().isEmpty()) || (pagamento.getCodigoBarras() != null && !pagamento.getCodigoBarras().isEmpty())) {
					try {
						Date dataVencimentoBoleto = obterDataVencimentoLinhaDigitavelOrCodigoBarras(pagamento.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS);
						if (dataVencimentoBoleto == null)
							dataVencimentoBoleto = obterDataVencimentoLinhaDigitavelOrCodigoBarras(pagamento.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL);

						if (dataVencimentoBoleto != null) {
							// veirificar se a data de vencimento prevista é um feriado, ou fim de semana
							boolean definiuData = false;
							Calendar calDataVencimento = Calendar.getInstance();
							calDataVencimento.setTime(dataVencimentoBoleto);

							while (!definiuData) {
								boolean isFeriado = verificarFeriado(calDataVencimento);
								if (calDataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calDataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
									calDataVencimento.add(Calendar.DAY_OF_MONTH, 1);
								} else if (isFeriado) {
									calDataVencimento.add(Calendar.DAY_OF_MONTH, 1);
								} else {
									definiuData = true;
								}
							}

							dataVencimentoBoleto = calDataVencimento.getTime();

							if (dataVencimentoBoleto.before(itemrelacao.getRelacaoBancaria().getDataPagamento())) {
								itemrelacao.setVencimentoPosterior(true);
								vencimentoPosterior = true;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return vencimentoPosterior;
	}

	public boolean verificarFeriado(Calendar feriado) {
		int dia = feriado.get(Calendar.DAY_OF_MONTH);
		int mes = feriado.get(Calendar.MONTH) + 1;
		int ano = feriado.get(Calendar.YEAR);

		List<Feriado> listaFeriado = feriadoRepository.findFeriadosByData(dia, mes, ano);

		if (listaFeriado == null || listaFeriado.isEmpty())
			return false;
		else
			return true;
	}

	public boolean verificarLinhaDigitavelGuia(List<PagamentoTO> pagamentos) {
		boolean pagamentoInvalido = false;
		if (Pagamento.TipoDocumentoPagamento.GUIA.equals(pagamentos.get(0).getTipoDocumentoPagamento())) {
			if (Pagamento.TipoPagamento.TRIBUTO.equals(pagamentos.get(0).getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pagamentos.get(0).getTipo())) {
				if ("ISS".equals(obterNomeTributo(pagamentos))) {
					if (!StringUtils.isNotBlank(pagamentos.get(0).getLinhaDigitavel()) || !StringUtils.isNotBlank(pagamentos.get(0).getLinhaDigitavel())) {
						pagamentoInvalido = true;
					}
				}
			}
		}

		return pagamentoInvalido;
	}

	public String obterNomeTributo(ItemRelacao itemRelacao) {
		if (!itemRelacao.getPagamentos().isEmpty()) {
			if (itemRelacao.getPagamentos().get(0).getTipo() == Pagamento.TipoPagamento.TRIBUTO)
				return itemRelacao.getPagamentos().get(0).getNotaTributoAliquota().getAliquota().getCategoriaTributo().getTributo().getNome();
			else if (itemRelacao.getPagamentos().get(0).getTipo() == Pagamento.TipoPagamento.TRIBUTO_ACUMULADO)
				return itemRelacao.getPagamentos().get(0).getListaPagamento().get(0).getNotaTributoAliquota().getAliquota().getCategoriaTributo().getTributo().getNome();
		}
		return null;
	}

	public boolean verificarLinhaDigitavelGuia(ItemRelacao itemRelacao) throws RelacaoBancariaValidateException {
		boolean pagamentoInvalido = false;
		Pagamento pagamento = itemRelacao.getListaPagamentos().get(0).getPagamento();

		if (Pagamento.TipoDocumentoPagamento.GUIA.equals(pagamento.getTipoDocumentoPagamento())) {
			if (Pagamento.TipoPagamento.TRIBUTO.equals(pagamento.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pagamento.getTipo())) {
				if ("ISS".equals(obterNomeTributo(itemRelacao))) {
					if (!StringUtils.isNotBlank(pagamento.getLinhaDigitavel()) || !StringUtils.isNotBlank(pagamento.getLinhaDigitavel())) {
						pagamentoInvalido = true;
					}
				}
			}
		}

		if (pagamentoInvalido) {
			itemRelacao.setPagamentoSemNumeroBoleto(true);
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_INVALIDA_ISS_GUIA);
		}

		return true;
	}

	public boolean verificarLinhaDigitavelConcessionaria(Pagamento pagamento) {
		boolean pagamentoInvalido = false;

		if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.equals(pagamento.getTipoDocumentoPagamento())) {
			if (!StringUtils.isNotBlank(pagamento.getLinhaDigitavel()) || !StringUtils.isNotBlank(pagamento.getLinhaDigitavel())) {
				pagamentoInvalido = true;
			}
		}

		return pagamentoInvalido;
	}

	public boolean verificarLinhaDigitavelConcessionaria(ItemRelacao itemRelacao) throws RelacaoBancariaValidateException {
		boolean pagamentoInvalido = false;
		Pagamento pagamento = itemRelacao.getListaPagamentos().get(0).getPagamento();

		if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.equals(pagamento.getTipoDocumentoPagamento())) {
			if (!StringUtils.isNotBlank(pagamento.getLinhaDigitavel()) || !StringUtils.isNotBlank(pagamento.getLinhaDigitavel())) {
				pagamentoInvalido = true;
			}
		}

		if (pagamentoInvalido) {
			itemRelacao.setPagamentoSemNumeroBoleto(true);
			throw new RelacaoBancariaValidateException(MSG_LINHA_DIGITAVEL_INVALIDA_CONCESSIONARIA);
		}

		return true;
	}

	public Date obterDataVencimentoLinhaDigitavelOrCodigoBarras(String digitos, TipoInformacaoNumeroBoleto tipo) throws ParseException, NullPointerException, IllegalArgumentException, CodigoDeBarrasException {
		if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.equals(tipo)) {
			return BoletoUtil.getDataVencimentoCodigoBarras(digitos);
		} else {
			return BoletoUtil.getDataVencimentoLinhaDigitavel(digitos.replace(".", ""));
		}
	}

	public boolean vencimentoPosteriorCodigoBarrasLinhaDigitavelRB(RelacaoBancaria relacaoBancaria, List<ItemRelacao> listaItensRelacao) {
		boolean vencimentoPosterior = false;
		if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())) {
			for (ItemRelacao itemRelacao : listaItensRelacao) {
				for (Pagamento pagamento : itemRelacao.getPagamentos()) {
					if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pagamento.getTipoDocumentoPagamento())) {
						if ((pagamento.getLinhaDigitavel() != null && !pagamento.getLinhaDigitavel().isEmpty()) || (pagamento.getCodigoBarras() != null && !pagamento.getCodigoBarras().isEmpty())) {
							try {
								Date dataVencimentoBoleto = this.obterDataVencimentoLinhaDigitavelOrCodigoBarras(pagamento.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS);
								if (dataVencimentoBoleto == null)
									dataVencimentoBoleto = this.obterDataVencimentoLinhaDigitavelOrCodigoBarras(pagamento.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL);

								if (dataVencimentoBoleto != null) {
									// veirificar se a data de vencimento prevista é um feriado, ou fim de semana
									boolean definiuData = false;
									Calendar calDataVencimento = Calendar.getInstance();
									calDataVencimento.setTime(dataVencimentoBoleto);

									while (!definiuData) {
										boolean isFeriado = feriadoService.verificarFeriado(calDataVencimento);
										if (isFeriado) {
											calDataVencimento.add(Calendar.DAY_OF_MONTH, 1);
										} else if (calDataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calDataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
											calDataVencimento.add(Calendar.DAY_OF_MONTH, 1);
										} else {
											definiuData = true;
										}
									}

									dataVencimentoBoleto = calDataVencimento.getTime();

									if (dataVencimentoBoleto.before(relacaoBancaria.getDataPagamento())) {
										itemRelacao.setVencimentoPosterior(true);
										vencimentoPosterior = true;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return vencimentoPosterior;
	}

	private PagamentoContaContabil.Tipo getTipoPagamentoContaContabil(String tipo) {

		switch (tipo) {
			case "Multas":
				return PagamentoContaContabil.Tipo.MULTAS;
			case "Juros":
				return PagamentoContaContabil.Tipo.JUROS;
			case "Descontos concedidos":
				return PagamentoContaContabil.Tipo.DESCONTOS;
			default:
				return null;
		}
	}


	public BigDecimal getValorJurosMulta(Pagamento pagamento) {
		BigDecimal valor = BigDecimal.ZERO;

		if (pagamento.getValorJuros() != null) {
			valor = valor.add(pagamento.getValorJuros());
		}

		if (pagamento.getValorJurosMulta() != null) {
			valor = valor.add(pagamento.getValorJurosMulta());
		}

		return valor;
	}

	public BigDecimal getValorTributo(Pagamento pagamento) {
		return pagamentoService.calcularTotalAcumulado(pagamento.getCodigo(), pagamento.getTipo().toString());
	}


	public BigDecimal getTotalOutrosDescontos(Pagamento pagamento) {
		return pagamentoService.getTotalOutrosDescontos(pagamento.getCodigo());
	}

	@Override
	public boolean dadosBancariosHomologados(ItemRelacaoTO itemRelacao, List<PagamentoTO> pagamentos, RelacaoBancariaTO relacaoBancariaTO, BancoTO bancoTO) {
		String tipo = "";

		if (pagamentos != null && !pagamentos.isEmpty()) {
			tipo = pagamentos.get(0).getTipoDocumentoPagamento();
		} else {
			return Boolean.FALSE;
		}

		if (pagamentos.get(0).getCredor() == null) {
			return Boolean.FALSE;
		}

		Pessoa credor = getMapper().map(pagamentos.get(0).getCredor(), Pessoa.class);

		if (tipo != null && credor != null && relacaoBancariaTO != null && relacaoBancariaTO.getMeioPagamento() != null) {
			if (RelacaoBancaria.MeioPagamento.INTERNET_BANKING.getDescricao().equals(relacaoBancariaTO.getMeioPagamento())) {
				switch (tipo) {
					case "Depósito em conta/Transferência":
					case "DEPOSITO":
					case "DOC":
					case "TED":
					case "DDA":
						List<DadosBancarios> listaDadosbancarios = obterDadosbancariosDeUmCredor(credor.getCodigo());
						if (!listaDadosbancarios.isEmpty()) {
							for (DadosBancarios dadosBancarios : listaDadosbancarios) {
								if (dadosBancarios != null && dadosBancarios.getCodigo() != null) {
									if (dadosBancarios.getHomologado() && dadosBancarios.getBanco().getNome().equals(bancoTO.getNome())) {
										return Boolean.TRUE;
									}
								}
							}
						}
					default:
						break;
				}
			}
		}
		return Boolean.FALSE;
	}

	private ItemRelacao createItemRelacaoDadosBancariosEnvio(Pagamento pagamento, RelacaoBancaria.MeioPagamento meioPagamento) {
		RelacaoBancaria relacaoBancaria = new RelacaoBancaria();
		relacaoBancaria.setMeioPagamento(meioPagamento);
		ItemRelacao itemRelacao = new ItemRelacao();
		itemRelacao.setRelacaoBancaria(relacaoBancaria);
		itemRelacao.setTipoDocumentoPagamento(pagamento.getTipoDocumentoPagamento());
		itemRelacao.setListaPagamentos(new ArrayList<>());
		ItemRelacaoPagamento itemRelacaoPagamento = new ItemRelacaoPagamento();
		itemRelacaoPagamento.setItemRelacao(itemRelacao);
		itemRelacaoPagamento.setPagamento(pagamento);
		itemRelacao.getListaPagamentos().add(itemRelacaoPagamento);
		return itemRelacao;
	}

	private Boolean isFaltandoDadosBancariosEnvio(Pagamento pagamento, RelacaoBancaria.MeioPagamento meioPagamento) {
		return isFaltandoDadosBancariosEnvio(createItemRelacaoDadosBancariosEnvio(pagamento, meioPagamento));
	}

	private Boolean isFaltandoDadosBancariosEnvio(ItemRelacao itemRelacao) {
		Boolean faltandoDadosEnvio = null;
		RelacaoBancaria relacaoBancaria = itemRelacao.getRelacaoBancaria();
		boolean necessitaHomologacao =
			RelacaoBancaria.MeioPagamento.INTERNET_BANKING.equals(relacaoBancaria.getMeioPagamento())
				&& (Pagamento.TipoDocumentoPagamento.DEPOSITO.equals(itemRelacao.getTipoDocumentoPagamento())
				|| Pagamento.TipoDocumentoPagamento.DDA.equals(itemRelacao.getTipoDocumentoPagamento())
				|| Pagamento.TipoDocumentoPagamento.DOC.equals(itemRelacao.getTipoDocumentoPagamento())
				|| Pagamento.TipoDocumentoPagamento.TED.equals(itemRelacao.getTipoDocumentoPagamento())
			);

		if (necessitaHomologacao) {
			DadosBancarios dadosBancarios = getDadosBancarios(itemRelacao);
			faltandoDadosEnvio = !isDadosBancariosInformados(dadosBancarios);
		}

		return faltandoDadosEnvio;
	}

	private DadosBancarios getDadosBancarios(ItemRelacao itemRelacao) {
		return getDadosBancarios(getPrimeiroPagamento(itemRelacao));
	}

	private DadosBancarios getDadosBancarios(Pagamento primeiroPagamento) {
		DadosBancarios dadosBancarios = null;
		Pessoa credor = primeiroPagamento != null && primeiroPagamento.getCredor() != null ? primeiroPagamento.getCredor() : null;

		if (primeiroPagamento != null && primeiroPagamento.getDadosBancarios() != null) {
			dadosBancarios = primeiroPagamento.getDadosBancarios();
		}

		if (dadosBancarios == null && credor != null) {
			// Encontrando dados bancarios marcados como principal
			dadosBancarios = credor.getDadosBancarios();
		}

		return dadosBancarios;
	}

	private Pagamento getPrimeiroPagamento(ItemRelacao itemRelacao) {
		Pagamento pagamento = null;
		if (itemRelacao.getPagamentos() != null && !itemRelacao.getPagamentos().isEmpty()) {
			pagamento = itemRelacao.getPagamentos().get(0);
		}
		if (pagamento == null && itemRelacao.getListaPagamentos() != null && !itemRelacao.getListaPagamentos().isEmpty()) {
			ItemRelacaoPagamento item = itemRelacao.getListaPagamentos().get(0);
			pagamento = item.getPagamento();
		}
		return pagamento;
	}

	private boolean isDadosBancariosInformados(DadosBancarios dadosBancarios) {
		return dadosBancarios != null && dadosBancarios.getCodigo() != null
			&& dadosBancarios.getBanco() != null && dadosBancarios.getBanco().getCodigo() != null
			&& dadosBancarios.getAgencia() != null && !dadosBancarios.getAgencia().isEmpty()
			&& dadosBancarios.getConta() != null && !dadosBancarios.getConta().isEmpty()
			&& dadosBancarios.getDigito() != null && !dadosBancarios.getDigito().isEmpty();
	}

	@Override
	public RelacaoBancariaTO adicionarPagamentos(RelacaoBancariaTO rbTO) {
		if (rbTO.getRelacaoBancariaPagamentosVO() == null) {
			rbTO.setRelacaoBancariaPagamentosVO(new ArrayList<>());
		}

		for (RelacaoBancariaPagamentosVO vo : rbTO.getPesquisaPagamentosVO()) {
			if (vo.getSelecionado()) {
				RelacaoBancaria.MeioPagamento meioPagamento = RelacaoBancaria.MeioPagamento.findByDescricao(rbTO.getMeioPagamento());

				vo.setStatusPagamento(ItemRelacao.StatusItemRelacao.ABERTO.toString());
				if (vo.getQtdadePagamentos() == 1) {
					boolean dadosBancariosEnvioVerificados = false;

					Pagamento pagamento = getMapper().map(pagamentoService.obterPagamentoSimples(vo.getCodigoPagamento()), Pagamento.class);

					vo.setValorMulta(pagamento.getValorMulta());
					vo.setValorJuros(pagamento.getValorJuros());
					vo.setValorDesconto(pagamento.getValorDesconto());
					vo.setValorAdiantamento(pagamento.getValorAdiantamento());
					vo.setValorDevolucao(pagamento.getValorDevolucao());
					vo.setValorTributos(pagamentoService.calcularTotalAcumulado(pagamento.getCodigo(), pagamento.getTipo().toString()));
					vo.setValorOutrosDescontos(pagamentoService.getTotalOutrosDescontos(pagamento.getCodigo()));
					vo.setValorLiquido(pagamento.getValorLiquido().subtract(vo.getValorTributos()));

					if (pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO)) {
						vo.setValorTributos(notaPagamentoService.obterValorTibutoDeNotaPagamentoPeloNumero(pagamento.getNumero()));
					}

					vo.setValorAPagar(pagamentoService.obterValorPagar(pagamento.getCodigo(), pagamento.getTipo().toString()));

					vo.setCredor(getMapper().map(pagamento.getCredor(), PessoaJuridicaTO.class));
					vo.setCentroFinanceiro(getMapper().map(pagamento.getCentroFinanceiro(), CentroFinanceiroTO.class));

					if (Pagamento.TipoPagamento.NOTA_PAGAMENTO.equals(pagamento.getTipo()) || Pagamento.TipoPagamento.NOTA_FISCAL.equals(pagamento.getTipo())) {
						ParcelaNotaTO parcelaNota = parcelaNotaService.obterParcelaCodigoEHomologador(pagamento.getCodigo());

						vo.setValorNota(parcelaNota.getNota().getValor());

						List<DadosBancariosTO> dadosBancariosListaTO = dadosBancariosService.obterListaDadosBancarioSimplesPeloCodigoCredor(pagamento.getCredor().getCodigo());

						if (dadosBancariosListaTO != null && !dadosBancariosListaTO.isEmpty()) {
							vo.setListDadosBancarios(new ArrayList<>());
							for (DadosBancariosTO dados : dadosBancariosListaTO)
								vo.getListDadosBancarios().add(dados);
						} else {
							vo.setListDadosBancarios(new ArrayList<>());
							vo.getListDadosBancarios().add(getMapper().map(pagamento.getDadosBancarios(), DadosBancariosTO.class));
						}

						vo.setFaltandoDadosBancariosEnvio(vo.getListDadosBancarios().isEmpty());

					}
				}
				rbTO.getRelacaoBancariaPagamentosVO().add(vo);
			}
		}

		if (rbTO.getPagamentoIndividual() != null) {
//            regerarSequencia();
		}

		BigDecimal total = BigDecimal.ZERO;
		for (RelacaoBancariaPagamentosVO vo : rbTO.getRelacaoBancariaPagamentosVO()) {
			total = total.add(vo.getValorAPagar());
		}

		rbTO.setTotal(total);
		rbTO.setPesquisaPagamentosVO(new ArrayList<>());
		rbTO.setPesquisaPagamentoRBTO(null);

		return rbTO;
	}

	@Override
	public RelacaoBancariaTO pesquisarPagamentos(RelacaoBancariaTO rbTO) {
		PesquisaPagamentoRBTO pagamentoRBTO = rbTO.getPesquisaPagamentoRBTO();
		List<Long> codigosPagamentos = new ArrayList<>(); //utilizado para remover da pesquisa os pagamentos já associados a RB

		if (rbTO.getRelacaoBancariaPagamentosVO() != null) {
			for (RelacaoBancariaPagamentosVO vo : rbTO.getRelacaoBancariaPagamentosVO()) {
				if (vo.getItemRelacao() != null) {
					for (ItemRelacaoPagamentoTO itemRelacaoPagamentoTO : vo.getItemRelacao().getListaPagamentos())
						codigosPagamentos.add(itemRelacaoPagamentoTO.getPagamento().getCodigo());
				}
			}
		}


		QPagamento qPagamento = QPagamento.pagamento;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;

		QPagamento qPagamentoTA = new QPagamento("pagamentoTA");
		QNotaTributoAliquota qNotaTributoAliquotaTR = new QNotaTributoAliquota("listaNotaTributoAliquotaTR");
		QNotaTributoAliquota qNotaTributoAliquotaTA = new QNotaTributoAliquota("listaNotaTributoAliquotaTA");
		QNota qNota = new QNota("nota");
		QNota qNotaTR = new QNota("notaTR");
		QNota qNotaTA = new QNota("notaTA");

		JPAQuery<Pagamento> jpaQuery = pagamentoService.jpaQueryPagamento();
		jpaQuery.leftJoin(qPagamento.centroFinanceiro, qCentroFinanceiro);

		jpaQuery.leftJoin(qPagamento.as(QParcelaNota.class).nota, qNota);
		jpaQuery.leftJoin(qPagamento.listaNotaTributoAliquota, qNotaTributoAliquotaTR);
		jpaQuery.leftJoin(qNotaTributoAliquotaTR.nota, qNotaTR);

		jpaQuery.leftJoin(qPagamento.listaPagamento, qPagamentoTA);
		jpaQuery.leftJoin(qPagamentoTA.listaNotaTributoAliquota, qNotaTributoAliquotaTA);
		jpaQuery.leftJoin(qNotaTributoAliquotaTA.nota, qNotaTA);

		BooleanBuilder condicoes = new BooleanBuilder();

		condicoes.and(qPagamento.status.eq(Pagamento.Status.PAGAMENTO));

		condicoes.andAnyOf(
			qNota.empresa.codigo.eq(rbTO.getUnidade().getCodigo()),
			qNotaTR.empresa.codigo.eq(rbTO.getUnidade().getCodigo()),
			qNotaTA.empresa.codigo.eq(rbTO.getUnidade().getCodigo())
		);

		//Número
		if (Check.isNotEmpty(pagamentoRBTO.getNumero())) {
			condicoes.and(qPagamento.numero.toUpperCase().contains(pagamentoRBTO.getNumero().toUpperCase()));
		}

		//Credor
		QPessoa qPessoa = QPessoa.pessoa;
		jpaQuery.leftJoin(qPagamento.credor, qPessoa);

		//Banco - DadosBancarios e Banco serão sempre adicionados a qRelacaoBancaria, pois é necessário recuperar colunas destas tabelas para o resultado.
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;
		jpaQuery.leftJoin(qPagamento.dadosBancarios, qDadosBancarios);
		jpaQuery.leftJoin(qDadosBancarios.banco, qBanco);

		if (Check.isNotEmpty(pagamentoRBTO.getCodigoBanco())) {
			condicoes.and(qBanco.codigo.eq(pagamentoRBTO.getCodigoBanco()));
		}

		if (Check.isNotEmpty(pagamentoRBTO.getNomeCredor())) {
			QPessoaJuridica qPessoaJuridica = qPagamento.credor.as(QPessoaJuridica.class);
			condicoes.andAnyOf(
				qPessoaJuridica.nome.containsIgnoreCase(pagamentoRBTO.getNomeCredor()),
				qPessoaJuridica.documento.containsIgnoreCase(pagamentoRBTO.getNomeCredor()),
				qPessoaJuridica.nomeFantasia.containsIgnoreCase(pagamentoRBTO.getNomeCredor())
			);
		}

		if (Check.isNotEmpty(pagamentoRBTO.getTipoDocPagamento())) {
			for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : Pagamento.TipoDocumentoPagamento.values()) {
				if (tipoDocumentoPagamento.toString().equals(pagamentoRBTO.getTipoDocPagamento())) {
					condicoes.and(qPagamento.tipoDocumentoPagamento.eq(tipoDocumentoPagamento));
					break;
				}
			}
		}

		//Datas
		if (Check.isNotEmpty(pagamentoRBTO.getDataPagamentoDe())) {
			condicoes.and(qPagamento.dataPagamento.goe(pagamentoRBTO.getDataPagamentoDe()));
		}

		if (Check.isNotEmpty(pagamentoRBTO.getDataPagamentoAte())) {
			condicoes.and(qPagamento.dataPagamento.loe(pagamentoRBTO.getDataPagamentoAte()));
		}

		//Valores
		if (Check.isNotEmpty(pagamentoRBTO.getValorDe())) {
			condicoes.and(qPagamento.valor.goe(pagamentoRBTO.getValorDe()));
		}

		if (Check.isNotEmpty(pagamentoRBTO.getValorAte())) {
			condicoes.and(qPagamento.valor.loe(pagamentoRBTO.getValorAte()));
		}

		//Gerência
		if (Check.isNotEmpty(pagamentoRBTO.getCodigoGerencia())) {
			condicoes.and(qCentroFinanceiro.codigo.eq(pagamentoRBTO.getCodigoGerencia()));
		}

		if (codigosPagamentos != null && !codigosPagamentos.isEmpty()) {
			condicoes.and(qPagamento.codigo.notIn(codigosPagamentos));
		}

		QBean qBean = Projections.bean(
			ParcelaNota.class, qPagamento.codigo,
			qPagamento.numero,
			qPagamento.valor,
			qPagamento.dataPagamento,
			qPagamento.tipoDocumentoPagamento,
			qPagamento.tipo,
			qPagamento.codigoBarras,
			qPagamento.linhaDigitavel,
			Projections.bean(PessoaJuridica.class,
				qPessoa.codigo,
				qPessoa.tipoPessoa,
				qPessoa.nome,
				qPessoa.as(QPessoaJuridica.class).nomeFantasia
			).as("credor"),
			Projections.bean(DadosBancarios.class,
				qDadosBancarios.codigo,
				qDadosBancarios.digitoAgencia,
				qDadosBancarios.agencia,
				qDadosBancarios.conta,
				qDadosBancarios.digito,
				Projections.bean(Banco.class, qBanco.nome, qBanco.codigo).as("banco")
			).as("dadosBancarios")
		);


		int maxResultados = 300; //não há parâmetro cadastrado na tabela parâmetros.
		jpaQuery.select(qBean).where(condicoes).distinct()
			.orderBy(qPagamento.dataPagamento.asc(), qPessoa.nome.asc())
			.limit(maxResultados);

		List<Pagamento> pagamentos = jpaQuery.fetch();

		RelacaoBancaria.MeioPagamento meioPagamento = RelacaoBancaria.MeioPagamento.findByDescricao(rbTO.getMeioPagamento());

		for (Pagamento pagamento : pagamentos) {
			if (pagamento.getCredor() == null) {
				JPAQuery jpaQueryPagamento = pagamentoService.jpaQueryPagamento();
				jpaQueryPagamento
					.select(
						Projections.bean(
							Pessoa.class,
							qPagamento.credor.codigo,
							qPagamento.credor.nome,
							qPagamento.credor.tipoPessoa
						)
					)
					.join(qPagamento.credor)
					.where(qPagamento.codigo.eq(pagamento.getCodigo()));
				Pagamento pagamentoCredor = (Pagamento) jpaQueryPagamento.fetchFirst();
				if (pagamentoCredor != null) pagamento.setCredor(pagamentoCredor.getCredor());
			}
		}

		List<Pagamento> listaPagamento = new ArrayList<>();

		listaPagamento.addAll(pagamentos);

		List<ItemRelacaoTO> itens = this.filtrarPagamentoEmItensRelacoes(listaPagamento, meioPagamento);

		rbTO.setPesquisaPagamentosVO(montarRelacaoBancariaPagamentosVO(itens));

		rbTO.getPesquisaPagamentosVO().sort((o1, o2) -> (o1.getValorAPagar().compareTo(o2.getValorAPagar()) * -1));

		return rbTO;
	}

	private List<ItemRelacaoTO> filtrarPagamentoEmItensRelacoes(List<Pagamento> listaPagamentos, RelacaoBancaria.MeioPagamento meioPagamento) {
		List<ItemRelacaoTO> itensRelacao = new ArrayList<>();

		/*
		 * Utilizado para agrupar os pagamentos. Tributos, guias e concessionárias sempre serão pagos de forma individual.
		 * Os demais pagamentos são agrupados pelos seguintes critérios:
		 * - Boleto : agrupado pelo código de barras.
		 * - Fornecedores : data pagamento, tipo pagamento(NF ou NP), credor e dados bancários
		 */

		Map<String, List<PagamentoTO>> mapPagamentos = new HashMap<>();
		Map<String, Pagamento> primeirosPagamentos = new HashMap<>();

		RelacaoBancaria relacaoBancariaEnvio = new RelacaoBancaria();
		relacaoBancariaEnvio.setMeioPagamento(meioPagamento);

		for (Pagamento pagamento : listaPagamentos) {

			PagamentoTO pagamentoTO = new PagamentoTO();
			pagamentoTO = getMapper().map(pagamento, PagamentoTO.class);
			pagamentoTO.setValorPagar(pagamentoService.obterValorPagar(pagamento.getCodigo(), pagamento.getTipo().getDescricao()));
			if (pagamento.getCredor() instanceof PessoaJuridica) {
				PessoaJuridicaTO pessoaJuridica = getMapper().map(pagamento.getCredor(), PessoaJuridicaTO.class);
				pagamentoTO.setCredor(pessoaJuridica);
			}
			ItemRelacaoTO itemRelacaoTO = null;

			//Nos três primeiros ifs os pagamentos serão sempre individuais.
			if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.equals(pagamento.getTipoDocumentoPagamento())) {
				itemRelacaoTO = montarItemDeRelacao(pagamentoTO);
			} else if (Pagamento.TipoPagamento.TRIBUTO.equals(pagamento.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pagamento.getTipo())) {
				itemRelacaoTO = montarItemDeRelacao(pagamentoTO);
			} else if (Pagamento.TipoDocumentoPagamento.GUIA.equals(pagamento.getTipoDocumentoPagamento())) {
				itemRelacaoTO = montarItemDeRelacao(pagamentoTO);
			} else {
				String chave = "";

				if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(pagamento.getTipoDocumentoPagamento())) {
					String dadosBoleto = null;
					if (pagamento.getCodigoBarras() != null && !pagamento.getCodigoBarras().isEmpty()) {
						dadosBoleto = pagamento.getCodigoBarras();
					} else if (pagamento.getLinhaDigitavel() != null && !pagamento.getLinhaDigitavel().isEmpty()) {
						dadosBoleto = pagamento.getLinhaDigitavel();
					}
					if (dadosBoleto != null && !dadosBoleto.isEmpty()) {
						chave = dadosBoleto.trim() + "*";
					}
					if (chave.isEmpty()) {
						chave = System.currentTimeMillis() + "*";
					}
				}

				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
				chave = chave + sdf.format(pagamento.getDataPagamento()) + "*" + pagamento.getTipo().getDescricao() + "*" + pagamento.getCredor().getCodigo() + "*" + getDadosBancarios(pagamento).getCodigo();

				if (!mapPagamentos.containsKey(chave)) {
					List<PagamentoTO> pags = new ArrayList<>();
					pags.add(pagamentoTO);
					mapPagamentos.put(chave, pags);
					primeirosPagamentos.put(chave, pagamento);
				} else {
					mapPagamentos.get(chave).add(pagamentoTO);
				}
			}

			if (itemRelacaoTO != null) {
				itensRelacao.add(itemRelacaoTO);
				itemRelacaoTO.setFaltandoDadosBancariosEnvio(isFaltandoDadosBancariosEnvio(pagamento, meioPagamento));
			}
		}

		for (String chave : mapPagamentos.keySet()) {
			Pagamento primeiroPagamento = primeirosPagamentos.get(chave);
			ItemRelacaoTO itemRelacaoTO = montarItemDeRelacao(mapPagamentos.get(chave));
			itensRelacao.add(itemRelacaoTO);
			itemRelacaoTO.setFaltandoDadosBancariosEnvio(isFaltandoDadosBancariosEnvio(primeiroPagamento, meioPagamento));
		}

		return itensRelacao;
	}

	private ItemRelacaoTO montarItemDeRelacao(PagamentoTO pagamentoTO) {
		List<PagamentoTO> pagamentos = new ArrayList<>();
		pagamentos.add(pagamentoTO);
		return montarItemDeRelacao(pagamentos);
	}

	private ItemRelacaoTO montarItemDeRelacao(List<PagamentoTO> pagamentos) {
		ItemRelacaoTO itemRelacaoTO = new ItemRelacaoTO();
		itemRelacaoTO.setListaPgtos(pagamentos);
		itemRelacaoTO.setStatus(ItemRelacao.StatusItemRelacao.ABERTO.toString());
		itemRelacaoTO.setQtdPagamentos(pagamentos.size());

		List<ItemRelacaoPagamentoTO> listItemPag = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;

		for (PagamentoTO pag : pagamentos) {
			total = total.add(pag.getValorPagar());
			pag.setStatus(Pagamento.Status.RELACIONADO.toString());

			ParcelaNotaTO parcelaNotaTO = parcelaNotaService.obterParcelaCodigoEHomologador(pag.getCodigo());
			if ("NF".equals(pag.getTipo())) {
				NotaFiscalTO nf = notaFiscalService.buscarNotaFiscalById(parcelaNotaTO.getNota().getId());
				pag.setDataEmissao(nf.getDataEmissao());
			}
			else if ("NP".equals(pag.getTipo()))
				pag.setDataEmissao(parcelaNotaTO.getNota().getDataCriacao());

			listItemPag.add(new ItemRelacaoPagamentoTO(pag, null));
		}

		itemRelacaoTO.setTotal(total);
		itemRelacaoTO.setListaPagamentos(listItemPag);

		return itemRelacaoTO;
	}

	private List<RelacaoBancariaPagamentosVO> montarRelacaoBancariaPagamentosVO(List<ItemRelacaoTO> itensRelacao) {
		List<RelacaoBancariaPagamentosVO> list = new ArrayList<>();

		for (ItemRelacaoTO itemRelacao : itensRelacao) {
			boolean exibirDadosCongelados = false;
			RelacaoBancariaPagamentosVO vo = new RelacaoBancariaPagamentosVO();
			PagamentoTO primeiroPagamento = itemRelacao.getListaPagamentos().get(0).getPagamento();

			vo.setQtdadePagamentos(itemRelacao.getQtdPagamentos());
			if (itemRelacao.getQtdPagamentos() == 1)
				vo.setCodigoPagamento(primeiroPagamento.getCodigo());

			vo.setTipoDocumentoPagamento(primeiroPagamento.getTipoDocumentoPagamento());

			vo.setTotal(itemRelacao.getTotal());

			for (ItemRelacaoPagamentoTO itemRelacaoPagamentoTO : itemRelacao.getListaPagamentos()) {
				PagamentoTO pagamentoTO = itemRelacaoPagamentoTO.getPagamento();

				vo.setValorMulta(vo.getValorMulta() == null ? pagamentoTO.getValorMulta() : vo.getValorMulta().add(pagamentoTO.getValorMulta()));

				vo.setValorJuros(vo.getValorJuros() == null ? pagamentoTO.getValorJuros() : vo.getValorJuros().add(pagamentoTO.getValorJuros()));
				vo.setValor(vo.getValor() == null ? pagamentoTO.getValor() : vo.getValor().add(pagamentoTO.getValor()));
				vo.setValorDesconto(vo.getValorDesconto() == null ? pagamentoTO.getValorDesconto() : vo.getValorDesconto().add(pagamentoTO.getValorDesconto()));
				vo.setValorDevolucao(vo.getValorDevolucao() == null ? pagamentoTO.getValorDevolucao() : vo.getValorDevolucao().add(pagamentoTO.getValorDevolucao()));

				BigDecimal valorOutrosDescontos = pagamentoService.getTotalOutrosDescontos(pagamentoTO.getCodigo());
				vo.setValorOutrosDescontos(vo.getValorOutrosDescontos() == null ? valorOutrosDescontos : vo.getValorOutrosDescontos().add(valorOutrosDescontos));

				Pagamento pagamento = getMapper().map(pagamentoTO, Pagamento.class);
				BigDecimal valorTributo = getValorTributo(pagamento);

				if (pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO)) {
					valorTributo = notaPagamentoService.obterValorTibutoDeNotaPagamentoPeloNumero(pagamento.getNumero());
				}

				vo.setValorTributos(vo.getValorTributos() == null ? valorTributo : vo.getValorTributos().add(valorTributo));
				vo.setValorAdiantamento(vo.getValorAdiantamento() == null ? pagamento.getValorAdiantamento() : vo.getValorAdiantamento().add(pagamento.getValorAdiantamento()));
			}

			BigDecimal valor = vo.getValor() == null ? BigDecimal.ZERO : vo.getValor();
			BigDecimal valorTributos = vo.getValorTributos() == null ? BigDecimal.ZERO : vo.getValorTributos();
			vo.setValor(valor.subtract(valorTributos));

			vo.setValorLiquido(vo.getTotal().subtract(vo.getValorOutrosDescontos()).subtract(vo.getValorDevolucao()).subtract(vo.getValorAdiantamento()));

			if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.getDescricao().equals(itemRelacao.getListaPagamentos().get(0).getPagamento().getTipo())) {
				vo.setValorAPagar(vo.getTotal().add(vo.getValorJuros()).add(vo.getValorMulta()).subtract(vo.getValorDesconto()).subtract(vo.getValorOutrosDescontos()).subtract(vo.getValorDevolucao()).subtract(vo.getValorAdiantamento()));
			} else {
				vo.setValorAPagar(vo.getTotal().add(vo.getValorJuros()).add(vo.getValorMulta()).subtract(vo.getValorDesconto()).subtract(vo.getValorOutrosDescontos()).subtract(vo.getValorDevolucao()).subtract(vo.getValorAdiantamento()));
			}

			if (itemRelacao.getQtdPagamentos() > 1) {
				vo.setPgtoAgrupadoCodigoBarras(primeiroPagamento.getCodigoBarras() != null);
				vo.setNumero("—");
			} else {
				vo.setNumero(primeiroPagamento.getNumero());
			}

			if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equals(primeiroPagamento.getTipo()) || Pagamento.TipoPagamento.TRIBUTO.toString().equals(primeiroPagamento.getTipo())) {
				vo.settAorTR(true);
				vo.setTributo(pagamentoService.obterNomeTributo(primeiroPagamento.getCodigo(), primeiroPagamento.getTipo()));
				vo.setBancoSelecionado(new BancoTO());
			}

			if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.toString().equals(primeiroPagamento.getTipoDocumentoPagamento())
				|| Pagamento.TipoDocumentoPagamento.GUIA.toString().equals(primeiroPagamento.getTipoDocumentoPagamento()))
				vo.setBancoSelecionado(new BancoTO());

			if (!vo.istAorTR()) {
				vo.setCredor(primeiroPagamento.getCredor());
				if (exibirDadosCongelados) {
					vo.setListaDadosBancariosRB(itemRelacao);
				} else if (primeiroPagamento.getCredor() != null) {
					vo.setBancoSelecionado(primeiroPagamento.getDadosBancarios().getBanco());
				}
			}

			if (exibirDadosCongelados) {
				vo.setDataPagamento(itemRelacao.getDataPagamento());
			} else {
				vo.setDataPagamento(primeiroPagamento.getDataPagamento());
			}

			vo.setItemRelacao(itemRelacao);
			vo.setStatusPagamento(primeiroPagamento.getStatus());
			vo.setTipoDocumentoPagamento(primeiroPagamento.getTipoDocumentoPagamento());

			if (Pagamento.TipoDocumentoPagamento.BOLETO.toString().equals(vo.getTipoDocumentoPagamento())) {
				Pagamento pag = getMapper().map(primeiroPagamento, Pagamento.class);
				List<Pagamento> pags = new ArrayList<>();
				pags.add(pag);
				vo.setNomeBancoDoCodigoDeBarrasELinhaDigitavel(obterNomeBancoDoCodigoDeBarrasELinhaDigitavel(pags));
			}

			vo.setSelecionado(false);
			vo.setFaltandoDadosBancariosEnvio(itemRelacao.getFaltandoDadosBancariosEnvio());
			list.add(vo);
		}

		return list;
	}

	@Override
	public List<RetornoBancarioTO> obterRetornosBancarios() throws Exception {
		try {
			return obterRetornoBancarioPor(ItemRelacao.StatusItemRelacao.ESTORNADO);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public List<RetornoBancarioTO> obterRetornoBancarioPor(ItemRelacao.StatusItemRelacao statusItemRelacao) {
		List<RetornoBancario> retornoBancarioList = retornoBancarioRepository.findByStatusItemRelacaoAndUtilizado(statusItemRelacao, Boolean.TRUE);
		List<RetornoBancarioTO> list = new ArrayList<>();

		retornoBancarioList.sort((o1, o2) -> o1.getDescricao().compareTo(o2.getDescricao()));

		for (RetornoBancario retornoBancario : retornoBancarioList) {
			list.add(getMapper().map(retornoBancario, RetornoBancarioTO.class));
		}

		return list;
	}

	@Override
	public ContaCorrenteSimpleTO obterUltimoChequeRB(@PathVariable Long id) throws Exception {
		try {
			return obterUltimoCheque(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public ContaCorrenteSimpleTO obterUltimoCheque(Long contaCorrenteCod) {
		return getMapper().map(contaCorrenteRepository.findById(contaCorrenteCod).orElse(null), ContaCorrenteSimpleTO.class);
	}

	@Override
	public void imprimirCheques(RelacaoBancariaTO to, String tipo) throws Exception {
		try {
			List<ItemRelacao> listaItem = new ArrayList<>();
			for (RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO : to.getRelacaoBancariaPagamentosVO()) {
				if (relacaoBancariaPagamentosVO.getSelecionado() != null && relacaoBancariaPagamentosVO.getSelecionado()) {
					ItemRelacao itemRelacao = itemRelacaoRepository.findByCodigo(new Long(relacaoBancariaPagamentosVO.getCodigo()));
					itemRelacao.setCheque(Long.valueOf(relacaoBancariaPagamentosVO.getCheque()));
					listaItem.add(itemRelacao);
				} else if ("chequetodos".equalsIgnoreCase(tipo) || "chequetodoscopia".equalsIgnoreCase(tipo) || "chequetodosendosso".equalsIgnoreCase(tipo)) {
					listaItem.add(itemRelacaoRepository.findByCodigo(new Long(relacaoBancariaPagamentosVO.getCodigo())));
				}
			}

			if (listaItem.isEmpty()) {
				return;
			}

			List<ItemRelacaoPagamento> listItensPagamento = obterListaItemRelacaoPagamento(listaItem);

			RelacaoBancaria relacaoBancaria = select()
				.from(qRelacaoBancaria)
				.select(Projections.fields(RelacaoBancaria.class,
					qRelacaoBancaria.codigo,
					qRelacaoBancaria.dataPagamento,
					qRelacaoBancaria.pagamentoIndividual,
					qRelacaoBancaria.total,
					qRelacaoBancaria.status,
					Projections.bean(ContaCorrente.class, QContaCorrente.contaCorrente.codigo).as("contaCorrente"),
					Projections.bean(Unidade.class, QUnidade.unidade.codigo, QUnidade.unidade.nome, QUnidade.unidade.nomeFantasia).as("unidade")))
				.where(qRelacaoBancaria.codigo.eq(to.getCodigo()))
				.fetchOne();

			List<ItemRelacaoTO> listaItemTO = new ArrayList<>();

			Set<RelacaoBancariaPagamentosVO> listsVO = criarRelacaoBancariaPagamentosVO(relacaoBancaria, listaItem, listItensPagamento);

			for (ItemRelacao itemRelacao : listaItem) {
				itemRelacao.setRelacaoBancaria(relacaoBancaria);
				//FIXME: Feio, horrível, mas só funcionou assim rsrsrs
				for (RelacaoBancariaPagamentosVO vo : listsVO) {
					if (vo.getCodigo().equals(itemRelacao.getCodigo().toString())) {
						itemRelacao.setValorAPagar(vo.getValorAPagar());
					}
				}
				itemRelacaoRepository.updateCheque(itemRelacao.getCheque(), itemRelacao.getCodigo());
				listaItemTO.add(getMapper().map(itemRelacao, ItemRelacaoTO.class));
			}

			/*
			 * ATUALIZANDO O CHEQUE ANTERIOR DA CONTA CORRENTE.
			 */
			contaCorrenteRepository.updateChequeAnterior(to.getChequeAnterior(), relacaoBancaria.getContaCorrente().getCodigo());

			List<ChequeContaCorrenteItemRelacao> listaItensCheque = new ArrayList<>();

			// Guarda o código para associar o chequeContaCorrente com o DocumentPrintEntity.
			List<Long> chequeContaCorrenteCodigos = new ArrayList<>();

			/*
			 * ATUALIZANDO O CHEQUECONTACORRENTE ASSOCIADA AO ITEM PARA CANCELADO.
			 * NESTE MOMENTO SERÃO CRIADO NOVOS CHEQUECONTACORRENTE.
			 */
			for (ItemRelacao itemRelacao : listaItem) {
				List<ChequeContaCorrenteItemRelacao> list = chequeContaCorrenteItemRelacaoRepository.findByCodigoItemRelacao(itemRelacao.getCodigo());

				for (ChequeContaCorrenteItemRelacao chequeContaCorrenteItemRelacao : list) {
					if (!chequeContaCorrenteRepository.findById(chequeContaCorrenteItemRelacao.getCodigoChequeContaCorrente()).get().getCheque().equals(itemRelacao.getCheque())) {
						chequeContaCorrenteRepository.updateSituacao(ChequeContaCorrente.StatusChequeContaCorrente.CANCELADO_REIMPRESSAO, chequeContaCorrenteItemRelacao.getCodigoChequeContaCorrente());
						chequeContaCorrenteRepository.updateResponsavelCancelamento(securityService.getUser().getPessoaFisica(), chequeContaCorrenteItemRelacao.getCodigoChequeContaCorrente());
						chequeContaCorrenteRepository.updateDataCancelamento(new Date(), chequeContaCorrenteItemRelacao.getCodigoChequeContaCorrente());
					} else {
						chequeContaCorrenteCodigos.add(chequeContaCorrenteItemRelacao.getCodigoChequeContaCorrente());
					}
				}

				/*
				 * ATUALIZANDO O CHEQUE ANTERIOR O ITEM RELAÇÃO.
				 */
				itemRelacaoRepository.updateCheque(itemRelacao.getCheque(), itemRelacao.getCodigo());
				ChequeContaCorrenteItemRelacao chequeItemRelacao = new ChequeContaCorrenteItemRelacao();
				chequeItemRelacao.setCodigoItemRelacao(itemRelacao.getCodigo());
				listaItensCheque.add(chequeItemRelacao);
			}

			/*
			 * CRIANDO NOVOS CHEQUECANTACORRENTE E O CHEQUECONTACORRENTEITEMRELACAO DO ITEMRELACAO
			 */
			List<Long> chequeContaCorrenteOutrosCodigos = new ArrayList<>();
			if (!listaItensCheque.isEmpty()) {
				if (relacaoBancaria.getPagamentoIndividual()) {
					for (ItemRelacao itemRelacao : listaItem) {
						List<ChequeContaCorrente> listCheques = chequeContaCorrenteRepository.findByChequeAndContaCorrenteCodigo(itemRelacao.getCheque(), relacaoBancaria.getContaCorrente().getCodigo());
						if (listCheques.isEmpty()) {
							ChequeContaCorrente cheque = new ChequeContaCorrente();
							cheque.setContaCorrente(relacaoBancaria.getContaCorrente());
							cheque.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
							cheque.setValor(itemRelacao.getValorAPagar());
							cheque.setDataCompensacao(itemRelacao.getDataPagamento());
							cheque.setCheque(itemRelacao.getCheque());
							cheque.setCriador(securityService.getUser().getLogin());
							cheque.setDataCriacao(new Date());

							chequeContaCorrenteRepository.save(cheque);

							chequeContaCorrenteOutrosCodigos.add(cheque.getCodigo());

							chequeContaCorrenteItemRelacaoRepository.save(new ChequeContaCorrenteItemRelacao(cheque.getCodigo(), itemRelacao.getCodigo()));
						} else {
							chequeContaCorrenteOutrosCodigos.add(listCheques.get(0).getCodigo());
						}
					}
				} else {
					ChequeContaCorrente cheque = new ChequeContaCorrente();
					cheque.setContaCorrente(relacaoBancaria.getContaCorrente());
					cheque.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
					cheque.setValor(relacaoBancaria.getTotal());
					cheque.setDataCompensacao(relacaoBancaria.getDataPagamento());
					cheque.setCheque(to.getChequeAnterior());
					cheque.setCriador(securityService.getUser().getLogin());
					cheque.setDataCriacao(new Date());

					chequeContaCorrenteRepository.save(cheque);

					chequeContaCorrenteOutrosCodigos.add(cheque.getCodigo());

					for (ChequeContaCorrenteItemRelacao chequeItemRelacao : listaItensCheque) {
						chequeItemRelacao.setCodigoChequeContaCorrente(cheque.getCodigo());
						chequeContaCorrenteItemRelacaoRepository.save(chequeItemRelacao);
					}
				}
			}

			Long codigoDocumentoImpresso = null;

			if ("cheque".equalsIgnoreCase(tipo)) {
				codigoDocumentoImpresso = chequesService.imprimirChequesSelecionadosIndividual(listaItemTO, TiposImpressao.CHEQUE, securityService.getUser().getLogin(), getMapper().map(relacaoBancaria, RelacaoBancariaTO.class));
				updateImpressaoCheque(relacaoBancaria.getCodigo(), Boolean.TRUE);
			} else if ("endosso".equalsIgnoreCase(tipo)) {
				codigoDocumentoImpresso = chequesService.imprimirChequesEndossoSelecionados(listaItemTO, TiposImpressao.ENDOSSO, securityService.getUser().getLogin(), getMapper().map(relacaoBancaria, RelacaoBancariaTO.class));
				updateImpressaoEndosso(relacaoBancaria.getCodigo(), Boolean.TRUE);
			} else if ("copia".equalsIgnoreCase(tipo)) {
				codigoDocumentoImpresso = chequesService.imprimirChequesCopiaSelecionados(listaItemTO, TiposImpressao.COPIA, securityService.getUser().getLogin(), getMapper().map(relacaoBancaria, RelacaoBancariaTO.class));
				updateImpressao2via(relacaoBancaria.getCodigo(), Boolean.TRUE);
			} else if ("chequetodos".equalsIgnoreCase(tipo)) {
				codigoDocumentoImpresso = chequesService.imprimirTodosCheques(listaItemTO, getMapper().map(relacaoBancaria, RelacaoBancariaTO.class), securityService.getUser().getLogin());
				updateImpressaoCheque(relacaoBancaria.getCodigo(), Boolean.TRUE);
			} else if ("chequetodosendosso".equalsIgnoreCase(tipo)) {
				codigoDocumentoImpresso = chequesService.imprimirTodosChequesEndosso(listaItemTO, getMapper().map(relacaoBancaria, RelacaoBancariaTO.class), securityService.getUser().getLogin());
				updateImpressaoEndosso(relacaoBancaria.getCodigo(), Boolean.TRUE);
			} else if ("chequetodoscopia".equalsIgnoreCase(tipo)) {
				codigoDocumentoImpresso = chequesService.imprimirTodosChequesCopia(listaItemTO, getMapper().map(relacaoBancaria, RelacaoBancariaTO.class), securityService.getUser().getLogin());
				updateImpressao2via(relacaoBancaria.getCodigo(), Boolean.TRUE);
			}

			if (codigoDocumentoImpresso != null) {
				if (!chequeContaCorrenteOutrosCodigos.isEmpty()) {
					for (Long codigo : chequeContaCorrenteOutrosCodigos) {
						chequeContaCorrenteDocumentosImpressaoRepository.save(new ChequeContaCorrenteDocumentosImpressao(codigo, codigoDocumentoImpresso));
					}
				} else if (!chequeContaCorrenteCodigos.isEmpty()) {
					if (!chequeContaCorrenteCodigos.isEmpty()) {
						for (Long codigo : chequeContaCorrenteCodigos) {
							chequeContaCorrenteDocumentosImpressaoRepository.save(new ChequeContaCorrenteDocumentosImpressao(codigo, codigoDocumentoImpresso));
						}
					}
				}
			}

			/*
			 * ATUALIZAR CHEQUE ANTERIOR DA RELAÇÃO BANCÁRIA.
			 */
			updateAnterior(to.getCodigo(), to.getChequeAnterior());
			updateAtualizadorAndDataAtualizacao(relacaoBancaria.getCodigo());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public List<Object> obterItensRelacao(@PathVariable Long id) throws Exception {
		try {
			return itemRelacaoRepository.obterItensDaRelacaobancaria(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public List<PagamentoTO> converterListaPagamentoParaPagamentoTO(List<Pagamento> lista) {
		List<PagamentoTO> pagamentoTOS = new ArrayList<>();
		for (Pagamento pagamento : lista) {
			pagamentoTOS.add(getMapper().map(pagamento, PagamentoTO.class));
		}
		return pagamentoTOS;
	}

	@Override
	public List<RelacaoBancariaPagamentosVO> obterItensDaRelacaoBancaria(Long id) throws Exception {
		try {
			return preencherListaDeItens(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void baixarComprovante(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		File arquivo = verificarArquivoParaDownload(new Long(id));

		if (!arquivo.exists()) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Comprovante não encontrado");
		}

		try {
			Util.download(arquivo, "application/pdf", request, response);
		} catch (DownloadException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void baixarComprovanteExiste(String id) throws Exception {
		File arquivo = verificarArquivoParaDownload(new Long(id));

		if (!arquivo.exists()) {
			throw new ValidationException("RelacaoBancariaServiceImpl", "Comprovante não encontrado");
		}
	}

	private File verificarArquivoParaDownload(Long codigoItemRelacao) throws FileNotFoundException {
		ItemRelacao itemRelacao = itemRelacaoRepository.findByCodigo(codigoItemRelacao);
		if (itemRelacao == null) {
			throw new FileNotFoundException("Comprovante não encontrado");
		} else {
			String path = parametroRepository.findByChave("ib.recibo.output.filepath").getValor();

			return new File(path + itemRelacao.getCaminhoComprovante());
		}
	}

	// Paliativo...
	@SuppressWarnings("unchecked")
	private <T extends DomainObject> JPAQuery<T> cast(JPAQuery<?> query, Class<T> entityClass) {
		return (JPAQuery<T>) query;
	}

	/**
	 * Obter a lista de {@link ItemRelacao} de uma {@link RelacaoBancaria}.
	 *
	 * @param codigoRelacaobancaria O códgio da relação bancária.
	 * @return A lista de itens de uma relação bancária.
	 */
	private List<ItemRelacao> obterListaItemRelacao(Long codigoRelacaobancaria) {
		QRelacaoBancaria qRelacaoBancaria = QRelacaoBancaria.relacaoBancaria;
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;

		JPAQuery<ItemRelacao> jpaQuery = cast(itemRelacaoService.jpaQueryItemRelacao(), ItemRelacao.class);
		jpaQuery.from(qItemRelacao);
		jpaQuery.leftJoin(qItemRelacao.banco);
		jpaQuery.leftJoin(qItemRelacao.retornoBancario);
		jpaQuery.leftJoin(qItemRelacao.relacaoBancaria, qRelacaoBancaria);

		BooleanBuilder condicoesItem = new BooleanBuilder();

		condicoesItem.and(qItemRelacao.relacaoBancaria.codigo.eq(codigoRelacaobancaria));

		QBean<ItemRelacao> qBeanItens = Projections.bean(
			ItemRelacao.class,
			qItemRelacao.codigo,
			qItemRelacao.status,
			qItemRelacao.cheque,
			qItemRelacao.total,
			qItemRelacao.qtdPagamentos,
			qItemRelacao.agencia,
			qItemRelacao.digitoAgencia,
			qItemRelacao.conta,
			qItemRelacao.digito,
			qItemRelacao.tipoConta,
			qItemRelacao.tipoDocumentoPagamento,
			qItemRelacao.dataPagamento,
			qItemRelacao.caminhoComprovante,
			Projections.bean(
				Banco.class,
				qItemRelacao.banco.codigo,
				qItemRelacao.banco.nome
			).as("banco"),
			Projections.bean(
				RetornoBancario.class,
				qItemRelacao.retornoBancario.codigo,
				qItemRelacao.retornoBancario.descricao,
				qItemRelacao.retornoBancario.descricaoOriginal,
				qItemRelacao.retornoBancario.identificacaoErro
			).as("retornoBancario"),
			Projections.bean(
				RelacaoBancaria.class,
				qRelacaoBancaria.codigo,
				qRelacaoBancaria.meioPagamento
			).as("relacaoBancaria")
		);

		jpaQuery.select(qBeanItens).where(condicoesItem).distinct();
		return jpaQuery.fetch();
	}

	/**
	 * Retorna a lista de {@link ItemRelacaoPagamento} de uma coleção de {@link ItemRelacao}.
	 *
	 * @param listaItensRelacao A lista de itens de uma relação bancária.
	 * @return A lista de item de relação pagamento de uma relação bancária.
	 */
	private List<ItemRelacaoPagamento> obterListaItemRelacaoPagamento(List<ItemRelacao> listaItensRelacao) {
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;
		QPagamento qPagamento = QPagamento.pagamento;
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;
		QPessoa qPessoa = QPessoa.pessoa;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		JPAQuery jpaQuery = itemRelacaoPagamentoService.jpaQueryItemRelacaoPagamento();
		jpaQuery.join(qItemRelacaoPagamento.pagamento, qPagamento);
		jpaQuery.join(qItemRelacaoPagamento.itemRelacao, qItemRelacao);
		jpaQuery.leftJoin(qPagamento.dadosBancarios, qDadosBancarios);
		jpaQuery.leftJoin(qPagamento.centroFinanceiro, qCentroFinanceiro);
		jpaQuery.leftJoin(qPagamento.credor, qPessoa);
		jpaQuery.leftJoin(qDadosBancarios.banco, qBanco);

		QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);

		BooleanBuilder condicoesItemPag = new BooleanBuilder();

		condicoesItemPag.and(qItemRelacaoPagamento.itemRelacao.in(listaItensRelacao));

		QBean qBeanItensPag = Projections.bean(
			ItemRelacaoPagamento.class,
			qItemRelacaoPagamento.codigo,
			Projections.bean(ItemRelacao.class, qItemRelacao.codigo, qItemRelacao.status).as("itemRelacao"),
			Projections.bean(
				Pagamento.class,
				qPagamento.codigo,
				qPagamento.numero,
				qPagamento.valor,
				qPagamento.dataPagamento,
				qPagamento.dataVencimento,
				qPagamento.tipoDocumentoPagamento,
				qPagamento.tipo,
				qPagamento.codigoBarras,
				qPagamento.linhaDigitavel,
				qPagamento.valorDesconto,
				qPagamento.valorMulta,
				qPagamento.valorJuros,
				Projections.bean(CentroFinanceiro.class,
					qPagamento.centroFinanceiro.codigo,
					qPagamento.centroFinanceiro.nome,
					qPagamento.centroFinanceiro.identificador,
					qPagamento.centroFinanceiro.identificadorFinal
				).as("centroFinanceiro"),
				Projections.bean(DadosBancarios.class,
					qDadosBancarios.codigo,
					qDadosBancarios.tipoConta,
					qDadosBancarios.principal,
					qDadosBancarios.homologado,
					qDadosBancarios.agencia,
					qDadosBancarios.digitoAgencia,
					qDadosBancarios.conta,
					qDadosBancarios.digito,
					qDadosBancarios.dataHomologacao,
					qDadosBancarios.tipoHomologacao,
					qDadosBancarios.situacao,
					Projections.bean(Banco.class, qBanco.codigo, qBanco.nome).as("banco")
				).as("dadosBancarios"),
				Projections.bean(PessoaJuridica.class, qPessoa.codigo, qPessoa.nome, qPessoaJuridica.nomeFantasia).as("credor")
			).as("pagamento")
		);

		jpaQuery.select(qBeanItensPag).where(condicoesItemPag);

		return jpaQuery.fetch();
	}

	/**
	 * Obter uma {@link RelacaoBancaria} com apenas dados simples.
	 *
	 * @param codigoRelacaobancaria O código da relação bancária.
	 * @return A relação bancária.
	 */
	private RelacaoBancaria obterRelacaoBancariaSimples(Long codigoRelacaobancaria) {
		QRelacaoBancaria qRelacaoBancaria = QRelacaoBancaria.relacaoBancaria;
		JPAQuery<RelacaoBancaria> jpaQuery = select();
		QBean qBean = Projections.bean(
			RelacaoBancaria.class,
			qRelacaoBancaria.codigo,
			qRelacaoBancaria.numero,
			qRelacaoBancaria.total,
			qRelacaoBancaria.chequeAnterior,
			qRelacaoBancaria.meioPagamento,
			qRelacaoBancaria.pagamentoIndividual,
			qRelacaoBancaria.qtdPagamentos,
			qRelacaoBancaria.status
		);

		jpaQuery.where(new BooleanBuilder().and(qRelacaoBancaria.codigo.eq(codigoRelacaobancaria))).select(qBean);
		return jpaQuery.fetchOne();
	}

	/**
	 * Obter o {@link PagamentoContaContabil} a partir do código do paghamento.
	 *
	 * @param codigoPagamento O código do pagamento.
	 * @return A lista de {@link PagamentoContaContabil} associado ao código do pagamento informado.
	 */
	private List<PagamentoContaContabil> obterPagamentoContaContabilSimplesPeloCodigoPagamento(Long codigoPagamento) {
		QPagamentoContaContabil qPagamentoContaContabil = QPagamentoContaContabil.pagamentoContaContabil;
		QPagamento qPagamento = QPagamento.pagamento;
		QContaContabil qContaContabil = QContaContabil.contaContabil;
		JPAQuery jpaQuery = pagamentoContaContabilService.jpaQueryPagamentoContaContabil();
		jpaQuery.join(qPagamentoContaContabil.pagamento, qPagamento);
		jpaQuery.join(qPagamentoContaContabil.contaContabil, qContaContabil);
		QBean qBean = Projections.bean(
			PagamentoContaContabil.class, qPagamentoContaContabil.codigo, qPagamentoContaContabil.tipo,
			Projections.bean(Pagamento.class, qPagamento.codigo, qPagamento.numero, qPagamento.valor, qPagamento.dataPagamento, qPagamento.tipoDocumentoPagamento, qPagamento.tipo).as("pagamento"),
			Projections.bean(ContaContabil.class, qContaContabil.codigo, qContaContabil.numero, qContaContabil.descricao, qContaContabil.tipo, qContaContabil.tipoEspecial).as("contaContabil")
		);

		jpaQuery.select(qBean).where(new BooleanBuilder().and(qPagamento.codigo.eq(codigoPagamento)));
		return jpaQuery.fetch();
	}

	/**
	 * Retorna a lista de {@link ItemRelacaoPagamento} de uma {@link RelacaoBancaria}.
	 *
	 * @param codigoRelacaobancaria O código da relação bancária.
	 * @return A lista de itens de pagamento de uma relação bnacária.
	 */
	private List<ItemRelacaoPagamento> obterListaItemRelacaoPagamento(Long codigoRelacaobancaria) {
		return obterListaItemRelacaoPagamento(obterListaItemRelacao(codigoRelacaobancaria));
	}

	/**
	 * Preencher a lista de {@link ItemRelacao} de uma relação bancária.
	 *
	 * @param codigoRelacaobancaria O código da relação bancária.
	 * @return A {@link RelacaoBancariaTO} com sua lista de itens preenchida.
	 */
	private List<RelacaoBancariaPagamentosVO> preencherListaDeItens(Long codigoRelacaobancaria) throws ValidationException {
		return criarRelacaoBancariaPagamentosVO(codigoRelacaobancaria);
	}

	@Override
	public RelacaoBancariaPagamentosVO criarRelacaoBancariaPagamentosVO(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) {
		List<DadosBancariosTO> novaLista = new ArrayList<>();
		for (DadosBancariosTO dadosBancariosTO : relacaoBancariaPagamentosVO.getListDadosBancarios()) {
			if (dadosBancariosTO != null && dadosBancariosTO.getCodigo() != null) {
				novaLista.add(dadosBancariosService.load(dadosBancariosTO.getCodigo()));
			}
		}
		relacaoBancariaPagamentosVO.setListDadosBancarios(novaLista);
		return relacaoBancariaPagamentosVO;
	}

	public List<RelacaoBancariaPagamentosVO> criarRelacaoBancariaPagamentosVO(Long codigoRelacaobancaria) throws ValidationException {
		long start = System.currentTimeMillis();
		List<ItemRelacao> listaItens = obterListaItemRelacao(codigoRelacaobancaria);
		Set<RelacaoBancariaPagamentosVO> listaVO = criarRelacaoBancariaPagamentosVO(obterRelacaoBancariaSimples(codigoRelacaobancaria), listaItens, obterListaItemRelacaoPagamento(listaItens));
//		System.out.println(String.format("Tempo total ao obter itens: %ds", (System.currentTimeMillis() - start) / 1000));
		List<RelacaoBancariaPagamentosVO> listaOrdenadaVO = new ArrayList<>(listaVO);

		listaOrdenadaVO.sort((o1, o2) -> o1.getCodigo().compareTo(o2.getCodigo()));

		return listaOrdenadaVO;
	}

	/**
	 * Montar a lista de VIEW OBJECT dos itens de uma {@link RelacaoBancaria}.
	 *
	 * @param itemRelacoes              A lista de itens de relação.
	 * @param listaItemRelacaoPagamento A lista de itens relação pagamento referentes a lista itemRelacoes do primeiro parâmetro.
	 * @return A lista de VO dos itens de uma {@link RelacaoBancaria}.
	 */
	public Set<RelacaoBancariaPagamentosVO> criarRelacaoBancariaPagamentosVO(RelacaoBancaria relacaoBancaria, List<ItemRelacao> itemRelacoes, List<ItemRelacaoPagamento> listaItemRelacaoPagamento) throws ValidationException {
		List<RelacaoBancariaPagamentosVO> list = new ArrayList<>();

		for (ItemRelacao itemRelacao : itemRelacoes) {
			// Obter os pagamentos do item.
			List<Pagamento> listaDePagamentos = new ArrayList<>();

			for (ItemRelacaoPagamento itemRelacaoPagamento : listaItemRelacaoPagamento) {
				if (itemRelacaoPagamento.getItemRelacao().getCodigo().equals(itemRelacao.getCodigo())) {
					listaDePagamentos.add(itemRelacaoPagamento.getPagamento());
				}
			}

			itemRelacao.setPagamentos(listaDePagamentos);

			if (listaDePagamentos != null && !listaDePagamentos.isEmpty()) {
				list.add(criarVODoItem(itemRelacao, relacaoBancaria, listaDePagamentos));
			}
		}

		return new LinkedHashSet<>(list);
	}

	private RelacaoBancariaPagamentosVO criarVODoItem(ItemRelacao itemRelacao, RelacaoBancaria relacaoBancaria, List<Pagamento> listaDePagamentos) throws ValidationException {
		RelacaoBancariaPagamentosVO vo = new RelacaoBancariaPagamentosVO();

		boolean exibirDadosCongelados = exibirDadosCongelados(itemRelacao, relacaoBancaria.getStatus().toString());

		Pagamento pagamento = listaDePagamentos.get(0);

		if (pagamento != null && pagamento.getCodigo() != null) {
			ParcelaNotaTO parcelaNotaTO = parcelaNotaService.obterParcelaCodigoEHomologador(pagamento.getCodigo());
			if (parcelaNotaTO != null) {
				vo.setValorNota(parcelaNotaTO.getNota().getValor());
				vo.setValor(pagamento.getValor());
			}
		}

		// É usado na Dialog de Conta Contábil
		List<PagamentoContaContabil> listaPagamentoContaContabil = obterPagamentoContaContabilSimplesPeloCodigoPagamento(listaDePagamentos.get(0).getCodigo());

		if (listaPagamentoContaContabil != null && !listaPagamentoContaContabil.isEmpty()) {
			List<PagamentoContaContabilTO> listaTO = new ArrayList<>();

			for (PagamentoContaContabil pcc : listaPagamentoContaContabil) {
				PagamentoContaContabilTO pagamentoContaContabilTO = new PagamentoContaContabilTO();
				pagamentoContaContabilTO.setCodigo(pcc.getCodigo());
				pagamentoContaContabilTO.setTipo(pcc.getTipo().getDescricao());

				pagamentoContaContabilTO.setPagamento(new PagamentoTO(pcc.getPagamento().getCodigo()));
				pagamentoContaContabilTO.setContaContabil(getMapper().map(pcc.getContaContabil(), ContaContabilTO.class));

				listaTO.add(pagamentoContaContabilTO);
			}

			vo.setListaPgtoCC(listaTO);
		}

		// O valor a pagar já está sendo carregado aqui.
		List<ItemRelacaoTO> itens = filtrarPagamentoEmItensRelacoes(listaDePagamentos, relacaoBancaria.getMeioPagamento());

		// Criando novo item, pois o mesmo está sendo filtrado e recebendo seus pagamentos.
		ItemRelacaoTO itemRelacaoTO = itens.get(0);

		if (itemRelacao != null && itemRelacao.getCodigo() != null) {
			itemRelacaoTO.setCodigo(itemRelacao.getCodigo());
			itemRelacaoTO.setDataPagamento(itemRelacao.getDataPagamento());
			itemRelacaoTO.setCheque(itemRelacao.getCheque());
		}

		vo.setItemRelacao(itemRelacaoTO);
		vo.setCodigo(itemRelacao.getCodigo().toString());
		vo.setPgtoAgrupadoCodigoBarras(itemRelacao.isPgtoAgrupadoCodigoBarras());

		vo.setNumero(obterNumero(listaDePagamentos, listaDePagamentos));
		vo.settAorTR(isTRorTA(listaDePagamentos));
		vo.setDataPagamento(listaDePagamentos.get(0).getDataPagamento());
		vo.setDataVencimento(listaDePagamentos.get(0).getDataVencimento());
		vo.setQtdadePagamentos(listaDePagamentos.size());

		if (listaDePagamentos.size() > 0) {
			if (listaDePagamentos.get(0).getDadosBancarios().getCodigo() != null) {
				vo.setDadosBancarios(dadosBancariosService.obterDadosBancarioSimples(listaDePagamentos.get(0).getDadosBancarios().getCodigo()));
			}
		}

		vo.setValorLiquido(BigDecimal.ZERO);
		vo.setValorAPagar(BigDecimal.ZERO);
		vo.setTotal(BigDecimal.ZERO);

		for (PagamentoTO pagamentoTO : itemRelacaoTO.getListaPgtos()) {
			vo.setTotal(vo.getTotal().add(pagamentoTO.getValor()));
			vo.setValorAPagar(vo.getValorAPagar().add(pagamentoTO.getValorPagar()));
		}

		vo.setValorLiquido(vo.getTotal());

		if (ItemRelacao.StatusItemRelacao.CONFIRMADO.equals(itemRelacao.getStatus()) && itemRelacao.getCaminhoComprovante() != null) {
			vo.setCaminhoComprovante(itemRelacao.getCaminhoComprovante());
		}

		vo.setValorDesconto(listaDePagamentos.get(0).getValorDesconto());
		vo.setValorDevolucao(listaDePagamentos.get(0).getValorDevolucao());
		vo.setValorJuros(listaDePagamentos.get(0).getValorJuros());
		vo.setValorMulta(listaDePagamentos.get(0).getValorMulta());
		vo.setValorOutrosDescontos(getTotalOutrosDescontos(listaDePagamentos.get(0)));

		vo.setValorTributos(getValorTributo(listaDePagamentos.get(0)));
		if (listaDePagamentos.get(0).getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO)) {
			vo.setValorTributos(notaPagamentoService.obterValorTibutoDeNotaPagamentoPeloNumero(listaDePagamentos.get(0).getNumero()));
		}

		vo.setValorAdiantamento(listaDePagamentos.get(0).getValorAdiantamento() == null ? BigDecimal.ZERO : listaDePagamentos.get(0).getValorAdiantamento());
		if (listaDePagamentos.get(0).getCodigoBarras() != null && !listaDePagamentos.get(0).getCodigoBarras().isEmpty())
			vo.setCodigoBarras(BoletoUtil.formatarCodigoDeBarras(listaDePagamentos.get(0).getCodigoBarras()));

		vo.setLinhaDigitavel(listaDePagamentos.get(0).getLinhaDigitavel());
		vo.setCentroFinanceiro(new CentroFinanceiroTO(listaDePagamentos.get(0).getCentroFinanceiro().getCodigo(), listaDePagamentos.get(0).getCentroFinanceiro().getNome()));
		vo.setStatusPagamento(itemRelacao.getStatus().getNome());
		vo.setQtdadePagamentos(listaDePagamentos.size());

		if (listaDePagamentos.get(0).getCodigoBarras() != null && !listaDePagamentos.get(0).getCodigoBarras().isEmpty() && listaDePagamentos.get(0).getTipoDocumentoPagamento() != null && "Boleto".equals(listaDePagamentos.get(0).getTipoDocumentoPagamento().toString())) {
			try {
				Banco banco = bancoRepository.findById(new Long(BoletoUtil.getCodigoDoBancoDoCodigoDeBarras(listaDePagamentos.get(0).getCodigoBarras()))).orElse(null);
				vo.setBancoSelecionado(getMapper().map(banco, BancoTO.class));
			} catch (CodigoDeBarrasException e) {
				e.printStackTrace();
				throw new ValidationException("RelacaoBancariaServiceImpl", e.getMessage());
			}
		} else {
			vo.setBancoSelecionado(
				listaDePagamentos.get(0).getDadosBancarios() == null ?
					obterBancoCredorSelecionado(itemRelacaoTO, converterListaPagamentoParaPagamentoTO(listaDePagamentos)) :
					getMapper().map(listaDePagamentos.get(0).getDadosBancarios().getBanco(), BancoTO.class));
		}

		vo.setCheque(itemRelacao.getCheque() != null ? itemRelacao.getCheque().toString() : "");
		vo.setRetornoBancario(itemRelacao.getRetornoBancario() != null ? getMapper().map(itemRelacao.getRetornoBancario(), RetornoBancarioTO.class) : null);

		if (vo.istAorTR()) {
			//TODO: Refatorar.
			vo.setTributo(obterNomeTributo(converterListaPagamentoParaPagamentoTO(listaDePagamentos)));
		} else {
			PessoaTO pessoaTO = pessoaService.obterPessoa(listaDePagamentos.get(0).getCredor().getCodigo());
			if (Pessoa.TipoPessoa.PESSOA_JURIDICA.equals(pessoaTO.getTipoPessoa())) {
				vo.setCredor(getMapper().map(listaDePagamentos.get(0).getCredor(), PessoaJuridicaTO.class));
			} else if (Pessoa.TipoPessoa.PESSOA_FISICA.equals(pessoaTO.getTipoPessoa())) {
				vo.setCredor(getMapper().map(listaDePagamentos.get(0).getCredor(), PessoaFisicaTO.class));
			} else {
				vo.setCredor(getMapper().map(listaDePagamentos.get(0).getCredor(), PessoaTO.class));
				vo.getCredor().setTipoPessoa(pessoaTO.getTipoPessoa().toString());
			}
			if (itemRelacao.getCredor() != null) {
				vo.setListDadosBancarios(obterDadosBancarios(itemRelacao.getCredor().getCodigo(), listaDePagamentos));
			}
		}

		if (exibirDadosCongelados) {
			vo.setTipoDocumentoPagamento(itemRelacao.getTipoDocumentoPagamento().toString());
		} else {
			if (listaDePagamentos.isEmpty()) {
				vo.setTipoDocumentoPagamento(null);
			} else {
				vo.setTipoDocumentoPagamento(listaDePagamentos.get(0).getTipoDocumentoPagamento().toString());
			}
		}

		if (exibirDadosCongelados) {
			vo.setDataPagamento(itemRelacao.getDataPagamento());
		} else {
			vo.setDataPagamento(listaDePagamentos.isEmpty() ? null : listaDePagamentos.get(0).getDataPagamento());
		}

		if (vo.getTipoDocumentoPagamento() != null && vo.getTipoDocumentoPagamento().equals(Pagamento.TipoDocumentoPagamento.BOLETO)) {
			vo.setNomeBancoDoCodigoDeBarrasELinhaDigitavel(obterNomeBancoDoCodigoDeBarrasELinhaDigitavel(listaDePagamentos));
		}

		if (vo.getBancoSelecionado() != null) {
			vo.getBancoSelecionado().setHomologado(dadosBancariosHomologados(itemRelacaoTO, itemRelacaoTO.getListaPgtos(), map(relacaoBancaria), vo.getBancoSelecionado()));
		}

		return vo;
	}

	/**
	 * Obter a lista de dados bancários de um credor.
	 *
	 * @param codigoPessoa O código do credor.
	 * @return A lista de dados bancários.
	 */
	private List<DadosBancarios> obterDadosbancariosDeUmCredor(Long codigoPessoa) {
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;
		QPessoa qPessoa = QPessoa.pessoa;
		JPAQuery jpaQuery = dadosBancariosService.jpaQueryDadosBancarios();
		jpaQuery.join(qDadosBancarios.banco, qBanco);
		jpaQuery.join(qDadosBancarios.pessoa, qPessoa);

		QBean qBean = Projections.bean(
			DadosBancarios.class,
			qDadosBancarios.codigo,
			qDadosBancarios.principal,
			qDadosBancarios.agencia,
			qDadosBancarios.digitoAgencia,
			qDadosBancarios.conta,
			qDadosBancarios.digito,
			qDadosBancarios.homologado,
			qDadosBancarios.tipoConta,
			qDadosBancarios.dataHomologacao,
			qDadosBancarios.tipoHomologacao,
			qDadosBancarios.situacao,
			Projections.bean(Pessoa.class, qPessoa.codigo, qPessoa.nome, qPessoa.documento).as("pessoa"),
			Projections.bean(Banco.class, qDadosBancarios.banco.codigo, qDadosBancarios.banco.nome).as("banco"));

		jpaQuery.select(qBean).where(new BooleanBuilder().and(qPessoa.codigo.eq(codigoPessoa)));

		return jpaQuery.fetch();
	}

	@Override
	public long updateAtualizadorAndDataAtualizacao(long codigo) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.atualizador, securityService.getUsername())
			.set(qRelacaoBancaria.dataAtualizacao, new Date())
			.execute();
	}

	@Override
	public long updateDataEnvio(long codigo) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.dataEnvio, new Date())
			.execute();
	}

	@Override
	public long updateColaboradorEnvio(long codigo) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.colaboradorEnvio,
				new Colaborador(securityService.getUser().getPessoaFisica().getCodigo()))
			.execute();
	}

	@Override
	public long updateNumero(long codigo) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.numero,
				"RB/" + codigo)
			.execute();
	}

	private long updateStatus(long codigo, RelacaoBancaria.StatusRelacaoBancaria status) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.status, status)
			.execute();
	}

	@Override
	public long updateStatusABERTA(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.ABERTA);
	}

	@Override
	public long updateStatusEMPROCESSAMENTO(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.EM_PROCESSAMENTO);
	}

	@Override
	public long updateStatusAGUARDANDORETORNO(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO);
	}

	@Override
	public long updateStatusPROCESSANDORETORNO(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.PROCESSANDO_RETORNO);
	}

	@Override
	public long updateStatusFINALIZADA(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.FINALIZADA);
	}

	@Override
	public long updateStatusCANCELADA(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.CANCELADA);
	}

	@Override
	public long updateStatusAPROCESSAR(long codigo) {
		return updateStatus(codigo, RelacaoBancaria.StatusRelacaoBancaria.A_PROCESSAR);
	}

	@Override
	public long updateImpressaoCheque(long codigo, boolean value) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.impressaoCheque, value)
			.execute();
	}

	@Override
	public long updateImpressaoEndosso(long codigo, boolean value) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.impressaoEndosso, value)
			.execute();
	}

	@Override
	public long updateImpressao2via(long codigo, boolean value) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.impressao2via, value)
			.execute();
	}

	@Override
	public long updateAnterior(long codigo, long value) {
		return selectUpdate(qRelacaoBancaria.codigo.eq(codigo))
			.set(qRelacaoBancaria.chequeAnterior, value)
			.execute();
	}
}
