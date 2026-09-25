package br.com.fasolti.sgi.repository.security;


import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.security.domain.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends QueryDslCrudRepository<Usuario> {

//    @Query(value = "SELECT u.perfis FROM Usuario u JOIN u.perfis perfis WHERE u.codigo = :codigoUsuario")
//    List<Perfil> findPerfisUsuario(@Param("codigoUsuario") Long codigoUsuario);

}
