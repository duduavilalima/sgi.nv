package br.com.araujoabreu.sgi.service.impl.contrato;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroEndereco;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroSituacao;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.ContratoAnexo;
import br.com.araujoabreu.sgi.administracao.domain.ContratoContato;
import br.com.araujoabreu.sgi.administracao.domain.ContratoEndereco;
import br.com.araujoabreu.sgi.administracao.domain.ContratoSituacao;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.QContrato;
import br.com.araujoabreu.sgi.administracao.domain.QContratoSituacao;
import br.com.araujoabreu.sgi.administracao.domain.QGerencia;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.comercial.domain.Aditivo;
import br.com.araujoabreu.sgi.comercial.domain.Fecho;
import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.contrato.ContratoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoSituacaoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.araujoabreu.sgi.service.fecho.FechoService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.administracao.TipoLogradouroRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroEnderecoRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroSituacaoRepository;
import br.com.fasolti.sgi.repository.cidade.CidadeRepository;
import br.com.fasolti.sgi.repository.comercial.AditivoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoAnexoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoContatoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoEnderecoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoSituacaoRepository;
import br.com.fasolti.sgi.repository.contrato.MotivoRetornoDocumentoContratoRepository;
import br.com.fasolti.sgi.repository.fecho.FechoRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ContratoServiceImpl extends BaseSearchService<Contrato, ContratoTO, ContratoPesquisaTO, ContratoRepository> implements ContratoService {

	private static final QContrato qContrato = QContrato.contrato;
	public static final long ZERO_LONG = 0L;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Inject
	private PessoaRepository pessoaRepository;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private ContratoEnderecoRepository contratoEnderecoRepository;

	@Autowired
	private ContratoContatoRepository contratoContatoRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private AditivoRepository aditivoRepository;

	@Autowired
	private ContratoAnexoRepository contratoAnexoRepository;

	@Autowired
	private ContratoSituacaoRepository contratoSituacaoRepository;

	@Autowired
	private MotivoRetornoDocumentoContratoRepository motivoRetornoDocumentoContratoRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private FechoService fechoService;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private TipoLogradouroRepository tipoLogradouroRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private CentroFinanceiroEnderecoRepository centroFinanceiroEnderecoRepository;

	@Autowired
	private FechoRepository fechoRepository;

	@Autowired
	private CentroFinanceiroSituacaoRepository centroFinanceiroSituacaoRepository;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	public ContratoServiceImpl(ContratoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Contrato> jpaQuery, ContratoPesquisaTO exampleObj) {
		jpaQuery.select(
			Projections.fields(
				Contrato.class,
				qContrato.codigo,
				Projections.fields(
					Pessoa.class,
					qContrato.cliente.nome
				).as("cliente"),
				qContrato.situacao,
				qContrato.estagioComercial,
				qContrato.dataAssinatura,
				qContrato.dataFimVigencia,
				qContrato.valorTotal
			)
		);
		if (exampleObj.getGerencia() != null) {
			QGerencia qGerencia = QGerencia.gerencia;
			jpaQuery.from(qContrato)
				.join(qContrato.listaGerencias, qGerencia);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ContratoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(qContrato.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getProposta())) {
			builder.and(qContrato.proposta.upper().contains(exampleObj.getProposta().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qContrato.situacao.eq((Contrato.SituacaoContrato) Util.getEnumByString(Contrato.SituacaoContrato.class, exampleObj.getSituacao())));
		}

		if (Check.isAssigned(exampleObj.getEstagioComercial())) {
			builder.and(qContrato.estagioComercial.eq((Contrato.EstagioComercial) Util.getEnumByString(Contrato.EstagioComercial.class, exampleObj.getEstagioComercial())));
		}

		if (Check.isAssigned(exampleObj.getEmpresa()) && exampleObj.getEmpresa().getCodigo() != null) {
			builder.and(qContrato.unidade.codigo.eq(exampleObj.getEmpresa().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getDiretoria()) && exampleObj.getDiretoria().getCodigo() != null) {
			builder.and(qContrato.diretoria.codigo.eq(exampleObj.getDiretoria().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getGerencia()) && exampleObj.getGerencia().getCodigo() != null) {
			QGerencia qGerencia = QGerencia.gerencia;
			builder.and(qGerencia.codigo.eq(exampleObj.getGerencia().getCodigo()));
		}

		if (exampleObj.getCliente() != null) {
			builder.and(qContrato.cliente.nome.upper().contains(exampleObj.getCliente().getNome().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getValorTotalDe())) {
			builder.and(qContrato.valorTotal.goe(exampleObj.getValorTotalDe()));
		}

		if (Check.isAssigned(exampleObj.getValorTotalAte())) {
			builder.and(qContrato.valorTotal.loe(exampleObj.getValorTotalAte()));
		}

		if (exampleObj.getAssinaturaDe() != null) {
			builder.and(qContrato.dataAssinatura.after(exampleObj.getAssinaturaDe()));
		}

		if (exampleObj.getAssinaturaAte() != null) {
			builder.and(qContrato.dataAssinatura.before(exampleObj.getAssinaturaAte()));
		}

		if (exampleObj.getVigenciaDe() != null) {
			builder.and(qContrato.dataInicioVigencia.after(exampleObj.getVigenciaDe()));
		}

		if (exampleObj.getVigenciaAte() != null) {
			builder.and(qContrato.dataFimVigencia.before(exampleObj.getVigenciaAte()));
		}

		if (Check.isAssigned(exampleObj.getTipo())) {
			builder.and(qContrato.tipo.eq((Contrato.TipoContrato) Util.getEnumByString(Contrato.TipoContrato.class, exampleObj.getTipo())));
		}
	}

	@Override
	protected void doBeforeUpdate(Contrato entity, ContratoTO object) throws ServiceException {
		Period age = this.definirPeriodo(object);
		Integer months = (int) age.toTotalMonths();
		Integer minimo = months - 1;
		Integer maximo = months + 1;
		Range<Integer> rangeMonths = Range.between(months - 1, months + 1);
		if (!rangeMonths.contains(object.getPrazoAtual()) )
			throw new ValidationException(String.format("O prazo só é permitido entre %d e %d meses.", minimo, maximo));
	}

	@Override
	protected void doAfterLoad(ContratoTO contratoTO) {
		ContratoSituacaoTO contSitToEmAnalise = dataSituacaoContrato(contratoTO, Contrato.SituacaoContrato.EM_ANALISE, false);
		ContratoSituacaoTO contSitToEncerrado = dataSituacaoContrato(contratoTO, Contrato.SituacaoContrato.ENCERRADO, false);
		ContratoSituacaoTO contSitToParalisado = dataSituacaoContrato(contratoTO, Contrato.SituacaoContrato.PARALISADO, false);
		ContratoSituacaoTO contSitToReativacao = dataSituacaoContrato(contratoTO, Contrato.SituacaoContrato.ATIVO, false);
		ContratoSituacaoTO contSitToCancelado = dataSituacaoContrato(contratoTO, Contrato.SituacaoContrato.CANCELADO, false);
		ContratoSituacaoTO contSitToAtivacao = dataSituacaoContrato(contratoTO, Contrato.SituacaoContrato.ATIVO, true);

		if (contratoTO != null && contratoTO.getCriador() != null) {
			contratoTO.setCriador(pessoaFisicaService.nomeUsuarioPorLogin(contratoTO.getCriador()));
		}

		if (contratoTO != null && contratoTO.getAtualizador() != null) {
			contratoTO.setAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(contratoTO.getAtualizador()));
		}

		if (contratoTO != null && contSitToEmAnalise.getCodigo() != null) {
			contratoTO.setEnvioAnalise(contSitToEmAnalise.getColaborador().getNome());
			contratoTO.setDataEnvioAnalise(contSitToEmAnalise.getDataSituacao());
		}

		if (contratoTO != null && contSitToEncerrado.getCodigo() != null) {
			contratoTO.setEncerramento(contSitToEncerrado.getColaborador().getNome());
			contratoTO.setDataEncerramento(contSitToEncerrado.getDataSituacao());
		}

		if (contratoTO != null && contSitToParalisado.getCodigo() != null) {
			contratoTO.setParalizacao(contSitToParalisado.getColaborador().getNome());
			contratoTO.setDataParalizacao(contSitToParalisado.getDataSituacao());
		}

		if (contratoTO != null && contSitToReativacao.getCodigo() != null) {
			contratoTO.setReativacao(contSitToReativacao.getColaborador().getNome());
			contratoTO.setDataReativacao(contSitToReativacao.getDataSituacao());
		}

		if (contratoTO != null && contSitToCancelado.getCodigo() != null) {
			contratoTO.setCancelamento(contSitToCancelado.getColaborador().getNome());
			contratoTO.setDataCancelamento(contSitToCancelado.getDataSituacao());
		}

		if (contratoTO != null && contSitToAtivacao.getCodigo() != null) {
			contratoTO.setAtivacao(contSitToAtivacao.getColaborador().getNome());
			contratoTO.setDataAtivacao(contSitToAtivacao.getDataSituacao());
		}

	} // fim do método DoAfterLoad

	public BigDecimal totalCentrosFinanceiros(ContratoTO contratoTO) {
		return centroFinanceiroService.totalCentrosFinanceirosDeContrato(contratoTO);
	}

	// Serve para carregar a aba Endereços na tela de Contrato
	@Override
	public List<ContratoEnderecoTO> buscarEnderecos(String codigoContrato) {

		List<ContratoEndereco> listaContratoEnderecos = contratoEnderecoRepository.findByContratoCodigo(new Long(codigoContrato));
		List<ContratoEnderecoTO> contratoEnderecoTOs = new ArrayList<>();

		if (listaContratoEnderecos != null && !listaContratoEnderecos.isEmpty()) {

			for (ContratoEndereco cE : listaContratoEnderecos) {
				contratoEnderecoTOs.add(getMapper().map(cE, ContratoEnderecoTO.class));
			}
		}
		return contratoEnderecoTOs;
	}

	// Serve para carregar a aba Contato na tela de Contrato
	@Override
	public List<ContratoContatoTO> buscarContato(String codigoContrato) {

		List<ContratoContato> listaContratoContato = contratoContatoRepository.findByContratoCodigo(new Long(codigoContrato));
		List<ContratoContatoTO> contratoContatoTOs = new ArrayList<>();

		if (listaContratoContato != null && !listaContratoContato.isEmpty()) {
			for (ContratoContato cC : listaContratoContato) {
				ContratoContatoTO contratoContatoTO = getMapper().map(cC, ContratoContatoTO.class);

				// Serve para carregar os telefones na aba Contato da tela de Contrato
				List<Telefone> listTelefone = telefoneRepository.findByContatoCodigo(cC.getContato().getCodigo());

				if (listTelefone != null && !listTelefone.isEmpty()) {
					contratoContatoTO.getContato().setTelefone1(getMapper().map(listTelefone.get(0), TelefoneTO.class));

					if (listTelefone.size() > 1) {
						contratoContatoTO.getContato().setTelefone2(getMapper().map(listTelefone.get(1), TelefoneTO.class));
					}
				}
				contratoContatoTOs.add(contratoContatoTO);
			}
		}
		return contratoContatoTOs;
	}

	// Serve para carregar a aba Centro de Lucro na tela de Contrato
	@Override
	public List<CentroFinanceiroTO> buscarCentroLucro(ContratoTO contrato) throws Exception {
		return centroFinanceiroService.obterCentroDeLucroPorContrato(contrato);
	}

	// Serve para carregar a aba de Aditivos na tela de Contrato
	@Override
	public List<AditivoTO> buscarAditivos(String codigoContrato) {

		List<Aditivo> listAditivo = aditivoRepository.findByContratoCodigo(new Long(codigoContrato));
		List<AditivoTO> aditivoTO = new ArrayList<>();

		// Serve para fazer uma ordenação pelo Id
		listAditivo.sort((aditivo1, aditivo2) -> {
			if (aditivo1.getId() == null) {
				return 0;
			}
			if (aditivo2.getId() == null) {
				return 0;
			}
			return aditivo1.getId().compareTo(aditivo2.getId());
		});

		if (listAditivo != null && !listAditivo.isEmpty()) {

			for (Aditivo aditivo : listAditivo) {
				aditivoTO.add(getMapper().map(aditivo, AditivoTO.class));
			}
		}
		return aditivoTO;
	}

	// Serve para carregar a aba de Anexos na tela de Contrato
	@Override
	public List<ContratoAnexoTO> buscarAnexos(String codigoContrato) {

		List<ContratoAnexo> listAnexo = contratoAnexoRepository.findByContratoCodigo(new Long(codigoContrato));
		List<ContratoAnexoTO> contratoAnexoTO = new ArrayList<>();

		if (listAnexo != null && !listAnexo.isEmpty()) {
			for (ContratoAnexo contratoAnexo : listAnexo) {

				// Serve para pegar o Nome do Criador
				List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(contratoAnexo.getAnexo().getCriador());
				if (list != null && !list.isEmpty()) {
					contratoAnexo.getAnexo().setCriador(list.get(0).getNome());
				}
				contratoAnexoTO.add(getMapper().map(contratoAnexo, ContratoAnexoTO.class));
			}
		}
		return contratoAnexoTO;
	}

	public ContratoSituacaoTO dataSituacaoContrato(ContratoTO contratoTO, Contrato.SituacaoContrato situacaoContrato, boolean primeiraAtivacao) {

		List<ContratoSituacao> list = getJPAQueryFactory()
			.from(QContratoSituacao.contratoSituacao)
			.select(Projections.fields(ContratoSituacao.class,
				QContratoSituacao.contratoSituacao.codigo,
				QContratoSituacao.contratoSituacao.situacao,
				QContratoSituacao.contratoSituacao.dataSituacao,
				Projections.bean(Colaborador.class, QContratoSituacao.contratoSituacao.colaborador.codigo, QContratoSituacao.contratoSituacao.colaborador.nome).as("colaborador")))
			.where(QContratoSituacao.contratoSituacao.contrato.codigo.eq(contratoTO.getCodigo()).and(QContratoSituacao.contratoSituacao.situacao.eq(situacaoContrato)))
			.fetch();

		ContratoSituacao cs;

		if (list != null && !list.isEmpty()) {

			if (Contrato.SituacaoContrato.ATIVO.equals(situacaoContrato) && list.size() > 1) {

				if (primeiraAtivacao) {
					cs = list.get(0);
				} else {
					cs = list.get(list.size() - 1);
				}

				return getMapper().map(cs, ContratoSituacaoTO.class);

			} else if (!Contrato.SituacaoContrato.ATIVO.equals(situacaoContrato)) {
				cs = list.get(list.size() - 1);
				return getMapper().map(cs, ContratoSituacaoTO.class);
			}
		}
		return new ContratoSituacaoTO();
	}

	@Override
	public CidadeTO findCidadeUnidade(@RequestBody ContratoTO contratoTO) throws Exception {
		try {
			return montarCidadeUnidadeContrato(contratoTO, Boolean.FALSE);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public CidadeTO findCidadeUnidadeMatriz(ContratoTO contratoTO) throws Exception {
		try {
			return montarCidadeUnidadeContrato(contratoTO, Boolean.TRUE);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public PessoaTO obterClienteContrato(@PathVariable String id) throws Exception {
		try {
			return montarClienteContrato(new Long(id));
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public PessoaTO montarClienteContrato(Long codigoContrato) {

		JPAQuery jpaQuery = select();
		jpaQuery
			.from(QContrato.contrato)
			.select(Projections.fields(Contrato.class, QContrato.contrato.codigo,
				Projections.bean(Pessoa.class, QContrato.contrato.cliente.codigo).as("cliente")))
			.where(QContrato.contrato.codigo.eq(codigoContrato));

		Contrato contrato = (Contrato) jpaQuery.fetchOne();

		if (contrato == null) {
			return new PessoaTO();
		}

		PessoaTO pessoaTO = pessoaService.obterPessoa(contrato.getCliente().getCodigo());

		if (pessoaTO == null) {
			return new PessoaTO();
		}

		return pessoaTO;
	}

	/**
	 * Cria um objeto CidadeTO.
	 *
	 * @param contratoTO ContratoTO
	 * @return CidadeTO
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	private CidadeTO montarCidadeUnidadeContrato(ContratoTO contratoTO, Boolean isMatriz) {
		Contrato contrato = getRepository().findById(contratoTO.getCodigo()).orElse(null);

		Unidade unidade = unidadeRepository.findById(contrato.getUnidade().getCodigo()).orElse(null);

		Endereco endereco;

		if (isMatriz) {
			endereco = enderecoRepository.findById(unidade.getMatriz().getEndereco().getCodigo()).orElse(null);
		} else {
			endereco = enderecoRepository.findById(unidade.getEndereco().getCodigo()).orElse(null);
		}

		Cidade cidade = cidadeRepository.findById(endereco.getCidade().getCodigo()).orElse(null);

		CidadeTO cidadeTO = new CidadeTO();

		cidadeTO.setCodigo(cidade.getCodigo());
		cidadeTO.setUf(cidade.getUf());
		cidadeTO.setNome(cidade.getNome());
		cidadeTO.setInterfacePrefeitura(cidade.getInterfacePrefeitura());

		return cidadeTO;
	}
	@Transactional
	public void adicionarContratoSituacao(Contrato contrato, Colaborador colaborador, Contrato.SituacaoContrato situacaoContrato) {
		ContratoSituacao contratoSituacao = new ContratoSituacao(colaborador, contrato, situacaoContrato, new Date());
		contratoSituacaoRepository.save(contratoSituacao);
	}

	@Transactional
	@Override
	public void adicionarCentroFinanceiroSituacao(ColaboradorTO colaborador, CentroFinanceiroTO centroFinanceiro, String situacaoCentroFinanceiro) {
		CentroFinanceiro.SituacaoCentroFinanceiro situacaoCF = null;
		for (CentroFinanceiro.SituacaoCentroFinanceiro situacao : CentroFinanceiro.SituacaoCentroFinanceiro.values()) {
			if (situacaoCentroFinanceiro.equals(situacao.toString())) {
				situacaoCF = situacao;
			}
		}
		CentroFinanceiroSituacao centroFinanceiroSituacao = new CentroFinanceiroSituacao(getMapper().map(centroFinanceiro, CentroFinanceiro.class), getMapper().map(colaborador, Colaborador.class), new Date(), situacaoCF);
		centroFinanceiroSituacaoRepository.save(centroFinanceiroSituacao);
	}

	@Override
	@Transactional()
	public void reativar(@PathVariable Long id) throws Exception {
		try {
			reativarContrato(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void reativarContrato(Long codigo) throws ValidationException {
		Contrato contrato = select()
			.from(QContrato.contrato)
			.select(Projections.fields(Contrato.class, QContrato.contrato.codigo, QContrato.contrato.situacao))
			.where(QContrato.contrato.codigo.eq(codigo))
			.fetchOne();

		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);

		if (Contrato.SituacaoContrato.PARALISADO.equals(contrato.getSituacao())) {
			adicionarContratoSituacao(contrato, colaborador, Contrato.SituacaoContrato.ATIVO);

			getRepository().updateSituacao(Contrato.SituacaoContrato.ATIVO, codigo);
			getRepository().updateDataAtualizacao(new Date(), codigo);
			getRepository().updateAtualizador(securityService.getUser().getLogin(), codigo);

			List<CentroFinanceiro> list = getCentrosFinanceiros(codigo);

			if (list != null && !list.isEmpty()) {
				for (CentroFinanceiro centroFinanceiro : list) {

					if (CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO.equals(centroFinanceiro.getSituacao())) {
						centroFinanceiroRepository.updateSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO, centroFinanceiro.getCodigo());
						centroFinanceiroRepository.updateDataAtualizacao(new Date(), centroFinanceiro.getCodigo());
						centroFinanceiroRepository.updateAtualizador(securityService.getUser().getLogin(), centroFinanceiro.getCodigo());

						adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class), getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), CentroFinanceiro.SituacaoCentroFinanceiro.REATIVADO.toString());
						adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class), getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO.toString());
					}
				}
			}
		} else {
			throw new ValidationException("ValidationException","Operação não permitida. Este contrato já foi atualizado por outro usuário.");
		}
	}
	@Transactional
	@Override
	public void encerrar(@PathVariable Long id) throws Exception {
		try {
			encerrarContrato(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional
	public void encerrarContrato(Long codigo) throws ValidationException {
//		Contrato contrato = getRepository().findById(codigo).orElse(null);
		Contrato contrato = select()
			.from(QContrato.contrato)
			.select(Projections.fields(Contrato.class, QContrato.contrato.codigo, QContrato.contrato.situacao, QContrato.contrato.dataFimVigencia,
				QContrato.contrato.dataAtualizacao, QContrato.contrato.atualizador))
			.where(QContrato.contrato.codigo.eq(codigo))
			.fetchOne();

		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);

		if (Contrato.SituacaoContrato.ATIVO.equals(contrato.getSituacao()) || Contrato.SituacaoContrato.PARALISADO.equals(contrato.getSituacao())) {
			if (contrato.getDataFimVigencia().before(new Date())) {
				adicionarContratoSituacao(contrato, colaborador, Contrato.SituacaoContrato.ENCERRADO);

				List<CentroFinanceiro> list = centroFinanceiroRepository.findByContratoCodigo(codigo);
				if (list != null && !list.isEmpty()) {
					for (CentroFinanceiro centroFinanceiro : list) {
						if (!CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO.equals(centroFinanceiro.getSituacao())
							&& !CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO.equals(centroFinanceiro.getSituacao())) {

							centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO);
							centroFinanceiro.setDataAtualizacao(new Date());
							centroFinanceiro.setAtualizador(securityService.getUser().getLogin());
							centroFinanceiroRepository.save(centroFinanceiro);
							adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class), getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO.toString());

						}
					}
				}
				getRepository().updateSituacao(Contrato.SituacaoContrato.ENCERRADO, codigo);
				getRepository().updateDataAtualizacao(new Date(), codigo);
				getRepository().updateAtualizador(securityService.getUser().getLogin(), codigo);
			}
		} else {
			throw new ValidationException("ValidationException","Operação não permitida. Este contrato já foi atualizado por outro usuário.");
		}
	}

	public boolean dataParaHabilitarParalisacaoDeContrato(Long codigo) {
		Contrato contrato = getRepository().findById(codigo).orElse(null);

		if (contrato.getDataFimVigencia() != null) {
			String dataHoje = GlobalBean.formatarData(new Date(), "yyy-MM-dd");
			String datadataFimVigencia = GlobalBean.formatarData(contrato.getDataFimVigencia(), "yyy-MM-dd");
			return dataHoje.equalsIgnoreCase(datadataFimVigencia) || contrato.getDataFimVigencia().after(new Date());
		} else {
			return false;
		}
	}

	@Override
	@Transactional()
	public void paralisar(@PathVariable Long id) throws Exception {
		try {
			paralisarContrato(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void paralisarContrato(Long codigo) throws ValidationException {
		Contrato contrato = select()
			.from(QContrato.contrato)
			.select(Projections.fields(Contrato.class, QContrato.contrato.codigo, QContrato.contrato.situacao))
			.where(QContrato.contrato.codigo.eq(codigo))
			.fetchOne();

		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);

		if (Contrato.SituacaoContrato.ATIVO.equals(contrato.getSituacao())) {
			if (dataParaHabilitarParalisacaoDeContrato(codigo)) {

				adicionarContratoSituacao(contrato, colaborador, Contrato.SituacaoContrato.PARALISADO);

				getRepository().updateSituacao(Contrato.SituacaoContrato.PARALISADO, codigo);
				getRepository().updateDataAtualizacao(new Date(), codigo);
				getRepository().updateAtualizador(securityService.getUser().getLogin(), codigo);

				List<CentroFinanceiro> list = getCentrosFinanceiros(codigo);

				if (list != null && !list.isEmpty()) {
					for (CentroFinanceiro centroFinanceiro : list) {
						if (!CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO.equals(centroFinanceiro.getSituacao())
							&& !CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO.equals(centroFinanceiro.getSituacao())
							&& !CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO.equals(centroFinanceiro.getSituacao())) {

							centroFinanceiroRepository.updateSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO, centroFinanceiro.getCodigo());
							centroFinanceiroRepository.updateDataAtualizacao(new Date(), centroFinanceiro.getCodigo());
							centroFinanceiroRepository.updateAtualizador(securityService.getUser().getLogin(), centroFinanceiro.getCodigo());

							adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class), getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO.toString());
						}
					}
				}
			}
		} else {
			throw new ValidationException("ValidationException","Operação não permitida. Este contrato já foi atualizado por outro usuário.");
		}
	}

	private List<CentroFinanceiro> getCentrosFinanceiros(Long codigo) {
		List<CentroFinanceiroTO> listTO = centroFinanceiroService.obterCentrosFinanceirosPeloCodigoDoContrato(codigo);
		List<CentroFinanceiro> list = new ArrayList<>();

		for (CentroFinanceiroTO centroFinanceiroTO : listTO) {
			list.add(getMapper().map(centroFinanceiroTO, CentroFinanceiro.class));
		}

		return list;
	}

	@Override
	public List<TipoLogradouroTO> buscarTipoLogradouro() {
		List<TipoLogradouro> tipoLogradouro = tipoLogradouroRepository.findAll();

		List<TipoLogradouroTO> listaTO = new ArrayList<>();

		listaTO.add(null);
		if (tipoLogradouro != null && !tipoLogradouro.isEmpty()) {
			for (TipoLogradouro tp : tipoLogradouro) {
				listaTO.add(getMapper().map(tp, TipoLogradouroTO.class));
			}
		}
		return listaTO;
	}

	@Override
	public ContratoTO consultarDocumento(ContratoTO contratoTO) throws Exception {

		try {
			if (Pessoa.TipoPessoa.PESSOA_FISICA.toString().equals(contratoTO.getCliente().getTipoPessoa())) {
				return findClientByCpf(contratoTO);
			} else {
				return findClientByCnpj(contratoTO);
			}
		} catch (ValidationException e) {
			throw new ValidationException("ValidationException",e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public ContratoTO findClientByCnpj(ContratoTO contratoTO) {

		String aux = contratoTO.getCliente().getDocumento();
		String documento = String.format("%s.%s.%s/%s-%s", aux.substring(0, 2), aux.substring(2, 5), aux.substring(5, 8), aux.substring(8, 12), aux.substring(12, 14));

		PessoaJuridica pj = pessoaJuridicaRepository.findByDocumento(documento);

		if (pj == null) {

			contratoTO.setCliente(new PessoaTO());

			throw new ValidationException("ValidationException","Este CNPJ não existe na base de dados.");

		} else if (Pessoa.Situacao.DISABLED.toString().equals(pj.getSituacao())) {

			contratoTO.setCliente(new PessoaTO());

			throw new ValidationException("ValidationException","Não é permitido associar contratos a clientes inativos.");

		} else if (Contrato.TipoContrato.CLIENTE.toString().equals(contratoTO.getTipo())) {

			if (pj instanceof PessoaJuridica) {

				if (PessoaJuridica.TipoPessoaJuridica.CLIENTE.equals(pj.getTipoPessoaJuridica())
					|| PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE.equals(pj.getTipoPessoaJuridica())) {

					PessoaJuridicaTO pessoaJuridicaTO = getMapper().map(pj, PessoaJuridicaTO.class);

					contratoTO.setCliente(pessoaJuridicaTO);
					return contratoTO;

				} else {
					throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Cliente.");
				}
			} else {
				throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Cliente/Fornecedor.");
			}
		} else if (Contrato.TipoContrato.FORNECEDOR.toString().equals(contratoTO.getTipo())) {

			if (pj instanceof PessoaJuridica) {

				if (PessoaJuridica.TipoPessoaJuridica.FORNECEDOR.equals(pj.getTipoPessoaJuridica())
					|| PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE.equals(pj.getTipoPessoaJuridica())) {

					PessoaJuridicaTO pessoaJuridicaTO = getMapper().map(pj, PessoaJuridicaTO.class);
					contratoTO.setCliente(pessoaJuridicaTO);
					return contratoTO;

				} else {
					throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Fornecedor.");
				}
			} else {
				throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Cliente/Fornecedor.");
			}
		}
		return null;
	}

	public ContratoTO findClientByCpf(ContratoTO contratoTO) {

		String aux = contratoTO.getCliente().getDocumento();
		String documento = String.format("%s.%s.%s-%s", aux.substring(0, 3), aux.substring(3, 6), aux.substring(6, 9), aux.substring(9, 11));

		Pessoa pessoa = pessoaRepository.findByDocumento(documento);
		if (pessoa == null) {
			contratoTO.setCliente(new PessoaTO());

			throw new ValidationException("ValidationException","Este CPF não existe na base de dados.");

		} else if (Pessoa.Situacao.DISABLED.toString().equals(pessoa.getSituacao())) {
			contratoTO.setCliente(new PessoaTO());

			throw new ValidationException("ValidationException","Não é permitido associar contratos a clientes inativos.");

		} else if (contratoTO.getFavorecido() == Boolean.TRUE) {
			if (Pessoa.TipoPessoa.FAVORECIDO.equals(pessoa.getTipoPessoa())) {

				PessoaTO pessoaTO = getMapper().map(pessoa, PessoaTO.class);
				contratoTO.setCliente(pessoaTO);

				return contratoTO;

			} else {
				throw new ValidationException("ValidationException","O CPF informado não é um favorecido.");
			}
		} else {
			if (Contrato.TipoContrato.CLIENTE.toString().equals(contratoTO.getTipo())) {
				if (pessoa instanceof PessoaFisica) {

					PessoaFisica fisica = (PessoaFisica) pessoa;

					if (PessoaFisica.TipoPessoaFisica.CLIENTE.equals(fisica.getTipoPessoaFisica())
						|| PessoaFisica.TipoPessoaFisica.FORNECEDOR_CLIENTE.equals(fisica.getTipoPessoaFisica())
						|| PessoaFisica.TipoPessoaFisica.USUARIO_CLIENTE.equals(fisica.getTipoPessoaFisica())
						|| PessoaFisica.TipoPessoaFisica.USUARIO_FORNECEDOR_CLIENTE.equals(fisica.getTipoPessoaFisica())) {

						PessoaFisicaTO pessoaFisicaTO = getMapper().map(fisica, PessoaFisicaTO.class);
						contratoTO.setCliente(pessoaFisicaTO);
						return contratoTO;

					} else {
						throw new ValidationException("ValidationException","O CPF informado não pertence a um Cliente.");
					}
				} else {
					throw new ValidationException("ValidationException","O CPF informado não pertence a um Cliente/Fornecedor.");
				}
			} else if (Contrato.TipoContrato.FORNECEDOR.toString().equals(contratoTO.getTipo())) {
				if (pessoa instanceof PessoaFisica) {
					PessoaFisica fisica = (PessoaFisica) pessoa;

					if (PessoaFisica.TipoPessoaFisica.FORNECEDOR.toString().equals(fisica.getTipoPessoaFisica())
						|| PessoaFisica.TipoPessoaFisica.FORNECEDOR_CLIENTE.toString().equals(fisica.getTipoPessoaFisica())
						|| PessoaFisica.TipoPessoaFisica.USUARIO_FORNECEDOR.toString().equals(fisica.getTipoPessoaFisica())
						|| PessoaFisica.TipoPessoaFisica.USUARIO_FORNECEDOR_CLIENTE.toString().equals(fisica.getTipoPessoaFisica())) {

						PessoaFisicaTO pessoaFisicaTO = getMapper().map(fisica, PessoaFisicaTO.class);
						contratoTO.setCliente(pessoaFisicaTO);
						return contratoTO;

					} else {
						throw new ValidationException("ValidationException","O CPF informado não pertence a um Fornecedor.");
					}
				} else {
					throw new ValidationException("ValidationException","O CPF informado não pertence a um Cliente/Fornecedor.");
				}
			}
		}
		return null;
	}

	@Override
	@Transactional
	public void atualizarContratoDeCentroFinanceiro(Long codigoContrato) {
			Contrato contrato = getRepository().findById(codigoContrato).orElseThrow(EntityNotFoundException::new);
			Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElseThrow(EntityNotFoundException::new);

			List<CentroFinanceiro> listCf = centroFinanceiroRepository.findByContratoAndSituacao(contrato, CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO);

			if (ObjectUtil.isNotEmpty(listCf)) {
				BigDecimal totalCustoMensal = new BigDecimal(ZERO_LONG);
				BigDecimal total = new BigDecimal(ZERO_LONG);

				int contadorAtivoCancelado = 0;
				int contadorElaboracao = 0;
				int contadorAnalise = 0;

				for (CentroFinanceiro cf : listCf) {
					totalCustoMensal = totalCustoMensal.add(centroFinanceiroService.calcularValorMensalCentroFinanceiro(cf.getCodigo()));
					total = total.add(centroFinanceiroService.calcularValorTotal(cf.getCodigo()));

					if (cf.isEmAnalise())
						contadorAnalise++;

					else if (cf.getSituacao().equals(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO) || cf.getSituacao().equals(CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO))
						contadorAtivoCancelado++;

					else if (cf.getSituacao().equals(CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO))
						contadorElaboracao++;
				}
				contrato.setValorMensal(totalCustoMensal.setScale(2, BigDecimal.ROUND_DOWN));
				contrato.setValorTotal(total.setScale(2, BigDecimal.ROUND_DOWN));

				if (!Contrato.SituacaoContrato.ATIVO.equals(contrato.getSituacao())) {
					if (contadorAtivoCancelado == listCf.size()) {
						contrato.setSituacao(Contrato.SituacaoContrato.ATIVO);
						contrato.setAtualizador(colaborador.getUsuario().getLogin());
						contrato.setDataAtualizacao(new Date(System.currentTimeMillis()));
						adicionarContratoSituacao(contrato, colaborador, Contrato.SituacaoContrato.ATIVO);
					} else if (contadorElaboracao == listCf.size()) {
						contrato.setSituacao(Contrato.SituacaoContrato.EM_ELABORACAO);
						contrato.setAtualizador(colaborador.getUsuario().getLogin());
						contrato.setDataAtualizacao(new Date(System.currentTimeMillis()));
						adicionarContratoSituacao(contrato, colaborador, Contrato.SituacaoContrato.EM_ELABORACAO);
					} else if (contadorAnalise > 0 && !Contrato.SituacaoContrato.EM_ANALISE.equals(contrato.getSituacao())) {
						contrato.setSituacao(Contrato.SituacaoContrato.EM_ANALISE);
						contrato.setAtualizador(colaborador.getUsuario().getLogin());
						contrato.setDataAtualizacao(new Date(System.currentTimeMillis()));
						adicionarContratoSituacao(contrato, colaborador, Contrato.SituacaoContrato.EM_ANALISE);
					}
				}

				contrato.setAtualizador(getUser().getUsername());
				contrato.setDataAtualizacao(new Date());

				getRepository().save(contrato);
			}
	}

	@Override
	@Transactional
	public void atualizarCustoMensalEhTotal(Long codigoContrato) {
		Contrato contrato = getRepository().findById(codigoContrato).orElseThrow(EntityNotFoundException::new);
		List<CentroFinanceiro> listCf = centroFinanceiroRepository.findByContratoAndSituacao(contrato, CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO);
		if (ObjectUtil.isNotEmpty(listCf)) {
			BigDecimal total = new BigDecimal(ZERO_LONG);
			BigDecimal totalCustoMensal = new BigDecimal(ZERO_LONG);
			for (CentroFinanceiro cf : listCf) {
				totalCustoMensal = totalCustoMensal.add(centroFinanceiroService.calcularValorMensalCentroFinanceiro(cf.getCodigo()));
				total = total.add(centroFinanceiroService.calcularValorTotal(cf.getCodigo()).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			contrato.setValorMensal(totalCustoMensal.setScale(2, BigDecimal.ROUND_HALF_UP));
			contrato.setValorTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		} else {
			contrato.setValorMensal(BigDecimal.ZERO);
			contrato.setValorTotal(BigDecimal.ZERO);
		}
		contrato.setAtualizador(getUser().getUsername());
		contrato.setDataAtualizacao(new Date());
		getRepository().save(contrato);
	}

	/**
	 * Atualiza a lista de gerência de um contrato do centro financeiro.
	 *
	 * @param centroFinanceiro
	 * @deprecated
	 */
	@Transactional
	public void atualizarContratoGerencia(CentroFinanceiro centroFinanceiro) {
		Contrato contrato = getRepository().findById(centroFinanceiro.getContrato().getCodigo()).orElseThrow(EntityNotFoundException::new);

		List<CentroFinanceiro> listaCF = centroFinanceiroRepository.findByContratoCodigo(contrato.getCodigo());

		if (ObjectUtil.isNotEmpty(contrato.getListaGerencias())) {
			contrato.getListaGerencias().clear();
		}

		if (centroFinanceiro.getCodigo() == null || centroFinanceiro.getCodigo() == 0) {//na criação do CF, este ainda não pertence a lista CF do Contrato, por este motivo o teste separado
			if (!contrato.getListaGerencias().contains(centroFinanceiro.getGerencia()))
				contrato.getListaGerencias().add((Gerencia) centroFinanceiro.getGerencia());
		}

		for (CentroFinanceiro cf : listaCF) {
			if (!contrato.getListaGerencias().contains(cf.getGerencia()))
				contrato.getListaGerencias().add((Gerencia) cf.getGerencia());
		}

		contrato.setAtualizador(getUser().getUsername());
		contrato.setDataAtualizacao(new Date());

		getRepository().save(contrato);
	}

	@Override
	public ContratoTO totalCentrosFinanceirosResource(@RequestBody ContratoTO contrato) throws Exception {
		try {
			List<CentroFinanceiro> listCentroFinanceiros = centroFinanceiroRepository.findByContratoCodigo(contrato.getCodigo());
			BigDecimal total = new BigDecimal(ZERO_LONG);
			if (ObjectUtil.isNotEmpty(listCentroFinanceiros)) {
				for (CentroFinanceiro centroFinanceiro : listCentroFinanceiros) {
					CentroFinanceiroTO centroFinanceiroTO = getMapper().map(centroFinanceiro, CentroFinanceiroTO.class);
					total = total.add(centroFinanceiroService.getValorTotalCentroFinanceiro(centroFinanceiroTO, contrato));
				}
			}
			contrato.setValorTotal(total);
			return contrato;

		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	@Transactional
	public void removeCentroFinanceiro(@PathVariable Long id) throws Exception {
		try {
			List<CentroFinanceiroEndereco> listaCentroFinanceiroEndereco = centroFinanceiroEnderecoRepository.findByCentroFinanceiroCodigo(id);
			List<CentroFinanceiroSituacao> listaCentroFinanceiroSituacao = centroFinanceiroSituacaoRepository.findByCentroFinanceiroCodigo(id);
			List<FechoTO> listaFecho = fechoService.buscarListaFechoByCodCentroFinanceiro(id);
			List<Endereco> listaEnderecoExcluido = new ArrayList<>();

			if (listaCentroFinanceiroSituacao != null && !listaCentroFinanceiroSituacao.isEmpty()) {
				// Deleta os CFsSituação
				for (CentroFinanceiroSituacao cfsituacao : listaCentroFinanceiroSituacao) {
					centroFinanceiroSituacaoRepository.delete(cfsituacao);
				}
			}

			if (listaCentroFinanceiroEndereco != null && !listaCentroFinanceiroEndereco.isEmpty()) {
				for (CentroFinanceiroEndereco cfe : listaCentroFinanceiroEndereco) {
					// Lista p/ adicionar os endereços que serão excluídos
					listaEnderecoExcluido.add(cfe.getEndereco());
					// Deleta os CFsEndereços
					centroFinanceiroEnderecoRepository.delete(cfe);
				}
			}

			if (!listaEnderecoExcluido.isEmpty()) {
				// Deleta a lista de endereços  da tabela de endereços
				for (Endereco e : listaEnderecoExcluido) {
					enderecoRepository.deleteById(e.getCodigo());
				}
			}

			if (listaFecho != null && !listaFecho.isEmpty()) {
				// Deleta os fechos ligados a Centro Financeiro
				for (FechoTO fecho : listaFecho) {
					Fecho to = getMapper().map(fecho, Fecho.class);
					fechoRepository.delete(to);
				}
			}

			// Deleta o Centro Financeiro em si
			centroFinanceiroRepository.deleteById(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public ContratoTO obterContratoSimpleNotaFiscal(Long codigoContrato) {

		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Contrato.class,
				qContrato.codigo,
				Projections.fields(Unidade.class, qContrato.unidade.codigo).as("unidade"))
		);
		jpaQuery.where(qContrato.codigo.eq(codigoContrato));
		return map((Contrato) jpaQuery.fetchOne());

	}

	@Override
	public ContratoTO calcularPrazoContrato(ContratoTO contrato){
		Period age = this.definirPeriodo(contrato);
		int months = (int) age.toTotalMonths();

		if(age.getDays() > 15){
			months = months + 1;
		}

		contrato.setPrazoAtual(months);

		return contrato;
	}

	private Period definirPeriodo(ContratoTO contrato) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Instant instantI = contrato.getDataInicioVigencia().toInstant();
		Instant instantF = contrato.getDataFimVigencia().toInstant();
		LocalDate start = instantI.atZone(defaultZoneId).toLocalDate();
		LocalDate end = instantF.atZone(defaultZoneId).toLocalDate();

		return Period.between(start, end);
	}

	@Override
	public JPAQuery getJpaQuery() {
		return select();
	}

	@Override
	protected void doAfterSave(Contrato entity, ContratoTO object) throws ServiceException {
		// Atualizar o valor do contrato ao atualizar o Fecho.
		atualizarCustoMensalEhTotal(entity.getCodigo());
	}

	@Override
	protected void doAfterUpdate(Contrato entity, ContratoTO object) throws ServiceException {
		// Atualizar o valor do contrato ao atualizar o Fecho.
		atualizarCustoMensalEhTotal(entity.getCodigo());
	}

	@Override
	public Integer obterPrazo(Long codigo) {
		JPAQuery<Contrato> jpaQuery = select(
			Projections.fields(Contrato.class,
				qContrato.codigo,
				qContrato.prazoAtual)
		).where(qContrato.codigo.eq(codigo));
		Contrato contrato = jpaQuery.fetchOne();
		if (contrato == null) {
			return 0;
		}
		return contrato.getPrazoAtual();
	}
}
