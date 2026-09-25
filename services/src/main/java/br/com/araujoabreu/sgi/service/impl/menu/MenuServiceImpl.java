package br.com.araujoabreu.sgi.service.impl.menu;

import br.com.araujoabreu.sgi.service.menu.MenuService;
import br.com.araujoabreu.sgi.service.menu.transfer.ModuloTO;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioTO;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.security.domain.Funcionalidade;
import br.com.fasolti.corporativo.security.domain.Modulo;
import br.com.fasolti.corporativo.security.domain.Permissao;
import br.com.fasolti.corporativo.security.domain.QFuncionalidade;
import br.com.fasolti.corporativo.security.domain.QModulo;
import br.com.fasolti.corporativo.security.domain.QPermissao;
import br.com.fasolti.corporativo.security.domain.QReceptor;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsável por montar o menu da aplicação.
 */
@Service
public class MenuServiceImpl implements MenuService {

	private final Long SISTEMA_GESTAO_INTEGRADA_V2 = 2L;
	@Autowired
	protected Mapper mapper;
	private final QModulo qModulo = QModulo.modulo1;
	private final QFuncionalidade qFuncionalidade = QFuncionalidade.funcionalidade;
	private final QPermissao qPermissao = QPermissao.permissao;
	@Autowired
	private DozerHelper dozerHelper;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Obter a lista de itens do menu.
	 *
	 * @return A lista de itens do menu.
	 */
	@Override
	public List<ModuloTO> getMenu() {
		// Obter os múdulos PAI da aplicação.
		List<Modulo> listModulo = getModulosPais();

		// Preencher os sub-itens do menu.
		preencherFilhos(listModulo);

		// Adicionar módulos com filhos apenas.
		List<Modulo> apenasComFilhos = listModulo.stream()
			.filter(modulo -> !modulo.getChildren().isEmpty())
			.collect(Collectors.toList());

		return mapModulo(apenasComFilhos);
	}

	private void preencherFilhos(List<Modulo> listModulo) {
		listModulo.forEach(modulo -> {
			modulo.setModulos(getModulosFilhos(modulo.getCodigo()));
			modulo.setFuncionalidades(getFuncionalidades(modulo.getCodigo()));
		});
	}

	private List<Funcionalidade> getFuncionalidades(Long codigoPai) {
		if (codigoPai == null) {
			return null;
		}
		List<Funcionalidade> funcionalidades = selectFuncionalidade(
			Projections.fields(Funcionalidade.class,
				qFuncionalidade.codigo,
				qFuncionalidade.nome,
				qFuncionalidade.url,
				qFuncionalidade.ordem))
			.where(qFuncionalidade.modulo.codigo.eq(codigoPai))
			.fetch();

		Set<Funcionalidade> funcionalidadePermitidas = new HashSet<>();

		funcionalidades.forEach(funcionalidade -> getUser().getPerfis().forEach(perfilTO -> {
			long count = selectPermissao(
				Projections.fields(Permissao.class, qPermissao.codigo))
				.innerJoin(qPermissao.receptor, QReceptor.receptor)
				.where(
					qPermissao.alvo.equalsIgnoreCase(funcionalidade.getLink())
						.and(QReceptor.receptor.codigo.eq(perfilTO.getCodigo()))
				)
				.fetchCount();
			if (count > 0) {
				funcionalidadePermitidas.add(funcionalidade);
			}
		}));

		return new ArrayList<>(funcionalidadePermitidas);
	}

	private List<Modulo> getModulosFilhos(Long codigoPai) {
		if (codigoPai == null) {
			return null;
		}
		List<Modulo> filhos = selectModulo(
			Projections.fields(Modulo.class,
				qModulo.codigo,
				qModulo.nome,
				qModulo.status,
				qModulo.contexto,
				qModulo.ordem))
			.where(qModulo.modulo.codigo.eq(codigoPai)
				.and(qModulo.status.eq(Modulo.Status.INSTALADO)
					.and(qModulo.sistema.codigo.eq(SISTEMA_GESTAO_INTEGRADA_V2)))
				.and(qModulo.funcionalidades.isNotEmpty().or(qModulo.modulos.isNotEmpty())))
			.orderBy(qModulo.ordem.asc())
			.fetch();

		preencherFilhos(filhos);

		return filhos.stream()
			.filter(modulo -> !modulo.getFuncionalidades().isEmpty())
			.collect(Collectors.toList());
	}

	private List<Modulo> getModulosPais() {
		return selectModulo(
			Projections.fields(Modulo.class,
				qModulo.codigo,
				qModulo.nome,
				qModulo.status,
				qModulo.contexto,
				qModulo.ordem)
		)
			.where(qModulo.modulo.isNull()
				.and(qModulo.status.eq(Modulo.Status.INSTALADO)
					.and(qModulo.sistema.codigo.eq(SISTEMA_GESTAO_INTEGRADA_V2)))
				.and(qModulo.funcionalidades.isNotEmpty().or(qModulo.modulos.isNotEmpty())))
			.orderBy(qModulo.ordem.asc())
			.fetch();
	}

	private JPAQuery<Modulo> selectModulo() {
		return new JPAQueryFactory(entityManager).selectFrom(qModulo);
	}

	private JPAQuery<Modulo> selectModulo(Expression<Modulo> expr) {
		return selectModulo().select(expr);
	}

	private JPAQuery<Funcionalidade> selectFuncionalidade() {
		return new JPAQueryFactory(entityManager).selectFrom(qFuncionalidade);
	}

	private JPAQuery<Funcionalidade> selectFuncionalidade(Expression<Funcionalidade> expr) {
		return selectFuncionalidade().select(expr);
	}

	private JPAQuery<Permissao> selectPermissao() {
		return new JPAQueryFactory(entityManager).selectFrom(qPermissao);
	}

	private JPAQuery<Permissao> selectPermissao(Expression<Permissao> expr) {
		return selectPermissao().select(expr);
	}

	private List<ModuloTO> mapModulo(Iterable<Modulo> iterable) {
		return dozerHelper.map(iterable, ModuloTO.class);
	}

	private UsuarioTO getUser() {
		return usuarioService.getUsuarioLogado();
	}

}
