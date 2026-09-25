package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.api.EmailAPIJavamail;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.webservices.soawebservices.Credenciais;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.PessoaFisicaNFe;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.LogPesquisa;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.corporativo.pessoa.enums.TipoServicoPesquisa;
import br.com.fasolti.corporativo.security.domain.QUsuario;
import br.com.fasolti.sgi.repository.LogPesquisaRepository;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.administracao.TipoLogradouroRepository;
import br.com.fasolti.sgi.repository.cidade.CidadeRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class PessoaFisicaServiceImpl extends
	BaseSearchService<PessoaFisica,
		PessoaFisicaTO,
		PessoaFisicaPesquisaTO,
		PessoaFisicaRepository> implements PessoaFisicaService {

	public static final String USUARIO_HOMOLOGADOR = "usuario.homologacao.pessoa.codigo";
	private static final QPessoaFisica qPessoaFisica = QPessoaFisica.pessoaFisica;
	private final String CNPJ_NAO_ENCONTRADO_RFB = "CNPJ nao encontrado na RFB!";

	private final String SERASA_CREDENCIAIS_SENHA = "serasa.servico.pesquisa.senha";
	private final String SERASA_CREDENCIAIS_EMAIL = "serasa.servico.pesquisa.login";
	//	private final String SERASA_URL_WEBSERVICE_CDC = "serasa.servico.pesquisa.url";
	private final String SERASA_URL_WEBSERVICE_JSON = "serasa.servico.pesquisa.json.url";

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private LogPesquisaRepository logPesquisaRepository;

	@Autowired
	private TipoLogradouroRepository tipoLogradouroRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private EmailAPIJavamail emailAPIJavamail;

	@Autowired
	private SecurityService<?> securityService;

	@Autowired
	public PessoaFisicaServiceImpl(PessoaFisicaRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(PessoaFisica entity, PessoaFisicaTO object) {
		String documento = getDocumentoFormatadoCPF(object);
		entity.setDocumento(getDocumentoFormatadoCPF(object));
		object.setDocumento(getDocumentoFormatadoCPF(object));
		//Adicionar Tipo de Pessoa e documento formatado
		for (PessoaFisica.TipoPessoaFisica tipoPessoaFisica : PessoaFisica.TipoPessoaFisica.values()) {
			if (tipoPessoaFisica.getNome().equals(object.getTipoPessoaFisica())) {
				entity.setTipoPessoaFisica(tipoPessoaFisica);
				entity.setDocumento(documento);
				setUpperCase(entity);
			}
		}

		//Salvar Endereço
		object.getEndereco().setCriador(entity.getCriador());
		object.getEndereco().setDataCriacao(entity.getDataCriacao());
		entity.setEndereco(enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class)));

		if (entity.getHomologado()) {
			String nomeUsuario = securityService.getUser().getUsername();
			Pessoa pessoa = pessoaRepository.findByUsuario(nomeUsuario);
			entity.setHomologador(pessoa);
			entity.setDataAtualizacao(new Date());
		}
	}

	@Override
	protected void doAfterSave(PessoaFisica entity, PessoaFisicaTO object) {
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
				"                <p>CPF: <b>#{documento}</b></p>\n" +
				"            \t\t<p>Nome: #{nome}</p>\n" +
				"                <p/>\n" +
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
				.replace("#{nomeCriadorEmail}", pessoaRepository.findByUsuario(securityService.getUser().getUsername()).getEmail())
				.replace("#{dataCriacao}", GlobalBean.formatarData(entity.getDataCriacao(), "dd/MM/yyyy"));

			try {
				emailAPIJavamail.sendEmailHomologador(
					"[SGI] Homologação de fornecedor",
					body
				);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doBeforeUpdate(PessoaFisica entity, PessoaFisicaTO object) {
		String documento = getDocumentoFormatadoCPF(object);
		entity.setDocumento(getDocumentoFormatadoCPF(object));
		object.setDocumento(getDocumentoFormatadoCPF(object));

		if (object.getClientePrincipal() == null || object.getClientePrincipal().getCodigo() == null) {
			entity.setClientePrincipal(null);
			object.setClassificacaoClienteTO(null);
			object.setClassificacaoCliente(null);
		}

		//Adicionar Tipo de Pessoa e documento formatado
		for (PessoaFisica.TipoPessoaFisica tipoPessoaFisica : PessoaFisica.TipoPessoaFisica.values()) {
			if (tipoPessoaFisica.getNome().equals(object.getTipoPessoaFisica())) {
				entity.setTipoPessoaFisica(tipoPessoaFisica);
				entity.setDocumento(documento);
				setUpperCase(entity);
			}
		}

		//Update de Endereço
		object.getEndereco().setAtualizador(entity.getAtualizador());
		object.getEndereco().setDataAtualizacao(entity.getDataAtualizacao());
		if (object.getEndereco().getTipoLogradouro().getCodigo() == null) {
			object.getEndereco().setTipoLogradouro(null);
		}
		entity.setEndereco(enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class)));

		//Update de Telefone
		if (object.getTelefones() != null && !object.getTelefones().isEmpty()) {
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
		}
	}

	@Override
	public String getDocumentoFormatadoCPF(PessoaFisicaTO object) {
		String documento = object.getDocumento();
		Pattern pattern = Pattern.compile("(^\\d{3}.\\d{3}.\\d{3}-\\d{2}$)");
		Matcher matcher = pattern.matcher(documento);

		if (!matcher.matches()) {

			pattern = Pattern.compile("([0-9]{11})");
			matcher = pattern.matcher(documento);

			if (matcher.matches()) {
				documento = String.format("%s.%s.%s-%s", documento.substring(0, 3), documento.substring(3, 6), documento.substring(6, 9), documento.substring(9, 11));
			}
		}
		return documento;
	}

	private void setUpperCase(PessoaFisica entity) {
		entity.setNome(entity.getNome().toUpperCase());
		entity.getEndereco().setEndereco(entity.getEndereco().getEndereco().toUpperCase());
		if (entity.getEndereco().getBairro() != null && !entity.getEndereco().getBairro().trim().equals("")) {
			entity.getEndereco().setBairro(entity.getEndereco().getBairro().toUpperCase());
		}
		if (entity.getEndereco().getComplemento() != null && !entity.getEndereco().getComplemento().trim().equals("")) {
			entity.getEndereco().setComplemento(entity.getEndereco().getComplemento().toUpperCase());
		}
	}

	@Override
	public void consultarCPF(PessoaTO object) {
		String aux = object.getDocumento();
		String documento = String.format("%s.%s.%s-%s", aux.substring(0, 3), aux.substring(3, 6), aux.substring(6, 9), aux.substring(9, 11));

		// Obtendo pessoa física na base de dados.
		Pessoa pessoaSearch = pessoaRepository.findByDocumento(documento);

		// Se existir pessoa física.
		if (pessoaSearch != null) {

			// Verificando se está ativa/inativa.
			if (Pessoa.Situacao.DISABLED.equals(pessoaSearch.getSituacao())) {
				throw new ValidationException("consultarCPF", "CPF já associado a um " + pessoaSearch.getTipoPessoa().toString().toLowerCase() + " inativa. Para ativá-lo entre em contato com o suporte.");

			} else if (Pessoa.TipoPessoa.PESSOA_FISICA.equals(pessoaSearch.getTipoPessoa())) {
				throw new ValidationException("consultarCPF", "CPF já cadastrado.");
			}

			throw new ValidationException("consultarCPF", "Este CPF está associado a um " + pessoaSearch.getTipoPessoa().toString().toLowerCase() + ". Por Favor, entre em contato com o administrador do sistema.");

		}
	}

	/**
	 * Busca uma lista de PessoaFisica pelo login do usuario.
	 *
	 * @param login
	 * @return
	 */
	@Override
	public List<PessoaFisicaTO> findByUsuarioLogin(String login) {
		QUsuario qUsuario = QUsuario.usuario;

		QBean qBean = Projections.bean(
			PessoaFisica.class,
			qPessoaFisica.codigo,
			qPessoaFisica.nome
		);

		JPAQuery jpaQuery = select();
		jpaQuery.select(qBean).from(qPessoaFisica).leftJoin(qPessoaFisica.usuario, qUsuario).where(qUsuario.login.eq(login));

		List<PessoaFisica> lista = jpaQuery.fetch();

		return map(lista);
	}

	@Override
	public PessoaFisicaTO consultarCpf(PessoaFisicaTO pessoaFisicaTO, PessoaTO pessoaTO, boolean converterFornecedor) throws RemoteException, JAXBException, ValidationException {
		if (!converterFornecedor) {
			// Validando CPF.
			GlobalBean globalBean = new GlobalBean();
			if (!globalBean.isValidCPF(pessoaFisicaTO.getDocumento())) {
				throw new ValidationException("consultarCpf", "CPF inválido.");
			}

			// Obtendo pessoa jurídica na base de dados.
			Pessoa pessoaSearch = getRepository().findByDocumento(GlobalBean.formatarCPFComPonto(pessoaFisicaTO.getDocumento()));
			if (pessoaSearch == null) {
				pessoaSearch = getRepository().findByDocumento(GlobalBean.formatarCPFComPonto(pessoaFisicaTO.getDocumento()));
			}

			// Se existir pessoa jurídica.
			if (pessoaSearch != null) {

				// Verificando se está ativa/inativa.
				if (Pessoa.Situacao.DISABLED.equals(pessoaSearch.getSituacao())) {
					throw new ValidationException("consultarCpf", "CPF já associado a um " + pessoaSearch.getTipoPessoa().toString().toLowerCase() + " inativa. Para ativá-lo entre em contato com o suporte.");

				} else if (Pessoa.TipoPessoa.PESSOA_FISICA.equals(pessoaSearch.getTipoPessoa())) {
					throw new ValidationException("consultarCpf", "CPF já cadastrado.");
				}
				// Apagar CPF.
				pessoaFisicaTO.setDocumento(null);

				throw new ValidationException("consultarCpf", "Este CPF está associado a um " + pessoaSearch.getTipoPessoa().toString().toLowerCase() + ". Por Favor, entre em contato com o administrador do sistema.");

			}
		}

		// Obter pesquisa anteriormente feito com o CPF informado.
		LogPesquisa logPesquisa = logPesquisaRepository.findByDocumento(GlobalBean.formatarCPFComPonto(pessoaFisicaTO.getDocumento()));

		PessoaFisicaNFe pessoaFisicaNFe = null;

		// se existir consulta anterior.
		if (logPesquisa != null) {

			Calendar calendar = Calendar.getInstance();
			String LOG_CONSULTAS_DIAS_EXPIRACAO = "log.consultas.dias.expiracao";
			calendar.add(Calendar.DAY_OF_MONTH, -(new Integer(parametroRepository.findByChave(LOG_CONSULTAS_DIAS_EXPIRACAO).getValor())));

			// Se a data da última pesquisa for anterior a 7 dias da data atual,
			// fazer nova pesquisa.
			if (logPesquisa.getDataPesquisa().before(calendar.getTime())) {
				pessoaFisicaNFe = consultarCpfReceitaFederalSoaWebService(pessoaFisicaTO, pessoaTO, logPesquisa);
			}
			// Se não, usar a pesquisa já existente.
			pessoaFisicaNFe = (PessoaFisicaNFe) GlobalBean.converterXMLParaObjecto(PessoaFisicaNFe.class, logPesquisa.getXmlRetorno());
		} else {
			// Não existindo pesquisa, fazer nova consulta.
			pessoaFisicaNFe = consultarCpfReceitaFederalSoaWebService(pessoaFisicaTO, pessoaTO, null);
		}

		String CPF_NAO_ENCONTRADO_RFB = "CPF não existe na base até o momento!";
		String CNPJ_NAO_ENCONTRADO_TESTES = "Este documento nao pode ser consultado em ambiente de testes";
		if (CNPJ_NAO_ENCONTRADO_TESTES.equals(pessoaFisicaNFe.getMensagem())) {
			throw new ValidationException("consultarCpf", "Este documento não pode ser consultado em ambiente de testes");
		} else if (CPF_NAO_ENCONTRADO_RFB.equals(pessoaFisicaNFe.getMensagem())) {
			throw new ValidationException("consultarCpf", "CPF não encontrado na base de dados da Receita Federal.");
		} else if ("BAIXADA".equalsIgnoreCase(pessoaFisicaNFe.getSituacaoRFB())) {
			throw new ValidationException("consultarCpf", "CPF não consta como ativo na base de dados da Receita Federal.");
		} else if (!pessoaFisicaNFe.isStatus()) {
			throw new ValidationException("consultarCpf", "Erro de conexão, login, ou dados.");
		} else {
			pessoaFisicaTO = map(ajustarPessoaJuridicaCpf(map(pessoaFisicaTO), pessoaFisicaNFe));
		}

		return pessoaFisicaTO;
	}

	public PessoaFisicaNFe consultarCpfReceitaFederalSoaWebService(PessoaFisicaTO pessoaFisica, PessoaTO pessoaTO, LogPesquisa logPesquisa) throws RemoteException, JAXBException {
		if (pessoaFisica == null) {
			throw new IllegalArgumentException("Pessoa Física não pode ser nula.");
		}

		return consultarCpfReceitaFederalSoaWebService(GlobalBean.formatarCPFComPonto(pessoaFisica.getDocumento()), pessoaTO == null ? null : getMapper().map(pessoaTO, Colaborador.class), logPesquisa);
	}

	public PessoaFisicaNFe consultarCpfReceitaFederalSoaWebService(String documento, Colaborador colaborador, LogPesquisa logPesquisa) throws JAXBException {
		if (documento == null) {
			throw new IllegalArgumentException("Documento de Pessoa Física não pode ser nula.");
		} else if (documento.isEmpty()) {
			throw new IllegalArgumentException("Documento não pode ser vazio.");
		}

		GlobalBean globalBean = new GlobalBean();
		if (!globalBean.isValidCPF(documento)) {
			throw new IllegalArgumentException("CPF inválido.");
		}

		Credenciais credenciais = new Credenciais(parametroRepository.findByChave(SERASA_CREDENCIAIS_EMAIL).getValor(), parametroRepository.findByChave(SERASA_CREDENCIAIS_SENHA).getValor(), documento);

		PessoaFisicaNFe pessoaFisicaNFe = new PessoaFisicaNFe();
		try {
			pessoaFisicaNFe = consultarCpfReceitaFederalJSONWebService(documento);
		} catch (Exception e) {
			if (e.getMessage().equals("Failed : HTTP error code : 302")) {
				pessoaFisicaNFe.setDocumento(documento);
				pessoaFisicaNFe.setStatus(false);
			}
		}

		LogPesquisa logConsultado = monterLogPesquisa(colaborador, TipoServicoPesquisa.SERASA_Experian, pessoaFisicaNFe.isStatus() == false ? LogPesquisa.SituacaoPesquisa.ERRO : LogPesquisa.SituacaoPesquisa.SUCESSO, documento, new Date(),
			GlobalBean.converterObjetoParaXML(credenciais), String.valueOf(pessoaFisicaNFe.isStatus()), GlobalBean.converterObjetoParaXML(pessoaFisicaNFe));

		// Se a pesquisa existir anteriormente, atualizar a mesma.
		if (logPesquisa != null) {
			logPesquisa.setDataPesquisa(new Date());
			logPesquisa.setSolicitante(colaborador);
			logPesquisa.setXmlEnvio(GlobalBean.converterObjetoParaXML(credenciais));
			logPesquisa.setResultado(String.valueOf(pessoaFisicaNFe.isStatus()));
			logPesquisa.setXmlRetorno(GlobalBean.converterObjetoParaXML(pessoaFisicaNFe));
			logPesquisaRepository.save(logPesquisa);
		} else {
			// Se não, criando novo log de pesquisa.
			logPesquisaRepository.save(logConsultado);
		}

		return pessoaFisicaNFe;
	}

	public LogPesquisa monterLogPesquisa(Colaborador colaborador, TipoServicoPesquisa tipoServicoPesquisa, LogPesquisa.SituacaoPesquisa situacao, String documento, Date dataPesquisa, String xmlEnvio, String resultado, String xmlRetorno) {
		return new LogPesquisa(colaborador, tipoServicoPesquisa, situacao, documento, dataPesquisa, xmlEnvio, resultado, xmlRetorno);
	}

	private PessoaFisicaNFe consultarCpfReceitaFederalJSONWebService(String documento) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String SERASA_CPF_URL_WEBSERVICE_JSON = "serasa.servico.pesquisa.pf.json.url";
			HttpPost postRequest = new HttpPost(parametroRepository.findByChave(SERASA_CPF_URL_WEBSERVICE_JSON).getValor().trim());

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

			return objectMapper.readValue(json.toString(), PessoaFisicaNFe.class);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * Ajustar dados da {@link PessoaJuridica} com os dados obtidos no serviço.
	 *
	 * @param pessoaFisica    A {@link PessoaJuridica} a ser ajustada.
	 * @param pessoaFisicaNFe Representa o XML obtido na consulta ao serviço.
	 * @return A {@link PessoaJuridica} atualizada.
	 */
	private PessoaFisica ajustarPessoaJuridicaCpf(PessoaFisica pessoaFisica, PessoaFisicaNFe pessoaFisicaNFe) {
		pessoaFisica.setNome(pessoaFisicaNFe.getNome());
		pessoaFisica.setSituacao(Pessoa.Situacao.ENABLED);
		pessoaFisica.setTipoPessoaFisica(PessoaFisica.TipoPessoaFisica.CLIENTE);

		// ENDERECO
		if (pessoaFisicaNFe.getEnderecos() != null && pessoaFisicaNFe.getEnderecos().length > 0) {
			br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.Endereco enderecoPessoaFisicaNFe = pessoaFisicaNFe.getEnderecos()[0];
			pessoaFisica.getEndereco().setTipoLogradouro(obterTipoLogradouro(enderecoPessoaFisicaNFe.getLogradouro()));
			pessoaFisica.getEndereco().setEndereco(removerTipoLogradouro(enderecoPessoaFisicaNFe.getLogradouro()));
			pessoaFisica.getEndereco().setNumero(enderecoPessoaFisicaNFe.getNumero());
			pessoaFisica.getEndereco().setComplemento(enderecoPessoaFisicaNFe.getComplemento());
			pessoaFisica.getEndereco().setBairro(enderecoPessoaFisicaNFe.getBairro());
			ajustarCidadecpf(pessoaFisica, enderecoPessoaFisicaNFe);
			pessoaFisica.getEndereco().setCep(enderecoPessoaFisicaNFe.getCEP());
			// enderecoNFe.getGeoLocalizacao() - Obter localização.
			pessoaFisica.getEndereco().setDataAtualizacao(enderecoPessoaFisicaNFe.getDataAtualizacao() != null ? enderecoPessoaFisicaNFe.getDataAtualizacao() : new Date(System.currentTimeMillis()));
		} else {
			pessoaFisica.setEndereco(null);
		}

		pessoaFisica.setHomologado(Boolean.TRUE);
		pessoaFisica.setHomologador(colaboradorRepository.findById(Long.parseLong(parametroRepository.findByChave(USUARIO_HOMOLOGADOR).getValor())).orElse(null));
		pessoaFisica.setDataHomologacao(new Date());

		return pessoaFisica;
	}

	/**
	 * Obter o {@link TipoLogradouro} a partir das informações de endereço do XML obtido do serviço.
	 *
	 * @param enderecoNFe O endereço obtido da pesquisa.
	 * @return O {@link TipoLogradouro}.
	 */
	private TipoLogradouro obterTipoLogradouro(String enderecoNFe) {
		TipoLogradouro tipoLogradouro = null;
		for (String value : enderecoNFe.split(" ")) {
			tipoLogradouro = findNameTipoLogradouroByValue(value);
			if (tipoLogradouro != null) {
				return tipoLogradouro;
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

	public TipoLogradouro findNameTipoLogradouroByValue(String nome) {
		return tipoLogradouroRepository.findByNome(nome);
	}

	public TipoLogradouro findTipoLogradouroByNome(String nome) {
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
	 * @param pessoaFisica
	 * @param enderecoNFe  O código IBGE.
	 * @return A cidade associada ao código IBGE.
	 */
	private void ajustarCidadecpf(PessoaFisica pessoaFisica, br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.Endereco enderecoNFe) {
		Cidade cidade = cidadeRepository.findByCodigo((long) enderecoNFe.getCodigoIBGE());
		if (cidade == null) {
			// Se não achar pelo código, procurar pelo nome.
			cidade = findCidadeByNomeUFNormalized(enderecoNFe.getCidade(), enderecoNFe.getEstado());

		}

		if (cidade != null) {
			pessoaFisica.getEndereco().setCidade(cidade);
			pessoaFisica.getEndereco().setUf(cidade.getUf());
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

	@Override
	public String removerAcento(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	@Override
	public List<PessoaFisicaTO> buscarPessoasFisicas(PessoaFisicaTO pessoaFisicaTO) {
		try {
			return buscarListaPessoaFisica(pessoaFisicaTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Busca uma lista de PessoaFisica, usando como critério para pesquisa os atributos da PessoaFisicaTO passado pelo parametro.
	 *
	 * @param pessoaFisicaTO
	 * @return
	 */
	@Override
	public List<PessoaFisicaTO> buscarListaPessoaFisica(PessoaFisicaTO pessoaFisicaTO) {
		QBean qBean = Projections.bean(
			PessoaFisica.class,
			qPessoaFisica.codigo,
			qPessoaFisica.nome
		);

		JPAQuery<PessoaFisica> jpaQuery = select();
		BooleanBuilder builder = criterioParaBuscarPessoaFisica(pessoaFisicaTO);

		jpaQuery
			.select(qBean)
			.from(qPessoaFisica)
			.where(builder)
			.orderBy(qPessoaFisica.nome.asc());

		List<PessoaFisica> lista = new ArrayList<>();
		lista = jpaQuery.fetch();

		return map(lista);
	}

	private BooleanBuilder criterioParaBuscarPessoaFisica(PessoaFisicaTO to) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(to.getNome())) {
			builder.and(qPessoaFisica.nome.upper().contains(to.getNome().toUpperCase()));
		}

		return builder;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String nomeUsuarioPorLogin(String login) {
		if (login == null) {
			return "Não informado";
		}
		QBean qBeanPessoaFisica = Projections.bean(PessoaFisica.class, qPessoaFisica.codigo, qPessoaFisica.nome);
		QUsuario qUsuario = QUsuario.usuario;
		JPAQuery<PessoaFisica> jpaQuery = select();
		jpaQuery.select(qBeanPessoaFisica).from(qPessoaFisica).leftJoin(qPessoaFisica.usuario, qUsuario).where(qUsuario.login.eq(login));
		List<PessoaFisica> list = jpaQuery.fetch();

		if (list != null && !list.isEmpty()) {
			return list.get(0).getNome();
		} else {
			return login;
		}
	}

	@Override
	public String nomeDoCriador(String login) {
		QBean<PessoaFisica> qBean = Projections.fields(
			PessoaFisica.class,
			QPessoaFisica.pessoaFisica.nome
		);
		JPAQuery<PessoaFisica> pessoaJPAQuery = select();
		pessoaJPAQuery.where(QPessoaFisica.pessoaFisica.usuario.login.eq(login));
		return pessoaJPAQuery.fetchFirst().getNome();
	}

	@Override
	public JPAQuery<PessoaFisica> jpaQueryPessoaFisica() {
		return select();
	}
}
