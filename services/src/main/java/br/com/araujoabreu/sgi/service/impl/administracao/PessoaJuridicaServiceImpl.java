package br.com.araujoabreu.sgi.service.impl.administracao;


import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.api.EmailAPIJavamail;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.webservices.soawebservices.Credenciais;
import br.com.araujoabreu.sgi.webservices.soawebservices.EnderecoNfe;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe.PessoaJuridicaNFe;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Cnae;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.LogPesquisa;
import br.com.fasolti.corporativo.pessoa.domain.NaturezaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.corporativo.pessoa.enums.TipoServicoPesquisa;
import br.com.fasolti.sgi.repository.CnaeRepository;
import br.com.fasolti.sgi.repository.LogPesquisaRepository;
import br.com.fasolti.sgi.repository.NaturezaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.administracao.TipoLogradouroRepository;
import br.com.fasolti.sgi.repository.cidade.CidadeRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.pessoaContaContabil.PessoaContaContabilRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.MessagingException;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PessoaJuridicaServiceImpl extends
	BaseSearchService<
		PessoaJuridica,
		PessoaJuridicaTO,
		PessoaJuridicaPesquisaTO,
		PessoaJuridicaRepository> implements PessoaJuridicaService {

	public static final String CLIENTE_JA_CADASTRADO = "Cliente já cadastrado.";
	public static final String CNPJ_JA_CADASTRADO = "CNPJ já cadastrado.";
	public static final String USUARIO_HOMOLOGADOR = "usuario.homologacao.pessoa.codigo";
	private static final QPessoaJuridica query = QPessoaJuridica.pessoaJuridica;
	private final String CPF_NAO_ENCONTRADO_RFB = "CPF nao encontrado na RFB!";

	private final String SERASA_CREDENCIAIS_SENHA = "serasa.servico.pesquisa.senha";
	private final String SERASA_CREDENCIAIS_EMAIL = "serasa.servico.pesquisa.login";

	private final String SERASA_CPF_URL_WEBSERVICE_JSON = "serasa.servico.pesquisa.pf.json.url";

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaContaContabilRepository pessoaContaContabilRepository;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private LogPesquisaRepository logPesquisaRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private CnaeRepository cnaeRepository;

	@Autowired
	private NaturezaJuridicaRepository naturezaJuridicaRepository;

	@Autowired
	private TipoLogradouroRepository tipoLogradouroRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private SecurityService<?> securityService;

	@Autowired
	private EmailAPIJavamail emailAPI;

	@Autowired
	public PessoaJuridicaServiceImpl(PessoaJuridicaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, PessoaJuridicaPesquisaTO exampleObj) {
		QPessoaJuridica qPessoaJuridica = query.as(QPessoaJuridica.class);

		if (Check.isAssigned(exampleObj.getCodigo()))
			builder.and(query.codigo.eq(exampleObj.getCodigo()));

		if (Check.isAssigned(exampleObj.getNome())) {
			builder.and(qPessoaJuridica.nome.containsIgnoreCase(exampleObj.getNome()));
		}

		if (Check.isAssigned(exampleObj.getNomeFantasiaDocumento())) {
			builder.or(qPessoaJuridica.nome.containsIgnoreCase(exampleObj.getNomeFantasiaDocumento()));
			builder.or(qPessoaJuridica.nomeFantasia.containsIgnoreCase(exampleObj.getNomeFantasiaDocumento()));
			builder.or(qPessoaJuridica.documento.containsIgnoreCase(exampleObj.getNomeFantasiaDocumento()));
		}

		if (Check.isAssigned(exampleObj.getClientePrincipalTO()) && Check.isAssigned(exampleObj.getClientePrincipalTO().getCodigo())) {
			builder.and(qPessoaJuridica.clientePrincipal.codigo.eq(exampleObj.getClientePrincipalTO().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getTipoPessoa())) {
			builder.and(qPessoaJuridica.tipoPessoa.eq((Pessoa.TipoPessoa) Util.getEnumByString(Pessoa.TipoPessoa.class, exampleObj.getTipoPessoa())));
		}

		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qPessoaJuridica.situacao.eq((Pessoa.Situacao) Util.getEnumByString(Pessoa.Situacao.class, exampleObj.getSituacao())));
		}
	}

	@Override
	protected OrderSpecifier defineSearchOrder(PessoaJuridicaPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getTipoPessoa()) && exampleObj.getTipoPessoa().equals(Pessoa.TipoPessoa.PESSOA_JURIDICA)) {
			QPessoaJuridica qPessoaJuridica = QPessoaJuridica.pessoaJuridica;

			if (exampleObj.getOrdenacao().equals("documento"))
				return (new OrderSpecifier<>(Order.ASC, qPessoaJuridica.documento));
			else if (exampleObj.getOrdenacao().equals("nomeFantasia"))
				return (new OrderSpecifier<>(Order.ASC, qPessoaJuridica.nomeFantasia));

		} else if (exampleObj.getOrdenacao() != null && exampleObj.getOrdenacao().equals("documento"))
			return (new OrderSpecifier<>(Order.ASC, query.documento));

		return (new OrderSpecifier<>(Order.ASC, query.nome));
	}

	public List<PessoaJuridicaPesquisaTO> getPessoas(String nome) {

		List<PessoaJuridica> lista = getRepository().findByNomeFantasiaContainsIgnoreCaseOrNomeContainsIgnoreCaseOrDocumentoContainsIgnoreCaseAndSituacaoAndTipoPessoaJuridica(nome, nome, nome, Pessoa.Situacao.ENABLED, PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE);

		return mapSearch(lista);
	}

	@Override
	@CachePut("pessoa")
	public PessoaJuridicaPesquisaTO getPessoaPopUp(Long id, String tipo, Long codigoCL) {

		JPAQuery jpaQuery = pessoaService.getJpaQuery();
		jpaQuery.leftJoin(QPessoa.pessoa.endereco, QEndereco.endereco1);
		jpaQuery.leftJoin(QEndereco.endereco1.cidade, QCidade.cidade);
		jpaQuery.where(QPessoa.pessoa.codigo.eq(id));
		jpaQuery.select(Projections.fields(Pessoa.class,
			QPessoa.pessoa.codigo,
			QPessoa.pessoa.tipoPessoa,
			QPessoa.pessoa.documento,
			QPessoa.pessoa.nome,
			Projections.bean(Endereco.class,
				QEndereco.endereco1.codigo,
				QEndereco.endereco1.uf,
				Projections.bean(Cidade.class,
					QCidade.cidade.codigo,
					QCidade.cidade.nome,
					QCidade.cidade.uf)
					.as(QCidade.cidade))
				.as(QEndereco.endereco1)));

		Pessoa pessoa = (Pessoa) jpaQuery.fetchOne();

		if (tipo.equals("ninformado")) {
			tipo = pessoa.getTipoPessoa().toString();
		}

		if (tipo.toLowerCase().equals("pessoa física") || tipo.toLowerCase().equals("favorecido") || tipo.toLowerCase().equals("pessoa simples")) {
			PessoaJuridicaPesquisaTO to = new PessoaJuridicaPesquisaTO();
			to.setCodigo(pessoa.getCodigo());
			to.setDocumento(pessoa.getDocumento());
			to.setNome(pessoa.getNome());
			to.setTipoPessoa(pessoa.getTipoPessoa().toString());
			if (pessoa.getEndereco() != null) {
				to.setUf(pessoa.getEndereco().getUf());
				to.setCidade(pessoa.getEndereco().getCidade().getNome());
			}

			PessoaTO cPrincipal = pessoaService.buscarClientePrincipalPorCodCliente(to.getCodigo());
			to.setClientePrincipalTO(cPrincipal);

			obterContaContabil(id, to, codigoCL);
			return to;
		} else {
			jpaQuery = select();
			jpaQuery.leftJoin(QPessoaJuridica.pessoaJuridica.endereco, QEndereco.endereco1);
			jpaQuery.leftJoin(QEndereco.endereco1.cidade, QCidade.cidade);
			jpaQuery.where(QPessoaJuridica.pessoaJuridica.codigo.eq(id));
			jpaQuery.select(Projections.fields(PessoaJuridica.class, QPessoaJuridica.pessoaJuridica.codigo,
				QPessoaJuridica.pessoaJuridica.nome,
				QPessoaJuridica.pessoaJuridica.nomeFantasia,
				QPessoaJuridica.pessoaJuridica.documento,
				QPessoaJuridica.pessoaJuridica.tipoPessoaJuridica,
				QPessoaJuridica.pessoaJuridica.inscricaoEstadual,
				QPessoaJuridica.pessoaJuridica.inscricaoMunicipal,
				Projections.bean(Endereco.class, QEndereco.endereco1.codigo, QEndereco.endereco1.uf,
					Projections.bean(Cidade.class, QCidade.cidade.codigo, QCidade.cidade.nome, QCidade.cidade.uf).as("cidade"))
					.as(QEndereco.endereco1)));
			PessoaJuridicaPesquisaTO to = getMapper().map(jpaQuery.fetchOne(), PessoaJuridicaPesquisaTO.class);

			PessoaTO cPrincipal = pessoaService.buscarClientePrincipalPorCodCliente(to.getCodigo());
			to.setClientePrincipalTO(cPrincipal);

			obterContaContabil(id, to, codigoCL);
			return to;
		}
	}

	@Override
	@CachePut("pessoa")
	@Transactional(readOnly = true)
	public PessoaJuridicaPesquisaTO getPessoaPopUp(Long id, String tipo) {
		return getPessoaPopUp(id, tipo, null);
	}

	private void obterContaContabil(Long codigoPessoa, PessoaJuridicaPesquisaTO to, Long codigoCL) {
		List<PessoaContaContabil> list = pessoaContaContabilRepository.findByPessoaCodigo(codigoPessoa);

		if (codigoCL != null) {
			List<PessoaContaContabil> filterList = list.stream()
				.filter(pessoaContaContabil -> PessoaContaContabil.TipoRelacionamento.CLIENTE.equals(pessoaContaContabil.getTipoEspecial()))
				.collect(Collectors.toList());

			filterList.forEach(pessoaContaContabil -> {
				CentroFinanceiro centroFinanceiro = (CentroFinanceiro) centroFinanceiroService.jPAQueryObterCentroFinanceiroComUnidade(codigoCL).fetchOne();
				Unidade unidade = centroFinanceiro.getUnidade();
				if (centroFinanceiro.getUnidade().getMatriz() != null) {
					unidade = centroFinanceiro.getUnidade().getMatriz();
				}
				if (unidade.getCodigo().equals(pessoaContaContabil.getUnidade().getCodigo())) {
					to.setContacontabil(pessoaContaContabil.getNumeroContaContabil());
				}
			});
		}
	}

	@Override
	public PessoaJuridicaTO findPessoaByCnpj(String documento) throws Exception {
		//33.005.574/0001-01
		Pattern pattern = Pattern.compile("(^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$)");
		Matcher matcher = pattern.matcher(documento);

		if (!matcher.matches()) {

			pattern = Pattern.compile("([0-9]{14})");
			matcher = pattern.matcher(documento);

			if (matcher.matches()) {
				//33.005.574/0001-01
				documento = String.format("%s.%s.%s/%s-%s", documento.substring(0, 2), documento.substring(2, 5), documento.substring(5, 8), documento.substring(8, 12), documento.substring(12, 14));
			} else {
				return new PessoaJuridicaTO();
			}
		}
		PessoaJuridica pessoa = getRepository().findByDocumento(documento);

		if (validaPessoaJuridica(pessoa)) {
			return getMapper().map(pessoa, PessoaJuridicaTO.class);
		}
		return new PessoaJuridicaTO();
	}

	private Boolean validaPessoaJuridica(PessoaJuridica pessoa) {

		//TODO: terminar a validação de acordo com o contrato.
		if (pessoa == null) {
			new PessoaJuridicaTO();
			throw new ValidationException("ValidationException","Este CNPJ não existe na base de dados.");

		} else if (PessoaJuridica.Situacao.DISABLED.equals(pessoa.getSituacao())) {
			throw new ValidationException("ValidationException","Não é permitido associar contratos a clientes inativos.");
		}
		GlobalBean globalBean = new GlobalBean();
		if (!globalBean.isValidCNPJ(pessoa.getDocumento())) {
			throw new ValidationException("ValidationException","CNPJ inválido.");
		}
		if (!PessoaJuridica.TipoPessoaJuridica.CLIENTE.equals(pessoa.getTipoPessoaJuridica())
			&& !PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE.equals(pessoa.getTipoPessoaJuridica())) {
			if (PessoaJuridica.TipoPessoaJuridica.FORNECEDOR.equals(pessoa.getTipoPessoaJuridica())) {
				return true;
			} else {
				throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Fornecedor.");
			}
		}
		if (!PessoaJuridica.TipoPessoaJuridica.FORNECEDOR.equals(pessoa.getTipoPessoaJuridica())
			&& !PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE.equals(pessoa.getTipoPessoaJuridica())) {
			if (PessoaJuridica.TipoPessoaJuridica.CLIENTE.equals(pessoa.getTipoPessoaJuridica())) {
				return true;
			} else {
				throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Cliente.");
			}
		}
		if (!PessoaJuridica.TipoPessoaJuridica.CLIENTE.equals(pessoa.getTipoPessoaJuridica())
			&& !PessoaJuridica.TipoPessoaJuridica.FORNECEDOR.equals(pessoa.getTipoPessoaJuridica())) {
			if (PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE.equals(pessoa.getTipoPessoaJuridica())) {
				return true;
			} else {
				throw new ValidationException("ValidationException","O CNPJ informado não pertence a um Cliente/Fornecedor.");
			}
		}
		return false;
	}

	@Override
	public List<PessoaJuridicaPesquisaTO> getPessoaJuridicaCliente(@PathVariable String nome) {

		List<PessoaJuridica> lista = getRepository().findByNomeContainsIgnoreCaseAndClientePrincipalCodigoIsNullOrNomeFantasiaContainsIgnoreCaseAndClientePrincipalCodigoIsNullOrDocumentoContainsIgnoreCaseAndClientePrincipalCodigoIsNullOrderByNomeAsc(nome, nome, nome);

		return mapSearch(lista);
	}

	@Override
	protected void doBeforeSave(PessoaJuridica entity, PessoaJuridicaTO object) {
		prepareValidateToSave(object);
		setUpperCase(object);
		String documento = getDocumentoFormatado(object);

		//Adicionar Tipo de Pessoa e documento formatado
		for (PessoaJuridica.TipoPessoaJuridica tipoPessoaJuridica : PessoaJuridica.TipoPessoaJuridica.values()) {
			if (tipoPessoaJuridica.getNome().equals(object.getTipoPessoaJuridica())) {
				entity.setTipoPessoaJuridica(tipoPessoaJuridica);
				entity.setDocumento(documento);
			}
		}

		//Salvar Endereço
		object.getEndereco().setCriador(entity.getCriador());
		object.getEndereco().setDataCriacao(entity.getDataCriacao());
		entity.setEndereco(enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class)));

		if (entity.getHomologado()) {
			entity.setHomologador(new Colaborador(pessoaRepository.findByUsuario(securityService.getUser().getUsername()).getCodigo()));
			entity.setDataAtualizacao(new Date());
		}
	}

	@Override
	protected void doAfterSave(PessoaJuridica entity, PessoaJuridicaTO object) {
		//Salvar Telefone
		if (object.getTelefones() != null && !object.getTelefones().isEmpty()) {
			for (TelefoneTO telefone : object.getTelefones()) {
				telefone.setPessoa(getMapper().map(entity, PessoaTO.class));
				telefone.setCriador(entity.getCriador());
				telefone.setDataCriacao(entity.getDataCriacao());
				telefoneRepository.save(getMapper().map(telefone, Telefone.class));
			}
		}

		if (!entity.getHomologado()) {
			// Envio de e-mail para homologação
			String body = "<html>\n" +
				"\t\t    <body>\n" +
				"                <p>Prezado homologador,</p>\n" +
				"                <p>Um novo cliente está disponível para homologação:</p>\n" +
				"                <p/>\n" +
				"                <p>CNPJ: <b>#{documento}</b></p>\n" +
				"            \t\t<p>Razão social: #{nome}</p>\n" +
				"                <p/>\n" +
				"                <p>Nome fantasia: #{nomeFantasia}</p>\n" +
				"                <p/>\n" +
				"              \t<p>Cadastrador: #{nomeCriadorEmail}</p>\n" +
				"                <p>Cadastramento: #{dataCriacao}\n" +
				"                <p/>\n" +
				"\t\t        <p>Equipe Araujo Abreu</p>\n" +
				"\t\t        <p><i>Esta mensagem foi gerada automaticamente pelo Sistema de Gestão Integrada da Araujo Abreu.</i></p>\n" +
				"\t\t    </body>\n" +
				"\t\t</html>       ";

			body = body
				.replace("#{documento}", entity.getDocumento())
				.replace("#{nome}", entity.getNome())
				.replace("#{nomeFantasia}", entity.getNomeFantasia())
				.replace("#{nomeCriadorEmail}", pessoaRepository.findByUsuario(securityService.getUser().getUsername()).getEmail())
				.replace("#{dataCriacao}", GlobalBean.formatarData(entity.getDataCriacao(), "dd/MM/yyyy"));

			try {
				emailAPI.sendEmailHomologador(
					"[SGI] Homologação de fornecedor",
					body
				);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doBeforeUpdate(PessoaJuridica entity, PessoaJuridicaTO object) {
		prepareValidateToSave(object);

		if (PessoaJuridica.TipoPessoaJuridica.CLIENTE.toString().equalsIgnoreCase(object.getTipoPessoaJuridica())) {
			entity.setClientePrincipal(null);
			entity.setTipoPessoaJuridica(PessoaJuridica.TipoPessoaJuridica.CLIENTE);
			object.setClassificacaoClienteTO(null);
			object.setClassificacaoCliente(null);
		}

		setUpperCase(object);
		String documento = getDocumentoFormatado(object);

		//Adicionar Tipo de Pessoa e documento formatado
		for (PessoaJuridica.TipoPessoaJuridica tipoPessoaJuridica : PessoaJuridica.TipoPessoaJuridica.values()) {
			if (tipoPessoaJuridica.getNome().equals(object.getTipoPessoaJuridica())) {
				entity.setTipoPessoaJuridica(tipoPessoaJuridica);
				entity.setDocumento(documento);
			}
		}

		//Update de Endereço
		object.getEndereco().setAtualizador(entity.getAtualizador());
		object.getEndereco().setDataAtualizacao(entity.getDataAtualizacao());
		if (object.getEndereco().getTipoLogradouro() != null && object.getEndereco().getTipoLogradouro().getCodigo() == null) {
			object.getEndereco().setTipoLogradouro(null);
		}
		entity.setEndereco(enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class)));

		//TODO Remover quando o telefone for retirado da edição do cliente
		int sizeTelEnt = telefoneRepository.findByPessoaCodigo(entity.getCodigo()).size();
		int sizeTelOb = object.getTelefones().size();

		//Update de Telefone
		if (object.getTelefones() != null && !(sizeTelEnt == 0 && sizeTelOb == 0)) {
			if(sizeTelOb > 0) {
				for (TelefoneTO telefone : object.getTelefones()) {
					if (telefone.getCodigo() != null && entity.getTelefones() != null && !entity.getTelefones().isEmpty()) {
						for (Telefone telefoneE : entity.getTelefones()) {
							if (telefoneE.getCodigo().longValue() == telefone.getCodigo().longValue()) {
								telefone.setPessoa(getMapper().map(entity, PessoaTO.class));
								telefone.setAtualizador(entity.getCriador());
								telefone.setDataAtualizacao(entity.getDataCriacao());
								telefoneRepository.save(getMapper().map(telefone, Telefone.class));
							} else {
								telefoneRepository.delete(telefoneE);
								entity.getTelefones().remove(telefoneE);
							}
						}
					} else {
						telefone.setPessoa(getMapper().map(entity, PessoaTO.class));
						telefone.setCriador(entity.getCriador());
						telefone.setDataCriacao(entity.getDataCriacao());
						telefoneRepository.save(getMapper().map(telefone, Telefone.class));
					}
				}
			}else{
				for(Telefone telEntity : entity.getTelefones()){
					telefoneRepository.delete(telEntity);
					entity.getTelefones().remove(telEntity);
				}
			}
		}
	}

	@Override
	public String getDocumentoFormatado(PessoaJuridicaTO object) {
		String documento = object.getDocumento();
		Pattern pattern = Pattern.compile("(^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$)");
		Matcher matcher = pattern.matcher(documento);

		if (!matcher.matches()) {

			pattern = Pattern.compile("([0-9]{14})");
			matcher = pattern.matcher(documento);

			if (matcher.matches()) {
				documento = String.format("%s.%s.%s/%s-%s", documento.substring(0, 2), documento.substring(2, 5), documento.substring(5, 8), documento.substring(8, 12), documento.substring(12, 14));
			}
		}
		return documento;
	}

	private void setUpperCase(PessoaJuridicaTO object) {
		object.setNome(object.getNome().toUpperCase());
		object.setNomeFantasia(object.getNomeFantasia().toUpperCase());
		object.getEndereco().setEndereco(object.getEndereco().getEndereco().toUpperCase());
		if (object.getEndereco().getBairro() != null && !object.getEndereco().getBairro().trim().equals("")) {
			object.getEndereco().setBairro(object.getEndereco().getBairro().toUpperCase());
		}
		if (object.getEndereco().getComplemento() != null && !object.getEndereco().getComplemento().trim().equals("")) {
			object.getEndereco().setComplemento(object.getEndereco().getComplemento().toUpperCase());
		}
	}

	public void prepareValidateToSave(PessoaJuridicaTO pessoaJuridicaTO) throws ServiceException {
		if (pessoaJuridicaTO.getMei() == true
			&& (pessoaJuridicaTO.getInscricaoEstadual() == null || pessoaJuridicaTO.getInscricaoEstadual().trim().equals(""))
			&& (pessoaJuridicaTO.getInscricaoMunicipal() == null || pessoaJuridicaTO.getInscricaoMunicipal().trim().equals(""))) {
			throw new ValidationException("ValidationException","É obrigatório informar a Inscrição Estadual ou Municipal.");
		}
		if (PessoaJuridica.CanalComunicacao.TELEFONE.getNome().equals(pessoaJuridicaTO.getCanalPreferencial())
			&& (pessoaJuridicaTO.getTelefones() == null || pessoaJuridicaTO.getTelefones().isEmpty())) {
			throw new ValidationException("ValidationException", "Para o meio de contato preferencial 'Telefone' informe, pelo menos, um telefone na aba \"Telefones\".");
		}
		if (PessoaJuridica.CanalComunicacao.EMAIL.getNome().equals(pessoaJuridicaTO.getCanalPreferencial())
			&& (pessoaJuridicaTO.getEmailContato() == null || pessoaJuridicaTO.getEmailContato().trim().equals(""))
			&& (pessoaJuridicaTO.getEmail() == null || pessoaJuridicaTO.getEmail().trim().equals(""))) {
			throw new ValidationException("ValidationException", "Para o meio de contato preferencial 'E-mail' informe, pelo menos, um e-mail na aba \"E-mail\".");
		}
	}

	@Override
	public void consultarCNPJ(PessoaTO object) {
		String aux = object.getDocumento();
		String documento = String.format("%s.%s.%s/%s-%s", aux.substring(0, 2), aux.substring(2, 5), aux.substring(5, 8), aux.substring(8, 12), aux.substring(12, 14));

		// Obtendo pessoa jurídica na base de dados.
		Pessoa pessoaSearch = pessoaRepository.findByDocumento(documento);

		// Se existir pessoa jurídica.
		if (pessoaSearch != null) {
			// Verificando se está ativa/inativa.
			if (Pessoa.Situacao.DISABLED.equals(pessoaSearch.getSituacao())) {
				throw new ValidationException("ValidationException","CNPJ já associado a um fornecedor inativo. Para ativá-lo entre em contato com o suporte.");

			} else if (Pessoa.TipoPessoa.PESSOA_SIMPLES.equals(pessoaSearch.getTipoPessoa())) {
				throw new ValidationException("ValidationException","CNPJ já associado a um fornecedor (cupom fiscal). Para convertê-lo para padrão entre em contato com o suporte.");

			} else if (Pessoa.TipoPessoa.PESSOA_JURIDICA.equals(pessoaSearch.getTipoPessoa())) {
				PessoaJuridica pj = getRepository().findByDocumento(documento);
				if (PessoaJuridica.TipoPessoaJuridica.FORNECEDOR.equals(pj.getTipoPessoaJuridica())) {
					throw new ValidationException("ValidationException","CNPJ de fornecedor.");
				}
			}

			throw new ValidationException("ValidationException","Cliente já cadastrado.");

		}
	}

	@Override
	public ValueTO verificarCnpjClientePopup(PessoaJuridicaTO pessoaJuridicaTO) {
		PessoaJuridica pessoaJuridica = getRepository().findByDocumento(GlobalBean.formatarCNPJComPonto(pessoaJuridicaTO.getDocumento()));
		if (pessoaJuridica == null) {
			pessoaJuridica = getRepository().findByDocumento(pessoaJuridicaTO.getDocumento());
		}
		if (pessoaJuridica != null && PessoaJuridica.TipoPessoaJuridica.FORNECEDOR.equals(pessoaJuridica.getTipoPessoaJuridica())) {
			return new ValueTO(Boolean.TRUE);
		}
		return new ValueTO(Boolean.FALSE);
	}

	@Override
	public PessoaJuridicaTO consultarCnpj(PessoaJuridicaTO pessoaJuridica, PessoaTO pessoaTO, boolean converterFornecedor) throws RemoteException, JAXBException, ValidationException {
		if (!converterFornecedor) {
			// Validando CNPJ.
			GlobalBean globalBean = new GlobalBean();
			if (!globalBean.isValidCNPJ(pessoaJuridica.getDocumento())) {
				throw new ValidationException("ValidationException","CNPJ inválido.");
			}

			// Obtendo pessoa jurídica na base de dados. Formatando o documento, pois do front-end vem apenas os números.
			Pessoa pessoaSearch = pessoaRepository.findByDocumento(GlobalBean.formatarCNPJComPonto(pessoaJuridica.getDocumento()));
			/*
			 * Há documentos com pontuação e sem pontuação, por isso duas verificações.
			 */
			if (pessoaSearch == null) {
				pessoaSearch = pessoaRepository.findByDocumento(GlobalBean.formatarCNPJComPonto(pessoaJuridica.getDocumento()));
			}

			// Se existir pessoa jurídica.
			if (pessoaSearch != null) {
				// Verificando se está ativa/inativa.
				if (Pessoa.Situacao.DISABLED.equals(pessoaSearch.getSituacao())) {
					throw new ValidationException("ValidationException","CNPJ já associado a um fornecedor inativo. Para ativá-lo entre em contato com o suporte.");
					// Verificando se fornecedor cupom fiscal.
				} else if (Pessoa.TipoPessoa.PESSOA_SIMPLES.equals(pessoaSearch.getTipoPessoa())) {
					throw new ValidationException("ValidationException","CNPJ já associado a um fornecedor (cupom fiscal). Para convertê-lo para padrão entre em contato com o suporte.");
				}
				// Apagar CNPJ.
				pessoaJuridica.setDocumento(null);

				throw new ValidationException("ValidationException",CLIENTE_JA_CADASTRADO);
			}
		}

		// Obter pesquisa anteriormente feito com o CNPJ informado.
		LogPesquisa logPesquisa = logPesquisaRepository.findByDocumento(GlobalBean.formatarCNPJComPonto(pessoaJuridica.getDocumento()));

		PessoaJuridicaNFe pessoaJuridicaNFe = null;

		// se existir consulta anterior.
		if (logPesquisa != null) {

			Calendar calendar = Calendar.getInstance();
			String LOG_CONSULTAS_DIAS_EXPIRACAO = "log.consultas.dias.expiracao";
			calendar.add(Calendar.DAY_OF_MONTH, -(new Integer(parametroRepository.findByChave(LOG_CONSULTAS_DIAS_EXPIRACAO).getValor())));

			// Se a data da última pesquisa for anterior a 7 dias da data atual,
			// fazer nova pesquisa.
			if (logPesquisa.getDataPesquisa().before(calendar.getTime())) {
				pessoaJuridicaNFe = consultarCnpjReceitaFederalSoaWebService(map(pessoaJuridica), pessoaTO == null ? null : getMapper().map(pessoaTO, Colaborador.class), logPesquisa);
			}
			// Se não, usar a pesquisa já existente.
			pessoaJuridicaNFe = (PessoaJuridicaNFe) GlobalBean.converterXMLParaObjecto(PessoaJuridicaNFe.class, logPesquisa.getXmlRetorno());
		} else {
			// Não existindo pesquisa, fazer nova consulta.
			pessoaJuridicaNFe = consultarCnpjReceitaFederalSoaWebService(map(pessoaJuridica), pessoaTO == null ? null : getMapper().map(pessoaTO, Colaborador.class), null);
		}

		String CNPJ_NAO_ENCONTRADO_RFB = "CNPJ nao encontrado na RFB!";
		String CNPJ_NAO_ENCONTRADO_TESTES = "Este documento nao pode ser consultado em ambiente de testes";
		if (CNPJ_NAO_ENCONTRADO_TESTES.equals(pessoaJuridicaNFe.getMensagem())) {
			throw new ValidationException("ValidationException","Este documento não pode ser consultado em ambiente de testes");
		} else if (CNPJ_NAO_ENCONTRADO_RFB.equals(pessoaJuridicaNFe.getMensagem())) {
			throw new ValidationException("ValidationException","CNPJ não encontrado na base de dados da Receita Federal.");
		} else if ("BAIXADA".equalsIgnoreCase(pessoaJuridicaNFe.getSituacaoRFB())) {
			throw new ValidationException("ValidationException","CNPJ não consta como ativo na base de dados da Receita Federal.");
		} else if (!pessoaJuridicaNFe.isStatus()) {
			throw new ValidationException("ValidationException","Erro de conexão, login, ou dados.");
		} else {
			pessoaJuridica = map(ajustarPessoaJuridica(map(pessoaJuridica), pessoaJuridicaNFe));
		}

		return pessoaJuridica;
	}

	/**
	 * Ajustar dados da {@link PessoaJuridica} com os dados obtidos no serviço.
	 *
	 * @param pessoaJuridica    A {@link PessoaJuridica} a ser ajustada.
	 * @param pessoaJuridicaNFe Representa o XML obtido na consulta ao serviço.
	 * @return A {@link PessoaJuridica} atualizada.
	 */
	private PessoaJuridica ajustarPessoaJuridica(PessoaJuridica pessoaJuridica, PessoaJuridicaNFe pessoaJuridicaNFe) {
		pessoaJuridica.setNome(pessoaJuridicaNFe.getRazaoSocial());
		pessoaJuridica.setNomeFantasia(pessoaJuridicaNFe.getNomeFantasia());
		pessoaJuridica.setCnae(obterCnae(pessoaJuridicaNFe.getCodigoAtividadeEconomica()));
		pessoaJuridica.setNaturezaJuridica(obterNaturezaJuridica(pessoaJuridicaNFe.getCodigoNaturezaJuridica()));
		pessoaJuridica.setSituacao("ATIVA".equals(pessoaJuridicaNFe.getSituacaoRFB()) ? Pessoa.Situacao.ENABLED : Pessoa.Situacao.DISABLED);

		// ENDERECO
		if (pessoaJuridicaNFe.getEnderecos().length > 0) {
			EnderecoNfe enderecoNFe = pessoaJuridicaNFe.getEnderecos()[0];
			pessoaJuridica.getEndereco().setTipoLogradouro(obterTipoLogradouro(enderecoNFe.getLogradouro()));
			pessoaJuridica.getEndereco().setEndereco(removerTipoLogradouro(enderecoNFe.getLogradouro()));
			pessoaJuridica.getEndereco().setNumero(enderecoNFe.getNumero());
			pessoaJuridica.getEndereco().setComplemento(enderecoNFe.getComplemento());
			pessoaJuridica.getEndereco().setBairro(enderecoNFe.getBairro());
			ajustarCidade(pessoaJuridica, enderecoNFe);
			pessoaJuridica.getEndereco().setCep(enderecoNFe.getCEP());
			// enderecoNFe.getGeoLocalizacao() - Obter localização.
			pessoaJuridica.getEndereco().setDataAtualizacao(enderecoNFe.getDataAtualizacao() != null ? enderecoNFe.getDataAtualizacao().getTime() : new Date(System.currentTimeMillis()));
		}

		pessoaJuridica.setHomologado(Boolean.TRUE);
		pessoaJuridica.setHomologador(colaboradorRepository.findById(Long.parseLong(parametroRepository.findByChave(USUARIO_HOMOLOGADOR).getValor())).orElse(null));
		pessoaJuridica.setDataHomologacao(new Date());

		return pessoaJuridica;
	}

	/**
	 * Obter a Classificação Nacional de Atividades Econômicas cadastrados na
	 * base de dados da aplicação.
	 *
	 * @param codigoCnae O código da Classificação Nacional de Atividades Econômicas.
	 * @return A Classificação Nacional de Atividades Econômicas.
	 */
	private Cnae obterCnae(String codigoCnae) {
		return findCnaeBySigle(codigoCnae);
	}

	private Cnae findCnaeBySigle(String sigla) {
		if (sigla == null) {
			return null;
		} else {
			String regex = "[\\.\\-\\/]";
			sigla = sigla.replaceAll(regex, "");
			StringBuilder siglaFormat = new StringBuilder(sigla);
			if (sigla.length() == 7) {
				siglaFormat.insert(2, ".");
				siglaFormat.insert(5, "-");
				siglaFormat.insert(7, "-");
				sigla = siglaFormat.toString();
			}
		}
		return cnaeRepository.findBySigla(sigla);
	}

	private NaturezaJuridica obterNaturezaJuridica(String codigoRfb) {
		return findNaturezaJuridicaByCodigoRfb(codigoRfb);
	}

	private NaturezaJuridica findNaturezaJuridicaByCodigoRfb(String codigoRfb) {
		if (codigoRfb != null && codigoRfb.length() == 4) {
			StringBuilder codigoRfbFormat = new StringBuilder(codigoRfb);
			codigoRfbFormat.insert(3, "-");
			codigoRfb = codigoRfbFormat.toString();
		}
		return naturezaJuridicaRepository.findByCodigoRfb(codigoRfb);
	}

	/**
	 * Obter o {@link TipoLogradouro} a partir das informações de endereço do XML obtido do serviço.
	 *
	 * @param enderecoNFe O endereço obtido da pesquisa.
	 * @return O {@link TipoLogradouro}.
	 */
	private TipoLogradouro obterTipoLogradouro(String enderecoNFe) {
		for (String value : enderecoNFe.split(" ")) {
			List<TipoLogradouro> list = findNameTipoLogradouroByValue(value);
			if (!list.isEmpty()) {
				return list.get(0);
			} else if ("AV".equals(value)) {
				return findTipoLogradouroByNome("AVENIDA");
			} else if ("R".equals(value)) {
				return findTipoLogradouroByNome("RUA");
			} else if ("EST".equals(value)) {
				return findTipoLogradouroByNome("ESTRADA");
			} else if ("AL".equals(value)) {
				return findTipoLogradouroByNome("ALAMEDA");
			}
		}
		return null;
	}

	private List<TipoLogradouro> findNameTipoLogradouroByValue(String nome) {
		return tipoLogradouroRepository.findByNomeStartsWithIgnoreCase(nome);
	}

	private TipoLogradouro findTipoLogradouroByNome(String nome) {
		if (nome == null) {
			return null;
		}
		return tipoLogradouroRepository.findByNome(nome);
	}

	private String removerTipoLogradouro(String enderecoNFe) {
		for (String value : enderecoNFe.split(" ")) {
			switch (value) {
				case " ":
					return enderecoNFe.substring(1).trim();
				case "AV":
					return enderecoNFe.substring(2).trim();
				case "R":
					return enderecoNFe.substring(1).trim();
				case "EST":
					return enderecoNFe.substring(3).trim();
				case "AL":
					return enderecoNFe.substring(2).trim();
			}
		}
		return enderecoNFe;
	}

	/**
	 * Obter a cidade cadastrada na base de dados.
	 *
	 * @param pessoaJuridica
	 * @param enderecoNFe    O código IBGE.
	 * @return A cidade associada ao código IBGE.
	 */
	private void ajustarCidade(PessoaJuridica pessoaJuridica, EnderecoNfe enderecoNFe) {
		Cidade cidade = cidadeRepository.findByCodigo((long) enderecoNFe.getCodigoIBGE());
		if (cidade == null) {
			// Se não achar pelo código, procurar pelo nome.
			cidade = findCidadeByNomeUFNormalized(enderecoNFe.getCidade(), enderecoNFe.getEstado());
		}

		if (cidade != null) {
			pessoaJuridica.getEndereco().setCidade(cidade);
			pessoaJuridica.getEndereco().setUf(cidade.getUf());
		}
	}

	public Cidade findCidadeByNomeUFNormalized(String nome, String uf) {

		if ((nome == null) || (uf == null)) {
			return null;
		}

		String nomeSemAcento = removerAcento(nome);

		List<Cidade> cidadeList = cidadeRepository.findByNomeQuery(nomeSemAcento.trim().toUpperCase(), nome.trim().toUpperCase(), uf.trim().toUpperCase());
		if (cidadeList != null && !cidadeList.isEmpty()) {
			return cidadeList.get(0);
		} else {
			return null;
		}

	}

	private String removerAcento(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	private PessoaJuridicaNFe consultarCnpjReceitaFederalSoaWebService(PessoaJuridica pessoaJuridica, Colaborador colaborador, LogPesquisa logPesquisa) throws RemoteException, JAXBException {
		if (pessoaJuridica == null) {
			throw new IllegalArgumentException("Pessoa Jurídica não pode ser nula.");
		}

		return consultarCnpjReceitaFederalSoaWebService(GlobalBean.formatarCNPJComPonto(pessoaJuridica.getDocumento()), colaborador, logPesquisa);
	}

	private PessoaJuridicaNFe consultarCnpjReceitaFederalSoaWebService(String documento, Colaborador colaborador, LogPesquisa logPesquisa) throws JAXBException {
		if (documento == null) {
			throw new IllegalArgumentException("Documento de Pessoa Jurídica não pode ser nula.");
		} else if (documento.isEmpty()) {
			throw new IllegalArgumentException("Documento não pode ser vazio.");
		}

		GlobalBean globalBean = new GlobalBean();
		if (!globalBean.isValidCNPJ(documento)) {
			throw new IllegalArgumentException("CNPJ inválido.");
		}

		Credenciais credenciais = new Credenciais(parametroRepository.findByChave(SERASA_CREDENCIAIS_EMAIL).getValor(), parametroRepository.findByChave(SERASA_CREDENCIAIS_SENHA).getValor(), documento);

		PessoaJuridicaNFe pessoaJuridicaNFe = new PessoaJuridicaNFe();
		try {
			pessoaJuridicaNFe = consultarCnpjReceitaFederalJSONWebService(documento);
		} catch (Exception e) {
			if (e.getMessage().equals("Failed : HTTP error code : 302")) {
				pessoaJuridicaNFe.setDocumento(documento);
				pessoaJuridicaNFe.setStatus(false);
			}
		}

		LogPesquisa logConsultado = monterLogPesquisa(colaborador, TipoServicoPesquisa.SERASA_Experian, pessoaJuridicaNFe.isStatus() == false ? LogPesquisa.SituacaoPesquisa.ERRO : LogPesquisa.SituacaoPesquisa.SUCESSO, documento, new Date(),
			GlobalBean.converterObjetoParaXML(credenciais), String.valueOf(pessoaJuridicaNFe.isStatus()), GlobalBean.converterObjetoParaXML(pessoaJuridicaNFe));

		// Se a pesquisa existir anteriormente, atualizar a mesma.
		if (logPesquisa != null) {
			logPesquisa.setDataPesquisa(new Date());
			logPesquisa.setSolicitante(colaborador);
			logPesquisa.setXmlEnvio(GlobalBean.converterObjetoParaXML(credenciais));
			logPesquisa.setResultado(String.valueOf(pessoaJuridicaNFe.isStatus()));
			logPesquisa.setXmlRetorno(GlobalBean.converterObjetoParaXML(pessoaJuridicaNFe));
			logPesquisaRepository.save(logPesquisa);
		} else {
			// Se não, criando novo log de pesquisa.
			logPesquisaRepository.save(logConsultado);
		}

		return pessoaJuridicaNFe;
	}

	private LogPesquisa monterLogPesquisa(Colaborador colaborador, TipoServicoPesquisa tipoServicoPesquisa, LogPesquisa.SituacaoPesquisa situacao, String documento, Date dataPesquisa, String xmlEnvio, String resultado, String xmlRetorno) {
		return new LogPesquisa(colaborador, tipoServicoPesquisa, situacao, documento, dataPesquisa, xmlEnvio, resultado, xmlRetorno);
	}

	private PessoaJuridicaNFe consultarCnpjReceitaFederalJSONWebService(String documento) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String SERASA_URL_WEBSERVICE_JSON = "serasa.servico.pesquisa.json.url";
			HttpPost postRequest = new HttpPost(parametroRepository.findByChave(SERASA_URL_WEBSERVICE_JSON).getValor());

			String credenciais = "{\"Credenciais\": { \"Email\": \"%s\", \"Senha\": \"%s\" },\"Documento\": \"%s\"}";
			StringEntity input = new StringEntity(String.format(credenciais, parametroRepository.findByChave(SERASA_CREDENCIAIS_EMAIL).getValor(), parametroRepository.findByChave(SERASA_CREDENCIAIS_SENHA).getValor(), documento));
			input.setContentType("application/json");
			postRequest.setEntity(input);

			CloseableHttpResponse response = httpclient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			StringBuilder json = new StringBuilder();

			while ((output = br.readLine()) != null)
				json.append(output);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

			response.close();
			httpclient.close();

			return objectMapper.readValue(json.toString(), PessoaJuridicaNFe.class);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	@Override
	public PessoaJuridicaTO obterPessoaJuridica(Long codigo) {
		JPAQuery<PessoaJuridica> jpaQueryPessoa = select();
		jpaQueryPessoa.from(query);
		QBean qBean = Projections.bean(
			PessoaJuridica.class,
			query.codigo,
			query.nome,
			query.nomeFantasia,
			query.documento
		);

		jpaQueryPessoa.where(new BooleanBuilder().and(query.codigo.eq(codigo))).select(qBean);

		return map(jpaQueryPessoa.fetchOne());
	}

	@Override
	public JPAQuery<PessoaJuridica> jpaQueryPessoaJuridica() {
		return select();
	}

	@Override
	public String obterPessoaFisicaApenasNomeFantasia(Long codigo) {

		PessoaJuridica pessoa = select(Projections.fields(
			PessoaJuridica.class,
			QPessoaJuridica.pessoaJuridica.codigo,
			QPessoaJuridica.pessoaJuridica.nomeFantasia))
			.where(QPessoaJuridica.pessoaJuridica.codigo.eq(codigo))
			.fetchOne();

		if (pessoa != null) {
			return pessoa.getNomeFantasia();
		}

		return null;
	}

	@Override
	public String obterInscricaoEstadual(Long codigo) {

		PessoaJuridica pessoa = select(Projections.fields(
			PessoaJuridica.class,
			QPessoaJuridica.pessoaJuridica.codigo,
			QPessoaJuridica.pessoaJuridica.inscricaoEstadual))
			.where(QPessoaJuridica.pessoaJuridica.codigo.eq(codigo))
			.fetchOne();

		if (pessoa != null) {
			return pessoa.getInscricaoEstadual();
		}

		return null;
	}

	@Override
	public List<PessoaJuridicaTO> buscarListaPorNomeFantasia(String nome) {
		JPAQuery<PessoaJuridica> jpaQueryPessoa = select();

		QBean qBean = Projections.bean(
			PessoaJuridica.class,
			query.codigo,
			query.nome,
			query.nomeFantasia,
			query.documento
		);

		jpaQueryPessoa.select(qBean)
			.from(query)
			.where(
				query.situacao.eq(Pessoa.Situacao.ENABLED)
					.and(query.tipoPessoa.eq(Pessoa.TipoPessoa.PESSOA_JURIDICA))
					.and(
						query.nome.lower().containsIgnoreCase(nome.toLowerCase())
							.or(query.nomeFantasia.lower().containsIgnoreCase(nome.toLowerCase()))
							.or(query.documento.lower().containsIgnoreCase(nome.toLowerCase()))
					)
			)
			.orderBy(query.documento.asc());

		List<PessoaJuridica> listaPj = jpaQueryPessoa.fetch();

		List<PessoaJuridicaTO> listaTO = new ArrayList<>();

		for (PessoaJuridica pessoaJuridica : listaPj) {
			PessoaJuridicaTO to = getMapper().map(pessoaJuridica, PessoaJuridicaTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}
}
