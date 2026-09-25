package br.com.araujoabreu.sgi.service.impl.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroEndereco;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiroEndereco;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroEnderecoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroEnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroEnderecoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QTipoLogradouro;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroEnderecoRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CentroFinanceiroEnderecoServiceImpl extends BaseSearchService<CentroFinanceiroEndereco, CentroFinanceiroEnderecoTO, CentroFinanceiroEnderecoPesquisaTO, CentroFinanceiroEnderecoRepository> implements CentroFinanceiroEnderecoService {

	private static final QCentroFinanceiroEndereco query = QCentroFinanceiroEndereco.centroFinanceiroEndereco;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	public CentroFinanceiroEnderecoServiceImpl(CentroFinanceiroEnderecoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CentroFinanceiroEnderecoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCentroFinanceiro()) && Check.isAssigned(exampleObj.getCentroFinanceiro().getCodigo())) {
			builder.and(query.centroFinanceiro.codigo.eq(exampleObj.getCentroFinanceiro().getCodigo()));
		}
	}

	private List<CentroFinanceiroEndereco> getListaByCentroFinanceiro(Long codCentroFinanceiro) {
		QEndereco qEndereco = QEndereco.endereco1;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		QBean qBean = Projections.bean(
			CentroFinanceiroEndereco.class,
			query.codigo,
			Projections.bean(Endereco.class, qEndereco.codigo).as("endereco")
		);

		JPAQuery<CentroFinanceiroEndereco> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(query)
			.join(query.endereco, qEndereco)
			.join(query.centroFinanceiro, qCentroFinanceiro)
			.where(qCentroFinanceiro.codigo.eq(codCentroFinanceiro));

		return jpaQuery.fetch();
	}

	@Override
	public List<CentroFinanceiroEnderecoTO> buscarTelefones(CentroFinanceiroEnderecoTO centroFinanceiroEnderecoTO) {
		return buscarListaCentroFinanceiroEndereco(centroFinanceiroEnderecoTO);
	}

	@Override
	public List<CentroFinanceiroEnderecoTO> buscarListaCentroFinanceiroEndereco(CentroFinanceiroEnderecoTO centroFinanceiroEnderecoTO) {
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QEndereco qEndereco = QEndereco.endereco1;
		QCidade qCidade = QCidade.cidade;
		QTipoLogradouro qTipoLogradouro = QTipoLogradouro.tipoLogradouro;

		QBean qBean = Projections.bean(
			CentroFinanceiroEndereco.class,
			query.codigo,
			query.tipo,
			query.principal,
			Projections.bean(
				Endereco.class,
				qEndereco.codigo,
				qEndereco.principal,
				qEndereco.endereco,
				qEndereco.tipoLogradouro,
				qEndereco.numero,
				qEndereco.complemento,
				qEndereco.cep,
				qEndereco.bairro,
				Projections.bean(
					Cidade.class,
					qCidade.codigo,
					qCidade.nome,
					qCidade.uf
				).as("cidade")
			).as("endereco")
		);

		JPAQuery<CentroFinanceiroEndereco> jpaQuery = select();
		BooleanBuilder builder = filtroParaBuscarCentroFinanceiroEndereco(centroFinanceiroEnderecoTO);

		jpaQuery
			.select(qBean)
			.from(query)
			.join(query.centroFinanceiro, qCentroFinanceiro)
			.join(query.endereco, qEndereco)
			.leftJoin(qEndereco.cidade, qCidade)
			.leftJoin(qEndereco.tipoLogradouro, qTipoLogradouro)
			.where(builder);

		List<CentroFinanceiroEndereco> lista = new ArrayList<>();
		lista = jpaQuery.fetch();

		return map(lista);
	}

	private BooleanBuilder filtroParaBuscarCentroFinanceiroEndereco(CentroFinanceiroEnderecoTO to) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(to.getCentroFinanceiro()) && Check.isAssigned(to.getCentroFinanceiro().getCodigo())) {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			builder.and(qCentroFinanceiro.codigo.eq(to.getCentroFinanceiro().getCodigo()));
		}

		return builder;
	}

	@Override
	protected void doBeforeSave(CentroFinanceiroEndereco entity, CentroFinanceiroEnderecoTO object) throws ServiceException {
		if (object.getEndereco() != null) {

			if (object.getEndereco().getCodigo() == null) {
				Endereco endereco = enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class));
				entity.setEndereco(endereco);
			} else {
				entity.setEndereco(enderecoRepository.findById(object.getEndereco().getCodigo()).orElse(null));
			}

			entity.getEndereco().setDataAtualizacao(new Date());
			entity.getEndereco().setAtualizador(usuarioSecurityService.getUsername());
		}
	}

	@Override
	protected void doBeforeUpdate(CentroFinanceiroEndereco entity, CentroFinanceiroEnderecoTO object) throws ServiceException {
		if (object.getEndereco() != null) {
			entity.setEndereco(enderecoRepository.findById(object.getEndereco().getCodigo()).orElse(null));
			entity.getEndereco().setDataAtualizacao(new Date());
			entity.getEndereco().setAtualizador(usuarioSecurityService.getUsername());
		}
	}
}
