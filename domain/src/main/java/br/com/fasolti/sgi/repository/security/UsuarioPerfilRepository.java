package br.com.fasolti.sgi.repository.security;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.security.domain.UsuarioPerfil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioPerfilRepository extends QueryDslCrudRepository<UsuarioPerfil> {

	List<UsuarioPerfil> findByUsuarioCodigo(Long codigoUsuario);

}
