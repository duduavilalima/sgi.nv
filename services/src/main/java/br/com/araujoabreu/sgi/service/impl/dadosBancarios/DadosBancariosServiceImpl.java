package br.com.araujoabreu.sgi.service.impl.dadosBancarios;

import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.dadosBancarios.DadosBancariosService;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosPesquisaTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.corporativo.pessoa.domain.QDadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.tesouraria.DadosBancariosRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional()
public class DadosBancariosServiceImpl extends BaseSearchService<DadosBancarios, DadosBancariosTO, DadosBancariosPesquisaTO, DadosBancariosRepository> implements DadosBancariosService {

	private static final QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public DadosBancariosServiceImpl(DadosBancariosRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<DadosBancarios> jpaQuery, DadosBancariosPesquisaTO exampleObj) {
		QBanco qBanco = QBanco.banco;
		QPessoa qPessoa = QPessoa.pessoa;
		QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);

		QBean qBean = Projections.fields(
			DadosBancarios.class,
			Projections.fields(PessoaJuridica.class,
				qPessoaJuridica.codigo,
				qPessoaJuridica.nome,
				qPessoaJuridica.nomeFantasia,
				qPessoaJuridica.documento).as("pessoa"),
			Projections.fields(Banco.class,
				qBanco.codigo,
				qBanco.nome).as("banco"),
			qDadosBancarios.codigo,
			qDadosBancarios.agencia,
			qDadosBancarios.digitoAgencia,
			qDadosBancarios.conta,
			qDadosBancarios.digito,
			qDadosBancarios.principal,
			qDadosBancarios.homologado,
			qDadosBancarios.dataHomologacao,
			qDadosBancarios.tipoHomologacao,
			qDadosBancarios.tipoConta,
			qDadosBancarios.situacao,
			Projections.fields(Pessoa.class,
				qPessoa.codigo,
				qPessoa.nome,
				qPessoa.documento).as("responsavelInativacao"),
			qDadosBancarios.dataInativacao
		);

		jpaQuery = select(qBean);
		jpaQuery.join(QDadosBancarios.dadosBancarios.pessoa, qPessoa);
		jpaQuery.join(QDadosBancarios.dadosBancarios.banco, qBanco);
		jpaQuery.leftJoin(QDadosBancarios.dadosBancarios.responsavelInativacao, qPessoa);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, DadosBancariosPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getPessoa())) {
			builder.and(qDadosBancarios.pessoa.codigo.eq(exampleObj.getPessoa().getCodigo()));
		}
	}

	@Transactional
	@Override
	public DadosBancariosTO restHomologarDadosBancariosManual(DadosBancariosTO dbto) {
		updateHomologado(dbto.getCodigo(), Boolean.TRUE);
		updateHomologador(dbto.getCodigo(), securityService.getUser().getCodigo());
		updateTipoHomologacao(dbto.getCodigo(), DadosBancarios.TipoHomologacaoDadosBancarios.HOMOLOG_MANUAL);
		updateDataHomologacao(dbto.getCodigo());

		pagamentoService.updateDadosBancarios(dbto.getCodigoPagamento(), dbto.getCodigo());

		return dbto;
	}

	public long updateHomologado(Long codigo, Boolean isHomologado) {
		return selectUpdate()
			.where(qDadosBancarios.codigo.eq(codigo))
			.set(qDadosBancarios.homologado, isHomologado)
			.execute();
	}

	public long updateHomologador(Long codigo, Long homologadorCodigo) {
		return selectUpdate(qDadosBancarios.codigo.eq(codigo))
			.set(qDadosBancarios.homologador, colaboradorRepository.findById(homologadorCodigo).orElse(null))
			.execute();
	}

	public long updateTipoHomologacao(Long codigo, DadosBancarios.TipoHomologacaoDadosBancarios tipoHomologacaoDadosBancarios) {
		return selectUpdate(qDadosBancarios.codigo.eq(codigo))
			.set(qDadosBancarios.tipoHomologacao, tipoHomologacaoDadosBancarios)
			.execute();
	}

	public long updateDataHomologacao(Long codigo) {
		return selectUpdate(qDadosBancarios.codigo.eq(codigo))
			.set(qDadosBancarios.dataHomologacao, new Date())
			.execute();
	}

	@Override
	public List<DadosBancariosTO> getDadosBancariosPessoa(Long idPessoa) {
		return obterDadosBancariosPessoaCodigo(idPessoa);
	}

	@Override
	public List<DadosBancariosTO> obterDadosBancariosPessoaCodigo(Long codigoPessoao) {
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;
		QPessoa qPessoa = QPessoa.pessoa;

		JPAQuery<DadosBancarios> jpaQuery = select();
		jpaQuery.join(QDadosBancarios.dadosBancarios.pessoa, qPessoa);
		jpaQuery.join(QDadosBancarios.dadosBancarios.banco, qBanco);
		jpaQuery.leftJoin(QDadosBancarios.dadosBancarios.responsavelInativacao, qPessoa);

		QBean qBean = Projections.bean(
			DadosBancarios.class,
			Projections.bean(PessoaJuridica.class, qDadosBancarios.pessoa.codigo, qDadosBancarios.pessoa.nome, qDadosBancarios.pessoa.as(QPessoaJuridica.class).nomeFantasia, qDadosBancarios.pessoa.documento).as("pessoa"),
			Projections.bean(Banco.class, qBanco.codigo, qBanco.nome).as("banco"),
			qDadosBancarios.codigo,
			qDadosBancarios.agencia,
			qDadosBancarios.digitoAgencia,
			qDadosBancarios.conta,
			qDadosBancarios.digito,
			qDadosBancarios.principal,
			qDadosBancarios.homologado,
			qDadosBancarios.dataHomologacao,
			qDadosBancarios.tipoHomologacao,
			qDadosBancarios.tipoConta,
			qDadosBancarios.situacao,
			Projections.bean(Pessoa.class, qDadosBancarios.responsavelInativacao.codigo, qDadosBancarios.responsavelInativacao.nome).as("responsavelInativacao"),
			qDadosBancarios.dataInativacao
		);

		jpaQuery.where(new BooleanBuilder(qDadosBancarios.pessoa.codigo.eq(codigoPessoao))).select(qBean);
		return map(jpaQuery.fetch());
	}

	@Override
	public DadosBancariosTO obterDadosBancarioCodigoComBanco(Long codigoDadosBancarios) {
		JPAQuery<DadosBancarios> jpaQuery = select();
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		jpaQuery.from(qDadosBancarios);
		jpaQuery.where(qDadosBancarios.codigo.eq(codigoDadosBancarios));
		jpaQuery.select(Projections.fields(DadosBancarios.class, qDadosBancarios.codigo, Projections.fields(Banco.class, qDadosBancarios.banco.codigo, qDadosBancarios.banco.nome).as("banco")));

		return map(jpaQuery.fetchOne());
	}

	@Override
	public DadosBancariosTO obterDadosBancarioSimples(Long codigoDadosBancarios) {
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;
		DadosBancarios dadosBancarios = select()
			.from(qDadosBancarios)
			.select(Projections.bean(
				DadosBancarios.class,
				qDadosBancarios.codigo,
				qDadosBancarios.agencia,
				qDadosBancarios.digitoAgencia,
				qDadosBancarios.conta,
				qDadosBancarios.digito,
				qDadosBancarios.principal,
				qDadosBancarios.dataHomologacao,
				qDadosBancarios.tipoHomologacao,
				qDadosBancarios.tipoConta,
				qDadosBancarios.situacao,
				Projections.bean(
					Banco.class,
					qBanco.nome,
					qBanco.codigo
				).as("banco"))
			).where(qDadosBancarios.codigo.eq(codigoDadosBancarios))
			.fetchOne();

		return map(dadosBancarios);
	}

	@Override
	public List<DadosBancariosTO> obterListaDadosBancarioSimplesPeloCodigoCredor(Long codigoCredor) {
		QBanco qBanco = QBanco.banco;

		JPAQuery jpaQueryDadosBancarios = select().from(qDadosBancarios);

		jpaQueryDadosBancarios.from(qDadosBancarios).join(qDadosBancarios.banco, qBanco);

		QBean qBean = Projections.bean(
			DadosBancarios.class,
			qDadosBancarios.codigo,
			qDadosBancarios.agencia,
			qDadosBancarios.digitoAgencia,
			qDadosBancarios.conta,
			qDadosBancarios.digito,
			qDadosBancarios.principal,
			qDadosBancarios.dataHomologacao,
			qDadosBancarios.tipoHomologacao,
			qDadosBancarios.tipoConta,
			Projections.bean(
				Banco.class,
				qBanco.nome,
				qBanco.codigo
			).as("banco")
		);

		jpaQueryDadosBancarios.select(qBean).where(qDadosBancarios.pessoa.codigo.eq(codigoCredor));
		List<DadosBancarios> dadosBancarios = jpaQueryDadosBancarios.fetch();

		return map(dadosBancarios);
	}

	@Override
	public JPAQuery jpaQueryDadosBancarios() {
		return select();
	}

}
