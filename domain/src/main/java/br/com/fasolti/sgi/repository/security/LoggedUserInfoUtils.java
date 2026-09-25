package br.com.fasolti.sgi.repository.security;

import br.com.fasolti.corporativo.security.domain.Perfil;

public interface LoggedUserInfoUtils {

	Boolean possuiTodosPerfisLista(String... listaPerfis);

	Boolean possuiTodosPerfisLista(Perfil... listaPerfis);

	Boolean possuiUmPerfilLista(String... listaPerfis);

	Boolean possuiUmPerfilLista(Perfil... listaPerfis);

}
