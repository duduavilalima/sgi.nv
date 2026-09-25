package br.com.fasolti.sgi.repository.security;


import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.security.domain.Perfil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends QueryDslCrudRepository<Perfil> {

	@Query("select p from Perfil p order by p.descricao asc")
	Iterable<Perfil> listarAtivos();
}
