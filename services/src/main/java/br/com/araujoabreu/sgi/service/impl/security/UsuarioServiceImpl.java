package br.com.araujoabreu.sgi.service.impl.security;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.ColaboradorCentroFinanceiro;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioPesquisaTO;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.QUsuario;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.security.UsuarioRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UsuarioServiceImpl extends BaseSearchService<Usuario, UsuarioTO, UsuarioPesquisaTO, UsuarioRepository>
	implements UsuarioService, ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private static final QUsuario query = QUsuario.usuario;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, UsuarioPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo()))
			builder.and(query.codigo.eq(exampleObj.getCodigo()));

		if (Check.isAssigned(exampleObj.getLogin()))
			builder.and(query.login.lower().contains(exampleObj.getLogin().toLowerCase()));
	}

	@Override
	protected OrderSpecifier defineSearchOrder(UsuarioPesquisaTO exampleObj) {
		return (new OrderSpecifier<>(Order.ASC, query.login.toLowerCase()));
	}

	@Override
	public UsuarioTO getUsuarioLogado() {
		Usuario usuario = securityService.getUser();

		// Não precisa informar a senha.
		usuario.setSenha(null);


		if (usuario.getLogin() == null) {
			return null;
		}

		UsuarioTO to = map(usuario);

		to.setPessoa(getMapper().map(usuario.getPessoaFisica(), PessoaFisicaTO.class));

		return to;
	}

	@Override
	public Map<String, Boolean> possuiTodosPerfisLista(String[] perfis) {
		Usuario user = securityService.getUser();

		if (Check.isAssigned(perfis) && user != null) {
			for (String perfi : perfis) {
				for (Perfil p : user.getPerfis()) {
					if (p.getNome().equals(perfi) || p.getChave().equals(perfi)) {
						return Collections.singletonMap("possuiTodosPerfisLista", true);
					}
				}
			}
		}

		return Collections.singletonMap("possuiTodosPerfisLista", false);
	}

	@Override
	public Map<String, Boolean> possuiUmPerfilLista(String[] perfis) {
		Usuario user = securityService.getUser();

		if (Check.isAssigned(perfis) && user != null) {
			for (String perfi : perfis) {
				for (Perfil p : user.getPerfis()) {
					if (p.getNome().equals(perfi)) {
						return Collections.singletonMap("possuiUmPerfilLista", true);
					}
				}
			}
		}

		return Collections.singletonMap("possuiUmPerfilLista", false);
	}

	@Override
	public boolean possuiPerfilLista(String[] perfis) {
		Usuario user = securityService.getUser();
		if (Check.isAssigned(perfis) && user != null) {
			for (String perfi : perfis) {
				return user.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(perfi));
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public boolean possuiUmPerfilLista(String perfil) {
		Usuario user = securityService.getUser();
		if (Check.isAssigned(perfil) && user != null) {
				for (Perfil p : user.getPerfis()) {
					if (p.getNome().equals(perfil)) {
						return true;
					}
				}
		}
		return false;
	}

	@Override
	public Map<String, Boolean> apenasFaturamento() {
		final String chaveHabilitarTodasGerencias = "habilitarTodasGerencias";
		final List<String> perfisValue  = Arrays.asList(
			"faturamento-analista",
			"faturamento-gestor");
		final List<String> perfisAtivarTodasGerencias = Arrays.asList(
			"faturamento-analista",
			"faturamento-gestor",
			"faturamento-cns-geral");
		final List<String> perfisDesativarTodasGerencias = Arrays.asList(
			"faturamento-cns-gerencia");

		List<Perfil> perfis = securityService.getUser().getPerfis();
		Map<String, Boolean> map = new HashMap<>();

		Optional<Perfil> perfilValue = perfis.stream()
			.filter(p -> perfisValue.contains(p.getNome()))
			.findFirst();
		Optional<Perfil> perfilAtivarTodasGerencias = perfis.stream()
			.filter(p -> perfisAtivarTodasGerencias.contains(p.getNome()))
			.findFirst();
		Optional<Perfil> perfilDesativarTodasGerencias = perfis.stream()
			.filter(p -> perfisDesativarTodasGerencias.contains(p.getNome()))
			.findFirst();

		map.put("value", perfilValue.isPresent());

		if (perfilAtivarTodasGerencias.isPresent()) {
			map.put(chaveHabilitarTodasGerencias, true);

		} else if (perfilDesativarTodasGerencias.isPresent()) {
			map.put(chaveHabilitarTodasGerencias, false);

		} else  {
			// TODO Ver qual é o padrão
			map.put(chaveHabilitarTodasGerencias, false);
		}

		return map;
	}

	@Override
	public List<CentroFinanceiroTO> getCentroFinanceiros() {
		Usuario usuario = securityService.getUser();

		Optional<Colaborador> colaborador = colaboradorRepository.findById(usuario.getPessoaFisica().getId());

		List<CentroFinanceiroTO> list = new ArrayList<>();

		if (colaborador.isPresent()) {
			List<ColaboradorCentroFinanceiro> colaboradorCentroFinanceiroList = colaborador.get().getCentrosFinanceiros();
			colaboradorCentroFinanceiroList.forEach(ccf -> {
				list.add(getMapper().map(ccf.getCentroFinanceiro(), CentroFinanceiroTO.class));
			});
		}

		return list;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		Usuario usuario = getRepository().findById(securityService.getUser().getCodigo()).get();
		Calendar calDataAcesso = Calendar.getInstance();
		calDataAcesso.setTimeInMillis(event.getTimestamp());
		Date dataAcesso = calDataAcesso.getTime();
		usuario.setDataUltimoAcesso(dataAcesso);
		getRepository().save(usuario);
	}
}
