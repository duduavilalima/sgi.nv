package br.com.araujoabreu.sgi.service.security;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;
import java.util.Map;

public interface UsuarioService extends CrudService<UsuarioTO> {
	UsuarioTO getUsuarioLogado();

	Map<String, Boolean> possuiTodosPerfisLista(String[] perfis);

	Map<String, Boolean> possuiUmPerfilLista(String[] perfis);

	boolean possuiPerfilLista(String... perfis);

	boolean possuiUmPerfilLista(String perfil);

	Map<String, Boolean> apenasFaturamento();

	List<CentroFinanceiroTO> getCentroFinanceiros();
}
