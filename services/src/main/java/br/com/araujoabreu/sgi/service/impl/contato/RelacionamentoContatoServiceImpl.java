package br.com.araujoabreu.sgi.service.impl.contato;

import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.contato.RelacionamentoContatoService;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.QRelacionamentoContato;
import br.com.fasolti.corporativo.pessoa.domain.RelacionamentoContato;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.contato.RelacionamentoContatoRepository;
import br.com.fasolti.sgi.repository.security.PerfilRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RelacionamentoContatoServiceImpl
	extends BaseSearchService<RelacionamentoContato, RelacionamentoContatoTO, RelacionamentoContatoPesquisaTO, RelacionamentoContatoRepository>
	implements RelacionamentoContatoService {

	private static final QRelacionamentoContato qRelacionamentoContato = QRelacionamentoContato.relacionamentoContato;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	public RelacionamentoContatoServiceImpl(RelacionamentoContatoRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(RelacionamentoContato entity, RelacionamentoContatoTO transferObj) {
		prepareValidateToSave(transferObj);

		if (entity.getPerfil() != null && entity.getPerfil().getCodigo() != null) {
			Perfil perfil = perfilRepository.findById(entity.getPerfil().getCodigo()).orElse(null);
			Objects.requireNonNull(perfil);
			entity.setPerfil(perfil);
		} else {
			entity.setPerfil(null);
		}
	}

	@Override
	protected void doBeforeDelete(RelacionamentoContatoTO object) throws ServiceException {
		if (object == null)
			throw new ValidationException("RelacionamentoContatoServiceImpl", "O ID do relacionamento de contato não pode ser nulo.");
		if (object.getId() <= 0)
			throw new ValidationException("RelacionamentoContatoServiceImpl", "O ID do relacionamento de contato não é válido.");
	}

	@Override
	protected void doBeforeUpdate(RelacionamentoContato entity, RelacionamentoContatoTO faturamentoEtapaTO) throws ServiceException {
		if (faturamentoEtapaTO.getCodigo() == null)
			throw new ValidationException("RelacionamentoContatoServiceImpl", "Código obrigatório!");
		prepareValidateToSave(faturamentoEtapaTO);
	}

	@Override
	protected void doAfterLoad(RelacionamentoContatoTO object) throws ServiceException {
		if (object != null && object.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(object.getCriador());
			if (list != null && !list.isEmpty()) {
				object.setNomeCriador(list.get(0).getNome());
			}
		}

		if (object != null && object.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(object.getAtualizador());
			if (list != null && !list.isEmpty()) {
				object.setNomeAtualizador(list.get(0).getNome());
			}
		}
	}

	public void prepareValidateToSave(RelacionamentoContatoTO transfer) throws ServiceException {

		if (transfer == null)
			throw new ValidationException("RelacionamentoContatoServiceImpl", "O relacionamento de contato não pode ser nulo.");
		if (transfer.getNome() == null)
			throw new ValidationException("RelacionamentoContatoServiceImpl", "O nome do relacionamento de contato não pode ser nulo.");
		if (transfer.getNome().isEmpty())
			throw new ValidationException("RelacionamentoContatoServiceImpl", "O nome do relacionamento de contato não pode ser vazio.");
		if (transfer.getNome().length() > 50)
			throw new ValidationException("RelacionamentoContatoServiceImpl", "O nome do relacionamento de contato é muito grande.");

		// Opcionais
		if (transfer.getPerfil() != null) {
			if (transfer.getPerfil().getCodigo() == null)
				throw new ValidationException("RelacionamentoContatoServiceImpl", "O código do perfil do relacionamento de contato não pode ser nulo.");
			if (transfer.getPerfil().getCodigo() <= 0)
				throw new ValidationException("RelacionamentoContatoServiceImpl", "O código do perfil do relacionamento de contato é inválido.");
		}
	}

	@Override
	protected void buildSearchQuery(JPAQuery<RelacionamentoContato> jpaQuery, RelacionamentoContatoPesquisaTO exampleObj) {
		jpaQuery.orderBy(qRelacionamentoContato.codigo.asc());

		jpaQuery.select(Projections.fields(
			RelacionamentoContato.class,
			qRelacionamentoContato.codigo,
			qRelacionamentoContato.nome,
			qRelacionamentoContato.perfil
		));
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, RelacionamentoContatoPesquisaTO exampleObj) {
		RelacionamentoContato entidade = getMapper().map(exampleObj, RelacionamentoContato.class);

		if (Check.isAssigned(entidade.getCodigo())) builder.and(qRelacionamentoContato.codigo.eq(entidade.getCodigo()));
		if (Check.isAssigned(entidade.getNome()))
			builder.and(qRelacionamentoContato.nome.upper().contains(entidade.getNome().toUpperCase()));
		if (Check.isAssigned(entidade.getPerfil()) && Check.isAssigned(entidade.getPerfil().getCodigo())) {
			builder.and(qRelacionamentoContato.perfil.codigo.eq(entidade.getPerfil().getCodigo()));
		}
	}

	@Override
	public List<RelacionamentoContatoTO> buscarTodos() {
		List<RelacionamentoContato> relacionamento = (List<RelacionamentoContato>) getRepository().findAll();

		List<RelacionamentoContatoTO> listaTO = new ArrayList<>();

		if (relacionamento != null && !relacionamento.isEmpty()) {
			for (RelacionamentoContato rela : relacionamento) {
				listaTO.add(getMapper().map(rela, RelacionamentoContatoTO.class));
			}
		}
		return listaTO;
	}

	@Override
	protected void buildLoadQuery(JPAQuery<RelacionamentoContato> jpaQuery) {
		jpaQuery.select(
			Projections.fields(RelacionamentoContato.class,
				qRelacionamentoContato.codigo,
				qRelacionamentoContato.nome,
				qRelacionamentoContato.perfil,
				qRelacionamentoContato.criador,
				qRelacionamentoContato.dataCriacao,
				qRelacionamentoContato.atualizador,
				qRelacionamentoContato.dataAtualizacao)
		);
	}
}
