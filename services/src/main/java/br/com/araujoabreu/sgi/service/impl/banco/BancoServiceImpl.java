package br.com.araujoabreu.sgi.service.impl.banco;

import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.banco.BancoService;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoPesquisaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.DadosBancariosService;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.sgi.repository.banco.BancoRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.DadosBancariosRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class BancoServiceImpl implements BancoService {
	private static final QBanco qBanco = QBanco.banco;

	protected final PathBuilder<Banco> pathBuilder;
	protected final EntityPath<Banco> entityPath;
	@Autowired
	protected Mapper mapper;
	@Autowired
	private SecurityService<?> securityService;
	@Autowired
	private DozerHelper dozerHelper;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private BancoRepository bancoRepository;

	@Autowired
	private DadosBancariosService dadosBancariosService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemRelacaoRepository itemRelacaoRepository;

	@Autowired
	private DadosBancariosRepository dadosBancariosRepository;

	public BancoServiceImpl() {
		this.pathBuilder = new PathBuilder<>(Banco.class, Util.varName(Banco.class));
		this.entityPath = this.pathBuilder;
	}

	protected JPAQuery<Banco> select() {
		return new JPAQueryFactory(entityManager).selectFrom(qBanco);
	}

	protected JPAQuery<Banco> select(Expression<Banco> expr) {
		return new JPAQueryFactory(entityManager).selectFrom(qBanco).select(expr);
	}

	protected BancoTO map(Banco entity) {
		if (entity == null) return null;
		return mapper.map(entity, BancoTO.class);
	}

	protected Banco map(BancoTO object) {
		if (object == null) return null;
		return mapper.map(object, Banco.class);
	}

	protected Banco map(BancoPesquisaTO object) {
		if (object == null) return null;
		return mapper.map(object, Banco.class);
	}

	protected Banco merge(BancoTO object, Banco entity) {
		if (object == null) return entity;
		mapper.map(object, entity);
		return entity;
	}

	protected List<BancoTO> map(Iterable<Banco> iterable) {
		return dozerHelper.map(iterable, BancoTO.class);
	}

	@Override
	public List<BancoTO> buscaTodosOrdenado() {
		return map(bancoRepository.findAllByOrderByNome());
	}

	protected Predicate createSearchPredicate(BancoPesquisaTO exampleObj) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (exampleObj.getCodigo() != null) {
			booleanBuilder.and(qBanco.codigo.eq(exampleObj.getCodigo()));
		}

		if (exampleObj.getNome() != null) {
			booleanBuilder.and(qBanco.nome.containsIgnoreCase(exampleObj.getNome()));
		}

		if (exampleObj.getDescricao() != null) {
			booleanBuilder.and(qBanco.descricao.containsIgnoreCase(exampleObj.getDescricao()));
		}

		if (exampleObj.getSituacao() != null) {
			if (Situacao.INATIVO.getDescricao().equals(exampleObj.getSituacao())) {
				booleanBuilder.and(qBanco.situacao.eq(Situacao.INATIVO));
			} else {
				booleanBuilder.and(qBanco.situacao.eq(Situacao.ATIVO));
			}
		}

		return booleanBuilder;
	}

	public BancoTO obterbancoPeloCodigoDadosBancarios(Long codigoDadosBancarios) {
		if (codigoDadosBancarios == null || codigoDadosBancarios <= 0) {
			return null;
		}
		DadosBancariosTO dadosBancarios = dadosBancariosService.obterDadosBancarioCodigoComBanco(codigoDadosBancarios);
		if (dadosBancarios != null) return dadosBancarios.getBanco();
		return null;
	}

	@Override
	public BancoTO obterBancoSimpleTelaNotaFiscal(Long codigoBanco) {

		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Banco.class,
				qBanco.codigo,
				qBanco.nome)
		);
		jpaQuery.where(qBanco.codigo.eq(codigoBanco));
		return map((Banco) jpaQuery.fetchOne());
	}

	@Override
	public JPAQuery<?> obterBancoDeContaCorrente(Long codigoContaCorrente) {
		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;

		return select(
			Projections.fields(Banco.class,
				qBanco.codigo,
				qBanco.nome)

		).from(qContaCorrente)
			.innerJoin(qContaCorrente.banco, qBanco)
			.where(qContaCorrente.codigo.eq(codigoContaCorrente));
	}

	protected Predicate createPredicate(BancoTO exampleObj) {
		return null;
	}

	@Override
	public Iterable<BancoTO> buscaPorExemploMethod(BancoTO exampleObj) {
		JPAQuery select = new JPAQueryFactory(this.entityManager).selectFrom(qBanco);

		if (exampleObj != null)
			select.where(createPredicate(exampleObj));

		Iterable<Banco> entityList = select.fetch();

		return map(entityList);
	}

	protected JPAQuery<Banco> createJPAQuery(BancoPesquisaTO exampleObj) {
		return null;
	}

	private OrderSpecifier createOrderBy(Sort sort) {
		if (sort != null) {
			Sort.Order sortOrder = sort.iterator().next();
			PathBuilder<Banco> builder = new PathBuilder<>(this.entityPath.getType(), this.entityPath.getMetadata());
			return new OrderSpecifier(sortOrder.isAscending() ? Order.ASC : Order.DESC, builder.getString(sortOrder.getProperty()));
		}

		return null;
	}

	protected OrderSpecifier createOrderBy(BancoPesquisaTO exampleObj) {
		return null;
	}

	protected QBean createQBean() {
		return null;
	}

	protected Page<BancoPesquisaTO> doAfterSearch(Page<BancoPesquisaTO> page) {
		return page;
	}

	protected Page<BancoPesquisaTO> convertSearchPage(Page<Banco> page, Pageable pageable) {
		List<BancoPesquisaTO> newContent = dozerHelper.map(page.getContent(), BancoPesquisaTO.class);
		return new PageImpl<>(newContent, pageable, page.getTotalElements());
	}

	@Override
	public Page<BancoPesquisaTO> buscaPorExemploMethod(BancoPesquisaTO exampleObj, Integer page, String sortDir, Integer pageSize, String sortField, String sortFieldString) {
		BooleanBuilder b = new BooleanBuilder();

		if (exampleObj != null)
			b.and(createSearchPredicate(exampleObj));

		Sort sort = null;
		if (sortField != null) {
			Sort.Direction dir = Sort.Direction.fromOptionalString(sortDir).orElse(null);
			Sort.Order order = new Sort.Order(dir, sortField);
			if (sortFieldString != null && Boolean.parseBoolean(sortFieldString))
				order = new Sort.Order(dir, sortField).ignoreCase();

			sort = Sort.by(order);
		}

		Pageable pageRequest = PageRequest.of(page, pageSize, sort);
		Page<Banco> entityList;

		JPAQuery<Banco> jpaQuery = createJPAQuery(exampleObj);

		if (jpaQuery == null) {
			entityList = bancoRepository.findAll(b.getValue(), pageRequest);
		} else {
			Iterable<Banco> resultado;
			OrderSpecifier order = this.createOrderBy(exampleObj); //permite ao backend criar uma ordenação específica

			long count = jpaQuery.where(b).fetchCount();
			QBean qBean = createQBean();

			jpaQuery.where(b);

			if (qBean != null) {
				jpaQuery.select(qBean);
			}

			if (sort == null && order != null) {
				jpaQuery.orderBy(order);
			}

			resultado = jpaQuery.offset(page * pageSize).limit(pageSize).fetch();

			entityList = new PageImpl<>((List<Banco>) resultado, pageRequest, count);
		}

		return doAfterSearch(convertSearchPage(entityList, pageRequest));
	}

	@Override
	public BancoTO buscaPorIdMethod(String id) {
		Banco entity = select().where(qBanco.codigo.eq(Long.parseLong(id))).fetchOne();
		BancoTO transferObj = map(entity);
		doAfterLoad(transferObj);
		return transferObj;
	}

	protected BancoTO doBeforeCreate(Class<BancoTO> toClass) {
		return null;
	}

	protected void doAfterCreate(BancoTO object) {
	}

	@Override
	public BancoTO createMethod() {
		try {
			BancoTO objectTO = doBeforeCreate(BancoTO.class);

			if (objectTO == null) {
				objectTO = BancoTO.class.newInstance();
			}

			doAfterCreate(objectTO);

			return objectTO;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public BancoTO saveMethod(BancoTO transferObj) {
		try {
			Banco entity = map(transferObj);
			doBeforeSave(entity, transferObj);
			entity = bancoRepository.save(entity);
			BancoTO to = (map(entity));
			doAfterLoad(to);
			return to;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	protected void doBeforeUpdate(Banco entity, BancoTO object) {
		long countNome = select()
			.where(qBanco.nome.toLowerCase().eq(object.getNome().toLowerCase())
				.and(qBanco.codigo.eq(object.getCodigo()).not()))
			.fetchCount();

		if (countNome > 0) {
			throw new ValidationException("validation", String.format("Já existe banco com o nome %s.", object.getNome()));
		}

		entity.setAtualizador(securityService.getUsername());
		entity.setDataAtualizacao(new Date());
	}

	@Override
	@Transactional
	public BancoTO updateMethod(BancoTO transferObj, Long id) {
		try {
			Banco entity = bancoRepository.findById(id).get();
			if (entity == null) {
				throw new RuntimeException("Entidade com ID " + id + " não encontrada");
			}
			entityManager.clear();
			merge(transferObj, entity);
			doBeforeUpdate(entity, transferObj);
			entity = bancoRepository.save(entity);
			BancoTO to = (map(entity));
			doAfterLoad(to);
			return to;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public void deleteMethod(String id) {
		try {
			doBeforeDelete(id);
			bancoRepository.deleteById(new Long(id));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void doAfterLoad(BancoTO transferObj) {
		// BUSCAR NOME DO CRIADOR
		if (transferObj != null && transferObj.getCriador() != null) {
			String value = pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getCriador());
			transferObj.setCriador(value == null || value.isEmpty() ? transferObj.getCriador() : value);
		}

		// BUSCAR NOME DO ATUALIZADOR
		if (transferObj != null && transferObj.getAtualizador() != null) {
			String value = pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getAtualizador());
			transferObj.setAtualizador(value == null || value.isEmpty() ? transferObj.getAtualizador() : value);
		}
	}

	@Transactional
	protected void doBeforeSave(Banco entity, BancoTO object) {
		long countCodigo = select().where(qBanco.codigo.eq(object.getCodigo())).fetchCount();

		if (countCodigo > 0) {
			throw new ValidationException("validation", String.format("Já existe banco com o código %d.", object.getCodigo()));
		}

		long countNome = select().where(qBanco.nome.toLowerCase().eq(object.getNome().toLowerCase())).fetchCount();

		if (countNome > 0) {
			throw new ValidationException("validation", String.format("Já existe banco com o nome %s.", object.getNome()));
		}

		// Como já vem o código, colocar a data e criado, pois o framework já valida quando não tem código.
		entity.setDataCriacao(new Date());
		entity.setCriador(securityService.getUser().getUsername());
	}

	protected void doBeforeDelete(String id) {
		Long codigo = Long.valueOf(id);
		Long countPagamentos = pagamentoRepository.countByBancoBoletoCodigo(codigo);
		if (countPagamentos > 0) {
			throw new ValidationException("ValidationException",String.format("Não é possível remover este banco. Há pagamentos associados.", countPagamentos));
		}

		Long countItensRelacao = itemRelacaoRepository.countByBancoCodigo(codigo);
		if (countItensRelacao > 0) {
			throw new ValidationException("ValidationException",String.format("Não é possível remover este banco. Há itens de relação bancária associados.", countItensRelacao));
		}

		Long countDadsoBancarios = dadosBancariosRepository.countByBancoCodigo(codigo);
		if (countDadsoBancarios > 0) {
			throw new ValidationException("ValidationException",String.format("Não é possível remover este banco. Há dados bancários associados.", countDadsoBancarios));
		}
	}
}
