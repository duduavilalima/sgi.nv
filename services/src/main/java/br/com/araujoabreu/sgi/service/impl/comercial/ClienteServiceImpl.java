package br.com.araujoabreu.sgi.service.impl.comercial;

import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.QContrato;
import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.comercial.domain.ClassificacaoCliente;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.QPessoaContaContabil;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.comercial.ClienteService;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClassificacaoClienteTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClientePesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClienteSimpleTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClienteTO;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.ContratoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoSimpleTO;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilTO;
import br.com.araujoabreu.sgi.service.telefone.TelefoneService;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Contato;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.RelacionamentoContato;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.administracao.TipoLogradouroRepository;
import br.com.fasolti.sgi.repository.comercial.ClassificacaoClienteRepository;
import br.com.fasolti.sgi.repository.comercial.ClienteRepository;
import br.com.fasolti.sgi.repository.contato.ContatoRepository;
import br.com.fasolti.sgi.repository.contato.RelacionamentoContatoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoRepository;
import br.com.fasolti.sgi.repository.pessoaContaContabil.PessoaContaContabilRepository;
import br.com.fasolti.sgi.repository.telefone.OperadoraRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteServiceImpl extends BaseSearchService<Pessoa, ClienteTO, ClientePesquisaTO, ClienteRepository> implements ClienteService {
	private static final QPessoa qClientes = QPessoa.pessoa;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private PessoaContaContabilRepository pessoaContaContabilRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private ContatoRepository contatoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ClassificacaoClienteRepository classificacaoClienteRepository;

	@Autowired
	private RelacionamentoContatoRepository relacionamentoContatoRepository;

	@Autowired
	private OperadoraRepository operadoraRepository;

	@Autowired
	private TipoLogradouroRepository tipoLogradouroRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private SecurityService<?> securityService;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private ContratoService contratoService;

	@Autowired
	public ClienteServiceImpl(ClienteRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Pessoa> jpaQuery, ClientePesquisaTO exampleObj) {
		jpaQuery.orderBy(qClientes.nome.asc());

		QEndereco qEndereco = QEndereco.endereco1;
		QCidade qCidade = QCidade.cidade;

		jpaQuery
			.join(qClientes.endereco, qEndereco)
			.join(qEndereco.cidade, qCidade);

		if (Check.isAssigned(exampleObj.getConta())) {
			QPessoaContaContabil qPessoaContaContabil = QPessoaContaContabil.pessoaContaContabil;
			QContaContabil qContaContabil = QContaContabil.contaContabil;
			QUnidade qUnidade = QUnidade.unidade;

			jpaQuery
				.leftJoin(qClientes.listaContaContabil, qPessoaContaContabil)
				.leftJoin(qPessoaContaContabil.contaContabil, qContaContabil)
				.leftJoin(qContaContabil.empresa, qUnidade);

		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ClientePesquisaTO formaObj) {
		QPessoaJuridica qPessoaJuridica = qClientes.as(QPessoaJuridica.class);
		QPessoaFisica qPessoaFisica = qClientes.as(QPessoaFisica.class);
		builder.andAnyOf(
			qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.CLIENTE),
			qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE),
			qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.SUBCLIENTE),
			qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.CLIENTE),
			qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.FORNECEDOR_CLIENTE),
			qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.USUARIO_CLIENTE),
			qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.USUARIO_FORNECEDOR_CLIENTE)
		);

		if (Check.isAssigned(formaObj.getDocumento())) {
			builder.and(qClientes.documento.containsIgnoreCase(formaObj.getDocumento()));
		}

		if (Check.isAssigned(formaObj.getSituacao())) {
			builder.and(qClientes.situacao.eq((Pessoa.Situacao) Util.getEnumByString(Pessoa.Situacao.class, formaObj.getSituacao())));
		}

		if (Check.isAssigned(formaObj.getNome())) {
			builder.andAnyOf(
				qClientes.nome.containsIgnoreCase(formaObj.getNome()),
				qPessoaJuridica.nomeFantasia.containsIgnoreCase(formaObj.getNome())
			);
		}

		if (Check.isAssigned(formaObj.getRamoNegocio())) {
			builder.andAnyOf(
				qPessoaJuridica.cnae.sigla.containsIgnoreCase(formaObj.getRamoNegocio().getSigla()),
				qPessoaJuridica.cnae.descricao.containsIgnoreCase(formaObj.getRamoNegocio().getDescricao())
			);
		}

		if (Check.isAssigned(formaObj.getCidade())) {
			builder.and(QCidade.cidade.nome.containsIgnoreCase(formaObj.getCidade().getNome()));
		}

		if (Check.isAssigned(formaObj.getTipo())) {
			if (formaObj.getTipo().compareTo("Cliente") == 0) {
				builder.andAnyOf(
					qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.CLIENTE),
					qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE),
					qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.CLIENTE),
					qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.FORNECEDOR_CLIENTE),
					qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.USUARIO_CLIENTE),
					qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.USUARIO_FORNECEDOR_CLIENTE)
				);

			}

			if (formaObj.getTipo().compareTo("... Somente cliente") == 0) {
				builder.andAnyOf(
					qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.CLIENTE),
					qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.CLIENTE)
				);

			}
			if (formaObj.getTipo().compareTo("... Fornecedor/Cliente") == 0) {
				builder.andAnyOf(
					qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE),
					qPessoaFisica.tipoPessoaFisica.eq(PessoaFisica.TipoPessoaFisica.FORNECEDOR_CLIENTE)
				);

			}
			if (formaObj.getTipo().compareTo("Subcliente") == 0) {
				builder.andAnyOf(
					qPessoaJuridica.tipoPessoaJuridica.eq(PessoaJuridica.TipoPessoaJuridica.SUBCLIENTE)
				);

			}
		}

		if (Check.isAssigned(formaObj.getClassificacaoCliente()) && Check.isAssigned(formaObj.getClassificacaoCliente().getCodigo())) {
			builder.and(qPessoaFisica.classificacaoCliente.codigo.eq(formaObj.getClassificacaoCliente().getCodigo()));
		}

		if (Check.isAssigned(formaObj.getFaturamentoCobranca())) {
			builder.and(qPessoaFisica.faturamentoCobranca.eq(formaObj.getFaturamentoCobranca()));
		}

		if (Check.isAssigned(formaObj.getPossuiContrato())) {
			JPAQuery jpaQueryContrato = contratoService.getJpaQuery();
			jpaQueryContrato.select(QContrato.contrato.cliente.codigo).from(QContrato.contrato).distinct();

			if (formaObj.getPossuiContrato()) {
				builder.and(qPessoaFisica.codigo.in(jpaQueryContrato));
			} else {
				builder.and(qPessoaFisica.codigo.notIn(jpaQueryContrato));
			}
		}

		if (Check.isAssigned(formaObj.getConta())) {
			QPessoaContaContabil qPessoaContaContabil = QPessoaContaContabil.pessoaContaContabil;
			QUnidade qUnidade = QUnidade.unidade;

			switch (formaObj.getConta()) {
				case 1:
					builder.and(
						qPessoaContaContabil.tipoEspecial.eq(PessoaContaContabil.TipoRelacionamento.CLIENTE)
							.and(qUnidade.matriz.codigo.eq(1l))
					);
					break;
				case 4:
					builder.and(
						qPessoaContaContabil.tipoEspecial.eq(PessoaContaContabil.TipoRelacionamento.CLIENTE)
							.and(qUnidade.matriz.codigo.eq(4l))
					);
					break;

				case 5:
					builder.and(
						qPessoaContaContabil.tipoEspecial.eq(PessoaContaContabil.TipoRelacionamento.CLIENTE)
							.and(qUnidade.matriz.codigo.eq(1l).or(qUnidade.matriz.codigo.eq(4l)))
					);
					break;

				case 7:
					builder.and(
						qPessoaContaContabil.tipoEspecial.eq(PessoaContaContabil.TipoRelacionamento.CLIENTE)
						.and(qPessoaContaContabil.codigo.isNull().or(qClientes.codigo.notIn(

							select(QPessoa.pessoa.codigo)
								.from(QPessoa.pessoa)
								.leftJoin(QPessoa.pessoa.listaContaContabil, QPessoaContaContabil.pessoaContaContabil)
								.leftJoin(QPessoaContaContabil.pessoaContaContabil.contaContabil, QContaContabil.contaContabil)
								.leftJoin(QContaContabil.contaContabil.empresa, QUnidade.unidade)
							.where(qUnidade.matriz.codigo.eq(1l))

						)  )  )
					);
					break;

				case 8:
					builder.and(
						qPessoaContaContabil.tipoEspecial.eq(PessoaContaContabil.TipoRelacionamento.CLIENTE)
							.and(qPessoaContaContabil.codigo.isNull().or(qClientes.codigo.notIn(

								select(QPessoa.pessoa.codigo)
									.from(QPessoa.pessoa)
									.leftJoin(QPessoa.pessoa.listaContaContabil, QPessoaContaContabil.pessoaContaContabil)
									.leftJoin(QPessoaContaContabil.pessoaContaContabil.contaContabil, QContaContabil.contaContabil)
									.leftJoin(QContaContabil.contaContabil.empresa, QUnidade.unidade)
									.where(qUnidade.matriz.codigo.eq(4l))

							)  )  )
					);
					break;

				case 9:
					builder.and(qPessoaContaContabil.codigo.isNull());
					break;

				default:
					break;

			}
		}
	}

	@Override
	protected void doAfterSearch(List<Pessoa> entities, List<ClientePesquisaTO> result) throws ServiceException {
		for (ClientePesquisaTO to : result) {

			if (Pessoa.TipoPessoa.PESSOA_JURIDICA.toString().equals(to.getTipoPessoa())) {

				JPAQuery<PessoaJuridica> jpaQueryPessoaJuridica = pessoaJuridicaService.jpaQueryPessoaJuridica();

				PessoaJuridica pessoaJuridica = jpaQueryPessoaJuridica
					.from(QPessoaJuridica.pessoaJuridica)
					.select(Projections.fields(PessoaJuridica.class, QPessoaJuridica.pessoaJuridica.codigo, QPessoaJuridica.pessoaJuridica.tipoPessoaJuridica))
					.where(QPessoaJuridica.pessoaJuridica.codigo.eq(to.getCodigo()))
					.fetchOne();
				to.setTipo(pessoaJuridica.getTipoPessoaJuridica().getNome());
			}

			if (Pessoa.TipoPessoa.PESSOA_FISICA.toString().equals(to.getTipoPessoa())) {
				JPAQuery<PessoaFisica> jpaQueryPessoaFisica = pessoaFisicaService.jpaQueryPessoaFisica();

				PessoaFisica pessoaFisica = jpaQueryPessoaFisica
					.from(QPessoaFisica.pessoaFisica)
					.select(Projections.fields(PessoaFisica.class, QPessoaFisica.pessoaFisica.codigo, QPessoaFisica.pessoaFisica.tipoPessoaFisica))
					.where(QPessoaFisica.pessoaFisica.codigo.eq(to.getCodigo()))
					.fetchOne();
				to.setTipo(pessoaFisica.getTipoPessoaFisica().getNome());
			}

		}
	}

	@Override
	protected void doAfterLoad(ClienteTO transferObj) {
		if (transferObj != null && transferObj.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getCriador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeCriador(list.get(0).getNome());
			}else{
				transferObj.setNomeCriador(transferObj.getCriador());
			}
		}

		if (transferObj != null && transferObj.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getAtualizador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeAtualizador(list.get(0).getNome());
			}else{
				transferObj.setNomeCriador(transferObj.getAtualizador());
			}

		}

		if (transferObj != null && transferObj.getInativador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getInativador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeInativador(list.get(0).getNome());
			}else{
				transferObj.setNomeCriador(transferObj.getInativador());
			}
		}
		//FIXME QUANDO OBJETO É UMA PESSOA FISICA ESTA ADICIONANDO NOME EM NOME FANTASIA
		if (Pessoa.TipoPessoa.PESSOA_FISICA.toString().equals(transferObj.getTipoPessoa())) {
			transferObj.setNomeFantasia(null);
		}
	}

	/*Carregar Abas de Cliente*/
	@Override
	public List<ContratoSimpleTO> buscarContratos(String codigoCliente) {

		List<Contrato> contratos = contratoRepository.findByClienteCodigo(new Long(codigoCliente));

		List<ContratoSimpleTO> listaTO = new ArrayList<>();

		if (contratos != null && !contratos.isEmpty()) {
			for (Contrato ct : contratos) {
				listaTO.add(getMapper().map(ct, ContratoSimpleTO.class));
			}
		}

		return listaTO;
	}

	@Override
	public List<ClienteSimpleTO> buscarSubclientes(String codigoCliente) {

		List<Pessoa> subclientes = pessoaRepository.findByClientePrincipalCodigo(new Long(codigoCliente));

		List<ClienteSimpleTO> listaTO = new ArrayList<>();

		if (subclientes != null && !subclientes.isEmpty()) {
			for (Pessoa ct : subclientes) {
				listaTO.add(getMapper().map(ct, ClienteSimpleTO.class));
			}
		}

		return listaTO;
	}

	@Override
	public List<PessoaContaContabilTO> buscarDadosContabeis(String codigoCliente) {

		List<PessoaContaContabil.TipoRelacionamento> listaTipoPessoaConta = new ArrayList<>();
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FATURAMENTO_CONTRATUAL);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FATURAMENTO_EXTRA_CONTRATUAL);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FATURAMENTO_RECEBER);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.CLIENTE);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FUNDO_FIXO);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.ADIANTAMENTO);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.REEMBOLSO);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.EMPRESTIMO);

		List<PessoaContaContabil> contasContabeis = pessoaContaContabilRepository.findByPessoaCodigoAndTipoEspecialIn(new Long(codigoCliente), listaTipoPessoaConta);

		List<PessoaContaContabilTO> listaTO = new ArrayList<>();

		if (contasContabeis != null && !contasContabeis.isEmpty()) {
			for (PessoaContaContabil pc : contasContabeis) {
				listaTO.add(getMapper().map(pc, PessoaContaContabilTO.class));
			}
		}

		return listaTO;
	}

	@Override
	public List<TelefoneTO> buscarTelefones(String codigoCliente) {

		List<Telefone> telefones = telefoneRepository.findByPessoaCodigo(new Long(codigoCliente));

		List<TelefoneTO> listaTO = new ArrayList<>();

		if (telefones != null && !telefones.isEmpty()) {
			for (Telefone t : telefones) {
				listaTO.add(getMapper().map(t, TelefoneTO.class));
			}
		}

		return listaTO;
	}

	/**
	 * FIXME: Foi feito uma refatoração neste método porque quando era feito o mapeamento de uma objeto Contato para ContatoTO, com os telefone já inclusos antes do mapeamento,
	 * ocorria um erro no frontend. A solução foi separar os mapeamentos do Contato e do Telefone. Juntando os dois quando eles já estavam no formato TO.
	 *
	 * @param codigoCliente
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ContatoTO> buscarContatos(String codigoCliente) {
		List<Contato> contatos = contatoRepository.findByPessoaCodigo(new Long(codigoCliente));

		List<ContatoTO> listaTO = new ArrayList<>();

		if (contatos != null && !contatos.isEmpty()) {
			for (Contato contato : contatos) {

				ContatoTO contatoTO = getMapper().map(contato, ContatoTO.class);

				try {
					TelefoneTO filtroTelefone = new TelefoneTO();
					filtroTelefone.setContato(contatoTO);
					List<TelefoneTO> listaTelefoneTO = telefoneService.buscarTelefones(filtroTelefone);

					if (listaTelefoneTO != null && !listaTelefoneTO.isEmpty()) {
						contatoTO.setTelefone1(listaTelefoneTO.get(0));
						if (listaTelefoneTO.size() > 1) {
							contatoTO.setTelefone2(listaTelefoneTO.get(1));
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				listaTO.add(contatoTO);
			}
		}

		return listaTO;
	}

	/*Remover Cliente*/
	private void prepareToRemove(Long id) {
		List<Contrato> contratos = contratoRepository.findByClienteCodigo(id);
		List<Pessoa> subclientes = pessoaRepository.findByClientePrincipalCodigo(id);

		if (contratos != null && !contratos.isEmpty()) {
			throw new ValidationException("ValidationException","Este cliente não pode ser removido, pois está associado a contrato(s).");
		} else if (subclientes != null && !subclientes.isEmpty()) {
			throw new ValidationException("ValidationException","Este cliente não pode ser removido.");
		}

	}

	private void removeLists(Long id, Pessoa.TipoPessoa tipoPessoa, Boolean removerTodasListas) {
		if (removerTodasListas) {
			/*Lista Contatos*/
			if (Pessoa.TipoPessoa.PESSOA_JURIDICA.equals(tipoPessoa)) {
				List<Contato> contatos = contatoRepository.findByPessoaCodigo(id);
				List<Telefone> listaTelContatos = new ArrayList<>();
				if (contatos != null && !contatos.isEmpty()) {
					for (Contato contato : contatos) {
						listaTelContatos = telefoneRepository.findByContatoCodigo(contato.getCodigo());
						if (listaTelContatos != null && !listaTelContatos.isEmpty()) {
							for (Telefone telC : listaTelContatos) {
								telefoneRepository.delete(telC);
							}
						}
						contatoRepository.delete(contato);
					}
				}
			}

			/*Lista Telefones*/
			List<Telefone> listaTelefones = telefoneRepository.findByPessoaCodigo(id);
			if (listaTelefones != null && !listaTelefones.isEmpty()) {
				for (Telefone tel : listaTelefones) {
					telefoneRepository.delete(tel);
				}
			}
		}

		/*Lista PessoaContaContabil*/
		List<PessoaContaContabil.TipoRelacionamento> listaTipoPessoaConta = new ArrayList<>();
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FATURAMENTO_CONTRATUAL);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FATURAMENTO_EXTRA_CONTRATUAL);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.FATURAMENTO_RECEBER);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.CLIENTE);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.ADIANTAMENTO);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.REEMBOLSO);
		listaTipoPessoaConta.add(PessoaContaContabil.TipoRelacionamento.EMPRESTIMO);

		List<PessoaContaContabil> contasContabeis = pessoaContaContabilRepository.findByPessoaCodigoAndTipoEspecialIn(id, listaTipoPessoaConta);

		if (contasContabeis != null && !contasContabeis.isEmpty()) {
			for (PessoaContaContabil pc : contasContabeis) {
				pessoaContaContabilRepository.deleteById(pc.getCodigo());
			}
		}
	}

	@Override
	@Transactional
	public void removerCliente(Long id) throws Exception {
		removeCliente(id);
	}

	@Transactional
	public void removeCliente(Long id) throws Exception {
		Pessoa cliente = pessoaRepository.findByCodigo(id);
		prepareToRemove(cliente.getCodigo());

		/*Cliente Pessoa Física*/
		if (cliente instanceof PessoaFisica) {
			if (PessoaFisica.TipoPessoaFisica.CLIENTE.equals(((PessoaFisica) cliente).getTipoPessoaFisica())) {
				removeLists(id, cliente.getTipoPessoa(), Boolean.TRUE);
				pessoaRepository.deleteById(cliente.getCodigo());
				enderecoRepository.delete(cliente.getEndereco());

			} else {
				switch (((PessoaFisica) cliente).getTipoPessoaFisica()) {

					case FORNECEDOR_CLIENTE:
						removeLists(id, cliente.getTipoPessoa(), Boolean.FALSE);
						((PessoaFisica) cliente).setTipoPessoaFisica(PessoaFisica.TipoPessoaFisica.FORNECEDOR);
						pessoaRepository.save(cliente);

					case USUARIO_CLIENTE:
						removeLists(id, cliente.getTipoPessoa(), Boolean.FALSE);
						((PessoaFisica) cliente).setTipoPessoaFisica(PessoaFisica.TipoPessoaFisica.USUARIO);
						pessoaRepository.save(cliente);

					case USUARIO_FORNECEDOR_CLIENTE:
						removeLists(id, cliente.getTipoPessoa(), Boolean.FALSE);
						((PessoaFisica) cliente).setTipoPessoaFisica(PessoaFisica.TipoPessoaFisica.USUARIO_FORNECEDOR);
						pessoaRepository.save(cliente);

					default:
						throw new ValidationException("ValidationException","Este cliente não pode ser removido.");
				}
			}
			/*Cliente Pessoa Jurídica*/
		} else if (cliente instanceof PessoaJuridica) {
			if (PessoaJuridica.TipoPessoaJuridica.CLIENTE.equals(((PessoaJuridica) cliente).getTipoPessoaJuridica())
				|| PessoaJuridica.TipoPessoaJuridica.SUBCLIENTE.equals(((PessoaJuridica) cliente).getTipoPessoaJuridica())) {
				removeLists(id, cliente.getTipoPessoa(), Boolean.TRUE);
				pessoaRepository.deleteById(cliente.getCodigo());
				enderecoRepository.delete(cliente.getEndereco());

			} else if (PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE.equals(((PessoaJuridica) cliente).getTipoPessoaJuridica())) {
				removeLists(id, cliente.getTipoPessoa(), Boolean.FALSE);
				((PessoaJuridica) cliente).setTipoPessoaJuridica(PessoaJuridica.TipoPessoaJuridica.FORNECEDOR);
				pessoaRepository.save(cliente);

			} else {
				throw new ValidationException("ValidationException","Este cliente não pode ser removido.");
			}
		} else {
			throw new ValidationException("ValidationException","Este cliente não pode ser removido.");
		}
	}//FIM REMOVER CLIENTE

	/*Carregar Combos de Cliente*/
	@Override
	public List<ClassificacaoClienteTO> buscarClassificacaoCliente() {
		List<ClassificacaoCliente> classificacao = classificacaoClienteRepository.findAll();

		List<ClassificacaoClienteTO> listaTO = new ArrayList<>();

		if (classificacao != null && !classificacao.isEmpty()) {
			for (ClassificacaoCliente clCl : classificacao) {
				listaTO.add(getMapper().map(clCl, ClassificacaoClienteTO.class));
			}
		}
		return listaTO;
	}

	@Override
	public List<RelacionamentoContatoTO> buscarRelacionamento() {

		List<RelacionamentoContato> relacionamento = (List<RelacionamentoContato>) relacionamentoContatoRepository.findAll();

		List<RelacionamentoContatoTO> listaTO = new ArrayList<>();

		listaTO.add(null);
		if (relacionamento != null && !relacionamento.isEmpty()) {
			for (RelacionamentoContato rela : relacionamento) {
				listaTO.add(getMapper().map(rela, RelacionamentoContatoTO.class));
			}
		}
		return listaTO;
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

	/**
	 * Verificar se o documento CNPJ é de um fornecedor já cadastrado.
	 *
	 * @param clienteTO O Cliente com o documento preeenchido.
	 * @return <code>TRUE</code> já é um fornecedor já cadastrado. <code>FALSE</code> não é cadastrado.
	 */
	@Override
	@Transactional()
	public ValueTO consultarDocumentoPopup(ClienteTO clienteTO) {
		return pessoaJuridicaService.verificarCnpjClientePopup(getMapper().map(clienteTO, PessoaJuridicaTO.class));
	}

	/*Consultar Documento na Base da Dados*/
	@Override
	@Transactional()
	public Object consultarDocumento(ClienteTO clienteTO) throws ValidationException {
		try {

			Usuario usuario = (Usuario) securityService.getUser();
			Pessoa pessoa = pessoaRepository.findByCodigo(usuario.getCodigo());

			if (Pessoa.TipoPessoa.PESSOA_FISICA.toString().equals(clienteTO.getTipoPessoa())) {
				return pessoaFisicaService.consultarCpf(
					getMapper().map(clienteTO, PessoaFisicaTO.class),
					pessoa == null ? null : getMapper().map(pessoa, PessoaTO.class),
					Boolean.FALSE);
			} else {
				return pessoaJuridicaService.consultarCnpj(
					getMapper().map(clienteTO, PessoaJuridicaTO.class),
					pessoa == null ? null : getMapper().map(pessoa, PessoaTO.class),
					Boolean.FALSE);
			}
		} catch (ValidationException e) {
			throw new ValidationException("ValidationException",e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*CONVERTER FORNECEDOR*/
	@Override
	public ClienteTO converterFornecedor(ClienteTO clienteTO) {
		String aux = clienteTO.getDocumento();
		String documento = String.format("%s.%s.%s/%s-%s", aux.substring(0, 2), aux.substring(2, 5), aux.substring(5, 8), aux.substring(8, 12), aux.substring(12, 14));

		PessoaJuridica pj = pessoaJuridicaRepository.findByDocumento(documento);
		if (pj == null) {
			pj = pessoaJuridicaRepository.findByDocumento(clienteTO.getDocumento());
		}
		pj.setTipoPessoaJuridica(PessoaJuridica.TipoPessoaJuridica.FORNECEDOR_CLIENTE);

		ClienteTO clienteTForn = getMapper().map(pj, ClienteTO.class);

		clienteTForn.setTelefones(new ArrayList<>());

		pj.getTelefones().forEach(telefone -> clienteTForn.getTelefones().add(getMapper().map(telefone, TelefoneTO.class)));

		return clienteTForn;
	}
}
