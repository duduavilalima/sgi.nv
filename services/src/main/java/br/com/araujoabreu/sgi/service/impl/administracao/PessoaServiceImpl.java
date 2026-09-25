package br.com.araujoabreu.sgi.service.impl.administracao;


import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PessoaServiceImpl extends
	BaseSearchService<
		Pessoa,
		PessoaTO,
		PessoaPesquisaTO,
		PessoaRepository> implements PessoaService {

	private static final QPessoa qPessoa = QPessoa.pessoa;
	private static final Map<String, Pessoa.Situacao> MAPA_SITUACAO = criarMapaSituacao();

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	public PessoaServiceImpl(PessoaRepository repository) {
		super(repository);
	}

	private static Map<String, Pessoa.Situacao> criarMapaSituacao() {
		Map<String, Pessoa.Situacao> mapa = new HashMap<>();
		for (Pessoa.Situacao situacao : Pessoa.Situacao.values()) {
			mapa.put(situacao.getNome(), situacao);
		}
		return mapa;
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, PessoaPesquisaTO exampleObj) {
		QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);

		if (Check.isAssigned(exampleObj.getNome())) {
			builder.and(qPessoaJuridica.nome.containsIgnoreCase(exampleObj.getNome()));
		}

		if (Check.isAssigned(exampleObj.getNomeFantasiaDocumento())) {
			builder.or(qPessoa.nome.lower().containsIgnoreCase(exampleObj.getNomeFantasiaDocumento().toLowerCase()));
			builder.or(qPessoaJuridica.nomeFantasia.lower().containsIgnoreCase(exampleObj.getNomeFantasiaDocumento().toLowerCase()));
			builder.or(qPessoa.documento.lower().containsIgnoreCase(exampleObj.getNomeFantasiaDocumento().toLowerCase()));
		} else if (exampleObj.getDocumento() != null && !exampleObj.getDocumento().trim().isEmpty()) {
			builder.and(qPessoa.documento.lower().containsIgnoreCase(exampleObj.getDocumento().toLowerCase()));
		}

		if (Check.isAssigned(exampleObj.getClientePrincipalTO()) && Check.isAssigned(exampleObj.getClientePrincipalTO().getCodigo())) {
			if (exampleObj.getClientePrincipalTO().getCodigo() == -1) {
				builder.and(qPessoaJuridica.clientePrincipal.codigo.isNull());
			} else {
				builder.and(qPessoaJuridica.clientePrincipal.codigo.eq(exampleObj.getClientePrincipalTO().getCodigo()));
			}
		}

		if (Check.isAssigned(exampleObj.getTipoPessoa())) {
			builder.and(qPessoaJuridica.tipoPessoa.eq((Pessoa.TipoPessoa) Util.getEnumByString(Pessoa.TipoPessoa.class, exampleObj.getTipoPessoa())));
		}

		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qPessoaJuridica.situacao.eq((Pessoa.Situacao) Util.getEnumByString(Pessoa.Situacao.class, exampleObj.getSituacao())));
		}
	}

	@Override
	protected OrderSpecifier defineSearchOrder(PessoaPesquisaTO exampleObj) {
		if (!Check.isAssigned(exampleObj.getOrdenacao()))
			exampleObj.setOrdenacao("nome");

		if (Check.isAssigned(exampleObj.getTipoPessoa()) && exampleObj.getTipoPessoa().equals(Pessoa.TipoPessoa.PESSOA_JURIDICA)) {
			QPessoaJuridica qPessoaJuridica = QPessoaJuridica.pessoaJuridica;

			if (exampleObj.getOrdenacao() != null && exampleObj.getOrdenacao().equals("documento"))
				return (new OrderSpecifier<>(Order.ASC, qPessoaJuridica.documento));

			return (new OrderSpecifier<>(Order.ASC, qPessoaJuridica.nomeFantasia));
		} else if (exampleObj.getOrdenacao().equals("documento"))
			return (new OrderSpecifier<>(Order.ASC, qPessoa.documento));

		return (new OrderSpecifier<>(Order.ASC, qPessoa.nome));
	}

	@Override
	public List<PessoaPesquisaTO> getPessoas(String nome) {
		List<Pessoa> lista = getRepository().findByNomeIgnoreCaseStartingWithOrderByNomeAsc(nome);

		return mapSearch(lista);
	}

	@Override
	public PessoaTO obterPessoa(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery
			.from(QPessoa.pessoa)
			.select(Projections.fields(Pessoa.class, QPessoa.pessoa.codigo, QPessoa.pessoa.nome, QPessoa.pessoa.tipoPessoa, QPessoa.pessoa.documento, QPessoa.pessoa.situacao))
			.where(QPessoa.pessoa.codigo.eq(codigo));

		Pessoa pessoa = (Pessoa) jpaQuery.fetchOne();

		return map(pessoa);
	}

	public Pessoa.Situacao getSituacaoByNome(String nome) {
		return MAPA_SITUACAO.get(nome);
	}

	@Override
	protected void doAfterLoad(PessoaTO transferObj) {
		Endereco endereco = buscarEnderecoDePessoa(transferObj.getCodigo());

		if (endereco != null && endereco.getCodigo() != null) {
			transferObj.setCodigoEndereco(endereco.getCodigo());
		}
	}

	private Endereco buscarEnderecoDePessoa(Long codPessoa) {
		QEndereco qEndereco = QEndereco.endereco1;

		QBean qBean = Projections.bean(
			Pessoa.class,
			qPessoa.codigo,
			Projections.bean(Endereco.class, qEndereco.codigo).as("endereco")
		);

		JPAQuery<Pessoa> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qPessoa).leftJoin(qPessoa.endereco, qEndereco)
			.where(qPessoa.codigo.eq(codPessoa));

		Pessoa pessoa = jpaQuery.fetchOne();

		return pessoa.getEndereco();
	}

	@Override
	public JPAQuery getJpaQuery() {
		return select();
	}

	@Override
	public PessoaTO obterClienteSimpleTelaNotaFiscal(Long codigoCliente) {

		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Pessoa.class,
				qPessoa.codigo,
				qPessoa.nome,
				qPessoa.tipoPessoa,
				Projections.bean(Pessoa.class, qPessoa.clientePrincipal.codigo).as("clientePrincipal")
			)
		);
		jpaQuery.where(qPessoa.codigo.eq(codigoCliente));
		return map((Pessoa) jpaQuery.fetchOne());

	}

	@Override
	public PessoaTO obterClientePrincipalSimpleTelaNotaFiscal(Long codigoClientePrincipal) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(PessoaJuridica.class,
				qPessoa.codigo,
				qPessoa.nome)
		);
		jpaQuery.where(qPessoa.codigo.eq(codigoClientePrincipal));
		return map((PessoaJuridica) jpaQuery.fetchOne());
	}



	@Override
	public List<PessoaTO> buscarNomeFantasiaDocumento(PessoaPesquisaTO exampleObj) {
		JPAQuery jpaQuery = select();
		QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);
		jpaQuery.select(
			Projections.fields(
				PessoaJuridica.class,
				QPessoa.pessoa.codigo,
				QPessoa.pessoa.documento,
				QPessoa.pessoa.nome,
				QPessoa.pessoa.situacao,
				QPessoa.pessoa.tipoPessoa,
				qPessoaJuridica.nomeFantasia)
		);

		BooleanBuilder builder = new BooleanBuilder();
		if (Check.isAssigned(exampleObj.getNomeFantasiaDocumento())) {
			builder.or(QPessoa.pessoa.nome.lower().containsIgnoreCase(exampleObj.getNomeFantasiaDocumento().toLowerCase()));
			builder.or(qPessoaJuridica.nomeFantasia.lower().containsIgnoreCase(exampleObj.getNomeFantasiaDocumento().toLowerCase()));
			builder.or(QPessoa.pessoa.documento.lower().containsIgnoreCase(exampleObj.getNomeFantasiaDocumento().toLowerCase()));
		}
		if (Check.isAssigned(exampleObj.getClientePrincipalTO()) && Check.isAssigned(exampleObj.getClientePrincipalTO().getCodigo())) {
			if (exampleObj.getClientePrincipalTO().getCodigo() == -1) {
				builder.and(qPessoaJuridica.clientePrincipal.codigo.isNull());
			} else {
				builder.and(qPessoaJuridica.clientePrincipal.codigo.eq(exampleObj.getClientePrincipalTO().getCodigo()));
			}
		}

		jpaQuery.where(builder);

		List<PessoaJuridica> listPessoaJuridica = jpaQuery.fetch();
		if (listPessoaJuridica.isEmpty()) {
			return new ArrayList<>();
		} else {
			List<PessoaTO> listPessoaTO = new ArrayList<>();
			for (PessoaJuridica pessoaJuridica : listPessoaJuridica) {
				PessoaTO pessoaTO = new PessoaTO();
				pessoaTO.setCodigo(pessoaJuridica.getCodigo());
				pessoaTO.setDocumento(pessoaJuridica.getDocumento());
				pessoaTO.setNome(pessoaJuridica.getNome());
				pessoaTO.setSituacao(pessoaJuridica.getSituacao().toString());
				pessoaTO.setTipoPessoa(pessoaJuridica.getTipoPessoa().toString());
				pessoaTO.setNomeFantasia(pessoaJuridica.getNomeFantasia());
				listPessoaTO.add(pessoaTO);
			}
			return listPessoaTO;
		}
	}

	@Override
	public PessoaTO buscarClientePrincipalPorCodCliente(Long codigo) {
		QPessoa qCliente = new QPessoa("pessoa");
		QPessoa qClientePrincipal = new QPessoa("clientePrincipal");

		QBean qBean = Projections.bean(Pessoa.class,
			qCliente.codigo,
			qCliente.nome,
			qCliente.documento,
			qCliente.tipoPessoa,
			Projections.bean(Pessoa.class,
				qClientePrincipal.codigo,
				qClientePrincipal.nome,
				qClientePrincipal.documento,
				qClientePrincipal.tipoPessoa
			).as("clientePrincipal")
		);

		JPAQuery jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qCliente).leftJoin(qCliente.clientePrincipal, qClientePrincipal)
			.where(qCliente.codigo.eq(codigo));

		Pessoa pessoa = (Pessoa) jpaQuery.fetchOne();
		Pessoa clientePrincipal = pessoa.getClientePrincipal();

		PessoaTO clientePrincipalTO = null;

		if (clientePrincipal != null && clientePrincipal.getCodigo() != null) {
			clientePrincipalTO = getMapper().map(clientePrincipal, PessoaTO.class);
		}

		return clientePrincipalTO;
	}
}
