package br.com.fasolti.sgi.repository.comercial;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends QueryDslCrudRepository<Pessoa> {

}
