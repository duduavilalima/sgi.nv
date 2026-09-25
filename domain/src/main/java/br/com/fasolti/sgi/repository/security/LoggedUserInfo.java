package br.com.fasolti.sgi.repository.security;

import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.Usuario;

import java.util.List;

public interface LoggedUserInfo {

	Usuario getUsuario();

	List<Perfil> getPerfis();

	Pessoa getPessoa();

//    Boolean possuiTodosPerfis(String... listaPerfis);
//
//    Boolean possuiTodosPerfis(Perfil... listaPerfis);
//
//    Boolean possuiPeleMenosUmPerfil(String... listaPerfis);
//
//    Boolean possuiPeleMenosUmPerfil(Perfil... listaPerfis);
}
