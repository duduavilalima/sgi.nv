package br.com.araujoabreu.sgi.service.impl.etapasNaoFaturadas;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.araujoabreu.sgi.compras.domain.DocumentoFiscal;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.administracao.ServicoTipoService;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.etapasNaoFaturadas.EtapasNaoFaturadasService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalFaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.documentoFiscal.DocumentoFiscalRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoAliquotaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import br.com.fasolti.sgi.repository.notaSaida.ServicoTipoRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EtapasNaoFaturadasServiceImpl implements EtapasNaoFaturadasService {

	private final QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;

	@Autowired
	protected Mapper mapper;

	@Autowired
	private SecurityService<?> securityService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private FaturamentoAliquotaRepository faturamentoAliquotaRepository;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

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
	private DozerHelper dozerHelper;

	@Autowired
	private ServicoTipoService servicoTipoService;

	@Override
	public NotaSaidaTO findNotaFiscalFaturamento(String id) {
		NotaSaida notaSaida = notaSaidaRepository.findById(Long.parseLong(id)).orElse(null);
		return map(notaSaida);
	}

	@Override
	public NotaFiscalFaturamentoTO createEtapaNaoFaturada(String id) {
		NotaFiscalFaturamentoTO notaFiscalFaturamentoTO = new NotaFiscalFaturamentoTO();
		FaturamentoEtapa faturamentoEtapa = faturamentoEtapaRepository.findByCodigo(Long.parseLong(id));
		notaFiscalFaturamentoTO.setFaturamentoEtapaTO(map(faturamentoEtapa));
		notaFiscalFaturamentoTO.setStatus("Aberta");
		return notaFiscalFaturamentoTO;
	}

	@Override
	public NotaFiscalFaturamentoTO findLastNotaSaidaByUnidadeCodigo(NotaFiscalFaturamentoTO exampleObj) {

		if (Check.isAssigned(exampleObj) && Check.isAssigned(exampleObj.getEmpresa()) && Check.isAssigned(exampleObj.getEmpresa().getCodigo())) {
			List<NotaSaida> ultimasNotaSaida = null;
			if (Check.isNotEmpty(ultimasNotaSaida)) {
				NotaSaida notaSaida = ultimasNotaSaida.get(0);
				int proximoNumero = Integer.parseInt(notaSaida.getNumero().replace("NF/", ""));
				proximoNumero++;
				exampleObj.setNumero("NF/" + Util.formataNumero(proximoNumero, 9));
			}
		}

		return exampleObj;
	}

	@Override
	public List<ServicoTipoPesquisaTO> buscarServicos(ServicoTipoTO exampleObj) {
		List<ServicoTipoTO> lista = servicoTipoService.findByNomeAndSituacaoAndTipoFaturamento(exampleObj.getNome(), ServicoTipo.NOME_SITUACAO_ATIVO, ServicoTipo.NOME_TF_HABILITADO);
		return dozerHelper.map(lista, ServicoTipoPesquisaTO.class);
	}

	@Transactional
	protected void doBeforeSave(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO) {
		prepareValidateToSave(notaFiscalFaturamentoTO);
	}

	@Transactional
	@Override
	public NotaFiscalFaturamentoTO save(NotaFiscalFaturamentoTO transferObj) {
		try {
			doBeforeSave(transferObj);

			NotaSaida notaSaida = salvarNota(transferObj);
			transferObj.setCodigo(notaSaida.getCodigo());

			return transferObj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void prepareValidateToSave(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO) throws ValidationException {
		if (!Check.isAssigned(notaFiscalFaturamentoTO.getEmpresa()) || !Check.isAssigned(notaFiscalFaturamentoTO.getEmpresa().getCodigo()))
			throw new ValidationException("prepareValidateToSave", "Empresa é um campo de preenchimento obrigatório.");

		if (Check.isEmpty(notaFiscalFaturamentoTO.getNumero()))
			throw new ValidationException("prepareValidateToSave", "Número é um campo de preenchimento obrigatório.");

		if (Check.isEmpty(notaFiscalFaturamentoTO.getStatus()))
			throw new ValidationException("prepareValidateToSave", "Situação é um campo de preenchimento obrigatório.");

		if (notaFiscalFaturamentoTO.getDataEmissao() == null)
			throw new ValidationException("prepareValidateToSave", "Emissão é um campo de preenchimento obrigatório.");

		if (Check.isEmpty(notaFiscalFaturamentoTO.getFormaPagamento()))
			throw new ValidationException("prepareValidateToSave", "Forma de pagamento é um campo de preenchimento obrigatório.");

		if (notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcela única") && notaFiscalFaturamentoTO.getVencimento() == null)
			throw new ValidationException("prepareValidateToSave", "Vencimento é um campo de preenchimento obrigatório.");

		if (Check.isAssigned(notaFiscalFaturamentoTO.getVencimento()) && notaFiscalFaturamentoTO.getDataEmissao().after(notaFiscalFaturamentoTO.getVencimento()))
			throw new ValidationException("prepareValidateToSave", "A data de emissão deve ser menor ou igual a data de vencimento.");

		if (Check.isAssigned(notaFiscalFaturamentoTO.getVencimento()) && Util.comparaDatas(notaFiscalFaturamentoTO.getVencimento(), notaFiscalFaturamentoTO.getDataEmissao()) < 0)
			throw new ValidationException("prepareValidateToSave", "A data de vencimento deve ser maior ou igual a data de emissão.");

		if (Check.isEmpty(notaFiscalFaturamentoTO.getInfoComplementarContribuinte()))
			throw new ValidationException("prepareValidateToSave", "Discriminação dos serviços é um campo de preenchimento obrigatório.");

		if (notaFiscalFaturamentoTO.getFaturamentoEtapaTO() == null)
			throw new ValidationException("prepareValidateToSave", "O objeto do etapa não pode ser nulo nem vazio.");

		if (notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getFaturamento() == null)
			throw new ValidationException("prepareValidateToSave", "O objeto do faturamento não pode ser nulo nem vazio.");

		//Verifica se a forma de pagamento é "Parcelada" e se não há nenhuma parcela associada
		if (Check.isEmpty(notaFiscalFaturamentoTO.getNotaSaidaParcelas()) && notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcelada"))
			throw new ValidationException("prepareValidateToSave", "O valor total das parcelas deve ser igual ao valor da etapa.");

		if (notaFiscalFaturamentoTO.getServicoTipo() == null || notaFiscalFaturamentoTO.getServicoTipo().getCodigo() == null) {
			throw new ValidationException("prepareValidateToSave", "O Código do serviço informado é inválido.");
		} else if (notaFiscalFaturamentoTO.getServicoTipo() != null || notaFiscalFaturamentoTO.getServicoTipo().getCodigo() != null) {
			ServicoTipo servicoTipo = servicoTipoRepository.findById(notaFiscalFaturamentoTO.getServicoTipo().getCodigo()).orElse(null);
			if (servicoTipo == null)
				throw new ValidationException("prepareValidateToSave", "O Código do serviço informado é inválido.");
		}

		//Verifica se a forma de pagamento é "Parcela única" e se há parcelas associadas
		if (!Check.isEmpty(notaFiscalFaturamentoTO.getNotaSaidaParcelas()) && notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcela única"))
			throw new ValidationException("prepareValidateToSave", "A forma de pagamento \"Parcela única\" não aceita parcelamento.");

		if (notaFiscalFaturamentoTO.getFormaPagamento().equals("Parcelada")) {
			BigDecimal valorServico = notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValor();
			//BigDecimal valorMaterial = new BigDecimal(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValorMaterial());
			BigDecimal valorMaterial = BigDecimal.ZERO;
			BigDecimal valorEtapa = valorServico.add(valorMaterial);
			BigDecimal valorTotalParcelas = new BigDecimal("0.00");

			//Percorre todas as parcelas somando os seus valores "Brutos"
			for (NotaSaidaParcelaTO parcela : notaFiscalFaturamentoTO.getNotaSaidaParcelas()) {
				if (parcela.getValorBruto() != null)
					valorTotalParcelas = valorTotalParcelas.add(parcela.getValorBruto());

				if (parcela.getValorBruto() == null)
					throw new ValidationException("prepareValidateToSave", "O valor da parcela é um campo de preenchimento obrigatório.");

				if (parcela.getDataVencimento() == null)
					throw new ValidationException("prepareValidateToSave", "A data de vencimento da parcela é um campo de preenchimento obrigatório.");
			}

			valorEtapa.setScale(2, RoundingMode.HALF_EVEN);
			valorTotalParcelas.setScale(2, RoundingMode.HALF_EVEN);

			//Verifica se o total das parcelas é diferente do total da etapa
			if (valorTotalParcelas.doubleValue() != valorEtapa.doubleValue())
				throw new ValidationException("prepareValidateToSave", "O valor total das parcelas deve ser igual ao valor da etapa.");

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
				if (parcela.getOrdem() > 1 && Util.comparaDatas(parcela.getDataVencimento(), vencimentoParcelaAnterior) <= 0) {
					numeroParcela = parcela.getOrdem();
					break;
				}

				//atualiza a váriavel vencimentoParcelaAnterior com a data de vencimento da parcela corrente para que possa ser feita a próxima comparação
				vencimentoParcelaAnterior = parcela.getDataVencimento();
			}

			if (numeroParcela > 0) {
				if (numeroParcela == 1) {
					throw new ValidationException("prepareValidateToSave", "O vencimento da primeira parcela deve ser maior ou igual a data de emissão.");
				} else {
					throw new ValidationException("prepareValidateToSave", "O vencimento da parcela " + numeroParcela + " tem que ser posterior a " + Util.converteDateEmString(vencimentoParcelaAnterior, Constants.DATE_FORMAT_DATE) + ".");
				}
			}
		}
	}

	@Transactional
	private NotaSaida salvarNota(NotaFiscalFaturamentoTO transferObj) {
		FaturamentoEtapa faturamentoEtapa = faturamentoEtapaRepository.findByCodigo(transferObj.getFaturamentoEtapaTO().getCodigo());
		NotaSaida notaSaida = notaSaidaRepository.save(createNotaSaida(transferObj));

		faturamentoEtapa.setSituacao(FaturamentoEtapa.Status.FATURADA);
		faturamentoEtapa.setNotaSaida(notaSaida);
		faturamentoEtapaRepository.save(faturamentoEtapa);

		faturamentoEtapa.getFaturamento().setSituacao(Faturamento.Status.EM_FATURAMENTO);
		faturamentoRepository.save(faturamentoEtapa.getFaturamento());

		return notaSaida;
	}


	private NotaSaida createNotaSaida(NotaFiscalFaturamentoTO transferObj) {
		Pessoa credor = pessoaRepository.findByCodigo(transferObj.getEmpresa().getCodigo());
		CentroFinanceiro centroFinanceiro = centroFinanceiroRepository.findById(transferObj.getFaturamentoEtapaTO().getFaturamento().getCentroFinanceiro().getCodigo()).orElse(null);

		NotaSaida notaSaida = new NotaSaida();
		notaSaida.setCredor(credor);
		notaSaida.setNumero(transferObj.getNumero());
		notaSaida.setStatus(Nota.StatusNota.ABERTA);
		notaSaida.setTipo(Pagamento.TipoPagamento.NOTA_FISCAL);
		notaSaida.setTipoNotaFiscal(NotaFiscal.TipoNotaFiscal.FATURAMENTO);
		notaSaida.setCentroFinanceiro(centroFinanceiro);

		if (transferObj.getNatureza().equals("Venda à vista"))
			notaSaida.setNatureza(NotaFiscal.NaturezaNotaFiscal.VENDA_AVISTA);
		else
			notaSaida.setNatureza(NotaFiscal.NaturezaNotaFiscal.VENDA_APRAZO);

		if (transferObj.getFormaPagamento().equals("Parcela única")) {
			notaSaida.setFormaPagamento(Nota.FormaPagamento.PARCELA_UNICA);
			notaSaida.setNotaSaidaParcelas(createParcelaUnica(transferObj, notaSaida));
		} else {
			notaSaida.setFormaPagamento(Nota.FormaPagamento.PARCELADA);
			notaSaida.setNotaSaidaParcelas(createParcelas(transferObj.getNotaSaidaParcelas(), notaSaida));
		}

		notaSaida.setDataEmissao(transferObj.getDataEmissao());
		notaSaida.setEmitente(credor);

		notaSaida.setInfoComplementarContribuinte(transferObj.getInfoComplementarContribuinte());

		List<FaturamentoAliquota> listaTributos = faturamentoAliquotaRepository.findByFaturamentoCodigoOrderByCodigoAsc(transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo());

		if (Check.isNotEmpty(listaTributos)) {
			notaSaida.setListaNotaTributoAliquota(createTributos(listaTributos, notaSaida));

			if (transferObj.getServicoTipo() != null && transferObj.getServicoTipo().getCodigo() != null) {
				ServicoTipo servicoTipo = servicoTipoRepository.findById(transferObj.getServicoTipo().getCodigo()).orElse(null);
				notaSaida.setServicoTipo(servicoTipo);
			}
		}

		String codigoReceita = "55";
		String parametroDocFiscal = "faturamento.notafiscal.documentoFiscal.codigo";
		DocumentoFiscal documentoFiscal = null;

		Parametro resultadoDocFiscal = parametroRepository.findById(parametroDocFiscal).orElse(null);
		if (resultadoDocFiscal != null) {
			documentoFiscal = documentoFiscalRepository.findByCodigo(Long.parseLong(resultadoDocFiscal.getValor()));
		} else {
			documentoFiscal = documentoFiscalRepository.findByCodigoReceita(codigoReceita);
		}

		notaSaida.setDocumentoFiscal(documentoFiscal);

		String parametroIsentoIcms = "faturamento.notafiscal.isento_icms";
		Parametro resultadoIsentoIcms = parametroRepository.findById(parametroIsentoIcms).orElse(null);
		if (resultadoIsentoIcms != null) {
			notaSaida.setIsentoICMS(resultadoIsentoIcms.getValor().equals("1") ? true : false);
		} else {
			notaSaida.setIsentoICMS(false);
		}

		return notaSaida;
	}

	private List<NotaTributoAliquota> createTributos(List<FaturamentoAliquota> listaFaturamentoAliquotas, Nota nota) {
		List<NotaTributoAliquota> tributos = new ArrayList<>();

		for (FaturamentoAliquota faturamentoAliquota : listaFaturamentoAliquotas) {
			NotaTributoAliquota notaTributoAliquota = new NotaTributoAliquota();
			notaTributoAliquota.setNota(nota);
			notaTributoAliquota.setBaseCalculo(faturamentoAliquota.getBaseCalculo());
			notaTributoAliquota.setPercentual(faturamentoAliquota.getAliquota().getPercentual());
			notaTributoAliquota.setAliquota(faturamentoAliquota.getAliquota());
			tributos.add(notaTributoAliquota);
		}

		return tributos;
	}

	private List<NotaSaidaParcela> createParcelas(List<NotaSaidaParcelaTO> listaParcelas, NotaSaida nota) {
		List<NotaSaidaParcela> parcelas = new ArrayList<>();

		for (NotaSaidaParcelaTO parcela : listaParcelas) {
			NotaSaidaParcela notaSaidaParcela = new NotaSaidaParcela();
			notaSaidaParcela.setNotaSaida(nota);
			notaSaidaParcela.setOrdem(parcela.getOrdem());
			notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.ABERTA);
			notaSaidaParcela.setValorBruto(parcela.getValorBruto());
			notaSaidaParcela.setValorDesconto(BigDecimal.ZERO);
			notaSaidaParcela.setValorJuros(BigDecimal.ZERO);
			notaSaidaParcela.setValorMulta(BigDecimal.ZERO);
			notaSaidaParcela.setDataVencimento(parcela.getDataVencimento());
			notaSaidaParcela.setDataPagamento(parcela.getDataVencimento());
			parcelas.add(notaSaidaParcela);
		}

		return parcelas;
	}

	private List<NotaSaidaParcela> createParcelaUnica(NotaFiscalFaturamentoTO notaFiscalFaturamentoTO, NotaSaida nota) {
		List<NotaSaidaParcela> parcelas = new ArrayList<>();
		BigDecimal valorServico = notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValor();
		//BigDecimal valorMaterial = new BigDecimal(notaFiscalFaturamentoTO.getFaturamentoEtapaTO().getValorMaterial());
		BigDecimal valorMaterial = BigDecimal.ZERO;
		BigDecimal valorParcela = valorServico.add(valorMaterial);

		NotaSaidaParcela notaSaidaParcela = new NotaSaidaParcela();
		notaSaidaParcela.setNotaSaida(nota);
		notaSaidaParcela.setOrdem(1);
		notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.ABERTA);
		notaSaidaParcela.setValorBruto(valorParcela);
		notaSaidaParcela.setValorDesconto(BigDecimal.ZERO);
		notaSaidaParcela.setValorJuros(BigDecimal.ZERO);
		notaSaidaParcela.setValorMulta(BigDecimal.ZERO);
		notaSaidaParcela.setDataVencimento(notaFiscalFaturamentoTO.getVencimento());
		notaSaidaParcela.setDataPagamento(notaFiscalFaturamentoTO.getVencimento());

		parcelas.add(notaSaidaParcela);

		return parcelas;
	}

	//FIXME Procurar e remover todos os métodos de mapeamento fora da classe base

	private FaturamentoEtapaTO map(FaturamentoEtapa entity) {
		if (entity == null) return null;
		return mapper.map(entity, FaturamentoEtapaTO.class);
	}

	private Aliquota map(AliquotaTO transfer) {
		if (transfer == null) return null;
		return mapper.map(transfer, Aliquota.class);
	}

	protected List<ServicoTipoPesquisaTO> mapSearchType(Iterable<ServicoTipo> iterable) {
		return dozerHelper.map(iterable, ServicoTipoPesquisaTO.class);
	}

	private NotaSaidaTO map(NotaSaida entity) {
		if (entity == null) return null;
		return mapper.map(entity, NotaSaidaTO.class);
	}

	protected JPAQuery<FaturamentoEtapa> select() {
		return new JPAQueryFactory(entityManager).selectFrom(qFaturamentoEtapa);
	}

	protected JPAQuery<FaturamentoEtapa> select(Expression<FaturamentoEtapa> expr) {
		return new JPAQueryFactory(entityManager).selectFrom(qFaturamentoEtapa).select(expr);
	}

	protected UserDetails getUser() {
		return securityService.getUser();
	}

	protected JPAUpdateClause selectUpdate() {
		return new JPAUpdateClause(entityManager, qFaturamentoEtapa);
	}

	protected JPADeleteClause selectDelete() {
		return new JPADeleteClause(entityManager, qFaturamentoEtapa);
	}

	@Override
	public NotaFiscalFaturamentoTO validarCL(Long id) {
		NotaFiscalFaturamentoTO response = new NotaFiscalFaturamentoTO();

		FaturamentoEtapa faturamentoEtapa =
			select(Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				Projections.fields(Faturamento.class,
					QFaturamento.faturamento.codigo,
					Projections.fields(CentroFinanceiro.class,
						QCentroFinanceiro.centroFinanceiro.codigo,
						QCentroFinanceiro.centroFinanceiro.identificadorFinal,
						QCentroFinanceiro.centroFinanceiro.situacao
					).as(QCentroFinanceiro.centroFinanceiro)
				).as(QFaturamento.faturamento)
			))
				.innerJoin(qFaturamentoEtapa.faturamento, QFaturamento.faturamento)
				.innerJoin(QFaturamento.faturamento.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
				.where(
					qFaturamentoEtapa.codigo.eq(id)
				)
				.fetchOne();

		response.setCentroFinanceiro(mapper.map(faturamentoEtapa.getFaturamento().getCentroFinanceiro(), CentroFinanceiroTO.class));

		if (!faturamentoEtapa.getFaturamento().getCentroFinanceiro().getSituacao().equals(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO) &&
			!faturamentoEtapa.getFaturamento().getCentroFinanceiro().getSituacao().equals(CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO_COM_FATURAMENTO)) {
			response.setValidadoCL(Boolean.FALSE);
		} else {
			response.setValidadoCL(Boolean.TRUE);
		}

		return response;
	}
}
