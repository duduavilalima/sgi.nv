package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.administracao.domain.Obra;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObraRepository extends QueryDslCrudRepository<Obra> {

}
