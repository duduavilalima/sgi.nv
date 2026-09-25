package br.com.fasolti.sgi.repository.menu;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.security.domain.Modulo;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuloRepository extends QueryDslCrudRepository<Modulo> {

}
